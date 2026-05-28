package dev.ahnaf30eidiot.tok.event;

import dev.ahnaf30eidiot.tok.command.TOKCommands;
import dev.ahnaf30eidiot.tok.item.TOKItems;
import dev.ahnaf30eidiot.tok.recipe.ingredient.TOKComponentIngredientSerializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class TOKEvents {

    public static void registerEvents() {
        // Command thing for the commands in the commands of moincraf
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TOKCommands.registerCommands(dispatcher);
        });
    }

    private static final Identifier WOODLAND_MANSION_CHEST_ID = LootTables.WOODLAND_MANSION_CHEST.getValue();
    private static final Identifier SIMPLE_DUNGEON_CHEST_ID = LootTables.SIMPLE_DUNGEON_CHEST.getValue();
    private static final Identifier JUNGLE_TEMPLE_CHEST_ID = LootTables.JUNGLE_TEMPLE_CHEST.getValue();
    private static final Identifier DESERT_PYRAMID_CHEST_ID = LootTables.DESERT_PYRAMID_CHEST.getValue();
    private static final Identifier STRONGHOLD_CORRIDOR_CHEST_ID = LootTables.STRONGHOLD_CORRIDOR_CHEST.getValue();
    private static final Identifier VILLAGE_TOOLSMITH_CHEST_ID = LootTables.VILLAGE_TOOLSMITH_CHEST.getValue();
    private static final Identifier VILLAGE_WEAPONSMITH_CHEST_ID = LootTables.VILLAGE_WEAPONSMITH_CHEST.getValue();
    private static final Identifier END_CITY_TREASURE_CHEST_ID = LootTables.END_CITY_TREASURE_CHEST.getValue();
    

    public static void registerLootTables() {

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            Identifier k = key.getValue();
            if (WOODLAND_MANSION_CHEST_ID.equals(k) 
            || SIMPLE_DUNGEON_CHEST_ID.equals(k) 
            || JUNGLE_TEMPLE_CHEST_ID.equals(k)
            || DESERT_PYRAMID_CHEST_ID.equals(k)
            || STRONGHOLD_CORRIDOR_CHEST_ID.equals(k)
            || VILLAGE_TOOLSMITH_CHEST_ID.equals(k)
            || VILLAGE_WEAPONSMITH_CHEST_ID.equals(k)) { // && source.isBuiltin()
                LootPool.Builder builder = LootPool.builder()
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_KEEPING)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.2F, 0.4F)))
                            .weight(5))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_PERSEVERANCE).weight(5))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_TENACITY).weight(5))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_FERROUS).weight(1))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_INSANITY).weight(1))
                        .with(ItemEntry.builder(TOKItems.TOTEM_CORE).weight(12))
                        .rolls(UniformLootNumberProvider.create(0.0F, 1.0F));

                tableBuilder.pool(builder);
            }

            if (END_CITY_TREASURE_CHEST_ID.equals(k)) {
                LootPool.Builder builder = LootPool.builder()
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_KEEPING)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.2F, 0.4F)))
                            .weight(1))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_PERSEVERANCE).weight(3))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_TENACITY).weight(3))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_INSANITY).weight(1))
                        .with(ItemEntry.builder(TOKItems.TOTEM_OF_FERROUS).weight(1))
                        .rolls(UniformLootNumberProvider.create(-0.4F, 1.0F));

                tableBuilder.pool(builder);
            }
        });
    }

    public static void registerIngredientSerializers() {
        CustomIngredientSerializer.register(
            new TOKComponentIngredientSerializer()
        );
    }
}
