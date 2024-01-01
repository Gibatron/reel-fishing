package dev.mrturtle.reel;

import dev.mrturtle.reel.block.RodTableBlock;
import dev.mrturtle.reel.item.ModeledPolymerBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class ReelBlocks {
    public static final Block ROD_TABLE_BLOCK = register(new RodTableBlock(AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).instrument(Instrument.BASS).strength(2.5F).sounds(BlockSoundGroup.WOOD).burnable()), "rod_table");

    public static void initialize() {}

    public static <T extends Block> T register(T block, String id) {
        return Registry.register(Registries.BLOCK, ReelFishing.id(id), block);
    }
}
