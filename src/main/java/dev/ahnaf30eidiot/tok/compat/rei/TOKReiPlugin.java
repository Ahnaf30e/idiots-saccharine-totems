package dev.ahnaf30eidiot.tok.compat.rei;

import dev.ahnaf30eidiot.tok.recipe.TotemCoreImbueRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;

public class TOKReiPlugin implements REIClientPlugin {
    private static final CraftingRecipeFiller<TotemCoreImbueRecipe> IMBUED_CORE_FILLER = new ImbuedCoreRecipeFiller();

    @Override
    public void registerCategories(CategoryRegistry registry) {
        IMBUED_CORE_FILLER.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        IMBUED_CORE_FILLER.registerDisplays(registry);
    }

}
