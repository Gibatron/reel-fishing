package dev.mrturtle.reel.gui;

import com.google.gson.JsonObject;
import dev.mrturtle.reel.ReelFishing;
import dev.mrturtle.reel.fish.FishType;
import dev.mrturtle.reel.item.UIItem;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BasstiaryGui extends SimpleGui {
    public BasstiaryGui(ServerPlayerEntity player) {
        super(ScreenHandlerType.GENERIC_9X3, player, true);
        setSlot(18, UIItem.getButton("inventory_text_cover").setName(Text.empty()));
        setSlot(player.getInventory().selectedSlot + 54, UIItem.getButton("basstiary_icon").setName(Text.empty()));
        setSlot(1, UIItem.getButton("basstiary/ritual_fish").setName(Text.empty()));
        setTitle(Text.literal("ab").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(ReelFishing.id("basstiary_gui"))));
    }

    public static void registerModels(ResourcePackBuilder builder) {
        for (Map.Entry<Identifier, FishType> entry : ReelFishing.FISH_TYPES.entrySet()) {
            registerModel(builder, entry.getKey(), entry.getValue());
        }
    }

    public static void registerModel(ResourcePackBuilder builder, Identifier fishId, FishType fish) {
        String path = "assets/reel/models/gui/basstiary/%s.json".formatted(fishId.getPath());
        JsonObject model = new JsonObject();
        model.addProperty("parent", "item/handheld");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", fish.itemId().withPath("item/%s".formatted(fish.itemId().getPath())).toString());
        model.add("textures", textures);
        builder.addData(path, model.toString().getBytes(StandardCharsets.UTF_8));
        ReelFishing.LOGGER.info("basstiary/%s".formatted(fishId.getPath()));
        UIItem.register("basstiary/%s".formatted(fishId.getPath()));
    }
}
