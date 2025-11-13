package dev.ahnaf30eidiot.tok.compat.rei;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.commons.lang3.tuple.Pair;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.item.TOKItems;
import dev.ahnaf30eidiot.tok.recipe.TotemCoreImbueRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.anvil.AnvilRecipe;
import me.shedaniel.rei.plugin.common.displays.anvil.DefaultAnvilDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TOKReiPlugin implements REIClientPlugin {
    private static final CraftingRecipeFiller<TotemCoreImbueRecipe> IMBUED_CORE_FILLER = new ImbuedCoreRecipeFiller();

    // @Override
    // public void registerCategories(CategoryRegistry registry) {
    // IMBUED_CORE_FILLER.registerCategories(registry);
    // }

    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        registry.group(Identifier.of("saccharine_totems", "totem_core"),
                Text.translatable("item.saccharine_totems.totem_core"),
                stack -> stack.getType() == VanillaEntryTypes.ITEM
                        && stack.<ItemStack>castValue().isOf(TOKItems.IMBUED_CORE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ItemStack tokStackBase = TOKItems.TOTEM_OF_KEEPING.getDefaultStack();
        ItemStack enderEye = Items.ENDER_PEARL.getDefaultStack();
        ItemStack result = new ItemStack(TOKItems.TOTEM_OF_KEEPING);

        // registry.registerFiller(TotemCoreImbueRecipe.class, DefaultAnvilDisplay::new);
        // IdiotsSaccharineTotems.LOGGER.info("Bollocs: " + output.get());
        IMBUED_CORE_FILLER.registerDisplays(registry);
        
        registry.add(new DefaultAnvilDisplay(
                List.of(EntryIngredients.of(tokStackBase), EntryIngredients.of(enderEye)),
                Collections.singletonList(EntryIngredients.of(result)),
                Optional.empty(),
                OptionalInt.of(1)));
    }

}
