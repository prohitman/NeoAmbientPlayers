/*
package com.prohitman.ambientplayers.loot.predicates;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prohitman.ambientplayers.AmbientPlayers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AnyPredicate extends ItemPredicate {
    public static final ResourceLocation ID = AmbientPlayers.id("any");
    public static final Codec<AnyPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(predicate -> ID),
                    ExtraCodecs.JSON.listOf().fieldOf("filters").xmap(
                            jsonElements -> (List<ItemPredicate>)jsonElements.stream().map(ItemPredicate::fromJson).collect(ImmutableList.toImmutableList()),
                            objects -> objects.stream().map(ItemPredicate::serializeToJson).toList()
                    ).forGetter(predicate -> predicate.nested)
            ).apply(instance, AnyPredicate::new)
    );

    private final List<ItemPredicate> nested;

    protected AnyPredicate(ResourceLocation _id, List<ItemPredicate> nested) {
        this.nested = nested;
    }

    public static AnyPredicate any(ItemPredicate... nested) {
        return new AnyPredicate(ID, ObjectArrayList.of(nested));
    }

    @Override
    public boolean matches(ItemStack pItem) {
        return nested.stream().anyMatch(predicate -> predicate.matches(pItem));
    }

    @Override
    public JsonElement serializeToJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, AmbientPlayers.LOGGER::error);
    }
}
*/
