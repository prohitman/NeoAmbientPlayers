package com.prohitman.ambientplayers.datagen;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.datagen.data.*;
import com.prohitman.ambientplayers.loot.ModLootContextParamSets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = AmbientPlayers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DatagenEntrypoint {

    @SubscribeEvent
    static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModItemTagProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new ModEntityTypeTagProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(SpawnWithLootTableSubProvider::new, ModLootContextParamSets.SPAWN_WITH)), lookupProvider));
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider, new RegistrySetBuilder().add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifierBootstrap::bootstrap), Set.of(AmbientPlayers.MODID)));

        generator.addProvider(event.includeClient(), new ModModelProvider(output, fileHelper));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
    }
}
