package dev.mrturtle.reel;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ReelItemTags {
    public static final TagKey<Item> FISH_WITH_BONES = TagKey.of(RegistryKeys.ITEM, ReelFishing.id("fish_with_bones"));
}
