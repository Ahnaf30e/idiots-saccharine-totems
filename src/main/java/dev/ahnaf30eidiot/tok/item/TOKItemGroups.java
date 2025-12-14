package dev.ahnaf30eidiot.tok.item;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TOKItemGroups {
    
    public static final ItemGroup TOTEMS_CORES = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(IdiotsSaccharineTotems.MOD_ID, "totems_cores"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.saccharine_totems.totems_cores"))
                    .icon(() -> new ItemStack(TOKItems.TOTEM_CORE))
                    .entries((displayContext, entries) -> {
                        entries.add(TOKItems.IMBUED_CORE);
                        Registries.POTION.stream().forEach(potion -> {
                            // if (potion.getEffects().isEmpty())
                            //     return; // Keep the water one for useless but fun honey core
                            ItemStack imbued = new ItemStack(TOKItems.IMBUED_CORE);
                            imbued.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(RegistryEntry.of(potion)));
                            entries.add(imbued);
                        });
                    })
                    .build());

    public static void registerItemGroups() {
        // just to make sure class loads
    }
}
