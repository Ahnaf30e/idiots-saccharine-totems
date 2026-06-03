package dev.ahnaf30eidiot.tok.event;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.item.TOKItems;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

public class TOKEventsClient {
    
    
    public static void registerItemClientEvents() {

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) { // bottom layer in item model
                PotionContentsComponent pot = stack.get(DataComponentTypes.POTION_CONTENTS);
                if (pot != null && pot.hasEffects()) {
                        return  0xFF000000 | pot.getColor(); // Force opaque
                }
                return 0xFFFFC336; // Honey-ish
            }
            return 0xFFFFFFFF;
        }, TOKItems.IMBUED_CORE);
    }
}
