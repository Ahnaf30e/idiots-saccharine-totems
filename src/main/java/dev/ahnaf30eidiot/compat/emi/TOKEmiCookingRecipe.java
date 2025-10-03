package dev.ahnaf30eidiot.compat.emi;

import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;

public class TOKEmiCookingRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiIngredient input;
    private final EmiStack output;
    private final EmiRecipeCategory category;
    private final int cookTime;
    @SuppressWarnings("unused")
    private final float xp;

    public TOKEmiCookingRecipe(Identifier id, EmiIngredient input, EmiStack output,
                               int cookTime, float xp, EmiRecipeCategory category) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.cookTime = cookTime;
        this.xp = xp;
        this.category = category;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 82;
    }

    @Override
    public int getDisplayHeight() {
        return 38;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(24, 5, 50 * cookTime);
        widgets.addSlot(input, 0, 4);
        widgets.addSlot(output, 56, 0).large(true).recipeContext(this);
    }
}

