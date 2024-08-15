package com.prohitman.ambientplayers.loot.rpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prohitman.ambientplayers.loot.ModItemSubPredicates;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.Map;

public record RNotPredicate(ItemPredicate itemPredicate) implements ItemSubPredicate {
    //public static final Map<Type<?>, ItemSubPredicate> MAP_INSTANCE = Map.of(ModItemSubPredicates.NOT_PREDICATE.get(), RNotPredicate.getInstance());


    public static final Codec<RNotPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(ItemPredicate.CODEC.fieldOf("predicate").forGetter(RNotPredicate::itemPredicate))
                    .apply(instance, RNotPredicate::not));

    @Override
    public boolean matches(ItemStack itemStack) {
        return !this.itemPredicate.test(itemStack);
    }

    public static RNotPredicate not(ItemPredicate nested) {
        return new RNotPredicate(nested);
    }
}
