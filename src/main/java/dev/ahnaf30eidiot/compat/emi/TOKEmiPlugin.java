package dev.ahnaf30eidiot.compat.emi;

import dev.ahnaf30eidiot.item.TOKItems;
import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class TOKEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        EmiStack totem = EmiStack.of(TOKItems.TOTEM_OF_KEEPING);
        EmiIngredient pearl_ingredient = EmiIngredient.of(Ingredient.ofItems(Items.ENDER_PEARL));
        EmiIngredient eye_ingredient = EmiIngredient.of(Ingredient.ofItems(Items.ENDER_EYE));

        // Add two repair recipes
        registry.addRecipe(new EmiAnvilRecipe(totem, pearl_ingredient,
                Identifier.of(IdiotsSaccharineTotems.MOD_ID, "/totem_repair_pearl")));
        registry.addRecipe(
                new TOKEmiFullRepairRecipe(totem, eye_ingredient,
                        Identifier.of(IdiotsSaccharineTotems.MOD_ID, "/totem_repair_eye")));
    }
}
