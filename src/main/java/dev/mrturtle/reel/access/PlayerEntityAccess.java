package dev.mrturtle.reel.access;

import dev.mrturtle.reel.entity.MinigameFishingBobberEntity;

public interface PlayerEntityAccess {
    MinigameFishingBobberEntity getMinigameBobber();
    void setMinigameBobber(MinigameFishingBobberEntity bobber);
}
