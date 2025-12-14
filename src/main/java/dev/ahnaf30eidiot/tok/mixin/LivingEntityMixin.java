package dev.ahnaf30eidiot.tok.mixin;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.api.TOKPersistentValues;
import dev.ahnaf30eidiot.tok.api.TOKTrackedEntity;
import dev.ahnaf30eidiot.tok.block.TOKBlocks;
import dev.ahnaf30eidiot.tok.compat.TOKModChecker;
import dev.ahnaf30eidiot.tok.compat.TOKTrinkets;
import dev.ahnaf30eidiot.tok.component.TOKComponents;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import dev.ahnaf30eidiot.tok.item.TOKItems;
import dev.ahnaf30eidiot.tok.tag.TOKTags;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TOKTrackedEntity {

	private static final TrackedData<Boolean> FERROUS = DataTracker.registerData(LivingEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	static {
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, source) -> {
			// if (!newPlayer.getWorld().isClient()) {
			// System.out.println("SERVER: Totem triggered for " + newPlayer);
			// } else {
			// System.out.println("CLIENT: Totem triggered for " + newPlayer);
			// }

			TOKPersistentValues state = TOKPersistentValues.get(newPlayer.getServerWorld());

			ItemStack pending = state.getHeldOn().remove(newPlayer.getUuid());
			state.markDirty();
			if (pending != null && !pending.isEmpty() && !newPlayer.getWorld().isClient()) {
				newPlayer.getInventory().insertStack(pending);
				// newPlayer.playerScreenHandler.sendContentUpdates();
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

	@ModifyVariable(
		method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V",
		at = @At(
			value = "LOAD"
		),
		ordinal = 0
	)
	private float handleFerrousDamage(float amnt, DamageSource source, float amount) {
		LivingEntity self = (LivingEntity) (Object) this;

		IdiotsSaccharineTotems.LOGGER.info("Shit being done: vam={} s={}, pam={}", amnt, source.getName(), amount);

		if (((TOKTrackedEntity) (Object) this).isFerrous()) {
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
				float f = MathHelper.clamp(Math.min(amnt * 0.1F, 2.0F), 0.0F, self.getHealth() - 1.0F);
				IdiotsSaccharineTotems.LOGGER.info("Modifying Damage: a={} m={}, is_ferrous={}, source={}", amnt, f, ((TOKTrackedEntity) (Object) this).isFerrous(), source.getName());
				return f;
			}
			if (!source.isOf(DamageTypes.GENERIC_KILL)) {
				IdiotsSaccharineTotems.LOGGER.info("Modifying Magic Damage: a={} m={}, is_ferrous={}, source={}", amnt, amnt * 0.35F, ((TOKTrackedEntity) (Object) this).isFerrous(), source.getName());
				return amnt * 0.35F;
			}
		}

		return amnt;
	}

	// @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
	// private void fuckingDebug(float health, CallbackInfo ci) {
	// 	IdiotsSaccharineTotems.LOGGER.info("Resulting: a={} to b={}", ((LivingEntity) (Object) this).getHealth(), health);
	// }

	
	// @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	// private void handleFerrous(DamageSource source, float amount, CallbackInfo ci) {
	// 	LivingEntity self = (LivingEntity) (Object) this;

	// 	if (((TOKTrackedEntity) (Object) this).isFerrous()) {
	// 		self.getWorld().playSound(
	// 				null,
	// 				self.getX(),
	// 				self.getY(),
	// 				self.getZ(),
	// 				SoundEvents.ENTITY_IRON_GOLEM_HURT,
	// 				self.getSoundCategory(),
	// 				1.0F, // volume
	// 				0.8F + self.getRandom().nextFloat() * 0.4F // pitch
	// 		);
	// 		if (!source.isIn(TOKTags.FERROUS_ALLOWED)) {
	// 			ci.cancel();
	// 		}
	// 		if (!source.isOf(DamageTypes.GENERIC_KILL)) {
	// 			float clampedDamage = MathHelper.clamp(amount * 0.75F, amount, amount)
	// 		}
	// 	}
	// }

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
				DefaultedList<ItemStack> inv = DefaultedList.of();

				inv.addAll(player.getInventory().main);
				inv.addAll(player.getInventory().armor);
				inv.addAll(player.getInventory().offHand);

				if (TOKModChecker.isTrinketsLoaded()) TOKTrinkets.collectTrinkets(player, inv);

				List<ItemStack> stacks = inv.stream()
						.filter(s -> !s.isEmpty() && s != used)
						.map(ItemStack::copy)
						.toList();

				// Attach component
				totem.set(TOKComponents.STORED_INVENTORY, new TOKComponents.StoredInventory(stacks));
				// Clear all items so nothing drops / grave mods get nothing
				player.getInventory().clear();
				if (TOKModChecker.isTrinketsLoaded()) TOKTrinkets.emptyTrinkets(player);
				TOKPersistentValues state = TOKPersistentValues.get(player.getServerWorld());
				state.getHeldOn().put(player.getUuid(), totem);
				state.markDirty();

				cir.setReturnValue(true);
				return;
			}

			self.setHealth(2.0F);
			self.clearStatusEffects();
			
				// this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
				// this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
				// this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
			if (used.isOf(TOKItems.TOTEM_OF_TENACITY)) {
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 50, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 700, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 240, 1));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 2, 0));
				self.getWorld().sendEntityStatus(self, EntityStatuses.USE_TOTEM_OF_UNDYING);
			} else if (used.isOf(TOKItems.TOTEM_OF_PERSEVERANCE)) {
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 580, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 2, 0));
				self.getWorld().sendEntityStatus(self, EntityStatuses.USE_TOTEM_OF_UNDYING);
			} else if (used.isOf(TOKItems.TOTEM_OF_FERROUS)) {
				self.addStatusEffect(new StatusEffectInstance(TOKEffects.FERROUS, 220, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 680, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 700, 0));
				self.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 2, 0));
				self.getWorld().sendEntityStatus(self, EntityStatuses.USE_TOTEM_OF_UNDYING);
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

	@Inject(method = "hasNoDrag", at = @At("HEAD"), cancellable = true)
	private void onTravel(CallbackInfoReturnable<Boolean> ci) {
		Entity self = (LivingEntity) (Object) this;
		if (self.isLogicalSideForUpdatingMovement()) {
			World world = self.getWorld();

			if ((world.getBlockState(self.getBlockPos().down()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
					&& self.groundCollision)
					|| (world.getBlockState(self.getBlockPos().up(2)).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
							&& self.verticalCollision)
					|| ((world.getBlockState(self.getBlockPos().north()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
							|| world.getBlockState(self.getBlockPos().south()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
							|| world.getBlockState(self.getBlockPos().east()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
							|| world.getBlockState(self.getBlockPos().west()).isOf(TOKBlocks.FERROUS_METAL_BLOCK))
							&& self.horizontalCollision)) {
				// Vec3d vel = self.getVelocity();
				// // cancel any damping (including gravity if you wish)
				// // self.setVelocity(vel.x, vel.y, vel.z);
				// self.setVelocity(vel.x, vel.y, vel.z);
				// self.updateVelocity(0.02F, movementInput);
				// self.move(MovementType.SELF, self.getVelocity());
				ci.setReturnValue(true);
			}
		}
	}
}