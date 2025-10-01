package dev.ahnaf30eidiot.tok.mixin;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ahnaf30eidiot.api.TOKTrackedEntity;
import dev.ahnaf30eidiot.component.TOKComponents;
import dev.ahnaf30eidiot.component.TOKPlayerComponents;
import dev.ahnaf30eidiot.effect.TOKEffects;
import dev.ahnaf30eidiot.item.TOKItems;
import dev.ahnaf30eidiot.tag.TOKTags;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TOKTrackedEntity {

	private static final TrackedData<Boolean> FERROUS = DataTracker.registerData(LivingEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	static {
		ServerPlayerEvents.AFTER_RESPAWN.register((newPlayer, oldPlayer, source) -> {
			if (!(newPlayer instanceof ServerPlayerEntity) && !newPlayer.getWorld().isClient())
				return;

			var comp = TOKPlayerComponents.PENDING_TOTEM.get(newPlayer);
			ItemStack pendingTotem = comp.get();
			if (!pendingTotem.isEmpty()) {
				newPlayer.getInventory().insertStack(pendingTotem);
				comp.set(ItemStack.EMPTY);
				TOKPlayerComponents.PENDING_TOTEM.sync(newPlayer);
				newPlayer.playerScreenHandler.sendContentUpdates();
			}
		});
	}

	public boolean isFerrous() { // For showing effect on non-player entities.
		return ((LivingEntity) (Object) this).getDataTracker().get(FERROUS);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void addFerrousTrackedData(DataTracker.Builder builder, CallbackInfo ci) {
		// Default to false when entity is created
		builder.add(FERROUS, false);
	}

	@Inject(method = "tickStatusEffects", at = @At("TAIL"))
	private void updateFerrousTrackedData(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;

		if (!self.getWorld().isClient()) {
			boolean hasFerrous = self.hasStatusEffect(TOKEffects.FERROUS);
			self.getDataTracker().set(FERROUS, hasFerrous, false);
		}
	}

	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	private void handleFerrous(DamageSource source, float amount, CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;

		if (self.hasStatusEffect(TOKEffects.FERROUS)) {
			self.getWorld().playSound(
					null,
					self.getX(),
					self.getY(),
					self.getZ(),
					SoundEvents.ENTITY_IRON_GOLEM_HURT,
					self.getSoundCategory(),
					1.0F, // volume
					0.8F + self.getRandom().nextFloat() * 0.4F // pitch
			);
			if (!source.isIn(TOKTags.FERROUS_ALLOWED)) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
	private void handleTOKTotems(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return;
		}
		LivingEntity self = (LivingEntity) (Object) this;

		ItemStack used = pickTotem(self);

		if (used.isEmpty() || used.isOf(Items.TOTEM_OF_UNDYING))
			return;

		if (!self.getWorld().isClient()) {

			if (self instanceof ServerPlayerEntity player && used.isOf(TOKItems.TOTEM_OF_KEEPING)) {
				ItemStack totem = used.copy();

				if (used.contains(TOKComponents.STORED_INVENTORY)) {
					return;
				}
				// Serialization or whatever
				DefaultedList<ItemStack> inv = player.getInventory().main;
				List<ItemStack> stacks = inv.stream()
						.filter(s -> !s.isEmpty() && !ItemStack.areEqual(s, used))
						.map(ItemStack::copy)
						.toList();

				// Attach component
				totem.set(TOKComponents.STORED_INVENTORY, new TOKComponents.StoredInventory(stacks));
				// Clear all items so nothing drops / grave mods get nothing
				player.getInventory().clear();
				TOKPlayerComponents.PENDING_TOTEM.get(player).set(totem);
				TOKPlayerComponents.PENDING_TOTEM.sync(player);

				cir.setReturnValue(true);
				return;
			}

			self.setHealth(2.0F);

			if (used.isOf(TOKItems.TOTEM_OF_TENACITY)) {
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 40, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 700, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 400, 1));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 4, 2));
			} else if (used.isOf(TOKItems.TOTEM_OF_PERSEVERANCE)) {
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 1));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 2, 2));
			} else if (used.isOf(TOKItems.TOTEM_OF_FERROUS)) {
				self.addStatusEffect(new StatusEffectInstance(TOKEffects.FERROUS, 160, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1000, 1));
			}

			self.getWorld().sendEntityStatus(self, (byte) 35);

			used.decrement(1);

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