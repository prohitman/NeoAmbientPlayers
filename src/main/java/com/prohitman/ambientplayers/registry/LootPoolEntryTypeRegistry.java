package com.prohitman.ambientplayers.registry;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.loot.entries.AnyItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LootPoolEntryTypeRegistry {
    public static final DeferredRegister<LootPoolEntryType> REGISTER = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, AmbientPlayers.MODID);

    public static final DeferredHolder<LootPoolEntryType, LootPoolEntryType> ANY_ITEM = REGISTER.register("any_item", () -> new LootPoolEntryType(AnyItemEntry.CODEC));
}
