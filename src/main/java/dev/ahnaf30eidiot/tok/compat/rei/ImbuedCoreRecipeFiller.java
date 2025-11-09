package dev.ahnaf30eidiot.tok.compat.rei;

import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import java.util.*;

import dev.ahnaf30eidiot.tok.item.TOKItems;
import dev.ahnaf30eidiot.tok.recipe.TotemCoreImbueRecipe;

public class ImbuedCoreRecipeFiller implements CraftingRecipeFiller<TotemCoreImbueRecipe> {
    @Override
    public Collection<Display> apply(RecipeEntry<TotemCoreImbueRecipe> recipe) {
        EntryIngredient coreStack = EntryIngredient.of(EntryStacks.of(TOKItems.TOTEM_CORE));
        Set<Identifier> registeredPotions = new HashSet<>();
        List<Display> displays = new ArrayList<>();
        
        EntryRegistry.getInstance().getEntryStacks().filter(entry -> entry.getValueType() == ItemStack.class && entry.<ItemStack>castValue().getItem() == Items.LINGERING_POTION).forEach(entry -> {
            ItemStack itemStack = (ItemStack) entry.getValue();
            PotionContentsComponent potion = itemStack.get(DataComponentTypes.POTION_CONTENTS);
            if (potion.potion().isPresent() && potion.potion().get().getKey().isPresent() && registeredPotions.add(potion.potion().get().getKey().get().getValue())) {
                List<EntryIngredient> input = new ArrayList<>();
                for (int i = 0; i < 4; i++)
                    input.add(coreStack);
                input.add(EntryIngredients.of(itemStack));
                for (int i = 0; i < 4; i++)
                    input.add(coreStack);
                ItemStack outputStack = new ItemStack(Items.TIPPED_ARROW, 8);
                outputStack.set(DataComponentTypes.POTION_CONTENTS, potion);
                displays.add(new DefaultCustomDisplay(recipe, input, List.of(EntryIngredients.of(outputStack))));
            }
        });
        
        return displays;
    }
    
    @Override
    public Class<TotemCoreImbueRecipe> getRecipeClass() {
        return TotemCoreImbueRecipe.class;
    }
}
