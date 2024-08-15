package com.prohitman.ambientplayers.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SBLDoorInteractionBehavior<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private Node lastNode;
    private long tryAgain;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT),
                Pair.of(MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryStatus.REGISTERED)
        );
    }

    @Override
    protected boolean doStartCheck(ServerLevel level, E entity, long gameTime) {
        return super.doStartCheck(level, entity, gameTime);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        Path path = BrainUtils.getMemory(entity, MemoryModuleType.PATH);
        if (path.notStarted() || path.isDone()) {
            return false;
        }
        return checkPathNodes(path.getNextNode(), level.getGameTime());
    }

    private boolean checkPathNodes(Node nextNode, long gameTime) {
        if (Objects.equals(lastNode, nextNode)) {
            tryAgain = gameTime + 20;
        } else {
            return gameTime >= tryAgain;
        }
        return true;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        this.taskStartCallback.accept(entity);
        Path path = BrainUtils.getMemory(entity, MemoryModuleType.PATH);
        lastNode = path.getNextNode();
        Node previous = path.getPreviousNode();
        BlockPos pos = previous.asBlockPos();
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.WOODEN_DOORS) && state.getBlock() instanceof DoorBlock doorBlock) {
            if (!doorBlock.isOpen(state)) {
                doorBlock.setOpen(entity, level, state, pos, true);
            }

            rememberToClose(level, entity, pos);
        }

        pos = lastNode.asBlockPos();
        state = level.getBlockState(pos);
        if (state.is(BlockTags.WOODEN_DOORS) && state.getBlock() instanceof DoorBlock doorBlock) {
            if (!doorBlock.isOpen(state)) {
                doorBlock.setOpen(entity, level, state, pos, true);
                rememberToClose(level, entity, pos);
            }
        }

        if (BrainUtils.hasMemory(entity, MemoryModuleType.DOORS_TO_CLOSE)) {
            InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(level, entity, previous, lastNode, BrainUtils.getMemory(entity, MemoryModuleType.DOORS_TO_CLOSE), entity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES));
        }
    }

    private void rememberToClose(ServerLevel pLevel, E entity, BlockPos pPos) {
        GlobalPos pos = GlobalPos.of(pLevel.dimension(), pPos);
        if (BrainUtils.hasMemory(entity, MemoryModuleType.DOORS_TO_CLOSE)) {
            BrainUtils.getMemory(entity, MemoryModuleType.DOORS_TO_CLOSE).add(pos);
        } else {
            Set<GlobalPos> doorsToClose = new ObjectOpenHashSet<>();
            doorsToClose.add(pos);
            BrainUtils.setMemory(entity, MemoryModuleType.DOORS_TO_CLOSE, doorsToClose);
        }
    }
}
