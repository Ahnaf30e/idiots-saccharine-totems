package dev.ahnaf30eidiot.tok.recipe;

import dev.ahnaf30eidiot.tok.item.TOKItems;
import dev.ahnaf30eidiot.tok.item.custom.ImbuedCoreItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class TotemCoreImbueRecipe extends SpecialCraftingRecipe {
    public static final SpecialRecipeSerializer<TotemCoreImbueRecipe> SERIALIZER = new SpecialRecipeSerializer<>(
            TotemCoreImbueRecipe::new);

    public TotemCoreImbueRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput inv, World world) {
        if (inv.getStackCount() < 5) return false;
        int cores = 0, sugars = 0, honeys = 0, potions = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty())
                continue;

            if (stack.isOf(TOKItems.TOTEM_CORE))
                cores++;
            else if (stack.isOf(Items.SUGAR))
                sugars++;
            else if (stack.isOf(Items.HONEY_BOTTLE))
                honeys++;
            else if (stack.isOf(Items.POTION))
                potions++;
            else
                return false; // invalid extra item
        }
        return cores == 1 && sugars == 2 && honeys == 1 && potions <= 1;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inv, RegistryWrapper.WrapperLookup registries) {
        ItemStack core = ItemStack.EMPTY;
        ItemStack potion = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isOf(TOKItems.TOTEM_CORE)) core = stack;
            if (stack.isOf(Items.POTION)) potion = stack;
        }

        if (!core.isEmpty()) {
            ItemStack result = new ItemStack(TOKItems.IMBUED_CORE);

            PotionContentsComponent potionData = potion.isEmpty() ? new PotionContentsComponent(Potions.WATER) : potion.get(DataComponentTypes.POTION_CONTENTS);
            if (potionData != null) {
                result.set(DataComponentTypes.POTION_CONTENTS, potionData);
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int w, int h) {
        return w * h >= 5;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
