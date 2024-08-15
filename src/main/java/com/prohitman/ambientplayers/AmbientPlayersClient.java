package com.prohitman.ambientplayers;

import com.prohitman.ambientplayers.entity.PlayerMob;
import com.prohitman.ambientplayers.registry.EntityTypeRegistry;
import com.prohitman.ambientplayers.registry.ItemRegistry;
import com.prohitman.ambientplayers.render.AmbientPlayerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class AmbientPlayersClient {
    static void init(IEventBus modBus) {
        modBus.addListener(AmbientPlayersClient::addRenderers);
        modBus.addListener(AmbientPlayersClient::addToTabs);
    }

    private static void addRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRendererProvider<PlayerMob> provider = new EntityRendererProvider<>() {
            AmbientPlayerRenderer<PlayerMob> renderer;

            @Override
            public EntityRenderer<PlayerMob> create(Context pContext) {
                if (renderer == null) {
                    renderer = new AmbientPlayerRenderer<>(pContext);
                }
                return renderer;
            }
        };
        event.registerEntityRenderer(EntityTypeRegistry.PASSIVE_WANDERER.get(), provider);
        event.registerEntityRenderer(EntityTypeRegistry.PASSIVE_MERCENARY.get(), provider);
        event.registerEntityRenderer(EntityTypeRegistry.PASSIVE_RANGER.get(), provider);
        event.registerEntityRenderer(EntityTypeRegistry.AGGRESSIVE_WANDERER.get(), provider);
        event.registerEntityRenderer(EntityTypeRegistry.AGGRESSIVE_MERCENARY.get(), provider);
        event.registerEntityRenderer(EntityTypeRegistry.AGGRESSIVE_RANGER.get(), provider);
        event.registerEntityRenderer(EntityTypeRegistry.BUILDER.get(), provider);
        event.registerEntityRenderer(EntityTypeRegistry.MISCREANT.get(), provider);
    }

    private static void addToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ItemRegistry.PASSIVE_WANDERER_SPAWN_EGG);
            event.accept(ItemRegistry.PASSIVE_MERCENARY_SPAWN_EGG);
            event.accept(ItemRegistry.PASSIVE_RANGER_SPAWN_EGG);
            event.accept(ItemRegistry.AGGRESSIVE_WANDERER_SPAWN_EGG);
            event.accept(ItemRegistry.AGGRESSIVE_MERCENARY_SPAWN_EGG);
            event.accept(ItemRegistry.AGGRESSIVE_RANGER_SPAWN_EGG);
            event.accept(ItemRegistry.BUILDER_SPAWN_EGG);
            event.accept(ItemRegistry.MISCREANT_SPAWN_EGG);
        }
    }
}
