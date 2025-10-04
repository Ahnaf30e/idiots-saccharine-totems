package dev.ahnaf30eidiot.tok.recipe.ingredient;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;

public class TOKComponentIngredient implements CustomIngredient {
    public final Item item;
    @Nullable
    public final RegistryEntry<Potion> requiredPotion;
    public final boolean allowAnyPotion;

    public TOKComponentIngredient(Item item, @Nullable RegistryEntry<Potion> requiredPotion, boolean allowAnyPotion) {
        this.item = item;
        this.requiredPotion = requiredPotion;
        this.allowAnyPotion = allowAnyPotion;
    }

    public TOKComponentIngredient(Item item, @Nullable RegistryEntry<Potion> requiredPotion) {
        this(item, requiredPotion, false);
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!stack.isOf(item)) return false;
        PotionContentsComponent comp = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (comp == null) return false;

        // Wildcard? only require the component
        if (allowAnyPotion) return true;

        // Must match exact potion
        return requiredPotion != null
            && comp.potion().isPresent()
            && comp.potion().get().equals(requiredPotion);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        ItemStack example = new ItemStack(item);
        if (allowAnyPotion) {
            example.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.WATER));
        } else if (requiredPotion != null) {
            example.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(requiredPotion));
        }
        return List.of(example);
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return new TOKComponentIngredientSerializer();
    }
}