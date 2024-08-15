package com.prohitman.ambientplayers.datagen.data;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.registry.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModModelProvider extends ItemModelProvider {
    public ModModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AmbientPlayers.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEgg(ItemRegistry.PASSIVE_WANDERER_SPAWN_EGG);
        spawnEgg(ItemRegistry.PASSIVE_MERCENARY_SPAWN_EGG);
        spawnEgg(ItemRegistry.PASSIVE_RANGER_SPAWN_EGG);
        spawnEgg(ItemRegistry.AGGRESSIVE_WANDERER_SPAWN_EGG);
        spawnEgg(ItemRegistry.AGGRESSIVE_MERCENARY_SPAWN_EGG);
        spawnEgg(ItemRegistry.AGGRESSIVE_RANGER_SPAWN_EGG);
        spawnEgg(ItemRegistry.BUILDER_SPAWN_EGG);
        spawnEgg(ItemRegistry.MISCREANT_SPAWN_EGG);
    }

    protected ItemModelBuilder spawnEgg(Supplier<Item> item) {
        String path = BuiltInRegistries.ITEM.getKey(item.get()).getPath();
        return withExistingParent(path, mcLoc("item/template_spawn_egg"));
    }
}
