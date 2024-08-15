/*
package com.prohitman.ambientplayers.loot.predicates;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dhyces.ambientplayers.AmbientPlayers;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public class NotPredicate extends ItemPredicate {
    public static final ResourceLocation ID = AmbientPlayers.id("not");
    public static final Codec<NotPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(predicate -> ID),
                    ExtraCodecs.JSON.fieldOf("filter").xmap(
                            ItemPredicate::fromJson,
                            ItemPredicate::serializeToJson
                    ).forGetter(predicate -> predicate.nested)
            ).apply(instance, NotPredicate::new)
    );

    private final ItemPredicate nested;

    protected NotPredicate(ResourceLocation id, ItemPredicate nested) {
        this.nested = nested;
    }

    public static NotPredicate not(ItemPredicate nested) {
        return new NotPredicate(ID, nested);
    }

    @Override
    public boolean matches(ItemStack pItem) {
        return !nested.matches(pItem);
    }

    @Override
    public JsonElement serializeToJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, AmbientPlayers.LOGGER::error);
    }
}
*/
