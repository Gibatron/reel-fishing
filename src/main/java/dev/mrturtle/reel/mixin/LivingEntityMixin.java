package dev.mrturtle.reel.mixin;

import dev.mrturtle.reel.ReelItemTags;
import dev.mrturtle.reel.ReelItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "eatFood", at = @At("HEAD"))
    public void createBonesFromEatingFish(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!stack.isFood())
            return;
        if (!stack.isIn(ReelItemTags.FISH_WITH_BONES))
            return;
        // There is only a 30% chance for a bone to drop
        if (world.random.nextFloat() > 0.3)
            return;
        ItemStack bonesStack = new ItemStack(ReelItems.FISH_BONES);
        Vec3d headPos = getCameraPosVec(1);
        Vec3d velocity = getRotationVector().multiply(0.2).add(0, 0.2, 0);
        ItemEntity entity = new ItemEntity(world, headPos.x, headPos.y, headPos.z, bonesStack, velocity.x, velocity.y, velocity.z);
        entity.setToDefaultPickupDelay();
        world.spawnEntity(entity);
        world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GOAT_HORN_BREAK, SoundCategory.PLAYERS, 0.8f, 1);
    }
}
