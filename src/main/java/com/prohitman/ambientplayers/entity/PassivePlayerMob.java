package com.prohitman.ambientplayers.entity;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.entity.ai.behaviors.StrafeBehavior;
import com.prohitman.ambientplayers.registry.EntityTypeRegistry;
import com.prohitman.ambientplayers.registry.MemoryTypeRegistry;
import com.prohitman.ambientplayers.tag.ModEntityTypeTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.SequentialBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomHeldBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.InvalidateMemory;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRetaliateTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.joml.Vector2f;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PassivePlayerMob extends PlayerMob {
    private static final AttributeModifier SPEED_MOD = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(AmbientPlayers.MODID, "speedmod"), 1.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public PassivePlayerMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public Brain<PassivePlayerMob> getBrain() {
        return (Brain<PassivePlayerMob>) super.getBrain();
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return ObjectArrayList.of(Activity.PANIC, Activity.FIGHT, Activity.IDLE);
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends PlayerMob>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends PlayerMob>> activities = super.getAdditionalTasks();
        if (this.getType() == EntityTypeRegistry.PASSIVE_WANDERER.get()) {
            activities.put(Activity.PANIC, getPanicTasks());
        }
        return activities;
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getCoreTasks() {
        var group = super.getCoreTasks();
        if (getType() == EntityTypeRegistry.PASSIVE_WANDERER.get() || getType() == EntityTypeRegistry.BUILDER.get()) {
            group.behaviours(
                    new CustomBehaviour<>(playerMob -> BrainUtils.setMemory(playerMob, MemoryTypeRegistry.PANICKED_BY.get(), BrainUtils.getMemory(playerMob, MemoryModuleType.HURT_BY_ENTITY)))
                            .startCondition(livingEntity -> BrainUtils.hasMemory(livingEntity, MemoryModuleType.HURT_BY_ENTITY))
            );
        } else {
            group.behaviours(
                    new SetRetaliateTarget<>()
                            .attackablePredicate(entity -> entity.isAlive() && !(entity instanceof Player)  && this.canAttack(entity, TargetingConditions.forCombat()) && !entity.getType().is(ModEntityTypeTags.PASSIVE))
                            .alertAlliesWhen((entity, entity2) -> true)
                            .isAllyIf((owner, ally) ->
                                    owner.getClass().isAssignableFrom(ally.getClass()) &&
                                            BrainUtils.getTargetOfEntity(ally) == null &&
                                            (!(owner instanceof TamableAnimal pet) || pet.getOwner() == ((TamableAnimal)ally).getOwner())
                                            && (BrainUtils.getMemory(owner, MemoryModuleType.HURT_BY_ENTITY) == null
                                            || !ally.isAlliedTo(BrainUtils.getMemory(owner, MemoryModuleType.HURT_BY_ENTITY)))
                            )
            );
        }

        return group;
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getFightTasks() {
        return super.getFightTasks();
    }

    public BrainActivityGroup<? extends PlayerMob> getPanicTasks() {
        return new BrainActivityGroup<PlayerMob>(Activity.PANIC)
                .priority(5)
                .onlyStartWithMemoryStatus(MemoryTypeRegistry.PANICKED_BY.get(), MemoryStatus.VALUE_PRESENT)
                .behaviours(
                        new InvalidateMemory<>(MemoryModuleType.WALK_TARGET),
                        new SequentialBehaviour<>(
                                new AllApplicableBehaviours<>(
                                        new StrafeBehavior<>(playerMob -> {
                                            LivingEntity attacker = BrainUtils.getMemory(playerMob, MemoryTypeRegistry.PANICKED_BY.get());
                                            if (attacker != null) {
                                                Vec3 vec = playerMob.position().subtract(attacker.position()).normalize();
                                                playerMob.setYRot((float)(Mth.atan2(vec.z(), vec.x()) * Mth.RAD_TO_DEG) - 90);
                                                return new Vector2f(1, 0);
                                            }
                                            return new Vector2f(0, 0);
                                        }).whenStarting(mob -> {
                                            mob.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(SPEED_MOD);
                                        }).whenStopping(mob -> {
                                            mob.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MOD);
                                        }).runFor(mob -> 80),
                                        new CustomHeldBehaviour<PlayerMob>(playerMob -> playerMob.getJumpControl().jump()).runFor(playerMob -> 80)
                                ).runFor(playerMob -> 80),
                                new CustomHeldBehaviour<PlayerMob>(playerMob -> {})
                                        .whenStarting(playerMob -> {
                                            BrainUtils.setMemory(playerMob, MemoryModuleType.LOOK_TARGET, new EntityTracker(BrainUtils.getMemory(playerMob, MemoryTypeRegistry.PANICKED_BY.get()), true));
                                        })
                                        .stopIf(playerMob -> {
                                            LivingEntity attacker = BrainUtils.getMemory(playerMob, MemoryTypeRegistry.PANICKED_BY.get());
                                            if (attacker == null) {
                                                return true;
                                            }
                                            if (PlayerMob.isFacingAway(playerMob, attacker.position(), 0.85)) {
                                                playerMob.getLookControl().setLookAt(playerMob.getForward());
                                                return true;
                                            }
                                            return false;
                                        })
                        ),
                        new InvalidateMemory<PlayerMob, LivingEntity>(MemoryTypeRegistry.PANICKED_BY.get())
                                .invalidateIf((PlayerMob entity, LivingEntity entity2) -> {
                                    if (entity2.isDeadOrDying() || entity2.isRemoved()) {
                                        return true;
                                    } else if (!entity2.canBeSeenByAnyone()) {
                                        return true;
                                    } else return !entity.getSensing().hasLineOfSight(entity2);
                                })
                );
    }
}
