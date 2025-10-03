package dev.ahnaf30eidiot.item;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TOKItemGroups {
    
    public static final ItemGroup TOTEMS = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of (IdiotsSaccharineTotems.MOD_ID, "totems_cores"),
            FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.saccharine_totems.totems_cores"))
                .icon(() -> new ItemStack(TOKItems.TOTEM_OF_KEEPING))
                .entries((displayContext, entries) -> {
                    entries.add(TOKItems.TOTEM_OF_KEEPING);
                    entries.add(TOKItems.TOTEM_OF_FERROUS);
                    entries.add(TOKItems.TOTEM_OF_TENACITY);
                    entries.add(TOKItems.TOTEM_OF_PERSEVERANCE);
                    entries.add(TOKItems.TOTEM_CORE);
                })
                .build()
    );

    public static void registerItemGroups() {
        // just to make sure class loads
    }
}
