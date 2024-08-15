/*
package com.prohitman.ambientplayers.loot.predicates;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prohitman.ambientplayers.AmbientPlayers;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ArmorItemPredicate extends ItemPredicate {
    public static final ResourceLocation ID = AmbientPlayers.id("armor_item");
    public static final Codec<ArmorItemPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(predicate -> ID),
                    ExtraCodecs.stringResolverCodec(ArmorItem.Type::getName, s -> {
                        try {
                            return ArmorItem.Type.valueOf(ArmorItem.Type.class, s.toUpperCase());
                        } catch (IllegalArgumentException ignored) {
                            return null;
                        }
                    }).optionalFieldOf("armor_type").forGetter(predicate -> predicate.slot)
            ).apply(instance, ArmorItemPredicate::new)
    );

    private final Optional<ArmorItem.Type> slot;

    protected ArmorItemPredicate(ResourceLocation id, Optional<ArmorItem.Type> slot) {
        this.slot = slot;
    }

    public static ArmorItemPredicate simple() {
        return new ArmorItemPredicate(ID, Optional.empty());
    }

    public static ArmorItemPredicate ofType(ArmorItem.Type type) {
        return new ArmorItemPredicate(ID, Optional.of(type));
    }

    @Override
    public boolean matches(ItemStack pItem) {
        if (!(pItem.getItem() instanceof ArmorItem armorItem)) {
            return false;
        }
        return slot.map(type -> armorItem.getType() == type).orElse(true);
    }

    @Override
    public JsonElement serializeToJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, AmbientPlayers.LOGGER::error);
    }
}
*/
