package com.prohitman.ambientplayers.mixin;

import com.google.common.collect.BiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(LootContextParamSets.class)
public interface LootContextParamSetsAccessor {
    //@Accessor
   // public BiMap<ResourceLocation, LootContextParamSet> getREGISTRY();

    @Invoker
    static LootContextParamSet invokeRegister(String pRegistryName, Consumer<LootContextParamSet.Builder> pBuilderConsumer) {
        throw new AssertionError("Implemented via mixin");
    }
}
