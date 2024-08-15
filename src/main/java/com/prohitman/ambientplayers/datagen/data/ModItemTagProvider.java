package com.prohitman.ambientplayers.datagen.data;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, CompletableFuture.completedFuture(TagLookup.empty()), AmbientPlayers.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModItemTags.C_MELEE_WEAPONS).addTags(ItemTags.SWORDS, Tags.Items.TOOLS_SPEAR).addOptionalTag(ModItemTags.C_SWORDS);
        tag(ModItemTags.C_RANGED_WEAPONS).addTags(Tags.Items.TOOLS_BOW, Tags.Items.TOOLS_CROSSBOW);
        tag(ModItemTags.C_WEAPONS).addTags(ModItemTags.C_MELEE_WEAPONS, ModItemTags.C_RANGED_WEAPONS);
        tag(ModItemTags.C_CONVENTIONAL_TOOLS).addTags(ItemTags.AXES, ItemTags.HOES, ItemTags.SHOVELS, ItemTags.PICKAXES);
        tag(ModItemTags.C_SHULKER_BOXES)
                .add(
                        Items.SHULKER_BOX,
                        Items.WHITE_SHULKER_BOX,
                        Items.ORANGE_SHULKER_BOX,
                        Items.MAGENTA_SHULKER_BOX,
                        Items.LIGHT_BLUE_SHULKER_BOX,
                        Items.YELLOW_SHULKER_BOX,
                        Items.LIME_SHULKER_BOX,
                        Items.PINK_SHULKER_BOX,
                        Items.GRAY_SHULKER_BOX,
                        Items.LIGHT_GRAY_SHULKER_BOX,
                        Items.CYAN_SHULKER_BOX,
                        Items.PURPLE_SHULKER_BOX,
                        Items.BLUE_SHULKER_BOX,
                        Items.BROWN_SHULKER_BOX,
                        Items.GREEN_SHULKER_BOX,
                        Items.RED_SHULKER_BOX,
                        Items.BLACK_SHULKER_BOX
                );
        tag(ModItemTags.BUILDER_TRADES).addTag(Tags.Items.COBBLESTONES);
        CreativeModeTab opTab = BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.OP_BLOCKS);
        opTab.buildContents(new CreativeModeTab.ItemDisplayParameters(FeatureFlags.REGISTRY.allFlags(), true, pProvider));
        tag(ModItemTags.UNOBTAINABLE)
                .add(
                        Items.POTION,
                        Items.SPLASH_POTION,
                        Items.LINGERING_POTION,
                        Items.TIPPED_ARROW,
                        Items.SUSPICIOUS_STEW,
                        Items.WRITTEN_BOOK,
                        Items.FILLED_MAP,
                        Items.ENCHANTED_BOOK,
                        Items.KNOWLEDGE_BOOK,
                        Items.FIREWORK_ROCKET,
                        Items.FIREWORK_STAR,
                        Items.SPAWNER
                )
                .add(BuiltInRegistries.ITEM.stream().filter(item -> item instanceof SpawnEggItem).toArray(Item[]::new))
                .add(
                        opTab.getDisplayItems().stream()
                                .map(ItemStack::getItem)
                                .distinct()
                                .toArray(Item[]::new)
                );
        tag(ModItemTags.SHOULD_NOT_SPAWN_WITH)
                .addTag(ModItemTags.UNOBTAINABLE)
                .addTag(ModItemTags.C_SHULKER_BOXES)
                .addTag(Tags.Items.ORES)
                .addTag(Tags.Items.STORAGE_BLOCKS)
                .add(
                        Items.NETHERITE_SCRAP,
                        Items.NETHERITE_INGOT,
                        Items.NETHER_WART,
                        Items.NETHER_WART_BLOCK,
                        Items.RESPAWN_ANCHOR,
                        Items.WITHER_ROSE,
                        Items.WITHER_SKELETON_SKULL,
                        Items.DRAGON_EGG,
                        Items.ELYTRA,
                        Items.SHULKER_SHELL,
                        Items.SCULK,
                        Items.SCULK_SHRIEKER,
                        Items.SCULK_CATALYST,
                        Items.SCULK_VEIN,
                        Items.SCULK_SENSOR,
                        Items.CALIBRATED_SCULK_SENSOR,
                        Items.END_CRYSTAL,
                        Items.END_ROD,
                        Items.END_STONE,
                        Items.CHORUS_PLANT,
                        Items.CHORUS_FLOWER,
                        Items.END_PORTAL_FRAME,
                        Items.ENDER_CHEST,
                        Items.ENCHANTING_TABLE,
                        Items.BEE_NEST,
                        Items.BUDDING_AMETHYST,
                        Items.FROGSPAWN,
                        Items.TNT
                );
        tag(ModItemTags.BUILDER_BLACKLIST)
                .addTag(ModItemTags.SHOULD_NOT_SPAWN_WITH)
                .add(
                        Items.SNIFFER_EGG,
                        Items.TURTLE_EGG
                );
    }
}
