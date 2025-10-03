package dev.ahnaf30eidiot.tok.item;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.item.custom.TotemCoreItem;
import dev.ahnaf30eidiot.tok.item.custom.TotemOfKeepingItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class TOKItems {
    public static final Item TOTEM_OF_KEEPING = registerItem("totem_of_keeping", new TotemOfKeepingItem(new TotemOfKeepingItem.Settings().maxCount(1).rarity(Rarity.UNCOMMON).fireproof().maxDamage(5)));
    public static final Item TOTEM_OF_FERROUS = registerItem("totem_of_ferrous", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).fireproof()));
    public static final Item TOTEM_OF_TENACITY = registerItem("totem_of_tenacity", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).fireproof()));
    public static final Item TOTEM_OF_PERSEVERANCE = registerItem("totem_of_perseverance", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).fireproof()));

    
    public static final Item TOTEM_CORE = registerItem("totem_core", new TotemCoreItem(new TotemCoreItem.Settings().maxCount(64).rarity(Rarity.UNCOMMON).fireproof()));

    
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(IdiotsSaccharineTotems.MOD_ID, name), item);
    }

    public static void registerModItems() {
        IdiotsSaccharineTotems.LOGGER.info("Registering Mod Items for " + IdiotsSaccharineTotems.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(TOTEM_OF_KEEPING);
            entries.add(TOTEM_OF_FERROUS);
            entries.add(TOTEM_OF_TENACITY);
            entries.add(TOTEM_OF_PERSEVERANCE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(TOTEM_CORE);
        });
    }
}
