package com.prohitman.ambientplayers.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.HeldBehaviour;
import org.joml.Vector2fc;

import java.util.List;
import java.util.function.Function;

public class StrafeBehavior<T extends Mob> extends HeldBehaviour<T> {
    protected Function<T, Vector2fc> strafeDirectionsProvider;
    protected Vector2fc directions;

    public StrafeBehavior(Function<T, Vector2fc> strafeDirectionsProvider) {
        this.strafeDirectionsProvider = strafeDirectionsProvider;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T entity) {
        directions = strafeDirectionsProvider.apply(entity);
        return !(directions.x() == 0 && directions.y() == 0);
    }

    @Override
    protected void tick(T entity) {
        entity.getMoveControl().strafe(directions.x(), directions.y());
    }
}