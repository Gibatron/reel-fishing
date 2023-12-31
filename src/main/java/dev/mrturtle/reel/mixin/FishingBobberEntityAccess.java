package dev.mrturtle.reel.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FishingBobberEntity.class)
public interface FishingBobberEntityAccess {
    @Accessor int getHookCountdown();
    @Accessor int getWaitCountdown();
    @Accessor void setWaitCountdown(int waitCountdown);
}
