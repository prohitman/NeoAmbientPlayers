package com.prohitman.ambientplayers.datagen.data;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.registry.EntityTypeRegistry;
import com.prohitman.ambientplayers.tag.ModEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, AmbientPlayers.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModEntityTypeTags.PASSIVE)
                .add(
                        EntityTypeRegistry.PASSIVE_WANDERER.get(),
                        EntityTypeRegistry.PASSIVE_MERCENARY.get(),
                        EntityTypeRegistry.PASSIVE_RANGER.get()
                );
        tag(ModEntityTypeTags.AGGRESSIVE)
                .add(
                        EntityTypeRegistry.AGGRESSIVE_WANDERER.get(),
                        EntityTypeRegistry.AGGRESSIVE_MERCENARY.get(),
                        EntityTypeRegistry.AGGRESSIVE_RANGER.get()
                );
        tag(ModEntityTypeTags.UNIQUE)
                .add(
                        EntityTypeRegistry.BUILDER.get(),
                        EntityTypeRegistry.MISCREANT.get()
                );
        tag(ModEntityTypeTags.WANDERER)
                .add(
                        EntityTypeRegistry.PASSIVE_WANDERER.get(),
                        EntityTypeRegistry.AGGRESSIVE_WANDERER.get()
                );
        tag(ModEntityTypeTags.MERCENARY)
                .add(
                        EntityTypeRegistry.PASSIVE_MERCENARY.get(),
                        EntityTypeRegistry.AGGRESSIVE_MERCENARY.get()
                );
        tag(ModEntityTypeTags.RANGER)
                .add(
                        EntityTypeRegistry.PASSIVE_RANGER.get(),
                        EntityTypeRegistry.AGGRESSIVE_RANGER.get()
                );
        tag(ModEntityTypeTags.AMBIENT_PLAYERS)
                .addTags(
                        ModEntityTypeTags.PASSIVE,
                        ModEntityTypeTags.AGGRESSIVE,
                        ModEntityTypeTags.UNIQUE
                );
        tag(ModEntityTypeTags.ATTACK_SPEED_BUFF)
                .addTag(
                        ModEntityTypeTags.MERCENARY
                );
    }
}
