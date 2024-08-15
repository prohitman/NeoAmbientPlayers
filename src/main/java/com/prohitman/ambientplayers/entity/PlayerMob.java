package com.prohitman.ambientplayers.entity;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.entity.ai.behaviors.LookAtNearestVisiblePlayer;
import com.prohitman.ambientplayers.entity.ai.behaviors.SBLCrossbowAttack;
import com.prohitman.ambientplayers.entity.ai.behaviors.StayWithinBehavior;
import com.prohitman.ambientplayers.registry.MemoryTypeRegistry;
import com.prohitman.ambientplayers.tag.ModEntityTypeTags;
import com.prohitman.ambientplayers.util.LootUtil;
import it.unimi.dsi.fastutil.doubles.DoubleDoublePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.BowAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BlockWithShield;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.InvalidateMemory;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.WalkOrRunToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.IncomingProjectilesSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

import java.util.List;
import java.util.function.Consumer;

public abstract class PlayerMob extends PathfinderMob implements SmartBrainOwner<PlayerMob>, CrossbowAttackMob {
    public static final int PASSIVE_ATTENTION_DISTANCE = 6;
    public static final double ATTENTION_SPAN_DOT = 0.26;
    private static final UniformInt TIRED_EXPIRY = UniformInt.of(200, 600);
    private static final AttributeModifier ATTACK_SPEED_MOD = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(AmbientPlayers.MODID, "speedmod"), 0.45, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    private static final EntityDataAccessor<Boolean> CHARGING_CROSSBOW = SynchedEntityData.defineId(PlayerMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CROSSBOW_CHARGED = SynchedEntityData.defineId(PlayerMob.class, EntityDataSerializers.BOOLEAN);


    public PlayerMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setPersistenceRequired();
        this.navigation = new SmoothGroundNavigation(this, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.BLOCK_INTERACTION_RANGE)//may break
                .add(Attributes.ENTITY_INTERACTION_RANGE);//may break
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGING_CROSSBOW, false);
        builder.define(CROSSBOW_CHARGED, false);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        LootTable lootTable = LootUtil.getSpawnWithLootTable(pLevel.getLevel(), this);
        LootContext lootContext = LootUtil.createSpawnWithContext(pLevel.getLevel(), this, lootTable);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            LootUtil.generateSingleItem(lootTable, lootContext, slot.getName()).ifPresent(itemStack -> setItemSlot(slot, itemStack));
        }
        this.xpReward = BinomialDistributionGenerator.binomial(5, .2f).getInt(lootContext);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @Override
    public void aiStep() {
        updateSwingTime();
        super.aiStep();
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return this.getAttackBoundingBox().intersects(entity.getHitbox());
    }

    protected AABB getAttackBoundingBox() {
        Entity entity = this.getVehicle();
        AABB aabb;
        if (entity != null) {
            AABB aabb1 = entity.getBoundingBox();
            AABB aabb2 = this.getBoundingBox();
            aabb = new AABB(Math.min(aabb2.minX, aabb1.minX), aabb2.minY, Math.min(aabb2.minZ, aabb1.minZ), Math.max(aabb2.maxX, aabb1.maxX), aabb2.maxY, Math.max(aabb2.maxZ, aabb1.maxZ));
        } else {
            aabb = this.getBoundingBox();
        }

        return aabb.inflate(/*Math.sqrt(2.0399999618530273) - 0.6000000238418579 +*/ getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) - 2, 0.0, /*Math.sqrt(2.0399999618530273) - 0.6000000238418579 + */getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) - 2);
    }

/*    @Override
    public double getMeleeAttackRangeSqr(LivingEntity pEntity) {
        return super.getMeleeAttackRangeSqr(pEntity) + getAttributeValue(ForgeMod.ENTITY_REACH.get());
    }*/

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        Vec3 upVec = getUpVector(1.0f);
        float angleOffset = random.nextInt(0, 2) * (random.nextBoolean() ? -1 : 1);

        // https://www.gamedeveloper.com/programming/predictive-aim-mathematics-for-ai-targeting
        double dist = this.distanceTo(pTarget);
        Vec3 distVec = this.getEyePosition().vectorTo(pTarget.getEyePosition());
        Vec3 targetVec = pTarget.getDeltaMovement();
        double targetSpeed = targetVec.length();
        double cosTheta = distVec.normalize().dot(targetVec.normalize());
        DoubleDoublePair tVals = quadEqu(Mth.square(pVelocity) - Mth.square(targetSpeed), 2 * dist * targetSpeed * cosTheta, Mth.square(dist));
        double t = tVals.firstDouble() >= 0 && tVals.firstDouble() < tVals.secondDouble() ? tVals.firstDouble() : tVals.secondDouble();
        Vec3 gravity = new Vec3(0, -0.05F, 0);
        Vec3 fireVec = targetVec.subtract(gravity.scale(0.5).scale(t)).add(distVec.scale(1/t)).normalize();

        Quaternionfc rotation = new Quaternionf().setAngleAxis(angleOffset * Mth.DEG_TO_RAD, upVec.x(), upVec.y(), upVec.z());
        Vector3fc viewVec = fireVec.toVector3f().rotate(rotation);
        Arrow arrow = new Arrow(level(), this, new ItemStack(Items.ARROW), null);
        arrow.shoot(viewVec.x(), viewVec.y(), viewVec.z(), pVelocity, 1.0f);
        level().addFreshEntity(arrow);
    }

    public static DoubleDoublePair quadEqu(double a, double b, double c) {
        double denom = (2 * a);
        double sqrt = Math.sqrt(Mth.square(b) + (4 * a * c));
        double sub = (-b - sqrt) / denom;
        double add = (-b + sqrt) / denom;
        return DoubleDoublePair.of(sub, add);
    }

    @Override
    public void setChargingCrossbow(boolean pChargingCrossbow) {
        this.entityData.set(CHARGING_CROSSBOW, pChargingCrossbow);
    }

    @Override
    public ItemStack getProjectile(ItemStack pWeaponStack) {
        ItemStack ammo = getOffhandItem().is(ItemTags.ARROWS) || (pWeaponStack.is(Tags.Items.TOOLS_CROSSBOW) && getOffhandItem().getItem() instanceof FireworkRocketItem) ? getOffhandItem().copy() : new ItemStack(Items.ARROW);
        return CommonHooks.getProjectile(this, pWeaponStack, ammo);
    }



 /*   @Override
    public void shootCrossbowProjectile(LivingEntity pTarget, ItemStack pCrossbowStack, Projectile pProjectile, float pProjectileAngle) {
        Vec3 upVec = getUpVector(1.0f);
        Quaternionfc rotation = new Quaternionf().setAngleAxis(pProjectileAngle * Mth.DEG_TO_RAD, upVec.x(), upVec.y(), upVec.z());
        Vector3fc viewVec = getViewVector(1.0f).toVector3f().rotate(rotation);
        pProjectile.shoot(viewVec.x(), viewVec.y(), viewVec.z(), 3.15f, 1.0f);
    }*/

    @Override
    public void onCrossbowAttackPerformed() {

    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem pProjectileWeapon) {
        return true;
    }

    public static boolean isFacingTowards(Entity entity, Vec3 vec, double deviation) {
        Vec3 forward = Vec3.directionFromRotation(entity.getXRot(), entity.getYHeadRot());
        Vec3 between = vec.subtract(entity.position()).normalize();
        return forward.dot(between) < deviation;
    }

    public static boolean isFacingAway(Entity entity, Vec3 vec, double deviation) {
        Vec3 forward = Vec3.directionFromRotation(entity.getXRot(), entity.getYHeadRot());
        Vec3 between = vec.subtract(entity.position()).normalize();
        return forward.dot(between) > deviation;
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<? extends PlayerMob> getBrain() {
        return (Brain<PlayerMob>) super.getBrain();
    }

    @Override
    public List<? extends ExtendedSensor<? extends PlayerMob>> getSensors() {
        List<ExtendedSensor<? extends PlayerMob>> list = ObjectArrayList.of(
                new NearbyLivingEntitySensor<>(),
                new NearbyPlayersSensor<>(),
                new IncomingProjectilesSensor<>(),
                new UnreachableTargetSensor<>(),
                new HurtBySensor<>()
        );
        addMoreSensors(list::add);
        return list;
    }

    protected void addMoreSensors(Consumer<? super ExtendedSensor<? extends PlayerMob>> consumer) {
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new WalkOrRunToWalkTarget<>(),
                new LookAtTarget<>(),
                new CustomBehaviour<>(playerMob -> {
                    if (!playerMob.getType().is(ModEntityTypeTags.ATTACK_SPEED_BUFF)) {
                        return;
                    }
                    AttributeInstance movementInstance = playerMob.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (playerMob.getBrain().getActiveNonCoreActivity().filter(activity -> activity == Activity.FIGHT).isPresent()) {
                        if (!movementInstance.hasModifier(ATTACK_SPEED_MOD.id())) {
                            movementInstance.addTransientModifier(ATTACK_SPEED_MOD);
                        }
                    } else {
                        if (movementInstance.hasModifier(ATTACK_SPEED_MOD.id())) {
                            movementInstance.removeModifier(ATTACK_SPEED_MOD);
                        }
                    }
                })
        );
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new InvalidateMemory<PlayerMob, PositionTracker>(MemoryModuleType.LOOK_TARGET)
                        .invalidateIf((entity, tracker) -> {
                            if(tracker == null || entity == null) return false;
                            if (!(tracker instanceof EntityTracker entityTracker)) return false;
                            if (entityTracker.getEntity() == null) return false;
                            if (entity.distanceToSqr(tracker.currentPosition()) > PASSIVE_ATTENTION_DISTANCE * PASSIVE_ATTENTION_DISTANCE) {
                                return true;
                            }
                            return isFacingTowards(entity, tracker.currentPosition(), ATTENTION_SPAN_DOT);
                        }),
                new SetRandomWalkTarget<>()
                        .startCondition(pathfinderMob -> !BrainUtils.hasMemory(pathfinderMob, MemoryTypeRegistry.TIRED.get()))
                        .whenStopping(pathfinderMob -> BrainUtils.setForgettableMemory(pathfinderMob, MemoryTypeRegistry.TIRED.get(), Unit.INSTANCE, TIRED_EXPIRY.sample(pathfinderMob.getRandom()))),
                new LookAtNearestVisiblePlayer<>(),
                new SetRandomLookTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends PlayerMob> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((attacker, target) -> {
                    LivingEntity immediateEnemy = EntityRetrievalUtil.getNearestEntity(attacker, 2, entity -> BrainUtils.getMemory(attacker, MemoryModuleType.HURT_BY_ENTITY) == entity);
                    if (immediateEnemy != null && immediateEnemy != target) {
                        return true;
                    }
                    double range = attacker.getAttribute(Attributes.FOLLOW_RANGE).getValue();
                    return attacker.distanceToSqr(target) >= range*range;
                }),
                new FirstApplicableBehaviour<>(
                        new AllApplicableBehaviours<>(
                                new StayWithinBehavior<>()
                                        .speedMod(1.5f)
                                        .repositionSpeedMod(2.5f),
                                new FirstApplicableBehaviour<>(
                                        new SBLCrossbowAttack<>(),
                                        new BowAttack<PlayerMob>(20)
                                ).startCondition(mob -> mob.getNavigation().isDone())
                        ).startCondition(PlayerMob::hasBowOrCrossbow),
                        new AllApplicableBehaviours<>(
                                new SetWalkTargetToAttackTarget<>(),
                                new BlockWithShield<>()
                                        .startCondition(entity ->
                                                BrainUtils.hasMemory(entity, SBLMemoryTypes.INCOMING_PROJECTILES.get())
                                        ),
                                new AnimatableMeleeAttack<>(0)
                        )
                )
        );
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    public static boolean hasBow(LivingEntity entity) {
        return entity.isHolding(itemStack -> itemStack.is(Tags.Items.TOOLS_BOW));
    }

    public static boolean hasBowOrCrossbow(LivingEntity entity) {
        return entity.isHolding(itemStack -> itemStack.is(Tags.Items.TOOLS_BOW) || itemStack.is(Tags.Items.TOOLS_CROSSBOW));
    }
}
