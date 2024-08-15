package com.prohitman.ambientplayers.loot.rpredicates;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.loot.ModItemSubPredicates;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record RBlockItemPredicate() implements ItemSubPredicate {
    //public static final ResourceLocation ID = AmbientPlayers.id("block_item");
    private static final Supplier<RBlockItemPredicate> LAZY_INSTANCE = Suppliers.memoize(RBlockItemPredicate::new);
//    public static final Map<Type<?>, ItemSubPredicate> MAP_INSTANCE = Map.of(ModItemSubPredicates.BLOCK_ITEM_PREDICATE.get(), RBlockItemPredicate.getInstance());
/*    public static final Codec<RBlockItemPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(predicate -> ID)
            ).apply(instance, resourceLocation -> LAZY_INSTANCE.get())
    );*/

    public static final Codec<RBlockItemPredicate> CODEC = Codec.unit(RBlockItemPredicate::getInstance);
 /*           RecordCodecBuilder.create(instance -> instance.group(Codec.PASSTHROUGH).
            apply(instance, RBlockItemPredicate::new));*/
    /*RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemSubPredicate.CODEC.fieldOf("type").forGetter()
            ))*/


    public static RBlockItemPredicate getInstance() {
        return LAZY_INSTANCE.get();
    }

    @Override
    public boolean matches(ItemStack pItem) {
        return pItem.getItem() instanceof BlockItem;
    }

/*    @Override
    public JsonElement serializeToJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, AmbientPlayers.LOGGER::error);
    }*/
}
