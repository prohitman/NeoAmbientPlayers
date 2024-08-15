package com.prohitman.ambientplayers.registry;

import com.prohitman.ambientplayers.AmbientPlayers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(AmbientPlayers.MODID);

    public static final DeferredItem<Item> PASSIVE_WANDERER_SPAWN_EGG = REGISTER.register("passive_wanderer_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.PASSIVE_WANDERER, 0x66AAFF, 0xAAAA99, new Item.Properties()));
    public static final DeferredItem<Item> PASSIVE_MERCENARY_SPAWN_EGG = REGISTER.register("passive_mercenary_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.PASSIVE_MERCENARY, 0x66AAFF, 0xAAAA99, new Item.Properties()));
    public static final DeferredItem<Item> PASSIVE_RANGER_SPAWN_EGG = REGISTER.register("passive_ranger_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.PASSIVE_RANGER, 0x66AAFF, 0xAAAA99, new Item.Properties()));
    public static final DeferredItem<Item> AGGRESSIVE_WANDERER_SPAWN_EGG = REGISTER.register("aggressive_wanderer_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.AGGRESSIVE_WANDERER, 0xCC3355, 0x333333, new Item.Properties()));
    public static final DeferredItem<Item> AGGRESSIVE_MERCENARY_SPAWN_EGG = REGISTER.register("aggressive_mercenary_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.AGGRESSIVE_MERCENARY, 0xCC3355, 0x333333, new Item.Properties()));
    public static final DeferredItem<Item> AGGRESSIVE_RANGER_SPAWN_EGG = REGISTER.register("aggressive_ranger_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.AGGRESSIVE_RANGER, 0xCC3355, 0x333333, new Item.Properties()));
    public static final DeferredItem<Item> BUILDER_SPAWN_EGG = REGISTER.register("builder_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.BUILDER, 0x6688CC, 0xCCCCCC, new Item.Properties()));
    public static final DeferredItem<Item> MISCREANT_SPAWN_EGG = REGISTER.register("miscreant_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.MISCREANT, 0x555555, 0xBB5555, new Item.Properties()));
}
