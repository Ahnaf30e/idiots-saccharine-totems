package dev.ahnaf30eidiot.tok.potion;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TOKPotions {
    public static final RegistryEntry<Potion> FERROUS_POTION = registerPotion("ferrous_potion", new Potion(new StatusEffectInstance(TOKEffects.FERROUS)));


    private static RegistryEntry<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(IdiotsSaccharineTotems.MOD_ID, name), potion);
    } 

    public static void registerPotions() {
        // NoOp
    }

}
