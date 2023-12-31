package dev.mrturtle.reel.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.Optional;

import static net.minecraft.util.StringIdentifiable.createCodec;

public record FishCategory(Optional<Identifier> biomeRequirement, Optional<Identifier> dimensionRequirement, Optional<Boolean> weightedRequirement, Optional<Boolean> fireproofRequirement, Priority priority) {
	public static final Codec<Priority> PRIORITY_CODEC = createCodec(Priority::values);
	public static final Codec<FishCategory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.optionalFieldOf("biome_requirement").forGetter(FishCategory::biomeRequirement),
			Identifier.CODEC.optionalFieldOf("dimension_requirement").forGetter(FishCategory::dimensionRequirement),
			Codec.BOOL.optionalFieldOf("weighted_requirement").forGetter(FishCategory::weightedRequirement),
			Codec.BOOL.optionalFieldOf("fireproof_requirement").forGetter(FishCategory::fireproofRequirement),
			PRIORITY_CODEC.optionalFieldOf("priority", Priority.DEFAULT).forGetter(FishCategory::priority)
	).apply(instance, FishCategory::new));

	public enum Priority implements StringIdentifiable {
		LOW(0, "low"),
		DEFAULT(1, "default"),
		HIGH(2, "high");

		private final int rank;
		private final String name;

		Priority(int rank, String name) {
			this.rank = rank;
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		@Override
		public String asString() {
			return this.name;
		}

		public int toInt() {
			return this.rank;
		}
	}
}
