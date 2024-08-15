package com.prohitman.ambientplayers.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.GenericAttackTargetSensor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class AggressivePlayerMob extends PlayerMob {
    public AggressivePlayerMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        setAggressive(true);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public static AttributeSupplier.Builder createMiscreantAttributes() {
        return PlayerMob.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 64);
    }

    @Override
    public Brain<AggressivePlayerMob> getBrain() {
        return (Brain<AggressivePlayerMob>) super.getBrain();
    }

    @Override
    protected void addMoreSensors(Consumer<? super ExtendedSensor<? extends PlayerMob>> consumer) {
        consumer.accept(
                new GenericAttackTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getCoreTasks() {
        return super.getCoreTasks().behaviours(
                new TargetOrRetaliate<>()
        );
    }

    public static boolean checkAggressiveSpawnRules(EntityType<? extends AggressivePlayerMob> pEntityType, ServerLevelAccessor pServerLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pServerLevel.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(pEntityType, pServerLevel, pSpawnType, pPos, pRandom);
    }
}
