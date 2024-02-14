package dev.mrturtle.reel.item;

import dev.mrturtle.reel.gui.BasstiaryGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BasstiaryItem extends SimpleModeledPolymerItem {
    public BasstiaryItem(Settings settings, Item polymerItem) {
        super(settings, polymerItem);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            BasstiaryGui gui = new BasstiaryGui((ServerPlayerEntity) user);
            gui.open();
        }
        ItemStack itemStack = user.getStackInHand(hand);
        return TypedActionResult.success(itemStack);
    }
}
