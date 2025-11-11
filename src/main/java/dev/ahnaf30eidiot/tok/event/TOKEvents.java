package dev.ahnaf30eidiot.tok.event;

import dev.ahnaf30eidiot.tok.command.TOKCommands;
import dev.ahnaf30eidiot.tok.item.TOKItems;
import dev.ahnaf30eidiot.tok.recipe.ingredient.TOKComponentIngredientSerializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TOKEvents {

    public static void registerEvents() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TOKCommands.registerCommands(dispatcher);
        });
    }

    private static final Identifier WOODLAND_MANSION_CHEST_ID = LootTables.WOODLAND_MANSION_CHEST.getValue();
    private static final Identifier SIMPLE_DUNGEON_CHEST_ID = LootTables.SIMPLE_DUNGEON_CHEST.getValue();
    private static final Identifier ABANDONED_MINESHAFT_CHEST_ID = LootTables.ABANDONED_MINESHAFT_CHEST.getValue();
    private static final Identifier JUNGLE_TEMPLE_CHEST_ID = LootTables.JUNGLE_TEMPLE_CHEST.getValue();
    private static final Identifier DESERT_PYRAMID_CHEST_ID = LootTables.DESERT_PYRAMID_CHEST.getValue();
    private static final Identifier STRONGHOLD_CORRIDOR_CHEST_ID = LootTables.STRONGHOLD_CORRIDOR_CHEST.getValue();
    

    public static void registerLootTables() {

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            Identifier k = key.getValue();
            if (WOODLAND_MANSION_CHEST_ID.equals(k) 
            || SIMPLE_DUNGEON_CHEST_ID.equals(k) 
            || ABANDONED_MINESHAFT_CHEST_ID.equals(k)
            || JUNGLE_TEMPLE_CHEST_ID.equals(k)
            || DESERT_PYRAMID_CHEST_ID.equals(k)
            || STRONGHOLD_CORRIDOR_CHEST_ID.equals(k)) { // && source.isBuiltin()
                LootPool.Builder builder = LootPool.builder()
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_KEEPING)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.2F, 0.4F)))
                            .weight(2))
                        .with(ItemEntry.builder(TOKItems.TOTEM_CORE).weight(15))
                        .rolls(UniformLootNumberProvider.create(0, 0.4F));

                tableBuilder.pool(builder);
            }
        });
    }

    public static void registerItemEvents() {

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) { // bottom layer in item model
                PotionContentsComponent pot = stack.get(DataComponentTypes.POTION_CONTENTS);
                if (pot != null && pot.potion().isPresent()) {
                    RegistryEntry<Potion> potion = pot.potion().get();
                    if (!potion.value().getEffects().isEmpty() || Registries.POTION.getId(potion.value()).getNamespace() != "minecraft") {
                        return pot.getColor();
                    }
                    return 0xFFF5A83D; // Honey-ish
                }
                return 0;
            }
            return 0xFFFFFFFF;
        }, TOKItems.TOTEM_CORE);
    }

    public static void registerIngredientSerializers() {
        CustomIngredientSerializer.register(
            new TOKComponentIngredientSerializer()
        );
    }
}
