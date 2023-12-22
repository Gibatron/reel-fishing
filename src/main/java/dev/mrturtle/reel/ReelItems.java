package dev.mrturtle.reel;

import dev.mrturtle.reel.item.ButtonItem;
import dev.mrturtle.reel.item.ModularFishingRodItem;
import dev.mrturtle.reel.item.SimpleModeledPolymerItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ReelItems {
    // Fishing Rod Components
    public static final Item MODULAR_FISHING_ROD_ITEM = register(new ModularFishingRodItem(new Item.Settings().maxCount(1)), "modular_fishing_rod");
    // Rods
    public static final Item OAK_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "oak_rod");
    // Reels
    public static final Item WOODEN_REEL = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "wooden_reel");
    // Hooks
    public static final Item IRON_HOOK = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "iron_hook");
    public static final Item SPIKED_HOOK = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "spiked_hook");
    // Fish
    public static final Item GILDED_FISH = register(new SimpleModeledPolymerItem(new Item.Settings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).build()), Items.PAPER), "gilded_fish");
    // UI Items
    public static final Item BUTTON_ITEM = register(new ButtonItem(new Item.Settings()), "ui_button");

    public static void initialize() {}

    public static <T extends Item> T register(T item, String id) {
        if (item instanceof SimpleModeledPolymerItem) {
            ((SimpleModeledPolymerItem) item).registerModel(id);
        }
        return Registry.register(Registries.ITEM, ReelFishing.id(id), item);
    }
}
