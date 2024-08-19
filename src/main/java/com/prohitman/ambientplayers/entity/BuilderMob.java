package com.prohitman.ambientplayers.entity;

import com.prohitman.ambientplayers.entity.ai.behaviors.SBLDoorInteractionBehavior;
import com.prohitman.ambientplayers.tag.ModItemTags;
import com.prohitman.ambientplayers.util.LootUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearestItemSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class BuilderMob extends PlayerMob {
    public BuilderMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setCanPickUpLoot(true);
        ((GroundPathNavigation)getNavigation()).setCanOpenDoors(true);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (getOffhandItem().isEmpty() && pPlayer.getItemInHand(pHand).is(ModItemTags.BUILDER_TRADES)) {
            ItemStack playerTrade = pPlayer.getItemInHand(pHand).copy();
            playerTrade.setCount(1);
            if (!pPlayer.getAbilities().instabuild && !pPlayer.getAbilities().mayfly) {
                pPlayer.getItemInHand(pHand).shrink(1);
            }
            if (!pPlayer.level().isClientSide) {
                getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 60);
                setItemInHand(InteractionHand.OFF_HAND, playerTrade);
            }
            return InteractionResult.sidedSuccess(pPlayer.level().isClientSide);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public boolean wantsToPickUp(ItemStack pStack) {
        return pStack.is(ModItemTags.BUILDER_TRADES) && this.getOffhandItem().isEmpty();
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        this.onItemPickup(itemEntity);
        pickUpItem(this, itemEntity);
    }

    protected static void pickUpItem(BuilderMob piglin, ItemEntity itemEntity) {
        ItemStack itemstack;

        piglin.take(itemEntity, 1);
        itemstack = removeOneItemFromItemEntity(itemEntity);

        boolean flag = !piglin.equipItemIfPossible(itemstack).equals(ItemStack.EMPTY);
        if (!flag) {
            piglin.setItemSlot(EquipmentSlot.OFFHAND, itemstack);
        }
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        ItemStack itemstack1 = itemstack.split(1);
        if (itemstack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(itemstack);
        }

        return itemstack1;
    }

    @Override
    public ItemStack equipItemIfPossible(ItemStack pStack) {
        if (pStack.is(ModItemTags.BUILDER_TRADES) && getOffhandItem().isEmpty()) {
            getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 60);
            setItemInHand(InteractionHand.OFF_HAND, pStack);
            return pStack;
        }
        return super.equipItemIfPossible(pStack);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        LootUtil.generateSingleItem(pLevel.getLevel(), this, "mainhand").ifPresent(itemStack -> setItemInHand(InteractionHand.MAIN_HAND, itemStack));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<BuilderMob> getBrain() {
        return (Brain<BuilderMob>) super.getBrain();
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return ObjectArrayList.of(Activity.ADMIRE_ITEM, Activity.IDLE);
    }

    @Override
    protected void addMoreSensors(Consumer<? super ExtendedSensor<? extends PlayerMob>> consumer) {
        consumer.accept(new NearestItemSensor<PlayerMob>().setRadius(8));
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getCoreTasks() {
        return super.getCoreTasks().behaviours(
                new FollowEntity<>()
                        .following(entity -> BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM))
                        .stopFollowingWithin(0)
                        .startCondition(entity -> entity.getOffhandItem().isEmpty() && BrainUtils.hasMemory(entity, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM))
                        .stopIf(entity -> !entity.getOffhandItem().isEmpty()),
                        new SBLDoorInteractionBehavior<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getIdleTasks() {
        return super.getIdleTasks().behaviours(

        );
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends PlayerMob>> getAdditionalTasks() {
        return Map.of(
                Activity.ADMIRE_ITEM, new BrainActivityGroup<PlayerMob>(Activity.ADMIRE_ITEM)
                        .priority(10)
                                .onlyStartWithMemoryStatus(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_PRESENT)
                                .onlyStartWithMemoryStatus(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED)
                                .wipeMemoriesWhenFinished(MemoryModuleType.LOOK_TARGET)
                        .behaviours(
                                new Idle<>()
                                        .runFor(entity -> Math.toIntExact(entity.getBrain().getTimeUntilExpiry(MemoryModuleType.ADMIRING_ITEM)))
                                        .whenStopping(entity -> {
                                            entity.getOffhandItem().shrink(1);
                                            LivingEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_PLAYER);
                                            if (player != null) {
                                                entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(player, true));
                                                Level level = entity.level();
                                                Vec3 pos = entity.position().add(0, 1, 0);
                                                ItemStack trade = entity.getMainHandItem().copy();
                                                trade.setCount(3);
                                                ItemEntity itemEntity = new ItemEntity(level, pos.x(), pos.y(), pos.z(), trade);
                                                Vec3 throwVec = player.position().subtract(pos).normalize().scale(0.2);
                                                itemEntity.setDeltaMovement(throwVec);
                                                level.addFreshEntity(itemEntity);
                                            }
                                            entity.swing(InteractionHand.MAIN_HAND);
                                        })
                        )
        );
    }
}
