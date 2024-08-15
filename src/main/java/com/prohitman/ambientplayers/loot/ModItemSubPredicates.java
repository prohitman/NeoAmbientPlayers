package com.prohitman.ambientplayers.loot;

import com.prohitman.ambientplayers.AmbientPlayers;
import com.prohitman.ambientplayers.loot.predicates.RAnyPredicate;
import com.prohitman.ambientplayers.loot.predicates.RArmorItemPredicate;
import com.prohitman.ambientplayers.loot.predicates.RBlockItemPredicate;
import com.prohitman.ambientplayers.loot.predicates.RNotPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItemSubPredicates {
    public static final DeferredRegister<ItemSubPredicate.Type<?>> REGISTER = DeferredRegister.create(Registries.ITEM_SUB_PREDICATE_TYPE, AmbientPlayers.MODID);
    public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<RBlockItemPredicate>> BLOCK_ITEM_PREDICATE = REGISTER.register("block_item", () -> new ItemSubPredicate.Type<>(RBlockItemPredicate.CODEC));
    public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<RNotPredicate>> NOT_PREDICATE = REGISTER.register("not", () -> new ItemSubPredicate.Type<>(RNotPredicate.CODEC));
    public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<RArmorItemPredicate>> ARMOR_ITEM_PREDICATE = REGISTER.register("armor_item", () -> new ItemSubPredicate.Type<>(RArmorItemPredicate.CODEC));
    public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<RAnyPredicate>> ANY_PREDICATE = REGISTER.register("any", () -> new ItemSubPredicate.Type<>(RAnyPredicate.CODEC));

}
