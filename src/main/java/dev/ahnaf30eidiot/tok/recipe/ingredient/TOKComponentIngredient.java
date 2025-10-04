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
import net.minecraft.registry.entry.RegistryEntry;

public class TOKComponentIngredient implements CustomIngredient {
    public final Item item;
    @Nullable
    public final RegistryEntry<Potion> requiredPotion;

    public TOKComponentIngredient(Item item, @Nullable RegistryEntry<Potion> requiredPotion) {
        this.item = item;
        this.requiredPotion = requiredPotion;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!stack.isOf(item)) return false;
        if (requiredPotion == null) return true;

        PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (contents == null || contents.potion().isEmpty()) return false;

        return contents.potion().get().equals(requiredPotion);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        ItemStack example = new ItemStack(item);
        if (requiredPotion != null) {
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