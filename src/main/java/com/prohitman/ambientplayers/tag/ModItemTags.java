package com.prohitman.ambientplayers.tag;

import com.prohitman.ambientplayers.AmbientPlayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> BUILDER_TRADES = create("builder_trades");
    public static final TagKey<Item> BUILDER_WHITELIST = create("builder_whitelist");
    public static final TagKey<Item> BUILDER_BLACKLIST = create("builder_blacklist");
    public static final TagKey<Item> SHOULD_NOT_SPAWN_WITH = create("should_not_spawn_with");
    public static final TagKey<Item> UNOBTAINABLE = create("unobtainable");

    public static final TagKey<Item> C_SHULKER_BOXES = createCommonTag("shulker_boxes");

    public static final TagKey<Item> C_WEAPONS = createCommonTag("weapons");
    public static final TagKey<Item> C_MELEE_WEAPONS = createCommonTag("melee_weapons");
    public static final TagKey<Item> C_RANGED_WEAPONS = createCommonTag("ranged_weapons");
    public static final TagKey<Item> C_SWORDS = createCommonTag("swords");
    public static final TagKey<Item> C_CONVENTIONAL_TOOLS = createCommonTag("conventional_tools");

    private static TagKey<Item> create(String tag) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AmbientPlayers.MODID, tag));
    }

    private static TagKey<Item> createNeoForgeTag(String tag) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("neoforge", tag));
    }

    private static TagKey<Item> createCommonTag(String tag) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", tag));
    }
}
