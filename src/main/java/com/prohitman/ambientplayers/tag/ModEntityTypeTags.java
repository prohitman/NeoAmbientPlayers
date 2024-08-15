package com.prohitman.ambientplayers.tag;

import com.prohitman.ambientplayers.AmbientPlayers;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTags {
    public static final TagKey<EntityType<?>> AMBIENT_PLAYERS = create("ambient_players");
    public static final TagKey<EntityType<?>> PASSIVE = create("ambient_players/passive");
    public static final TagKey<EntityType<?>> AGGRESSIVE = create("ambient_players/aggressive");
    public static final TagKey<EntityType<?>> UNIQUE = create("ambient_players/unique");
    public static final TagKey<EntityType<?>> WANDERER = create("ambient_players/wanderer");
    public static final TagKey<EntityType<?>> MERCENARY = create("ambient_players/mercenary");
    public static final TagKey<EntityType<?>> RANGER = create("ambient_players/ranger");
    public static final TagKey<EntityType<?>> ATTACK_SPEED_BUFF = create("has_attack_movement_buff");

    private static TagKey<EntityType<?>> create(String tag) {
        return TagKey.create(Registries.ENTITY_TYPE, AmbientPlayers.id(tag));
    }
}
