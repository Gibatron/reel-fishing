package dev.mrturtle.reel.gui;

import com.google.gson.JsonArray;
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
        setSlot(1, UIItem.getColoredButton("basstiary/ritual_fish", 0x000000).setName(Text.empty()));
        setSlot(6, UIItem.getColoredButton("basstiary/trawler_fish", 0xFFFFFF).setName(Text.empty()));
        setTitle(Text.literal("ab").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(ReelFishing.id("basstiary_gui"))));
    }

    // Builder argument can be null to only register UI icons, such as on a server without AutoHost enabled
    public static void registerModels(ResourcePackBuilder builder) {
        for (Map.Entry<Identifier, FishType> entry : ReelFishing.FISH_TYPES.entrySet()) {
            if (builder != null)
                registerModel(builder, entry.getKey(), entry.getValue());
            UIItem.register("basstiary/%s".formatted(entry.getKey().getPath()), true);
        }
    }

    public static void registerModel(ResourcePackBuilder builder, Identifier fishId, FishType fish) {
        String path = "assets/reel/models/gui/basstiary/%s.json".formatted(fishId.getPath());
        JsonObject model = new JsonObject();
        model.addProperty("parent", "item/handheld");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", fish.itemId().withPath("item/%s".formatted(fish.itemId().getPath())).toString());
        model.add("textures", textures);
        // Move the fish model into the center and double its scale
        JsonObject display = new JsonObject();
        JsonObject gui = new JsonObject();
        JsonArray scale = new JsonArray();
        scale.add(2);
        scale.add(2);
        scale.add(1);
        gui.add("scale", scale);
        JsonArray translation = new JsonArray();
        translation.add(8);
        translation.add(-8);
        translation.add(0);
        gui.add("translation", translation);
        display.add("gui", gui);
        model.add("display", display);
        builder.addData(path, model.toString().getBytes(StandardCharsets.UTF_8));
    }
}
