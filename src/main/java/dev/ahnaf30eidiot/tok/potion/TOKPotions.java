package dev.ahnaf30eidiot.tok.potion;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import dev.ahnaf30eidiot.tok.item.TOKItems;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TOKPotions {
    public static final RegistryEntry<Potion> FERROUS_POTION = registerPotion("ferrous", new Potion(new StatusEffectInstance(TOKEffects.FERROUS, 180, 0)));


    private static RegistryEntry<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(IdiotsSaccharineTotems.MOD_ID, name), potion);
    } 

    public static void registerPotions() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.REGENERATION, TOKItems.CLAMPED_FERROUS_LINK, TOKPotions.FERROUS_POTION);
        });
    }

}
