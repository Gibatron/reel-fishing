package dev.mrturtle.reel.item;

import dev.mrturtle.reel.ReelFishing;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SimpleModeledPolymerItem extends Item implements PolymerItem {
    public HashMap<SimpleModeledPolymerItem, PolymerModelData> modelData = new HashMap<>();
    public Item polymerItem;

    public SimpleModeledPolymerItem(Settings settings, Item polymerItem) {
        super(settings);
        this.polymerItem = polymerItem;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return polymerItem;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return modelData.get(this).value();
    }

    public void registerModel(String id) {
        modelData.put(this, PolymerResourcePackUtils.requestModel(polymerItem, ReelFishing.id("item/%s".formatted(id))));
    }
}
