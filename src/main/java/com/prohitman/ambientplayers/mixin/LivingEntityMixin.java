package com.prohitman.ambientplayers.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.prohitman.ambientplayers.entity.PlayerMob;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapWithCondition(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private boolean ambientplayers$stopKnockback(LivingEntity instance, double vec31, double v, double pStrength, @Local(argsOnly = true) float pAmount) {
        return !(((LivingEntity)(Object)this) instanceof PlayerMob && pAmount <= 0);
    }

    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playHurtSound(Lnet/minecraft/world/damagesource/DamageSource;)V"))
    private void ambientplayers$stopKnockback(LivingEntity instance, DamageSource pSource, Operation<Void> original, @Local(ordinal = 0) boolean shieldFlag) {
        if (((LivingEntity)(Object)this) instanceof PlayerMob && shieldFlag) {
            instance.playSound(SoundEvents.SHIELD_BLOCK);
        } else {
            original.call(instance, pSource);
        }
    }
}
