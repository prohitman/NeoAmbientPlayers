package com.prohitman.ambientplayers.loot.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record RAnyPredicate(List<ItemPredicate> itemPredicates) implements ItemSubPredicate {

    public static final Codec<RAnyPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(ItemPredicate.CODEC.listOf().fieldOf("predicates").forGetter(predicate -> predicate.itemPredicates)).apply(instance, RAnyPredicate::new));

    public static RAnyPredicate any(ItemPredicate... nested) {
        return new RAnyPredicate(ObjectArrayList.of(nested));
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return itemPredicates.stream().anyMatch(predicate -> predicate.test(itemStack));
    }
}
