package dev.ahnaf30eidiot.item.custom;

import java.util.List;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;

public class TotemCoreItem extends Item {
    public TotemCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);
        String imbuedText = "item.saccharine_totems.totem_core.imbued";
        if (potion != null && potion.potion().isPresent()) {
            List<StatusEffectInstance> effects = potion.potion().get().value().getEffects();
            if (effects.size() == 1) {
                return Text.translatable(imbuedText, Text.translatable(effects.get(0).getTranslationKey()));
            }
            // if (effects.size() > 1 && potion.potion().get().getKey().isPresent() &&
            // potion.potion().get().getKey().get().getValue() != null) { // Simplify
            // return Text.translatable(imbuedText,
            // Text.translatable(Util.createTranslationKey("potion",
            // potion.potion().get().getKey().get().getValue())));
            // }
            // Potion.finishTranslationKey(potion.potion(),
            // "item.minecraft.potion.effect.");
            if (effects.size() > 1) {
                return Text.translatable(imbuedText, Text.translatable(Potion.finishTranslationKey(potion.potion(),"item.minecraft.potion.effect.")));
            }

            return Text.translatable("item.saccharine_totems.totem_core.mysterious");
        }
        return Text.translatable("item.saccharine_totems.totem_core");
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 12;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
