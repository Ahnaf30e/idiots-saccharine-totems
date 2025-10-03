package dev.ahnaf30eidiot.tok.item.custom;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.StringHelper;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
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
                        .translatable(Potion.finishTranslationKey(potion.potion(),
                                Items.POTION.getTranslationKey() + ".effect.")));
            }

            if (pot == Potions.WATER.value()) {
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);

        if (potion == null || !potion.potion().isPresent())
            return TypedActionResult.fail(stack);

        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public static int getLickBalancedDuration(int duration, float n, float p) {
        return (int) (Math.max(1, (Math.log((duration - 1) / p + 1) * n)) + 15) * Integer.signum(duration - 1) + 1;
    }

    public static final int baseN = 318;
    public static final int baseP = 1120;

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potion != null && !world.isClient) {
            for (StatusEffectInstance inst : potion.getEffects()) {
                int scaled = getLickBalancedDuration(inst.getDuration(), baseN, baseP);
                user.addStatusEffect(new StatusEffectInstance(
                        inst.getEffectType(), scaled, inst.getAmplifier(), inst.isAmbient(), inst.shouldShowParticles(),
                        inst.shouldShowIcon()));
            }
        }

        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, 80);
        }
        return stack;
    }

    public static Text getDurationText(StatusEffectInstance effect, float multiplier, float tickRate) {
        if (effect.isInfinite()) {
            return Text.translatable("effect.duration.infinite");
        } else {
            int i = MathHelper.floor(getLickBalancedDuration(effect.getDuration(), baseN, baseP) * multiplier);
            return Text.literal(StringHelper.formatTicks(i, tickRate));
        }
    }

    public static void buildTooltip(Iterable<StatusEffectInstance> effects, Consumer<Text> textConsumer,
            float durationMultiplier, float tickRate) {
        List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> list = Lists
                .<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>>newArrayList();
        boolean bl = true;

        for (StatusEffectInstance statusEffectInstance : effects) {
            bl = false;
            MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
            RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
            registryEntry.value().forEachAttributeModifier(statusEffectInstance.getAmplifier(),
                    (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
            if (statusEffectInstance.getAmplifier() > 0) {
                mutableText = Text.translatable("potion.withAmplifier", mutableText,
                        Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
            }

            if (!statusEffectInstance.isDurationBelow(20)) {
                mutableText = Text.translatable("potion.withDuration", mutableText,
                        getDurationText(statusEffectInstance, durationMultiplier, tickRate));
            }

            textConsumer.accept(mutableText.formatted(registryEntry.value().getCategory().getFormatting()));
        }

        if (bl) {
            textConsumer.accept(Text.translatable("effect.none").formatted(Formatting.GRAY)); // NONE_TEXT
        }

        if (!list.isEmpty()) {
            textConsumer.accept(ScreenTexts.EMPTY);
            textConsumer.accept(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));

            for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : list) {
                EntityAttributeModifier entityAttributeModifier = pair.getSecond();
                double d = entityAttributeModifier.value();
                double e;
                if (entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && entityAttributeModifier
                                .operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = entityAttributeModifier.value();
                } else {
                    e = entityAttributeModifier.value() * 100.0;
                }

                if (d > 0.0) {
                    textConsumer.accept(
                            Text.translatable(
                                    "attribute.modifier.plus." + entityAttributeModifier.operation().getId(),
                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                    Text.translatable(pair.getFirst().value().getTranslationKey()))
                                    .formatted(Formatting.BLUE));
                } else if (d < 0.0) {
                    e *= -1.0;
                    textConsumer.accept(
                            Text.translatable(
                                    "attribute.modifier.take." + entityAttributeModifier.operation().getId(),
                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                    Text.translatable(pair.getFirst().value().getTranslationKey()))
                                    .formatted(Formatting.RED));
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potion != null && potion.potion().isPresent()) {
            Iterable<StatusEffectInstance> effects = potion.getEffects();
            buildTooltip(effects, tooltip::add, 1.0F, context.getUpdateTickRate());
        }
    }
}
