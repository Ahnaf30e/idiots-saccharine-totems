package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.ahnaf30eidiot.effect.TOKEffects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(method="render*", at=@At("HEAD"), cancellable = true)
    private void handleFerrousGrayscale(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (player.hasStatusEffect(TOKEffects.FERROUS)) {
            RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1.0f);
        }
    }

    @Inject(method="render*", at=@At("RETURN"), cancellable = true)
    private void handleFerrousReset(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
