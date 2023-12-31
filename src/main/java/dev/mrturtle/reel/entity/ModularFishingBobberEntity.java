package dev.mrturtle.reel.entity;

import dev.mrturtle.reel.ReelFishing;
import dev.mrturtle.reel.fish.FishHelper;
import dev.mrturtle.reel.item.ModularFishingRodItem;
import dev.mrturtle.reel.mixin.FishingBobberEntityAccess;
import dev.mrturtle.reel.rod.HookType;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

import static dev.mrturtle.reel.item.ModularFishingRodItem.attemptToResetCastState;

public class ModularFishingBobberEntity extends FishingBobberEntity implements PolymerEntity {
    public ModularFishingBobberEntity(PlayerEntity thrower, World world) {
        super(thrower, world, 0, 0);
    }

    @Override
    public void tick() {
        // This reduces the amount of time you need to wait for a fish to show up
        ((FishingBobberEntityAccess) this).setWaitCountdown(((FishingBobberEntityAccess) this).getWaitCountdown() - 10);
        super.tick();
    }

    @Override
    public int use(ItemStack usedItem) {
        PlayerEntity owner = getPlayerOwner();
        if (getWorld().isClient || owner == null)
            return 0;
        int durabilityLost = 0;
        if (getHookedEntity() != null) {
            // Player is pulling an entity
            pullHookedEntity(getHookedEntity());
            Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) owner, usedItem, this, List.of());
            getWorld().sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
            durabilityLost = 2;
        } else if (((FishingBobberEntityAccess) this).getHookCountdown() > 0) {
            // Player hooked a fish
            // Check what biome and dimension we are in
            var biome = getWorld().getBiome(getBlockPos());
            Identifier dimensionId = getWorld().getRegistryKey().getValue();
            // Get rod stats
            NbtCompound nbt = usedItem.getOrCreateNbt();
            HookType hookType = ReelFishing.HOOK_TYPES.get(new Identifier(nbt.getString(ModularFishingRodItem.HOOK_KEY)));
            // Find the category of fish for the biome and dimension we are in
            Identifier fishCategoryId = FishHelper.getCategoryFromConditions(biome, dimensionId, hookType);
            if (fishCategoryId != null) {
                // If we actually have a valid category, pull a random weighted fish from it
                Identifier fishId = FishHelper.getFishFromCategory(fishCategoryId, getWorld().random);
                if (fishId != null) {
                    // We have a valid fish
                    getWorld().spawnEntity(new MinigameFishingBobberEntity(owner, getWorld(), getPos(), usedItem, fishId));
                    durabilityLost = 1;
                }
            }
        }
        discard();
        return durabilityLost;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        PlayerEntity owner = getPlayerOwner();
        if (owner != null) {
            // Attempt to reset the cast state of the rod in the mainhand, if that fails try to reset the rod in the offhand.
            if (!attemptToResetCastState(owner.getMainHandStack()))
                attemptToResetCastState(owner.getOffHandStack());
        }
        super.remove(reason);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.FISHING_BOBBER;
    }
}
