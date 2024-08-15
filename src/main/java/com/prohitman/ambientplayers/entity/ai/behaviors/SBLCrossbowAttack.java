package com.prohitman.ambientplayers.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class SBLCrossbowAttack<E extends Mob & CrossbowAttackMob> extends ExtendedBehaviour<E> {
    protected long delayFinishedAt = 0;
    protected Consumer<E> delayedCallback = entity -> {};
    protected ToIntFunction<E> delaySupplier = entity -> 20 + entity.getRandom().nextInt(20);
    private CrossbowState crossbowState = CrossbowState.UNCHARGED;

    public final SBLCrossbowAttack<E> whenActivating(Consumer<E> callback) {
        this.delayedCallback = callback;
        return this;
    }

    public final SBLCrossbowAttack<E> delaySupplier(ToIntFunction<E> delaySupplier) {
        this.delaySupplier = delaySupplier;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        return entity.isHolding(stack -> stack.getItem() instanceof CrossbowItem) && BrainUtils.canSee(entity, target);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return super.canStillUse(level, entity, gameTime) && this.checkExtraStartConditions(level, entity);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET);
    }

    @Override
    protected void stop(E entity) {
        if (entity.isUsingItem()) {
            entity.stopUsingItem();
        }

        if (entity.isHolding(stack -> stack.getItem() instanceof CrossbowItem)) {
            entity.setChargingCrossbow(false);

            entity.getUseItem().set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            //CrossbowItem.setCharged(entity.getUseItem(), false);
        }
    }

    @Override
    protected void tick(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        switch (crossbowState) {
            case UNCHARGED -> {
                entity.startUsingItem(ProjectileUtil.getWeaponHoldingHand(entity, item -> item instanceof CrossbowItem));
                this.crossbowState = CrossbowState.CHARGING;
                entity.setChargingCrossbow(true);
            }
            case CHARGING -> {
                if (!entity.isUsingItem()) {
                    this.crossbowState = CrossbowState.UNCHARGED;
                }

                int usageTicks = entity.getTicksUsingItem();
                ItemStack crossbow = entity.getUseItem();
                if (usageTicks >= CrossbowItem.getChargeDuration(crossbow, entity)) {
                    entity.releaseUsingItem();
                    this.crossbowState = CrossbowState.CHARGED;
                    this.delayFinishedAt = entity.level().getGameTime() + delaySupplier.applyAsInt(entity);
                    entity.setChargingCrossbow(false);
                }
            }
            case CHARGED -> {
                if (this.delayFinishedAt <= entity.level().getGameTime()) {
                    this.crossbowState = CrossbowState.READY_TO_ATTACK;
                }
            }
            case READY_TO_ATTACK -> {
                entity.performRangedAttack(target, 1.0F);
                delayedCallback.accept(entity);
                ItemStack crossbow = entity.getItemInHand(ProjectileUtil.getWeaponHoldingHand(entity, item -> item instanceof CrossbowItem));
                crossbow.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);

                //CrossbowItem.setCharged(crossbow, false);
                this.crossbowState = CrossbowState.UNCHARGED;
            }
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    public enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;
    }
}
