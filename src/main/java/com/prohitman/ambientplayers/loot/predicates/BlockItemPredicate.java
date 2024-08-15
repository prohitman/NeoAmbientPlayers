/*
package com.prohitman.ambientplayers.loot.predicates;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prohitman.ambientplayers.AmbientPlayers;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BlockItemPredicate extends ItemPredicate {
    public static final ResourceLocation ID = AmbientPlayers.id("block_item");
    private static final Supplier<BlockItemPredicate> LAZY_INSTANCE = Suppliers.memoize(() -> new BlockItemPredicate(ID));
    public static final Codec<BlockItemPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(predicate -> ID)
            ).apply(instance, resourceLocation -> LAZY_INSTANCE.get())
    );

    protected BlockItemPredicate(ResourceLocation id) {
        super();
    }

    public static BlockItemPredicate getInstance() {
        return LAZY_INSTANCE.get();
    }

    @Override
    public boolean test(ItemStack pItem) {
        return pItem.getItem() instanceof BlockItem;
    }

    @Override
    public JsonElement serializeToJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, AmbientPlayers.LOGGER::error);
    }
}
*/
