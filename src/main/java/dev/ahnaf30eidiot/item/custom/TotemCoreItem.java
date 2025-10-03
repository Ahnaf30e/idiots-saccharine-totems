package dev.ahnaf30eidiot.item.custom;

import java.util.List;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class TotemCoreItem extends Item {
    public TotemCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);
        String imbuedText = "item.saccharine_totems.totem_core.imbued";
        if (potion != null && potion.potion().isPresent()) {

            Potion pot = potion.potion().get().value();
            List<StatusEffectInstance> effects = pot.getEffects();

            if (effects.size() == 1) {
                return Text.translatable(imbuedText, Text.translatable(effects.get(0).getTranslationKey()));
            }

            if (effects.size() > 1) {
                return Text.translatable(imbuedText, Text
                        .translatable(Potion.finishTranslationKey(potion.potion(), "item.minecraft.potion.effect.")));
            }

            if (pot == Potions.WATER.value() ) {
                return Text.translatable("item.saccharine_totems.totem_core.saccharine");
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

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potion != null && !world.isClient) {
            for (StatusEffectInstance inst : potion.getEffects()) {
                int scaled = (Math.max(1, inst.getDuration() / 26) + 16) * Integer.signum(inst.getDuration());
                user.addStatusEffect(new StatusEffectInstance(
                    inst.getEffectType(), scaled, inst.getAmplifier(), inst.isAmbient(), inst.shouldShowParticles(), inst.shouldShowIcon()
                ));
            }
        }

        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, 80);
        }
        return stack;
    }

    @Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);

        if (potion == null || !potion.potion().isPresent()) return TypedActionResult.fail(stack);

		return ItemUsage.consumeHeldItem(world, user, hand);
	}
}
