package com.prohitman.ambientplayers.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class LookAtNearestVisiblePlayer<E extends LivingEntity> extends ExtendedBehaviour<E> {
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> REQUIRED_MEMORIES = ObjectArrayList.of(
            Pair.of(MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT)
    );

    @Override
    protected void start(E entity) {
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_PLAYER), true));
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return REQUIRED_MEMORIES;
    }
}
