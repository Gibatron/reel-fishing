package dev.mrturtle.reel.item;

import dev.mrturtle.reel.ReelFishing;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ModeledPolymerBlockItem extends PolymerBlockItem {
    public HashMap<ModeledPolymerBlockItem, PolymerModelData> modelData = new HashMap<>();

    public ModeledPolymerBlockItem(Block block, Item.Settings settings, Item virtualItem) {
        super(block, settings, virtualItem);
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return modelData.get(this).value();
    }

    public void registerModel(String id, Item virtualItem) {
        modelData.put(this, PolymerResourcePackUtils.requestModel(virtualItem, ReelFishing.id("item/%s".formatted(id))));
    }
}
