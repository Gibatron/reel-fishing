package dev.mrturtle.reel;

import dev.mrturtle.reel.item.*;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public static final Item FISH_BONES = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER) {
        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            tooltip.add(Text.translatable("item.reel.fish_bones.tooltip").formatted(Formatting.GRAY));
        }
    }, "fish_bones");
    public static final Item BONEWORK_MECHANISM = register(new SimpleModeledPolymerItem(new Item.Settings(), Items.PAPER), "bonework_mechanism");
    public static final Item ROD_TABLE = register(new ModeledPolymerBlockItem(ReelBlocks.ROD_TABLE_BLOCK, new Item.Settings(), Items.SMITHING_TABLE), "rod_table");
    public static final Item BASSTIARY = register(new BasstiaryItem(new Item.Settings().maxCount(1), Items.PAPER), "basstiary");
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

    public static void initialize() {
        PolymerItemGroupUtils.registerPolymerItemGroup(ReelFishing.id("group"), ItemGroup.create(ItemGroup.Row.BOTTOM, -1)
                        .icon(COPPER_REEL::getDefaultStack)
                        .displayName(Text.translatable("itemgroup.reel"))
                        .entries(((context, entries) -> {
                            //// Blocks
                            entries.add(ROD_TABLE);
                            //// Rods
                            entries.add(OAK_ROD);
                            entries.add(SPRUCE_ROD);
                            entries.add(ACACIA_ROD);
                            entries.add(BIRCH_ROD);
                            entries.add(DARK_OAK_ROD);
                            entries.add(JUNGLE_ROD);
                            entries.add(CHERRY_ROD);
                            entries.add(MANGROVE_ROD);
                            entries.add(BAMBOO_ROD);
                            entries.add(CRIMSON_ROD);
                            entries.add(WARPED_ROD);
                            //// Reels
                            entries.add(WOODEN_REEL);
                            entries.add(BAMBOO_REEL);
                            entries.add(COPPER_REEL);
                            //// Hooks
                            entries.add(IRON_HOOK);
                            entries.add(SPIKED_HOOK);
                            entries.add(WEIGHTED_HOOK);
                            //// Other
                            entries.add(FISH_BONES);
                            entries.add(BONEWORK_MECHANISM);
                            //// Fish
                            // Shallow
                            entries.add(KETTLE_FISH);
                            entries.add(GILDED_FISH);
                            entries.add(ROSE_EYE);
                            // Ocean
                            entries.add(KELP_SPINE);
                            entries.add(BLUNDER);
                            entries.add(HALIBLOCK);
                            // Deep
                            entries.add(TRAWLER_FISH);
                            entries.add(DEATH_EEL);
                            entries.add(SQUISH_FISH);
                            entries.add(ABYSSAL_GLOW_SQUID);
                            entries.add(BABY_OARFISH);
                            // Cold
                            entries.add(RITUAL_FISH);
                            entries.add(SPIKE);
                            entries.add(ICE_CUBE_FISH);
                            // Tropical
                            entries.add(JESTER_FISH);
                            entries.add(MANTA_RAY);
                            entries.add(LIONFISH);
                            // Boiling
                            entries.add(BASSALT);
                            entries.add(CRIMSON_CROAKER);
                            entries.add(WARPED_EEL);
                            entries.add(TUNA_MELT);
                            //// Pre-assembled rods
                            entries.add(ModularFishingRodItem.getStackWithComponents(OAK_ROD, WOODEN_REEL, IRON_HOOK), ItemGroup.StackVisibility.PARENT_TAB_ONLY);
                            entries.add(ModularFishingRodItem.getStackWithComponents(BIRCH_ROD, WOODEN_REEL, SPIKED_HOOK), ItemGroup.StackVisibility.PARENT_TAB_ONLY);
                            entries.add(ModularFishingRodItem.getStackWithComponents(MANGROVE_ROD, BAMBOO_REEL, IRON_HOOK), ItemGroup.StackVisibility.PARENT_TAB_ONLY);
                            entries.add(ModularFishingRodItem.getStackWithComponents(SPRUCE_ROD, COPPER_REEL, WEIGHTED_HOOK), ItemGroup.StackVisibility.PARENT_TAB_ONLY);
                        }))
                .build());
    }

    public static <T extends Item> T register(T item, String id) {
        if (item instanceof SimpleModeledPolymerItem) {
            ((SimpleModeledPolymerItem) item).registerModel(id);
        }
        if (item instanceof ModeledPolymerBlockItem) {
            ((ModeledPolymerBlockItem) item).registerModel(id);
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
