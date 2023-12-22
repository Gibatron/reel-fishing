package dev.mrturtle.reel.rod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record HookType(Identifier itemId, float power, int size, String category) {
    public static final Codec<HookType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").forGetter(HookType::itemId),
            Codec.FLOAT.fieldOf("power").forGetter(HookType::power),
            Codec.INT.fieldOf("size").forGetter(HookType::size),
            Codec.STRING.fieldOf("category").forGetter(HookType::category)
    ).apply(instance, HookType::new));
}
