package dev.mrturtle.reel;

import dev.mrturtle.reel.item.UIItem;
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
    public static final Item SPRUCE_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "spruce_rod");
    public static final Item ACACIA_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "acacia_rod");
    public static final Item BIRCH_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "birch_rod");
    public static final Item DARK_OAK_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "dark_oak_rod");
    public static final Item JUNGLE_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "jungle_rod");
    public static final Item CHERRY_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "cherry_rod");
    public static final Item MANGROVE_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "mangrove_rod");
    public static final Item BAMBOO_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "bamboo_rod");
    public static final Item CRIMSON_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "crimson_rod");
    public static final Item WARPED_ROD = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "warped_rod");
    // Reels
    public static final Item WOODEN_REEL = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "wooden_reel");
    public static final Item BAMBOO_REEL = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "bamboo_reel");
    public static final Item COPPER_REEL = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "copper_reel");
    // Hooks
    public static final Item IRON_HOOK = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "iron_hook");
    public static final Item SPIKED_HOOK = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "spiked_hook");
    public static final Item WEIGHTED_HOOK = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "weighted_hook");
    // UI Items
    public static final Item BUTTON_ITEM = register(new UIItem(new Item.Settings()), "ui_button");
    // Other
    public static final Item FISH_BONES = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "fish_bones");
    public static final Item BONEWORK_MECHANISM = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "bonework_mechanism");
    // Fish
    // Shallow
    public static final Item KETTLE_FISH = registerCookableFood(2, 0.1f, 5, 0.6f, "kettle_fish");
    public static final Item GILDED_FISH = registerCookableFood(2, 0.1f, 5, 0.6f, "gilded_fish");
    public static final Item ROSE_EYE = registerCookableFood(2, 0.1f, 5, 0.6f, "rose_eye");
    // Ocean
    public static final Item KELP_SPINE = registerCookableFood(2, 0.1f, 5, 0.6f, "kelp_spine");
    public static final Item BLUNDER = registerCookableFood(2, 0.1f, 5, 0.6f, "blunder");
    public static final Item HALIBLOCK = registerCookableFood(2, 0.1f, 5, 0.6f, "haliblock");
    // Deep
    public static final Item TRAWLER_FISH = registerCookableFood(2, 0.1f, 5, 0.6f, "trawler_fish");
    public static final Item DEATH_EEL = registerCookableFood(2, 0.1f, 5, 0.6f, "death_eel");
    public static final Item SQUISH_FISH = registerCookableFood(2, 0.1f, 5, 0.6f, "squish_fish");
    public static final Item ABYSSAL_GLOW_SQUID = registerCookableFood(2, 0.1f, 5, 0.6f, "abyssal_glow_squid");
    public static final Item BABY_OARFISH = registerCookableFood(2, 0.1f, 5, 0.6f, "baby_oarfish");
    // Cold
    public static final Item RITUAL_FISH = registerCookableFood(2, 0.1f, 5, 0.6f, "ritual_fish");
    public static final Item SPIKE = registerCookableFood(2, 0.1f, 5, 0.6f, "spike");
    public static final Item ICE_CUBE_FISH = registerFood(2, 0.1f, "ice_cube_fish");
    // Tropical
    public static final Item JESTER_FISH = registerCookableFood(2, 0.1f, 5, 0.6f, "jester_fish");
    public static final Item MANTA_RAY = registerCookableFood(2, 0.1f, 5, 0.6f, "manta_ray");
    public static final Item LIONFISH = registerCookableFood(2, 0.1f, 5, 0.6f, "lionfish");
    // Boiling
    public static final Item BASSALT = registerCookableFood(2, 0.1f, 5, 0.6f, "bassalt");
    public static final Item CRIMSON_CROAKER = registerCookableFood(2, 0.1f, 5, 0.6f, "crimson_croaker");
    public static final Item WARPED_EEL = registerCookableFood(2, 0.1f, 5, 0.6f, "warped_eel");
    public static final Item TUNA_MELT = registerFood(2, 0.1f, "tuna_melt");

    public static void initialize() {}

    public static <T extends Item> T register(T item, String id) {
        if (item instanceof SimpleModeledPolymerItem) {
            ((SimpleModeledPolymerItem) item).registerModel(id);
        }
        return Registry.register(Registries.ITEM, ReelFishing.id(id), item);
    }

    public static SimpleModeledPolymerItem registerFood(int hunger, float saturation, String id) {
        SimpleModeledPolymerItem item = new SimpleModeledPolymerItem(new Item.Settings()
                .food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()), Items.BREAD);
        item.registerModel(id);
        return Registry.register(Registries.ITEM, ReelFishing.id(id), item);
    }

    public static SimpleModeledPolymerItem registerCookableFood(int hunger, float saturation, int cookedHunger, float cookedSaturation, String id) {
        SimpleModeledPolymerItem item = new SimpleModeledPolymerItem(new Item.Settings()
                .food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()), Items.BREAD);
        item.registerModel(id);
        SimpleModeledPolymerItem cookedItem = new SimpleModeledPolymerItem(new Item.Settings()
                .food(new FoodComponent.Builder().hunger(cookedHunger).saturationModifier(cookedSaturation).build()), Items.BREAD);
        cookedItem.registerModel("cooked_" + id);
        Registry.register(Registries.ITEM, ReelFishing.id("cooked_" + id), cookedItem);
        return Registry.register(Registries.ITEM, ReelFishing.id(id), item);
    }
}
