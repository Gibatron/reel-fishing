package dev.mrturtle.reel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.mrturtle.reel.fish.FishCategory;
import dev.mrturtle.reel.fish.FishPattern;
import dev.mrturtle.reel.fish.FishType;
import dev.mrturtle.reel.gui.BasstiaryGui;
import dev.mrturtle.reel.gui.GuiElements;
import dev.mrturtle.reel.item.ModularFishingRodItem;
import dev.mrturtle.reel.item.UIItem;
import dev.mrturtle.reel.rod.HookType;
import dev.mrturtle.reel.rod.ReelType;
import dev.mrturtle.reel.rod.RodType;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

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
	public static final HashMap<Identifier, FishPattern> FISH_PATTERNS = new HashMap<>();

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(MOD_ID);
		PolymerResourcePackUtils.markAsRequired();
		PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register((builder) -> {
			loadReelData();
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
			for (String textureId : UIItem.TEXTURES.keySet()) {
				registerGuiModel(builder, textureId);
			}
			BasstiaryGui.registerModels(builder);
		});
		ServerLifecycleEvents.SERVER_STARTED.register((server -> {
			loadReelData();
			// This forces the models to be properly registered for the fishing rods on a server without AutoHost enabled
			// Something is going wrong here causing a model id mismatch between a non AutoHost server and a client with the mod
			((ModularFishingRodItem) ReelItems.MODULAR_FISHING_ROD_ITEM).registerModels();
			BasstiaryGui.registerModels(null);
		}));
		ReelItems.initialize();
		ReelBlocks.initialize();
		ReelEntities.initialize();
		GuiElements.initialize();
	}

	public static void loadReelData() {
		// Clear all previous data
		ROD_TYPES.clear();
		ROD_ITEMS_TO_IDS.clear();
		ROD_IDS_TO_ITEMS.clear();
		REEL_TYPES.clear();
		REEL_ITEMS_TO_IDS.clear();
		REEL_IDS_TO_ITEMS.clear();
		HOOK_TYPES.clear();
		HOOK_ITEMS_TO_IDS.clear();
		HOOK_IDS_TO_ITEMS.clear();
		FISH_TYPES.clear();
		FISH_CATEGORIES_TO_IDS.clear();
		FISH_IDS_TO_ITEMS.clear();
		FISH_CATEGORIES.clear();
		FISH_PATTERNS.clear();
		// Load new data
		LOGGER.info("Loading data...");
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			for (Path path : container.getRootPaths()) {
				// Component data
				loadDataFromPath(path, "reel_components/rods", container, RodType.CODEC, (id, rodType) -> {
					Item rodItem = Registries.ITEM.get(rodType.itemId());
					ROD_TYPES.put(id, rodType);
					ROD_ITEMS_TO_IDS.put(rodItem, id);
					ROD_IDS_TO_ITEMS.put(id, rodItem);
				});
				loadDataFromPath(path, "reel_components/reels", container, ReelType.CODEC, (id, reelType) -> {
					Item reelItem = Registries.ITEM.get(reelType.itemId());
					REEL_TYPES.put(id, reelType);
					REEL_ITEMS_TO_IDS.put(reelItem, id);
					REEL_IDS_TO_ITEMS.put(id, reelItem);
				});
				loadDataFromPath(path, "reel_components/hooks", container, HookType.CODEC, (id, hookType) -> {
					Item hookItem = Registries.ITEM.get(hookType.itemId());
					HOOK_TYPES.put(id, hookType);
					HOOK_ITEMS_TO_IDS.put(hookItem, id);
					HOOK_IDS_TO_ITEMS.put(id, hookItem);
				});
				// Fish data
				loadDataFromPath(path, "fish", container, FishType.CODEC, (id, fishType) -> {
					Item fishItem = Registries.ITEM.get(fishType.itemId());
					FISH_TYPES.put(id, fishType);
					for (FishType.CategoryData categoryData : fishType.categories()) {
						HashMap<Identifier, Integer> map = FISH_CATEGORIES_TO_IDS.getOrDefault(categoryData.id(), new HashMap<>());
						map.put(id, categoryData.weight());
						FISH_CATEGORIES_TO_IDS.put(categoryData.id(), map);
					}
					FISH_IDS_TO_ITEMS.put(id, fishItem);
				});
				loadDataFromPath(path, "fish_categories", container, FishCategory.CODEC, FISH_CATEGORIES::put);
				loadDataFromPath(path, "fish_patterns", container, FishPattern.CODEC, FISH_PATTERNS::put);
			}
		}
		LOGGER.info("Loaded data!");
	}

	public static <T> void loadDataFromPath(Path path, String directory, ModContainer container, Codec<T> CODEC, BiConsumer<Identifier, T> runnable) {
		Path directoryPath = path.resolve("data/" + container.getMetadata().getId() + "/" + directory);
		if (!Files.exists(directoryPath))
			return;
		try (Stream<Path> paths = Files.walk(directoryPath)) {
			paths.parallel().filter((filePath) -> filePath.toString().endsWith(".json")).forEach((filePath) -> {
				try (BufferedReader reader = Files.newBufferedReader(filePath)) {
					String fileName = filePath.getFileName().toString();
					String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
					T type = CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow(false, (message) -> {}).getFirst();
					runnable.accept(new Identifier(container.getMetadata().getId(), nameWithoutExtension), type);
				} catch (IOException e) {
					LOGGER.error("Failed to parse type {}", filePath);
				}
			});
		} catch (IOException e) {
			LOGGER.error("Failed to read data from {}", container.getMetadata().getId());
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
	}

	public static void registerGuiModel(ResourcePackBuilder builder, String textureId) {
		String path = "assets/reel/models/gui/%s.json".formatted(textureId);
		if (builder.getData(path) != null)
			return;
		JsonObject model = new JsonObject();
		model.addProperty("parent", "item/handheld");
		JsonObject textures = new JsonObject();
		textures.addProperty("layer0", id("item/gui/%s".formatted(textureId)).toString());
		model.add("textures", textures);
		builder.addData(path, model.toString().getBytes(StandardCharsets.UTF_8));
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