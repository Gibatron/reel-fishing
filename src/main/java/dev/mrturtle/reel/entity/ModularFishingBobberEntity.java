package dev.mrturtle.reel.entity;

import dev.mrturtle.reel.ReelItems;
import dev.mrturtle.reel.item.ModularFishingRodItem;
import dev.mrturtle.reel.mixin.FishingBobberEntityAccess;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.List;

public class ModularFishingBobberEntity extends FishingBobberEntity implements PolymerEntity {
    public ModularFishingBobberEntity(PlayerEntity thrower, World world) {
        super(thrower, world, 0, 0);
    }

    @Override
    public int use(ItemStack usedItem) {
        PlayerEntity owner = getPlayerOwner();
        if (getWorld().isClient || owner == null)
            return 0;
        int durabilityLost = 0;
        if (getHookedEntity() != null) {
            pullHookedEntity(getHookedEntity());
            Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) owner, usedItem, this, List.of());
            getWorld().sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
            durabilityLost = 2;
        } else if (((FishingBobberEntityAccess) this).getHookCountdown() > 0) {
            durabilityLost = 1;
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

    private boolean attemptToResetCastState(ItemStack stack) {
        if (!stack.isOf(ReelItems.MODULAR_FISHING_ROD_ITEM))
            return false;
        if (!stack.getOrCreateNbt().getBoolean(ModularFishingRodItem.CAST_KEY))
            return false;
        stack.getOrCreateNbt().putBoolean(ModularFishingRodItem.CAST_KEY, false);
        return true;
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.FISHING_BOBBER;
    }
}
