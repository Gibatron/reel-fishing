package dev.mrturtle.reel.item;

import dev.mrturtle.reel.ReelFishing;
import dev.mrturtle.reel.ReelItems;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ButtonItem extends Item implements PolymerItem {
    public static final HashMap<String, PolymerModelData> TEXTURES = new HashMap<>();

    public ButtonItem(Settings settings) {
        super(settings);
    }

    public static void registerButton(String textureId) {
        PolymerModelData model = PolymerResourcePackUtils.requestModel(Items.PAPER, ReelFishing.id("gui/" + textureId));
        TEXTURES.put(textureId, model);
    }

    public static GuiElementBuilder getButton(String textureId) {
        ItemStack itemStack = new ItemStack(ReelItems.BUTTON_ITEM);
        itemStack.getOrCreateNbt().putString("Texture", textureId);
        return GuiElementBuilder.from(itemStack);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
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
