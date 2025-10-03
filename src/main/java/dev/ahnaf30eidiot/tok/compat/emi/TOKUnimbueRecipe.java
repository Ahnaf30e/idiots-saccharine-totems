package dev.ahnaf30eidiot.tok.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import net.minecraft.util.Identifier;

import java.util.List;

public class TOKUnimbueRecipe extends EmiCraftingRecipe {

    public TOKUnimbueRecipe(List<EmiIngredient> inputs, EmiStack output, Identifier id) {
        super(inputs, output, id);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.CRAFTING;
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }
}
