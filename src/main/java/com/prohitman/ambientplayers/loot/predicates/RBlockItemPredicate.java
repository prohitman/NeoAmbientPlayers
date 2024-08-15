package com.prohitman.ambientplayers.loot.predicates;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record RBlockItemPredicate() implements ItemSubPredicate {
    private static final Supplier<RBlockItemPredicate> LAZY_INSTANCE = Suppliers.memoize(RBlockItemPredicate::new);
    public static final Codec<RBlockItemPredicate> CODEC = Codec.unit(RBlockItemPredicate::getInstance);


    public static RBlockItemPredicate getInstance() {
        return LAZY_INSTANCE.get();
    }

    @Override
    public boolean matches(ItemStack pItem) {
        return pItem.getItem() instanceof BlockItem;
    }
}
