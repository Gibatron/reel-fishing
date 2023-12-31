package dev.mrturtle.reel.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.List;

public record FishType(Identifier itemId, float speed, float temper, Identifier minigamePatternId, WeightData weight, List<CategoryData> categories) {
    public static final Codec<FishType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").forGetter(FishType::itemId),
            Codec.FLOAT.fieldOf("speed").forGetter(FishType::speed),
            Codec.FLOAT.fieldOf("temper").forGetter(FishType::temper),
            Identifier.CODEC.fieldOf("minigame_pattern").forGetter(FishType::minigamePatternId),
            WeightData.CODEC.fieldOf("weight").forGetter(FishType::weight),
            Codec.list(CategoryData.CODEC).fieldOf("categories").forGetter(FishType::categories)
    ).apply(instance, FishType::new));

    public boolean isInCategory(Identifier categoryId) {
        for (CategoryData category : categories) {
            if (category.id == categoryId)
                return true;
        }
        return false;
    }

    public record WeightData(int min, int max) {
        public static final Codec<WeightData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("min").forGetter(WeightData::min),
                Codec.INT.fieldOf("max").forGetter(WeightData::max)
        ).apply(instance, WeightData::new));

        public float getRandomWeight(Random random) {
            return random.nextBetween(min, max) + random.nextFloat();
        }
    }

    public record CategoryData(Identifier id, int weight) {
        public static final Codec<CategoryData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(CategoryData::id),
                Codec.INT.fieldOf("weight").forGetter(CategoryData::weight)
        ).apply(instance, CategoryData::new));
    }
}