package com.prohitman.ambientplayers.registry;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.entity.AggressivePlayerMob;
import com.prohitman.ambientplayers.entity.BuilderMob;
import com.prohitman.ambientplayers.entity.PassivePlayerMob;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(Registries.ENTITY_TYPE, AmbientPlayers.MODID);
    private static final int TRACK_DISTANCE = 10;

    public static final DeferredHolder<EntityType<?>, EntityType<PassivePlayerMob>> PASSIVE_WANDERER = REGISTER.register("passive_wanderer", () -> EntityType.Builder.of(PassivePlayerMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
    public static final DeferredHolder<EntityType<?>, EntityType<PassivePlayerMob>> PASSIVE_MERCENARY = REGISTER.register("passive_mercenary", () -> EntityType.Builder.of(PassivePlayerMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
    public static final DeferredHolder<EntityType<?>, EntityType<PassivePlayerMob>> PASSIVE_RANGER = REGISTER.register("passive_ranger", () -> EntityType.Builder.of(PassivePlayerMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
    public static final DeferredHolder<EntityType<?>, EntityType<AggressivePlayerMob>> AGGRESSIVE_WANDERER = REGISTER.register("aggressive_wanderer", () -> EntityType.Builder.of(AggressivePlayerMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
    public static final DeferredHolder<EntityType<?>, EntityType<AggressivePlayerMob>> AGGRESSIVE_MERCENARY = REGISTER.register("aggressive_mercenary", () -> EntityType.Builder.of(AggressivePlayerMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
    public static final DeferredHolder<EntityType<?>, EntityType<AggressivePlayerMob>> AGGRESSIVE_RANGER = REGISTER.register("aggressive_ranger", () -> EntityType.Builder.of(AggressivePlayerMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
    public static final DeferredHolder<EntityType<?>, EntityType<BuilderMob>> BUILDER = REGISTER.register("builder", () -> EntityType.Builder.of(BuilderMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
    public static final DeferredHolder<EntityType<?>, EntityType<AggressivePlayerMob>> MISCREANT = REGISTER.register("miscreant", () -> EntityType.Builder.of(AggressivePlayerMob::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_DISTANCE).updateInterval(2).build(""));
}
