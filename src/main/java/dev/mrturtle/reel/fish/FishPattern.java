package dev.mrturtle.reel.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record FishPattern(List<ChunkData> chunks) {
    public static final Codec<FishPattern> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(ChunkData.CODEC).fieldOf("chunks").forGetter(FishPattern::chunks)
    ).apply(instance, FishPattern::new));

    public boolean isAngleIn(int angle) {
        for (ChunkData chunk : chunks) {
            if (chunk.isAngleIn(angle))
                return true;
        }
        return false;
    }

    public record ChunkData(int startAngle, int endAngle) {
        public static final Codec<ChunkData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("start_angle").forGetter(ChunkData::startAngle),
                Codec.INT.fieldOf("end_angle").forGetter(ChunkData::endAngle)
        ).apply(instance, ChunkData::new));

        public boolean isAngleIn(int angle) {
            if (startAngle > endAngle)
                return startAngle < angle || endAngle > angle;
            return startAngle < angle && endAngle > angle;
        }
    }
}
