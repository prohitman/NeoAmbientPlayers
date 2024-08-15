package com.prohitman.ambientplayers.registry;

import com.prohitman.ambientplayers.AmbientPlayers;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class MemoryTypeRegistry {
    public static final DeferredRegister<MemoryModuleType<?>> REGISTER = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, AmbientPlayers.MODID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> TIRED = register("tired");
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> PANICKED_BY = register("panicked_by");

    private static <T> DeferredHolder<MemoryModuleType<?>, MemoryModuleType<T>> register(String id) {
        return REGISTER.register(id, () -> new MemoryModuleType<>(Optional.empty()));
    }
}
