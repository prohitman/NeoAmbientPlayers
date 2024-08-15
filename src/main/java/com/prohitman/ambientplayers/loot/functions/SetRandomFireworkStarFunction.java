package com.prohitman.ambientplayers.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prohitman.ambientplayers.registry.LootItemFunctionTypeRegistry;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetRandomFireworkStarFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomFireworkStarFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, SetRandomFireworkStarFunction::new)
    );
    protected SetRandomFireworkStarFunction(List<LootItemCondition> pPredicates) {
        super(pPredicates);
    }

    @Override
    protected @NotNull ItemStack run(ItemStack pStack, LootContext pContext) {
        if (pStack.is(Items.FIREWORK_STAR)) {
            pStack.set(DataComponents.FIREWORK_EXPLOSION, generateExplosion(pContext.getRandom()));
            System.out.println("i HAVE BEEN SUMMONED");
        } else if (pStack.is(Items.FIREWORK_ROCKET)) {
            RandomSource random = pContext.getRandom();
            int numExplosions = random.nextInt(1, 4);
            List<FireworkExplosion> fireworkExplosions = new ArrayList<>();
            for (int i = 0; i < numExplosions; i++) {
                fireworkExplosions.add(generateExplosion(random));
            }
            int[] durations = {1, 2, 3};

            Fireworks fireworks = new Fireworks(Util.getRandom(durations, random), fireworkExplosions);
            pStack.set(DataComponents.FIREWORKS, fireworks);
            System.out.println("i HAVE BEEN SUMMONED as well");
        }

        return pStack;
    }

    private FireworkExplosion generateExplosion(RandomSource random) {
        int numColors = random.nextInt(1, 5);
        IntList colors = new IntArrayList();
        for (int i = 0; i < numColors; i++) {
            colors.add(Util.getRandom(DyeColor.values(), random).getFireworkColor());
        }
        int numFadeColors = random.nextInt(3);
        IntList fadeColors = new IntArrayList();
        for (int i = 0; i < numFadeColors; i++) {
            fadeColors.add(Util.getRandom(DyeColor.values(), random).getFireworkColor());
        }
        FireworkExplosion.Shape shape = Util.getRandom(FireworkExplosion.Shape.values(), random);
        boolean hasTrail = random.nextBoolean();
        boolean hasFlicker = random.nextBoolean();
/*        CompoundTag tag = new CompoundTag();
        tag.putByte("Type", (byte) shape.getId());
        tag.putString("forge:shape_type", shape.name());
        tag.putIntArray("Colors", colors.stream().mapToInt(DyeColor::getFireworkColor).toArray());
        tag.putIntArray("FadeColors", fadeColors.stream().mapToInt(DyeColor::getFireworkColor).toArray());
        tag.putBoolean("Trails", hasTrail);
        tag.putBoolean("Flicker", hasFlicker);*/

        return new FireworkExplosion(shape, colors, fadeColors, hasTrail, hasFlicker);
    }

    public static Builder randomFireworkStar() {
        return new Builder();
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return LootItemFunctionTypeRegistry.SET_RANDOM_FIREWORK_STAR.get();
    }

    public static final class Builder extends LootItemConditionalFunction.Builder<Builder> {

        @Override
        protected @NotNull Builder getThis() {
            return this;
        }

        @Override
        public @NotNull LootItemFunction build() {
            return new SetRandomFireworkStarFunction(getConditions());
        }
    }

/*    public static final class Serializer extends LootItemConditionalFunction.Serializer<SetRandomFireworkStarFunction> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public SetRandomFireworkStarFunction deserialize(JsonObject pObject, JsonDeserializationContext pDeserializationContext, LootItemCondition[] pConditions) {
            return new SetRandomFireworkStarFunction(pConditions);
        }
    }*/
}
