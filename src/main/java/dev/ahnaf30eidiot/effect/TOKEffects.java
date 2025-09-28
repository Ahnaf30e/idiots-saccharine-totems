package dev.ahnaf30eidiot.effect;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TOKEffects {

    public static final RegistryEntry<StatusEffect> FERROUS = registerStatusEffect("ferrous", new FerrousEffect(StatusEffectCategory.BENEFICIAL, 0xa8a8a8).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(IdiotsSaccharineTotems.MOD_ID, "ferrous"), -0.2f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(IdiotsSaccharineTotems.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        IdiotsSaccharineTotems.LOGGER.info("Registering Mod Effects for " + IdiotsSaccharineTotems.MOD_ID);

    }
}
