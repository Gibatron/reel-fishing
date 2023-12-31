package dev.mrturtle.reel.entity;

import dev.mrturtle.reel.ReelEntities;
import dev.mrturtle.reel.ReelFishing;
import dev.mrturtle.reel.access.PlayerEntityAccess;
import dev.mrturtle.reel.fish.FishPattern;
import dev.mrturtle.reel.fish.FishType;
import dev.mrturtle.reel.item.ModularFishingRodItem;
import dev.mrturtle.reel.item.UIItem;
import dev.mrturtle.reel.rod.HookType;
import dev.mrturtle.reel.rod.ReelType;
import dev.mrturtle.reel.rod.RodType;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static dev.mrturtle.reel.item.ModularFishingRodItem.attemptToResetCastState;

public class MinigameFishingBobberEntity extends ProjectileEntity implements PolymerEntity {
    private final Identifier fishId;
    private final FishType fishType;
    private final FishPattern fishPattern;

    private final RodType rodType;
    private final ReelType reelType;
    private final HookType hookType;

    private ElementHolder holder;
    private ItemDisplayElement wheelElement;
    private ItemDisplayElement rodPatternElement;
    private ItemDisplayElement cursorElement;
    private ItemDisplayElement fishElement;
    private ItemDisplayElement waterBarElement;
    private ItemDisplayElement escapeBarElement;

    private PlayerEntity player;
    private Vec3d offsetVec;

    private int rotation = 0;
    private float catchingProgress = 40;

    public MinigameFishingBobberEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
        fishId = null;
        fishType = null;
        fishPattern = null;
        rodType = null;
        reelType = null;
        hookType = null;
    }

    public MinigameFishingBobberEntity(PlayerEntity owner, World world, Vec3d pos, ItemStack fishingRod, Identifier fishId) {
        super(ReelEntities.MINIGAME_FISHING_BOBBER, world);
        setOwner(owner);
        player = owner;
        this.fishId = fishId;
        this.fishType = ReelFishing.FISH_TYPES.get(fishId);
        this.fishPattern = ReelFishing.FISH_PATTERNS.get(fishType.minigamePatternId());
        NbtCompound nbt = fishingRod.getOrCreateNbt();
        this.rodType = ReelFishing.ROD_TYPES.get(new Identifier(nbt.getString(ModularFishingRodItem.ROD_KEY)));
        this.reelType = ReelFishing.REEL_TYPES.get(new Identifier(nbt.getString(ModularFishingRodItem.REEL_KEY)));
        this.hookType = ReelFishing.HOOK_TYPES.get(new Identifier(nbt.getString(ModularFishingRodItem.HOOK_KEY)));
        refreshPositionAfterTeleport(pos);
        ((PlayerEntityAccess) player).setMinigameBobber(this);
        holder = new ElementHolder() {
            @Override
            public boolean startWatching(ServerPlayNetworkHandler player) {
                if (player.getPlayer() != owner)
                    return false;
                return super.startWatching(player);
            }
        };
        // Spawn the UI in front of the player
        offsetVec = owner.getCameraPosVec(1).add(owner.getRotationVector().multiply(3)).subtract(getPos());
        wheelElement = holder.addElement(new ItemDisplayElement());
        wheelElement.setItem(UIItem.getStack(fishType.minigamePatternId().getPath()));
        wheelElement.setOffset(offsetVec);
        wheelElement.setBillboardMode(DisplayEntity.BillboardMode.CENTER);
        wheelElement.startInterpolation();
        rodPatternElement = holder.addElement(new ItemDisplayElement());
        rodPatternElement.setItem(UIItem.getStack(rodType.rodPattern().getPath()));
        rodPatternElement.setOffset(offsetVec);
        cursorElement = holder.addElement(new ItemDisplayElement());
        cursorElement.setItem(UIItem.getStack("cursor"));
        cursorElement.setOffset(offsetVec);
        fishElement = holder.addElement(new ItemDisplayElement());
        fishElement.setItem(UIItem.getStack("fish"));
        fishElement.setOffset(offsetVec);
        waterBarElement = holder.addElement(new ItemDisplayElement());
        waterBarElement.setItem(UIItem.getStack("water_bar"));
        waterBarElement.setOffset(offsetVec);
        escapeBarElement = holder.addElement(new ItemDisplayElement());
        escapeBarElement.setItem(UIItem.getStack("escape_bar"));
        escapeBarElement.setOffset(offsetVec);
        EntityAttachment.ofTicking(holder, this);
    }

    @Override
    public void tick() {
        Entity owner = getOwner();
        if (owner == null) {
            discard();
            return;
        }
        // Rotate the wheel by the fish speed multiplied by 4, multiplied by 1 - the reel's strength, with a minimum value of 4
        rotation += Math.max(fishType.speed() * 4 * (1 - rodType.stability()), 4);
        if (rotation >= 360)
            rotation = 0;
        // Catching progress decays by the fish's temper every second, multiplied by 1 - the reel's strength
        catchingProgress -= fishType.temper() / 20.0 * (1 - reelType.strength());
        // Did the fish escape?
        if (catchingProgress <= 0) {
            player.playSound(SoundEvents.ENTITY_GOAT_HORN_BREAK, SoundCategory.PLAYERS, 1, 1);
            player.playSound(SoundEvents.ENTITY_PLAYER_SPLASH_HIGH_SPEED, SoundCategory.PLAYERS, 0.6f, 1);
            discard();
        }
        // Update transformations
        wheelElement.setTransformation(new Matrix4f().rotateZ((float) Math.toRadians(-rotation)));
        rodPatternElement.setTransformation(getOffsetBillboardMatrix(0, 0, 0).scale(1.2f, 1.2f, 30));
        cursorElement.setTransformation(getOffsetBillboardMatrix(0.4f, 0.05f, -0.05f));
        fishElement.setTransformation(getOffsetBillboardMatrix(-1f, -0.5f + catchingProgress / 100f, 0f));
        waterBarElement.setTransformation(getOffsetBillboardMatrix(-1f, 0.5f, -0.05f));
        escapeBarElement.setTransformation(getOffsetBillboardMatrix(-1f, -0.7f, -0.05f));
        super.tick();
    }

    public void useRod(ItemStack fishingRod) {
        player.playSound(SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.PLAYERS, 1, 1);
        // Is the cursor actually over the fish pattern?
        if (fishPattern.isAngleIn(rotation)) {
            float power = hookType.power() * 2;
            // Hooks are only half as powerful when used for the wrong category of fish
            if (!fishType.isInCategory(hookType.category()))
                power /= 2;
            catchingProgress += power;
            // Jump forward 10 degrees
            rotation += 10;
            addVelocity(0, -0.05f, 0);
            player.playSound(SoundEvents.ENTITY_EVOKER_FANGS_ATTACK, SoundCategory.PLAYERS, 1, 2);
            // Did the fish get caught?
            if (catchingProgress >= 100) {
                ItemStack stack = new ItemStack(ReelFishing.FISH_IDS_TO_ITEMS.get(fishId));
                ItemEntity entity = new ItemEntity(getWorld(), getX(), getY(), getZ(), stack);
                double velocityX = player.getX() - getX();
                double velocityY = player.getY() - getY();
                double velocityZ = player.getZ() - getZ();
                // Math that vanilla uses for Y velocity
                velocityY = velocityY * 0.1 + Math.sqrt(Math.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ)) * 0.08;
                entity.setVelocity(velocityX * 0.1, velocityY, velocityZ * 0.1);
                getWorld().spawnEntity(entity);
                getWorld().spawnEntity(new ExperienceOrbEntity(getWorld(), player.getX(), player.getY(), player.getZ(), random.nextInt(6) + 1));
                player.increaseStat(Stats.FISH_CAUGHT, 1);
                discard();
            }
        } else {
            // When you miss the fish, the catching progress will go down
            catchingProgress -= fishType.temper() * 2;
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HAT.value(), SoundCategory.PLAYERS, 1, 1.4f);
        }
    }

    public Matrix4f getOffsetBillboardMatrix(float offsetX, float offsetY, float offsetZ) {
        Entity owner = getOwner();
        // Get the owner's eye position based on their current pose
        Vec3d targetEyePos = owner.getPos().add(0, owner.getEyeHeight(owner.getPose()), 0);
        // Get the relative position of the owner's eyes to us
        Vector3f targetPos = getPos().add(offsetVec).subtract(targetEyePos).toVector3f();
        // Use the matrix billboard method to rotate towards the owner's eye position relative to us
        return new Matrix4f().billboardSpherical(new Vector3f(0, 0, 0), targetPos, new Vector3f(0, 1, 0)).translate(offsetX, offsetY, offsetZ).scale(1, 1, 0.1f);
    }

    @Override
    public void remove(RemovalReason reason) {
        ((PlayerEntityAccess) player).setMinigameBobber(null);
        if (!attemptToResetCastState(player.getMainHandStack()))
            attemptToResetCastState(player.getOffHandStack());
        super.remove(reason);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.FISHING_BOBBER;
    }
}
