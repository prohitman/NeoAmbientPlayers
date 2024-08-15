package com.prohitman.ambientplayers.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;

/**
 * Movement behaviour to handle proximal strafing. Will run away if too close, or run towards if too far. <br>
 * Useful for ranged attackers. <br>
 * Defaults:
 * <ul>
 *     <li>Continues strafing until the target is no longer in memory</li>
 *     <li>Stays between 5 and 20 blocks of the target</li>
 *     <li>Normal strafing speed</li>
 *     <li>30% speed boost to repositioning</li>
 * </ul>
 * @param <E> The entity
 */
public class StayWithinBehavior<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));

    protected ToDoubleBiFunction<E, LivingEntity> distMax = (entity, target) -> 20d;
    protected ToDoubleBiFunction<E, LivingEntity> distMin = (entity, target) -> 5d;
    protected float speedMod = 1;
    protected float repositionSpeedMod = 1.3f;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    /**
     * Set how far the entity should attempt to stay away from the target at a minimum.
     * @param distance The distance, in blocks
     * @return this
     */
    public StayWithinBehavior<E> minDistance(double distance) {
        return minDistance((entity, target) -> distance);
    }

    /**
     * Set how far the entity should attempt to stay away from the target at a minimum.
     * @param distance The distance function, in blocks
     * @return this
     */
    public StayWithinBehavior<E> minDistance(ToDoubleBiFunction<E, LivingEntity> distance) {
        this.distMin = distance;

        return this;
    }

    /**
     * Set how far the entity should attempt to stay away from the target at most.
     * @param distance The distance, in blocks
     * @return this
     */
    public StayWithinBehavior<E> maxDistance(double distance) {
        return maxDistance((entity, target) -> distance);
    }

    /**
     * Set how far the entity should attempt to stay away from the target at most.
     * @param distance The distance function, in blocks
     * @return this
     */
    public StayWithinBehavior<E> maxDistance(ToDoubleBiFunction<E, LivingEntity> distance) {
        this.distMax = distance;

        return this;
    }

    /**
     * Set the movespeed modifier for when the entity is strafing.
     * @param modifier The multiplier for movement speed
     * @return this
     */
    public StayWithinBehavior<E> speedMod(float modifier) {
        this.speedMod = modifier;

        return this;
    }

    /**
     * Set the movespeed modifier for when the entity is repositioning due to being too close or too far.
     * @param modifier The multiplier for movement speed
     * @return this
     */
    public StayWithinBehavior<E> repositionSpeedMod(float modifier) {
        this.speedMod = modifier;

        return this;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET);
    }

    @Override
    protected void tick(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        double targetDistSq = target.distanceToSqr(entity);
        double maxDist = this.distMax.applyAsDouble(entity, target);
        double maxDistSq = Mth.square(maxDist);
        double minDistSq = Mth.square(this.distMin.applyAsDouble(entity, target));
        PathNavigation navigation = entity.getNavigation();

        if (targetDistSq > maxDistSq || !entity.hasLineOfSight(target)) {
            if (navigation.isDone())
                navigation.moveTo(target, this.repositionSpeedMod);

            return;
        }

        if (targetDistSq < minDistSq) {
            if (navigation.isDone()) {
                Vec3 runPos = DefaultRandomPos.getPosAway(entity, (int)maxDist, 5, target.position());

                if (runPos != null)
                    navigation.moveTo(navigation.createPath(BlockPos.containing(runPos), 1), this.repositionSpeedMod);
            }

            return;
        }

        if (navigation instanceof GroundPathNavigation)
            navigation.stop();

        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));

        if (targetDistSq > maxDistSq * 0.5f) {
            entity.lookAt(target, 30, 30);
            entity.getMoveControl().strafe(0.5f * this.speedMod, 0);
            entity.setYRot(Mth.rotateIfNecessary(entity.getYRot(), entity.yHeadRot, 0.0F));
        }
        else if (targetDistSq < minDistSq * 3f) {
            entity.lookAt(target, 30, 30);
            entity.getMoveControl().strafe(-0.5f * this.speedMod, 0);
            entity.setYRot(Mth.rotateIfNecessary(entity.getYRot(), entity.yHeadRot, 0.0F));
        }
    }
}
