package com.prohitman.ambientplayers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.prohitman.ambientplayers.entity.AggressivePlayerMob;
import com.prohitman.ambientplayers.entity.PlayerMob;
import com.prohitman.ambientplayers.loot.ModItemSubPredicates;
import com.prohitman.ambientplayers.loot.ModLootContextParamSets;
import com.prohitman.ambientplayers.mixin.StructureTemplatePoolAccessor;
import com.prohitman.ambientplayers.registry.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Mod(AmbientPlayers.MODID)
public class AmbientPlayers {
    public static final String MODID = "ambientplayers";
    public static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(MODID, id);
    }
    public static final Logger LOGGER = LoggerFactory.getLogger(AmbientPlayers.class);

    public AmbientPlayers(IEventBus modEventBus, ModContainer modContainer) {
        IEventBus forgeBus = NeoForge.EVENT_BUS;

        ItemRegistry.REGISTER.register(modEventBus);
        EntityTypeRegistry.REGISTER.register(modEventBus);
        MemoryTypeRegistry.REGISTER.register(modEventBus);
        LootPoolEntryTypeRegistry.REGISTER.register(modEventBus);
        LootItemFunctionTypeRegistry.REGISTER.register(modEventBus);
        ModItemSubPredicates.REGISTER.register(modEventBus);

        registerItemPredicates();
        ModLootContextParamSets.bootstrap();

        modEventBus.addListener(this::addDefaultAttributes);
        modEventBus.addListener(this::registerSpawnPlacements);
        forgeBus.addListener(this::addGoalsToEntity);
        forgeBus.addListener(this::addStructures);

        if (FMLLoader.getDist().isClient()) {
            AmbientPlayersClient.init(forgeBus, modEventBus);
        }

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void addDefaultAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypeRegistry.PASSIVE_WANDERER.get(), PlayerMob.createAttributes().build());
        event.put(EntityTypeRegistry.PASSIVE_MERCENARY.get(), PlayerMob.createAttributes().build());
        event.put(EntityTypeRegistry.PASSIVE_RANGER.get(), PlayerMob.createAttributes().build());
        event.put(EntityTypeRegistry.AGGRESSIVE_WANDERER.get(), PlayerMob.createAttributes().build());
        event.put(EntityTypeRegistry.AGGRESSIVE_MERCENARY.get(), PlayerMob.createAttributes().build());
        event.put(EntityTypeRegistry.AGGRESSIVE_RANGER.get(), PlayerMob.createAttributes().build());
        event.put(EntityTypeRegistry.BUILDER.get(), PlayerMob.createAttributes().build());
        event.put(EntityTypeRegistry.MISCREANT.get(), AggressivePlayerMob.createMiscreantAttributes().build());
    }

    private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(EntityTypeRegistry.PASSIVE_WANDERER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityTypeRegistry.PASSIVE_MERCENARY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityTypeRegistry.PASSIVE_RANGER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityTypeRegistry.AGGRESSIVE_WANDERER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AggressivePlayerMob::checkAggressiveSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityTypeRegistry.AGGRESSIVE_MERCENARY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AggressivePlayerMob::checkAggressiveSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityTypeRegistry.AGGRESSIVE_RANGER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AggressivePlayerMob::checkAggressiveSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityTypeRegistry.BUILDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityTypeRegistry.MISCREANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AggressivePlayerMob::checkAggressiveSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private void addGoalsToEntity(final EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Monster monster) {
            monster.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(monster, PlayerMob.class, true));
        }
    }

    private void addStructures(final ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();

        Registry<StructureTemplatePool> templatePoolRegistry = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);

        SinglePoolElement builder = StructurePoolElement.single("ambientplayers:village/common/builder").apply(StructureTemplatePool.Projection.RIGID);

        addToAllVillagers(templatePoolRegistry, builder, 2);
    }

    private <T extends StructurePoolElement> void addToAllVillagers(Registry<StructureTemplatePool> templatePoolRegistry, T poolElement, int weight) {
        StructureTemplatePool plainsPool = templatePoolRegistry.get(ResourceLocation.fromNamespaceAndPath("minecraft", "village/plains/villagers"));
        StructureTemplatePool desertPool = templatePoolRegistry.get(ResourceLocation.fromNamespaceAndPath("minecraft", "village/desert/villagers"));
        StructureTemplatePool savannaPool = templatePoolRegistry.get(ResourceLocation.fromNamespaceAndPath("minecraft", "village/savanna/villagers"));
        StructureTemplatePool snowyPool = templatePoolRegistry.get(ResourceLocation.fromNamespaceAndPath("minecraft", "village/snowy/villagers"));
        StructureTemplatePool taigaPool = templatePoolRegistry.get(ResourceLocation.fromNamespaceAndPath("minecraft", "village/taiga/villagers"));

        addStructureToPool(plainsPool, poolElement, weight);
        addStructureToPool(desertPool, poolElement, weight);
        addStructureToPool(savannaPool, poolElement, weight);
        addStructureToPool(snowyPool, poolElement, weight);
        addStructureToPool(taigaPool, poolElement, weight);
    }

    private <T extends StructurePoolElement> void addStructureToPool(@Nullable StructureTemplatePool templatePool, T poolElement, int weight) {
        if (templatePool == null) {
            return;
        }

        StructureTemplatePoolAccessor accessor = (StructureTemplatePoolAccessor) templatePool;
        for (int i = 0; i < weight; i++) {
            accessor.getTemplates().add(poolElement);
        }

        List<Pair<StructurePoolElement, Integer>> rawTemplates = new ObjectArrayList<>(accessor.getRawTemplates());
        rawTemplates.add(Pair.of(poolElement, weight));
        accessor.setRawTemplates(rawTemplates);
    }

    private void registerItemPredicates() {
/*        ItemPredicate.register(
                AnyPredicate.ID,
                jsonObject -> AnyPredicate.CODEC.parse(JsonOps.INSTANCE, jsonObject)
                        .getOrThrow(false, LOGGER::error)
        );
        ItemPredicate.register(
                NotPredicate.ID,
                jsonObject -> NotPredicate.CODEC.parse(JsonOps.INSTANCE, jsonObject)
                        .getOrThrow(false, LOGGER::error)
        );
        ItemPredicate.register(
                ArmorItemPredicate.ID,
                jsonObject -> ArmorItemPredicate.CODEC.parse(JsonOps.INSTANCE, jsonObject)
                        .getOrThrow(false, LOGGER::error)
        );
        ItemPredicate.register(
                BlockItemPredicate.ID,
                jsonObject -> BlockItemPredicate.CODEC.parse(JsonOps.INSTANCE, jsonObject)
                        .getOrThrow(false, LOGGER::error)
        );*/
    }
}
