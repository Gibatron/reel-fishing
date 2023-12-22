package dev.mrturtle.reel.rod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record RodType(Identifier itemId, float fieldOfView, float maxFishWeight) {
    public static final Codec<RodType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").forGetter(RodType::itemId),
            Codec.FLOAT.fieldOf("field_of_view").forGetter(RodType::fieldOfView),
            Codec.FLOAT.fieldOf("max_fish_weight").forGetter(RodType::maxFishWeight)
    ).apply(instance, RodType::new));
}
