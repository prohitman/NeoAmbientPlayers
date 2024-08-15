package com.prohitman.ambientplayers.loot.rpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import java.util.Optional;

public record RArmorItemPredicate(Optional<ArmorItem.Type> armorType) implements ItemSubPredicate {
    public static Codec<RArmorItemPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(ArmorItem.Type.CODEC.fieldOf("armor_type")
                    .forGetter(predicate -> predicate.armorType.get())).apply(instance, RArmorItemPredicate::ofType));

    public static RArmorItemPredicate ofType(ArmorItem.Type type) {
        return new RArmorItemPredicate(Optional.of(type));
    }

    @Override
    public boolean matches(ItemStack pItem) {
        if (!(pItem.getItem() instanceof ArmorItem armorItem)) {
            return false;
        }
        return this.armorType.map(type -> armorItem.getType() == type).orElse(true);
    }
}
