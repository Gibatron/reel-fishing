package dev.mrturtle.reel;

import dev.mrturtle.reel.entity.MinigameFishingBobberEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ReelEntities {
    public static EntityType<MinigameFishingBobberEntity> MINIGAME_FISHING_BOBBER = register(EntityType.Builder.<MinigameFishingBobberEntity>create(MinigameFishingBobberEntity::new, SpawnGroup.MISC)
            .disableSaving()
            .disableSummon()
            .setDimensions(0.25F, 0.25F)
            .maxTrackingRange(4)
            .trackingTickInterval(5)
            .build("minigame_fishing_bobber"), "minigame_fishing_bobber");

    public static void initialize() {}

    public static <T extends EntityType<?>> T register(T entityType, String id) {
        PolymerEntityUtils.registerType(entityType);
        return Registry.register(Registries.ENTITY_TYPE, ReelFishing.id(id), entityType);
    }
}
