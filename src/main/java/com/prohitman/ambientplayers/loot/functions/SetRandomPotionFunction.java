package com.prohitman.ambientplayers.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prohitman.ambientplayers.registry.LootItemFunctionTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SetRandomPotionFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomPotionFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, SetRandomPotionFunction::new)
    );

    protected SetRandomPotionFunction(List<LootItemCondition> pPredicates) {
        super(pPredicates);
    }

    @Override
    protected @NotNull ItemStack run(@NotNull ItemStack pStack, LootContext pContext) {
        Optional<Holder.Reference<Potion>> potionOptional;
        System.out.println("i HAVE BEEN SUMMONED before potion");
        potionOptional = BuiltInRegistries.POTION.getRandom(pContext.getRandom());

        //while ((potionOptional = BuiltInRegistries.POTION.getRandom(pContext.getRandom())).isPresent() /*&& potionOptional.get().is(PotionContents.EMPTY)*/);
        System.out.println("i HAVE BEEN SUMMONED after potion");
        return potionOptional.map(potion -> PotionContents.createItemStack(pStack.getItem(), potion)).get();
    }

    public static Builder randomPotion() {
        return new Builder();
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return LootItemFunctionTypeRegistry.SET_RANDOM_POTION.get();
    }

    public static final class Builder extends LootItemConditionalFunction.Builder<Builder> {
        @Override
        protected @NotNull Builder getThis() {
            return this;
        }

        @Override
        public @NotNull LootItemFunction build() {
            return new SetRandomPotionFunction(getConditions());
        }
    }

/*    public static final class Serializer extends LootItemConditionalFunction.Builder<SetRandomPotionFunction> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public SetRandomPotionFunction deserialize(JsonObject pObject, JsonDeserializationContext pDeserializationContext, LootItemCondition[] pConditions) {
            return new SetRandomPotionFunction(pConditions);
        }
    }*/
}
