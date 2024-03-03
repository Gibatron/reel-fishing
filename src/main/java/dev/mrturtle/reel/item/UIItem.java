package dev.mrturtle.reel.item;

import dev.mrturtle.reel.ReelFishing;
import dev.mrturtle.reel.ReelItems;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class UIItem extends Item implements PolymerItem {
    public static final HashMap<String, PolymerModelData> TEXTURES = new HashMap<>();

    public UIItem(Settings settings) {
        super(settings);
    }

    public static void register(String textureId) {
        register(textureId, false);
    }

    public static void register(String textureId, boolean canColor) {
        PolymerModelData model = PolymerResourcePackUtils.requestModel(canColor ? Items.LEATHER_HORSE_ARMOR : Items.PAPER, ReelFishing.id("gui/" + textureId));
        TEXTURES.put(textureId, model);
    }

    public static GuiElementBuilder getButton(String textureId) {
        return GuiElementBuilder.from(getStack(textureId));
    }

    public static GuiElementBuilder getColoredButton(String textureId, int color) {
        return GuiElementBuilder.from(getColoredStack(textureId, color));
    }

    public static ItemStack getStack(String textureId) {
        ItemStack itemStack = new ItemStack(ReelItems.BUTTON_ITEM);
        itemStack.getOrCreateNbt().putString("Texture", textureId);
        return itemStack;
    }

    public static ItemStack getColoredStack(String textureId, int color) {
        ItemStack itemStack = new ItemStack(ReelItems.BUTTON_ITEM);
        itemStack.getOrCreateNbt().putString("Texture", textureId);
        itemStack.getOrCreateNbt().putInt("Color", color);
        return itemStack;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipContext context, @Nullable ServerPlayerEntity player) {
        ItemStack stack = PolymerItem.super.getPolymerItemStack(itemStack, context, player);
        if (itemStack.hasNbt())
            if (itemStack.getOrCreateNbt().contains("Color"))
                stack.getOrCreateSubNbt("display").putInt("color", itemStack.getOrCreateNbt().getInt("Color"));
        return stack;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        if (itemStack.hasNbt())
            if (itemStack.getOrCreateNbt().contains("Color"))
                return Items.LEATHER_HORSE_ARMOR;
        return Items.PAPER;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        if (!itemStack.hasNbt())
            return -1;
        String textureId = itemStack.getOrCreateNbt().getString("Texture");
        if (!TEXTURES.containsKey(textureId))
            return -1;
        return TEXTURES.get(textureId).value();
    }
}
