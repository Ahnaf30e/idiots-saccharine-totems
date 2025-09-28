package dev.ahnaf30eidiot.item;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final Item TOTEM_OF_KEEPING = registerItem("totem_of_keeping", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_FERROUS = registerItem("totem_of_ferrous", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_TENACITY = registerItem("totem_of_tenacity", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_PERSEVERANCE = registerItem("totem_of_perseverance", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));

    
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(IdiotsSaccharineTotems.MOD_ID, name), item);
    }

    public static void registerModItems() {
        IdiotsSaccharineTotems.LOGGER.info("Registering mod items for " + IdiotsSaccharineTotems.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(TOTEM_OF_KEEPING);
            entries.add(TOTEM_OF_FERROUS);
            entries.add(TOTEM_OF_TENACITY);
            entries.add(TOTEM_OF_PERSEVERANCE);
        });
    }
}
