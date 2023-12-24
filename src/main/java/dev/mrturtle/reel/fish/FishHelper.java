package dev.mrturtle.reel.fish;

import dev.mrturtle.reel.ReelFishing;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FishHelper {
	public static Identifier getCategoryFromConditions(RegistryEntry<Biome> biome, Identifier dimensionId) {
		Identifier bestCategory = null;
		FishCategory.Priority bestPriority = null;
		// Check every fish category to see if it is eligible to be caught
		for (Map.Entry<Identifier, FishCategory> entry : ReelFishing.FISH_CATEGORIES.entrySet()) {
			FishCategory category = entry.getValue();
			// Are we in the right dimension?
			if (category.dimensionRequirement().isPresent())
				if (!category.dimensionRequirement().get().equals(dimensionId))
					continue;
			// Are we in a matching biome?
			if (category.biomeRequirement().isPresent()) {
				var biomeTag = TagKey.of(RegistryKeys.BIOME, category.biomeRequirement().get());
				if (!biome.isIn(biomeTag))
					continue;
			}
			// This is an eligible category
			// Check if it has a higher priority than the best category, if it does, or we don't have a best category, continue
			if (bestCategory != null)
				if (bestPriority.toInt() > category.priority().toInt())
					continue;
			bestCategory = entry.getKey();
			bestPriority = category.priority();
		}
		return bestCategory;
	}

	public static Identifier getFishFromCategory(Identifier categoryId, Random random) {
		// Create a weighted pool to add the fish of the category to
		DataPool.Builder<Identifier> builder = DataPool.builder();
		HashMap<Identifier, Integer> fishMap = ReelFishing.FISH_CATEGORIES_TO_IDS.get(categoryId);
		// Add all the fish to the pool
		for (Map.Entry<Identifier, Integer> entry : fishMap.entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		// Get a random* one
		Optional<Identifier> fish = builder.build().getDataOrEmpty(random);
		return fish.orElse(null);
	}
}
