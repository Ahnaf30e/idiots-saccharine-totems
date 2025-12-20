package dev.ahnaf30eidiot.tok.render.layer;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.api.TOKTrackedEntity;
import dev.ahnaf30eidiot.tok.compat.TOKModChecker;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import dev.ahnaf30eidiot.tok.render.TOKShaders;
import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class FerrousFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    public FerrousFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    public static final Identifier FERROUS_TEXTURE = Identifier.of(IdiotsSaccharineTotems.MOD_ID,
            "textures/misc/ferrous_glint_entity.png");


    @Override
    public void render(MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            T entity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch) {

        if (entity.hasStatusEffect(TOKEffects.FERROUS) || ((TOKTrackedEntity) entity).isFerrous()) {

            boolean isIrisActive = TOKModChecker.isIrisShaderLoaded();
            // boolean isIrisActive = true;

            // Identifier texture = getTexture(entity);
            Identifier texture = isIrisActive ? FERROUS_TEXTURE : getTexture(entity);
            // VertexConsumer consumer = ItemRenderer.getArmorGlintConsumer(
            // vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), true
            // );
            VertexConsumer consumer = isIrisActive ? vertexConsumers.getBuffer(FerrousRenderLayers.irisFerrous(texture,
                    (entity.age + tickDelta) * 0.01F, (entity.age + tickDelta) * 0.005F))
                    : vertexConsumers.getBuffer(FerrousRenderLayers.animatedFerrous(texture));

            ShaderProgram shader = isIrisActive ? null : TOKShaders.FERROUS_SHADER;

            if (shader != null) {
                RenderSystem.setShader(() -> shader);
                RenderSystem.setShaderTexture(0, texture);

                var timeUniform = shader.getUniform("Time");
                if (timeUniform != null)
                    timeUniform.set((MinecraftClient.getInstance().world.getTime() + tickDelta) % 60F);

                var posUniform = shader.getUniform("EntityPos");
                if (posUniform != null) {
                    if (!TOKModChecker.isIrisLoaded()) {
                        posUniform.set((float) entity.getX(), (float) entity.getY(), (float) entity.getZ());
                    } else {
                        posUniform.set(110F, 110F, 110F);
                    }
                }

            }

            this.getContextModel().render(matrices, consumer, light,
                    OverlayTexture.DEFAULT_UV);

        }
    }
}
