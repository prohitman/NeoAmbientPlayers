package com.prohitman.ambientplayers.registry;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.loot.functions.SetRandomFireworkStarFunction;
import com.prohitman.ambientplayers.loot.functions.SetRandomPotionFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LootItemFunctionTypeRegistry {
    public static final DeferredRegister<LootItemFunctionType<?>> REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, AmbientPlayers.MODID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<?>> SET_RANDOM_POTION = REGISTER.register("set_random_potion", () -> new LootItemFunctionType(SetRandomPotionFunction.CODEC));
    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<?>> SET_RANDOM_FIREWORK_STAR = REGISTER.register("set_random_firework_star", () -> new LootItemFunctionType(SetRandomFireworkStarFunction.CODEC));
}
