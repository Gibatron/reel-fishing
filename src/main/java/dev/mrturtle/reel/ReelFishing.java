package dev.mrturtle.reel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.mrturtle.reel.fish.FishCategory;
import dev.mrturtle.reel.fish.FishType;
import dev.mrturtle.reel.gui.GuiElements;
import dev.mrturtle.reel.item.ModularFishingRodItem;
import dev.mrturtle.reel.rod.HookType;
import dev.mrturtle.reel.rod.ReelType;
import dev.mrturtle.reel.rod.RodType;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ReelFishing implements ModInitializer {
	public static final String MOD_ID = "reel";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Rods
	public static final HashMap<Identifier, RodType> ROD_TYPES = new HashMap<>();
	public static final HashMap<Item, Identifier> ROD_ITEMS_TO_IDS = new HashMap<>();
	public static final HashMap<Identifier, Item> ROD_IDS_TO_ITEMS = new HashMap<>();
	// Reels
	public static final HashMap<Identifier, ReelType> REEL_TYPES = new HashMap<>();
	public static final HashMap<Item, Identifier> REEL_ITEMS_TO_IDS = new HashMap<>();
	public static final HashMap<Identifier, Item> REEL_IDS_TO_ITEMS = new HashMap<>();
	// Hooks
	public static final HashMap<Identifier, HookType> HOOK_TYPES = new HashMap<>();
	public static final HashMap<Item, Identifier> HOOK_ITEMS_TO_IDS = new HashMap<>();
	public static final HashMap<Identifier, Item> HOOK_IDS_TO_ITEMS = new HashMap<>();

	// Fish
	public static final HashMap<Identifier, FishType> FISH_TYPES = new HashMap<>();
	public static final HashMap<Identifier, HashMap<Identifier, Integer>> FISH_CATEGORIES_TO_IDS = new HashMap<>();
	public static final HashMap<Identifier, Item> FISH_IDS_TO_ITEMS = new HashMap<>();
	public static final HashMap<Identifier, FishCategory> FISH_CATEGORIES = new HashMap<>();

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(MOD_ID);
		PolymerResourcePackUtils.markAsRequired();
		PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register((builder) -> {
			LOGGER.info("Generating modular fishing rod models...");
			for (Identifier rodId : ReelFishing.ROD_TYPES.keySet()) {
				registerModel(builder, rodId, null, null);
				for (Identifier reelId : ReelFishing.REEL_TYPES.keySet()) {
					registerModel(builder, rodId, reelId, null);
					registerModel(builder, rodId, reelId, null, true);
					for (Identifier hookId : ReelFishing.HOOK_TYPES.keySet()) {
						registerModel(builder, rodId, reelId, hookId);
					}
				}
			}
			((ModularFishingRodItem) ReelItems.MODULAR_FISHING_ROD_ITEM).registerModels();
			LOGGER.info("Created modular fishing rod models!");
		});
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> ReelFishing.load(server));
		ReelItems.initialize();
		ReelBlocks.initialize();
		GuiElements.initialize();
	}

	public static void load(MinecraftServer server) {
		LOGGER.info("Loading modular fishing rod components...");
		loadComponents(server);
		LOGGER.info(ROD_TYPES.toString());
		LOGGER.info(REEL_TYPES.toString());
		LOGGER.info(HOOK_TYPES.toString());
		LOGGER.info("Loaded modular fishing rod components!");
		LOGGER.info("Loading fish...");
		loadFish(server);
		LOGGER.info(FISH_TYPES.toString());
		LOGGER.info("Loaded fish!");
	}

	public static void loadComponents(MinecraftServer server) {
		// Clear all current components
		ROD_TYPES.clear();
		ROD_ITEMS_TO_IDS.clear();
		ROD_IDS_TO_ITEMS.clear();
		REEL_TYPES.clear();
		REEL_ITEMS_TO_IDS.clear();
		REEL_IDS_TO_ITEMS.clear();
		HOOK_TYPES.clear();
		HOOK_ITEMS_TO_IDS.clear();
		HOOK_IDS_TO_ITEMS.clear();
		var ops = RegistryOps.of(JsonOps.INSTANCE, server.getRegistryManager());
		// Load rods
		for (var resource : server.getResourceManager().findResources("reel_components/rods", (id) -> id.getPath().endsWith(".json")).entrySet()) {
			Identifier rodId = new Identifier(resource.getKey().getNamespace(), resource.getKey().getPath().substring("reel_components/rods/".length(), resource.getKey().getPath().length() - 5));
			try {
				RodType rodType = RodType.CODEC.decode(ops, JsonParser.parseReader(resource.getValue().getReader())).getOrThrow(false, (message) -> {}).getFirst();
				Item rodItem = Registries.ITEM.get(rodType.itemId());
				ROD_TYPES.put(rodId, rodType);
				ROD_ITEMS_TO_IDS.put(rodItem, rodId);
				ROD_IDS_TO_ITEMS.put(rodId, rodItem);
			} catch (Throwable e) {
				LOGGER.warn("Failed to parse rod type {}!", resource.getKey().toString());
				e.printStackTrace();
			}
		}
		// Load reels
		for (var resource : server.getResourceManager().findResources("reel_components/reels", (id) -> id.getPath().endsWith(".json")).entrySet()) {
			Identifier reelId = new Identifier(resource.getKey().getNamespace(), resource.getKey().getPath().substring("reel_components/reels/".length(), resource.getKey().getPath().length() - 5));
			try {
				ReelType reelType = ReelType.CODEC.decode(ops, JsonParser.parseReader(resource.getValue().getReader())).getOrThrow(false, (message) -> {}).getFirst();
				Item reelItem = Registries.ITEM.get(reelType.itemId());
				REEL_TYPES.put(reelId, reelType);
				REEL_ITEMS_TO_IDS.put(reelItem, reelId);
				REEL_IDS_TO_ITEMS.put(reelId, reelItem);
			} catch (Throwable e) {
				LOGGER.warn("Failed to parse reel type {}!", resource.getKey().toString());
				e.printStackTrace();
			}
		}
		// Load hooks
		for (var resource : server.getResourceManager().findResources("reel_components/hooks", (id) -> id.getPath().endsWith(".json")).entrySet()) {
			Identifier hookId = new Identifier(resource.getKey().getNamespace(), resource.getKey().getPath().substring("reel_components/hooks/".length(), resource.getKey().getPath().length() - 5));
			try {
				HookType hookType = HookType.CODEC.decode(ops, JsonParser.parseReader(resource.getValue().getReader())).getOrThrow(false, (message) -> {}).getFirst();
				Item hookItem = Registries.ITEM.get(hookType.itemId());
				HOOK_TYPES.put(hookId, hookType);
				HOOK_ITEMS_TO_IDS.put(hookItem, hookId);
				HOOK_IDS_TO_ITEMS.put(hookId, hookItem);
			} catch (Throwable e) {
				LOGGER.warn("Failed to parse hook type {}!", resource.getKey().toString());
				e.printStackTrace();
			}
		}
	}

	public static void loadFish(MinecraftServer server) {
		// Clear all current fish
		FISH_TYPES.clear();
		FISH_CATEGORIES_TO_IDS.clear();
		FISH_IDS_TO_ITEMS.clear();
		FISH_CATEGORIES.clear();
		var ops = RegistryOps.of(JsonOps.INSTANCE, server.getRegistryManager());
		// Load fish
		for (var resource : server.getResourceManager().findResources("fish", (id) -> id.getPath().endsWith(".json")).entrySet()) {
			Identifier fishId = new Identifier(resource.getKey().getNamespace(), resource.getKey().getPath().substring("fish/".length(), resource.getKey().getPath().length() - 5));
			try {
				FishType fishType = FishType.CODEC.decode(ops, JsonParser.parseReader(resource.getValue().getReader())).getOrThrow(false, (message) -> {}).getFirst();
				Item fishItem = Registries.ITEM.get(fishType.itemId());
				FISH_TYPES.put(fishId, fishType);
				for (FishType.CategoryData categoryData : fishType.categories()) {
					HashMap<Identifier, Integer> map = FISH_CATEGORIES_TO_IDS.getOrDefault(categoryData.id(), new HashMap<>());
					map.put(fishId, categoryData.weight());
					FISH_CATEGORIES_TO_IDS.put(categoryData.id(), map);
				}
				FISH_IDS_TO_ITEMS.put(fishId, fishItem);
			} catch (Throwable e) {
				LOGGER.warn("Failed to parse fish type {}!", resource.getKey().toString());
				e.printStackTrace();
			}
		}
		// Load fish categories
		for (var resource : server.getResourceManager().findResources("fish_categories", (id) -> id.getPath().endsWith(".json")).entrySet()) {
			Identifier categoryId = new Identifier(resource.getKey().getNamespace(), resource.getKey().getPath().substring("fish_categories/".length(), resource.getKey().getPath().length() - 5));
			try {
				FishCategory fishCategory = FishCategory.CODEC.decode(ops, JsonParser.parseReader(resource.getValue().getReader())).getOrThrow(false, (message) -> {}).getFirst();
				FISH_CATEGORIES.put(categoryId, fishCategory);
			} catch (Throwable e) {
				LOGGER.warn("Failed to parse fish category {}!", resource.getKey().toString());
				e.printStackTrace();
			}
		}
	}

	public static void registerModel(ResourcePackBuilder builder, Identifier rodId, Identifier reelId, Identifier hookId) {
		registerModel(builder, rodId, reelId, hookId, false);
	}

	public static void registerModel(ResourcePackBuilder builder, Identifier rodId, Identifier reelId, Identifier hookId, boolean castVariant) {
		String id = convertComponentsIdsToRodId(rodId, reelId, hookId);
		if (castVariant)
			id += "_cast";
		JsonObject model = new JsonObject();
		model.addProperty("parent", "item/handheld_rod");
		JsonObject textures = new JsonObject();
		textures.addProperty("layer0", new Identifier(rodId.getNamespace(), "item/%s_rod".formatted(rodId.getPath())).toString());
		if (reelId != null)
			if (!castVariant)
				textures.addProperty("layer1", new Identifier(reelId.getNamespace(), "item/%s_reel".formatted(reelId.getPath())).toString());
			else
				textures.addProperty("layer1", new Identifier(reelId.getNamespace(), "item/%s_reel_cast".formatted(reelId.getPath())).toString());
		if (hookId != null)
			textures.addProperty("layer2", new Identifier(hookId.getNamespace(), "item/%s_hook".formatted(hookId.getPath())).toString());
		model.add("textures", textures);
		builder.addData("assets/%s/models/item/%s.json".formatted(rodId.getNamespace(), id), model.toString().getBytes(StandardCharsets.UTF_8));
		LOGGER.info("{} {}", id, model);
	}

	public static Identifier getRodFromItem(Item item) {
		return ROD_ITEMS_TO_IDS.get(item);
	}

	public static Identifier getReelFromItem(Item item) {
		return REEL_ITEMS_TO_IDS.get(item);
	}

	public static Identifier getHookFromItem(Item item) {
		return HOOK_ITEMS_TO_IDS.get(item);
	}

	public static Item getItemFromRod(Identifier id) {
		return ROD_IDS_TO_ITEMS.get(id);
	}

	public static Item getItemFromReel(Identifier id) {
		return REEL_IDS_TO_ITEMS.get(id);
	}

	public static Item getItemFromHook(Identifier id) {
		return HOOK_IDS_TO_ITEMS.get(id);
	}

	public static String convertComponentsIdsToRodId(Identifier rodId, Identifier reelId, Identifier hookId) {
		String rodStringId = rodId.toUnderscoreSeparatedString();
		String reelStringId = "none";
		if (reelId != null)
			reelStringId = reelId.toUnderscoreSeparatedString();
		String hookStringId = "none";
		if (hookId != null)
			hookStringId = hookId.toUnderscoreSeparatedString();
		return "%s_%s_%s".formatted(rodStringId, reelStringId, hookStringId);
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}