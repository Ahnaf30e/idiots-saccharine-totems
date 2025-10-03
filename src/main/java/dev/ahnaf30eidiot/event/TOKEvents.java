package dev.ahnaf30eidiot.event;

import dev.ahnaf30eidiot.command.TOKCommands;
import dev.ahnaf30eidiot.item.TOKItems;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;

public class TOKEvents {

    public static void registerEvents() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TOKCommands.registerCommands(dispatcher);
        });
    }

    public static void registerLootTables() {

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if ((key.equals(LootTables.WOODLAND_MANSION_CHEST) || key.equals(LootTables.SIMPLE_DUNGEON_CHEST))
                    && source.isBuiltin()) {
                tableBuilder.pool(LootPool.builder()
                        .with(ItemEntry.builder(TOKItems.TOTEM_CORE).weight(10))
                        .rolls(ConstantLootNumberProvider.create(1)));
            }
        });
    }

    public static void registerItemEvents() {

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) { // bottom layer in item model
                PotionContentsComponent pot = stack.get(DataComponentTypes.POTION_CONTENTS);
                if (pot != null && pot.potion().isPresent()) {
                    Potion potion = pot.potion().get().value();
                    if (potion != Potions.WATER && potion != Potions.AWKWARD && potion != Potions.MUNDANE) {
                        return pot.getColor();
                    }
                }
                return 0;
            }
            return 0xFFFFFFFF;
        }, TOKItems.TOTEM_CORE);
    }
}
