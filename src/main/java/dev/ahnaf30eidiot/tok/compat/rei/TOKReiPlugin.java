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
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.anvil.AnvilRecipe;
import me.shedaniel.rei.plugin.common.displays.anvil.DefaultAnvilDisplay;
import net.minecraft.item.Item;
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
        IMBUED_CORE_FILLER.registerDisplays(registry);

        Item tokItem = TOKItems.TOTEM_OF_KEEPING;
        ItemStack tokBase = tokItem.getDefaultStack();
        int max = tokBase.getMaxDamage();

        // Ender Eye recipe
        ItemStack enderEye = Items.ENDER_EYE.getDefaultStack();
        ItemStack eyeDamaged = new ItemStack(tokItem);
        eyeDamaged.setDamage(max - 1);

        registry.add(new DefaultAnvilDisplay(
                List.of(EntryIngredients.of(eyeDamaged), EntryIngredients.of(enderEye)),
                Collections.singletonList(EntryIngredients.of(new ItemStack(tokItem))),
                Optional.empty(),
                OptionalInt.of(1)));

        // Make multiple damage variants
        for (int i = 1; i <= 2; i++) {
            ItemStack damaged = new ItemStack(tokItem);
            damaged.setDamage(max / 3 * i);

            ItemStack output = tokBase;
            ItemStack enderPearlRepair = new ItemStack(Items.ENDER_PEARL);
            enderPearlRepair.setCount(i);
            EntryIngredient repairIng = EntryIngredients.of(enderPearlRepair);

            registry.add(new DefaultAnvilDisplay(
                    List.of(EntryIngredients.of(damaged), repairIng),
                    Collections.singletonList(EntryIngredients.of(output)),
                    Optional.empty(),
                    OptionalInt.of(i)));
        }
    }
}
