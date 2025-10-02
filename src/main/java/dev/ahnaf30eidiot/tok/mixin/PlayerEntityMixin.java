package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.ahnaf30eidiot.api.TOKTrackedEntity;
import dev.ahnaf30eidiot.tag.TOKTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    
	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	private void handleFerrous(DamageSource source, float amount, CallbackInfo ci) {
		LivingEntity self = (PlayerEntity) (Object) this;

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
				ci.cancel();
			}
		}
	}
}
