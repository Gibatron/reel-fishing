package dev.mrturtle.reel.mixin;

import dev.mrturtle.reel.access.PlayerEntityAccess;
import dev.mrturtle.reel.entity.MinigameFishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityAccess {
    MinigameFishingBobberEntity minigameBobber;

    public MinigameFishingBobberEntity getMinigameBobber() {
        return minigameBobber;
    }

    public void setMinigameBobber(MinigameFishingBobberEntity bobber) {
        minigameBobber = bobber;
    }
}
