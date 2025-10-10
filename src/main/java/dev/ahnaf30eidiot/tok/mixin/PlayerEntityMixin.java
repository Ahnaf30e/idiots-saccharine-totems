package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.ahnaf30eidiot.tok.api.TOKTrackedEntity;
import dev.ahnaf30eidiot.tok.block.TOKBlocks;
import dev.ahnaf30eidiot.tok.tag.TOKTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    // @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    // private void onTick(CallbackInfo ci) {
        
    //     Entity self = (Entity)(Object)this;
	// 	World world = self.getWorld();

    //     if (world.getBlockState(self.getBlockPos().down()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
    //     || world.getBlockState(self.getBlockPos().up()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
    //     || world.getBlockState(self.getBlockPos().north()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
    //     || world.getBlockState(self.getBlockPos().south()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
    //     || world.getBlockState(self.getBlockPos().east()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)
    //     || world.getBlockState(self.getBlockPos().west()).isOf(TOKBlocks.FERROUS_METAL_BLOCK)) {
    //         Vec3d vel = self.getVelocity();
    //         // cancel any damping (including gravity if you wish)
    //         // self.setVelocity(vel.x, vel.y, vel.z);
    //         self.setVelocity(vel.x, vel.y, vel.z);
    //         ci.cancel();
    //     }
    // }

	// Fuck you Mojang, why the fuck did you overwrite applyDamage for PlayerEntity.
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
