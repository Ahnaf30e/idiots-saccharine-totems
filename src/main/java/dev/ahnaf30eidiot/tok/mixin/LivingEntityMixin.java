package dev.ahnaf30eidiot.tok.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ahnaf30eidiot.effect.TOKEffects;
import dev.ahnaf30eidiot.item.TOKItems;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void handleFerrous(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity)(Object)this;
		
		if (self.hasStatusEffect(TOKEffects.FERROUS)) {
			if (source.isOf(DamageTypes.MAGIC) || source.isOf(DamageTypes.INDIRECT_MAGIC)) {
				return;
			}

			cir.setReturnValue(false);
		}
	}

	@Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
	private void handleTOKTotems(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return;
		}
		LivingEntity self = (LivingEntity) (Object) this;

		ItemStack used = pickTotem(self);
		ItemStack used2 = used;

		if (used.isEmpty() || used.isOf(Items.TOTEM_OF_UNDYING))
			return;
		if (used.isOf(TOKItems.TOTEM_OF_KEEPING)) {
			// TODO:
			cir.setReturnValue(true);
			return;
		}

		if (!self.getWorld().isClient()) {

			self.setHealth(2.0F);

			if (used.isOf(TOKItems.TOTEM_OF_TENACITY)) {
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 700, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 400, 1));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 4, 2));
			} else if (used.isOf(TOKItems.TOTEM_OF_PERSEVERANCE)) {
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 3, 1));
			} else if (used.isOf(TOKItems.TOTEM_OF_FERROUS)) {
				self.addStatusEffect(new StatusEffectInstance(TOKEffects.FERROUS, 160, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1000, 1));
			}
			self.getWorld().sendEntityStatus(self, (byte) 35);
			
			used2.decrement(1);

			cir.setReturnValue(true);
			return;
		}
	}

	private ItemStack pickTotem(LivingEntity self) {
		ItemStack main = self.getMainHandStack();
		ItemStack off = self.getOffHandStack();

		// If main hand is Totem of Keeping, let the offhand win if it has *any* other
		// valid totem
		if (main.isOf(TOKItems.TOTEM_OF_KEEPING)) {
			if (isCustomOrVanillaTotem(off)) {
				return off;
			}
			return main; // no better option, fallback
		}

		// Otherwise, normal rule: main hand wins if valid
		if (isCustomOrVanillaTotem(main))
			return main;
		if (isCustomOrVanillaTotem(off))
			return off;

		return ItemStack.EMPTY;
	}

	private boolean isCustomOrVanillaTotem(ItemStack stack) {
		return stack.isOf(TOKItems.TOTEM_OF_TENACITY)
				|| stack.isOf(TOKItems.TOTEM_OF_PERSEVERANCE)
				|| stack.isOf(TOKItems.TOTEM_OF_FERROUS)
				|| stack.isOf(Items.TOTEM_OF_UNDYING) // vanilla
				|| stack.isOf(TOKItems.TOTEM_OF_KEEPING);
	}
}