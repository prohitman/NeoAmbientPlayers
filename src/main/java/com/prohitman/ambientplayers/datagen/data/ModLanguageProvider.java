package com.prohitman.ambientplayers.datagen.data;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.registry.EntityTypeRegistry;
import com.prohitman.ambientplayers.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AmbientPlayers.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(EntityTypeRegistry.PASSIVE_WANDERER.get(), "Passive Wanderer");
        add(EntityTypeRegistry.PASSIVE_MERCENARY.get(), "Passive Mercenary");
        add(EntityTypeRegistry.PASSIVE_RANGER.get(), "Passive Ranger");
        add(EntityTypeRegistry.AGGRESSIVE_WANDERER.get(), "Aggressive Wanderer");
        add(EntityTypeRegistry.AGGRESSIVE_MERCENARY.get(), "Aggressive Mercenary");
        add(EntityTypeRegistry.AGGRESSIVE_RANGER.get(), "Aggressive Ranger");
        add(EntityTypeRegistry.BUILDER.get(), "Builder");
        add(EntityTypeRegistry.MISCREANT.get(), "Miscreant");

        add(ItemRegistry.PASSIVE_WANDERER_SPAWN_EGG.get(), "Passive Wanderer Spawn Egg");
        add(ItemRegistry.PASSIVE_MERCENARY_SPAWN_EGG.get(), "Passive Mercenary Spawn Egg");
        add(ItemRegistry.PASSIVE_RANGER_SPAWN_EGG.get(), "Passive Ranger Spawn Egg");
        add(ItemRegistry.AGGRESSIVE_WANDERER_SPAWN_EGG.get(), "Aggressive Wanderer Spawn Egg");
        add(ItemRegistry.AGGRESSIVE_MERCENARY_SPAWN_EGG.get(), "Aggressive Mercenary Spawn Egg");
        add(ItemRegistry.AGGRESSIVE_RANGER_SPAWN_EGG.get(), "Aggressive Ranger Spawn Egg");
        add(ItemRegistry.BUILDER_SPAWN_EGG.get(), "Builder Spawn Egg");
        add(ItemRegistry.MISCREANT_SPAWN_EGG.get(), "Miscreant Spawn Egg");
    }
}
