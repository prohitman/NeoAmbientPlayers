package com.prohitman.ambientplayers.datagen.data;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.registry.EntityTypeRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifierBootstrap {
    public static final ResourceKey<BiomeModifier> PASSIVE_WANDERER_SPAWNS = key("passive_wanderer_spawns");
    public static final ResourceKey<BiomeModifier> PASSIVE_MERCENARY_SPAWNS = key("passive_mercenary_spawns");
    public static final ResourceKey<BiomeModifier> PASSIVE_RANGER_SPAWNS = key("passive_ranger_spawns");
    public static final ResourceKey<BiomeModifier> AGGRESSIVE_WANDERER_SPAWNS = key("aggressive_wanderer_spawns");
    public static final ResourceKey<BiomeModifier> AGGRESSIVE_MERCENARY_SPAWNS = key("aggressive_mercenary_spawns");
    public static final ResourceKey<BiomeModifier> AGGRESSIVE_RANGER_SPAWNS = key("aggressive_ranger_spawns");
    public static final ResourceKey<BiomeModifier> MISCREANT_SPAWNS = key("miscreant_spawns");

    public static void bootstrap(BootstrapContext<BiomeModifier> bootstapContext) {
        HolderGetter<Biome> biomeGetter = bootstapContext.lookup(Registries.BIOME);
        bootstapContext.register(PASSIVE_WANDERER_SPAWNS,
                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                        biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                        new MobSpawnSettings.SpawnerData(EntityTypeRegistry.PASSIVE_WANDERER.get(), 1, 1, 1)
                )
        );
        bootstapContext.register(PASSIVE_MERCENARY_SPAWNS,
                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                        biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                        new MobSpawnSettings.SpawnerData(EntityTypeRegistry.PASSIVE_MERCENARY.get(), 1, 1, 1)
                )
        );
        bootstapContext.register(PASSIVE_RANGER_SPAWNS,
                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                        biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                        new MobSpawnSettings.SpawnerData(EntityTypeRegistry.PASSIVE_RANGER.get(), 1, 1, 1)
                )
        );
        bootstapContext.register(AGGRESSIVE_WANDERER_SPAWNS,
                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                        biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                        new MobSpawnSettings.SpawnerData(EntityTypeRegistry.AGGRESSIVE_WANDERER.get(), 1, 1, 1)
                )
        );
        bootstapContext.register(AGGRESSIVE_MERCENARY_SPAWNS,
                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                        biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                        new MobSpawnSettings.SpawnerData(EntityTypeRegistry.AGGRESSIVE_MERCENARY.get(), 1, 1, 1)
                )
        );
        bootstapContext.register(AGGRESSIVE_RANGER_SPAWNS,
                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                        biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                        new MobSpawnSettings.SpawnerData(EntityTypeRegistry.AGGRESSIVE_RANGER.get(), 1, 1, 1)
                )
        );
        bootstapContext.register(MISCREANT_SPAWNS,
                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                        biomeGetter.getOrThrow(BiomeTags.IS_NETHER),
                        new MobSpawnSettings.SpawnerData(EntityTypeRegistry.MISCREANT.get(), 750, 1, 1)
                )
        );
    }

    private static ResourceKey<BiomeModifier> key(String id) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, AmbientPlayers.id(id));
    }
}
