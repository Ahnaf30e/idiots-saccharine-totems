package dev.ahnaf30eidiot.compat.emi;

import java.util.ArrayList;
import java.util.List;

import dev.ahnaf30eidiot.item.TOKItems;
import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class TOKEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        EmiStack totem = EmiStack.of(TOKItems.TOTEM_OF_KEEPING);
        EmiIngredient pearl_ingredient = EmiIngredient.of(Ingredient.ofItems(Items.ENDER_PEARL));
        EmiIngredient eye_ingredient = EmiIngredient.of(Ingredient.ofItems(Items.ENDER_EYE));

        EmiStack core = EmiStack.of(TOKItems.TOTEM_CORE);

        List<EmiStack> sampleImbued = new ArrayList<>();
        // Imbuing: Core + 2x Sugar + Honey + Potion = Imbued Core (for every potion)
        for (Potion potion : Registries.POTION) {

            if (sampleImbued.size() < 8) {
                ItemStack imbued = new ItemStack(TOKItems.TOTEM_CORE);
                imbued.set(DataComponentTypes.POTION_CONTENTS,
                        new PotionContentsComponent(Registries.POTION.getEntry(potion)));
                sampleImbued.add(EmiStack.of(imbued));
            }

            // input potion stack with component
            ItemStack pot = new ItemStack(Items.POTION);
            pot.set(DataComponentTypes.POTION_CONTENTS,
                    new PotionContentsComponent(Registries.POTION.getEntry(potion)));
            EmiStack emiPotion = EmiStack.of(pot);

            // result imbued core (store component unchanged)
            ItemStack imbued = new ItemStack(TOKItems.TOTEM_CORE);
            imbued.set(DataComponentTypes.POTION_CONTENTS,
                    new PotionContentsComponent(Registries.POTION.getEntry(potion)));

            List<EmiIngredient> inputs = new ArrayList<>();
            inputs.add(core);
            inputs.add(EmiIngredient.of(Ingredient.ofItems(Items.SUGAR)));
            inputs.add(EmiIngredient.of(Ingredient.ofItems(Items.SUGAR)));
            inputs.add(EmiIngredient.of(Ingredient.ofItems(Items.HONEY_BOTTLE)));
            inputs.add(EmiIngredient.of(emiPotion.getEmiStacks()));

            registry.addRecipe(new EmiCraftingRecipe(
                    inputs,
                    EmiStack.of(imbued),
                    Identifier.of(IdiotsSaccharineTotems.MOD_ID,
                            "/imbue_core_" + Registries.POTION.getId(potion).getPath())));
        }

        // Add two repair recipes
        registry.addRecipe(new EmiAnvilRecipe(totem, pearl_ingredient,
                Identifier.of(IdiotsSaccharineTotems.MOD_ID, "/totem_repair_pearl")));
        registry.addRecipe(
                new TOKEmiFullRepairRecipe(totem, eye_ingredient,
                        Identifier.of(IdiotsSaccharineTotems.MOD_ID, "/totem_repair_eye")));
        
        
        // Un-imbue in furnace
        registry.addRecipe(new TOKEmiCookingRecipe(
                Identifier.of(IdiotsSaccharineTotems.MOD_ID, "/unimbue_core"),
                EmiIngredient.of(sampleImbued), // input shows rotating cores
                EmiStack.of(TOKItems.TOTEM_CORE), // output plain core
                200, 0.1f,
                VanillaEmiRecipeCategories.SMELTING));
    }
}
