package com.prohitman.ambientplayers.datagen.data;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.loot.ModItemSubPredicates;
import com.prohitman.ambientplayers.loot.ModLootContextParamSets;
import com.prohitman.ambientplayers.loot.entries.AnyItemEntry;
import com.prohitman.ambientplayers.loot.functions.SetRandomFireworkStarFunction;
import com.prohitman.ambientplayers.loot.functions.SetRandomPotionFunction;
import com.prohitman.ambientplayers.loot.rpredicates.RAnyPredicate;
import com.prohitman.ambientplayers.loot.rpredicates.RArmorItemPredicate;
import com.prohitman.ambientplayers.loot.rpredicates.RBlockItemPredicate;
import com.prohitman.ambientplayers.loot.rpredicates.RNotPredicate;
import com.prohitman.ambientplayers.tag.ModItemTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.function.BiConsumer;

public class SpawnWithLootTableSubProvider implements LootTableSubProvider {
    public static final ResourceLocation ITEM_WITH_EFFECTS = spawnWithId("common/item_with_effects");
    public static final ResourceLocation MAP = spawnWithId("common/map");
    public static final ResourceLocation FIREWORK = spawnWithId("common/firework");
    public static final ResourceLocation RANDOM_HELMET = spawnWithId("common/random_helmet");
    public static final ResourceLocation RANDOM_CHESTPLATE = spawnWithId("common/random_chestplate");
    public static final ResourceLocation RANDOM_LEGGINGS = spawnWithId("common/random_leggings");
    public static final ResourceLocation RANDOM_BOOTS = spawnWithId("common/random_boots");
    public static final ResourceLocation RANDOM_NON_WEAPON_NON_TOOL_NON_BLOCK = spawnWithId("common/random_non_weapon_non_tool");
    public static final ResourceLocation RANDOM_MELEE_WEAPON_OR_TOOL = spawnWithId("common/random_melee_weapon_or_tool");
    public static final ResourceLocation RANDOM_RANGED_WEAPON = spawnWithId("common/random_ranged_weapon");
    public static final ResourceLocation BUILDER_OFFER = spawnWithId("common/builder_offer");

    protected final HolderLookup.Provider registries;

    public SpawnWithLootTableSubProvider(HolderLookup.Provider registries){
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer pOutput) {
        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, ITEM_WITH_EFFECTS), LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(
                LootPool.lootPool().name("base")
                        .add(
                                LootItem.lootTableItem(Items.POTION)
                                        .apply(SetRandomPotionFunction.randomPotion())
                        )
                        .add(
                                LootItem.lootTableItem(Items.SPLASH_POTION)
                                        .apply(SetRandomPotionFunction.randomPotion())
                        )
                        .add(
                                LootItem.lootTableItem(Items.LINGERING_POTION)
                                        .apply(SetRandomPotionFunction.randomPotion())
                        )
                        .add(
                                LootItem.lootTableItem(Items.TIPPED_ARROW)
                                        .apply(SetRandomPotionFunction.randomPotion())
                        )
                        .add(
                                LootItem.lootTableItem(Items.SUSPICIOUS_STEW)
                                        .apply(
                                                SetStewEffectFunction.stewEffect()
                                                        .withEffect(MobEffects.ABSORPTION, UniformGenerator.between(60, 120))
                                                        .withEffect(MobEffects.DAMAGE_BOOST, UniformGenerator.between(60, 120))
                                                        .withEffect(MobEffects.FIRE_RESISTANCE, UniformGenerator.between(60, 120))
                                                        .withEffect(MobEffects.WATER_BREATHING, UniformGenerator.between(60, 120))
                                                        .withEffect(MobEffects.JUMP, UniformGenerator.between(60, 120))
                                                        .withEffect(MobEffects.MOVEMENT_SPEED, UniformGenerator.between(60, 120))
                                                        .withEffect(MobEffects.REGENERATION, UniformGenerator.between(60, 120))
                                        )
                        )
        ));

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, FIREWORK), LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(
                LootPool.lootPool().name("base")
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetComponentsFunction.setComponent(DataComponents.FIREWORKS, createFireworkFlightTag(1)))
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetComponentsFunction.setComponent(DataComponents.FIREWORKS, createFireworkFlightTag(2)))
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetComponentsFunction.setComponent(DataComponents.FIREWORKS, createFireworkFlightTag(3)))
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetRandomFireworkStarFunction.randomFireworkStar())
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetRandomFireworkStarFunction.randomFireworkStar())
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetRandomFireworkStarFunction.randomFireworkStar())
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetComponentsFunction.setComponent(DataComponents.FIREWORKS, createFireworkFlightTag(1)))
                                        .apply(SetRandomFireworkStarFunction.randomFireworkStar())
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetComponentsFunction.setComponent(DataComponents.FIREWORKS, createFireworkFlightTag(2)))
                                        .apply(SetRandomFireworkStarFunction.randomFireworkStar())
                        )
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                                        .apply(SetComponentsFunction.setComponent(DataComponents.FIREWORKS, createFireworkFlightTag(3)))
                                        .apply(SetRandomFireworkStarFunction.randomFireworkStar())
                        )
        ));

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, MAP), LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(
                LootPool.lootPool().name("base")
                        .add(
                                LootItem.lootTableItem(Items.MAP)
                                        .apply(
                                                ExplorationMapFunction.makeExplorationMap()
                                                        .setDestination(StructureTags.ON_TREASURE_MAPS)
                                                        .setMapDecoration(MapDecorationTypes.RED_X)
                                                        .setSearchRadius(10)
                                        )
                        )
                        .add(
                                LootItem.lootTableItem(Items.MAP)
                                        .apply(
                                                ExplorationMapFunction.makeExplorationMap()
                                                        .setDestination(StructureTags.ON_WOODLAND_EXPLORER_MAPS)
                                                        .setMapDecoration(MapDecorationTypes.WOODLAND_MANSION)
                                                        .setSearchRadius(10)
                                        )
                        )
                        .add(
                                LootItem.lootTableItem(Items.MAP)
                                        .apply(
                                                ExplorationMapFunction.makeExplorationMap()
                                                        .setDestination(StructureTags.ON_OCEAN_EXPLORER_MAPS)
                                                        .setMapDecoration(MapDecorationTypes.WOODLAND_MANSION)
                                                        .setSearchRadius(10)
                                        )
                        )
                        .add(
                                LootItem.lootTableItem(Items.MAP)
                                        .apply(
                                                ExplorationMapFunction.makeExplorationMap()
                                                        .setDestination(StructureTags.RUINED_PORTAL)
                                                        .setMapDecoration(MapDecorationTypes.TARGET_X)
                                                        .setSearchRadius(10)
                                        )
                        )
                        .add(
                                AlternativesEntry.alternatives(
                                        createVillageMap(MapDecorationTypes.BLUE_BANNER)
                                                .when(LootItemRandomChanceCondition.randomChance(0.166f)),
                                        createVillageMap(MapDecorationTypes.BROWN_BANNER)
                                                .when(LootItemRandomChanceCondition.randomChance(0.166f)),
                                        createVillageMap(MapDecorationTypes.GREEN_BANNER)
                                                .when(LootItemRandomChanceCondition.randomChance(0.166f)),
                                        createVillageMap(MapDecorationTypes.ORANGE_BANNER)
                                                .when(LootItemRandomChanceCondition.randomChance(0.166f)),
                                        createVillageMap(MapDecorationTypes.LIME_BANNER)
                                                .when(LootItemRandomChanceCondition.randomChance(0.166f)),
                                        createVillageMap(MapDecorationTypes.PINK_BANNER)
                                                .when(LootItemRandomChanceCondition.randomChance(0.166f))
                                )
                        )
        ));

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_HELMET),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.ARMOR_ITEM_PREDICATE.get(), RArmorItemPredicate.ofType(ArmorItem.Type.HELMET)).build())
                        //        .filter(ArmorItemPredicate.ofType(ArmorItem.Type.HELMET))
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_CHESTPLATE),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.ARMOR_ITEM_PREDICATE.get(), RArmorItemPredicate.ofType(ArmorItem.Type.CHESTPLATE)).build())
                                //.filter(ArmorItemPredicate.ofType(ArmorItem.Type.CHESTPLATE))
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_LEGGINGS),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.ARMOR_ITEM_PREDICATE.get(), RArmorItemPredicate.ofType(ArmorItem.Type.LEGGINGS)).build())
                                //.filter(ArmorItemPredicate.ofType(ArmorItem.Type.LEGGINGS))
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_BOOTS),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.ARMOR_ITEM_PREDICATE.get(), RArmorItemPredicate.ofType(ArmorItem.Type.BOOTS)).build())

                                //.filter(ArmorItemPredicate.ofType(ArmorItem.Type.BOOTS))
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_NON_WEAPON_NON_TOOL_NON_BLOCK),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.NOT_PREDICATE.get(), RNotPredicate.not(ItemPredicate.Builder.item().of(ModItemTags.C_WEAPONS).build())).build())
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.NOT_PREDICATE.get(), RNotPredicate.not(ItemPredicate.Builder.item().of(ModItemTags.C_CONVENTIONAL_TOOLS).build())).build())
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.NOT_PREDICATE.get(), RNotPredicate.not(ItemPredicate.Builder.item().of(ModItemTags.SHOULD_NOT_SPAWN_WITH).build())).build())

                                //.filter(NotPredicate.not(ItemPredicate.Builder.item().of(ModItemTags.C_CONVENTIONAL_TOOLS).build()))
                                //.filter(NotPredicate.not(ItemPredicate.Builder.item().of(ModItemTags.SHOULD_NOT_SPAWN_WITH).build()))
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.NOT_PREDICATE.get(), RNotPredicate.not(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.BLOCK_ITEM_PREDICATE.get(), RBlockItemPredicate.getInstance()).build())).build())

                                //.filter(NotPredicate.not(RBlockItemPredicate.getInstance()))
                                .setWeight(BuiltInRegistries.ITEM.size()/2)
                        )
                        .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, ITEM_WITH_EFFECTS)).setWeight(1))
                        .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, FIREWORK)).setWeight(1))
                        .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, MAP)).setWeight(1))
                        .add(
                                LootItem.lootTableItem(Items.FIREWORK_STAR)
                                        .apply(SetRandomFireworkStarFunction.randomFireworkStar()).setWeight(1)
                        )
                        .add(
                                LootItem.lootTableItem(Items.BOOK)
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)).setWeight(1)
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_MELEE_WEAPON_OR_TOOL),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.ANY_PREDICATE.get(), RAnyPredicate.any(ItemPredicate.Builder.item().of(ModItemTags.C_MELEE_WEAPONS).build(),
                                                ItemPredicate.Builder.item().of(ModItemTags.C_CONVENTIONAL_TOOLS).build())).build())
/*                                .filter(AnyPredicate.any(
                                        ItemPredicate.Builder.item().of(ModItemTags.C_MELEE_WEAPONS).build(),
                                        ItemPredicate.Builder.item().of(ModItemTags.C_CONVENTIONAL_TOOLS).build()
                                ))*/
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_RANGED_WEAPON),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().of(ModItemTags.C_RANGED_WEAPONS).build())
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, BUILDER_OFFER),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("base")
                        .add(AnyItemEntry.builder()
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.BLOCK_ITEM_PREDICATE.get(), RBlockItemPredicate.getInstance()).build())
                                //.filter(RBlockItemPredicate.MAP_INSTANCE)
                                .filter(ItemPredicate.Builder.item().withSubPredicate(ModItemSubPredicates.NOT_PREDICATE.get(), RNotPredicate.not(ItemPredicate.Builder.item().of(ModItemTags.BUILDER_BLACKLIST).build())).build())

                                //.filter(NotPredicate.not(ItemPredicate.Builder.item().of(ModItemTags.BUILDER_BLACKLIST).build()))
                        )
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("passive_wanderer")), wanderer());
        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("passive_mercenary")), mercenary());
        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("passive_ranger")), ranger());

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("aggressive_wanderer")), wanderer());
        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("aggressive_mercenary")), mercenary());
        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("aggressive_ranger")), ranger());

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("builder")),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("mainhand")
                        .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, BUILDER_OFFER)))
                )
        );

        pOutput.accept(ResourceKey.create(Registries.LOOT_TABLE, spawnWithId("miscreant")),
                LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("mainhand")
                        .add(LootItem.lootTableItem(Items.NETHERITE_SWORD)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                        .when(LootItemRandomChanceCondition.randomChance(0.125f))
                                )
                        )
                ).withPool(LootPool.lootPool().name("offhand")
                        .add(LootItem.lootTableItem(Items.SHIELD)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                        .when(LootItemRandomChanceCondition.randomChance(0.125f))
                                )
                        )
                ).withPool(LootPool.lootPool().name("head")
                        .add(LootItem.lootTableItem(Items.NETHERITE_HELMET)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                        .when(LootItemRandomChanceCondition.randomChance(0.125f))
                                )
                        )
                ).withPool(LootPool.lootPool().name("chest")
                        .add(LootItem.lootTableItem(Items.NETHERITE_CHESTPLATE)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                        .when(LootItemRandomChanceCondition.randomChance(0.125f))
                                )
                        )
                ).withPool(LootPool.lootPool().name("legs")
                        .add(LootItem.lootTableItem(Items.NETHERITE_LEGGINGS)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                        .when(LootItemRandomChanceCondition.randomChance(0.125f))
                                )
                        )
                ).withPool(LootPool.lootPool().name("feet")
                        .add(LootItem.lootTableItem(Items.NETHERITE_BOOTS)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                        .when(LootItemRandomChanceCondition.randomChance(0.125f))
                                )
                        )
                )
        );
    }

    protected LootTable.Builder wanderer() {
        return LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("mainhand")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_NON_WEAPON_NON_TOOL_NON_BLOCK)))
                .add(EmptyLootItem.emptyItem())
        );
    }
    
    protected LootTable.Builder mercenary() {
        return LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("mainhand")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_MELEE_WEAPON_OR_TOOL))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("head")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_HELMET))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("chest")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_CHESTPLATE))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("legs")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_LEGGINGS))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("feet")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_BOOTS))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        );
    }
    
    protected LootTable.Builder ranger() {
        return LootTable.lootTable().setParamSet(ModLootContextParamSets.SPAWN_WITH).withPool(LootPool.lootPool().name("mainhand")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_RANGED_WEAPON))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("head")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_HELMET))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("chest")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_CHESTPLATE))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("legs")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_LEGGINGS))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        ).withPool(LootPool.lootPool().name("feet")
                .add(NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, RANDOM_BOOTS))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                        )
                )
        );
    }

    protected LootPoolSingletonContainer.Builder<?> createVillageMap(Holder<MapDecorationType> marker) {
        return LootItem.lootTableItem(Items.MAP).apply(
                ExplorationMapFunction.makeExplorationMap()
                        .setDestination(StructureTags.VILLAGE)
                        .setMapDecoration(marker)
                        .setSearchRadius(10)
        );
    }

    protected Fireworks createFireworkFlightTag(int flight) {
        return new Fireworks(flight, List.of(FireworkExplosion.DEFAULT));
    }
    
    protected static ResourceLocation spawnWithId(String id) {
        return AmbientPlayers.id("spawn_with/" + id);
    }
}
