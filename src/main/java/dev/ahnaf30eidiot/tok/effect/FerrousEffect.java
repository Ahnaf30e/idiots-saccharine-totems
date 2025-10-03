package dev.ahnaf30eidiot.tok.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FerrousEffect extends StatusEffect {

    public FerrousEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
