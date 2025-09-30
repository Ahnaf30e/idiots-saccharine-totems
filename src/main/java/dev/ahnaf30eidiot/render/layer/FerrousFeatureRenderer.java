package dev.ahnaf30eidiot.render.layer;

import java.lang.reflect.Field;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.ahnaf30eidiot.effect.TOKEffects;
import dev.ahnaf30eidiot.render.TOKShaders;
import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
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

    public static void debugullah(EntityModel shader) {
        if (shader == null) {
            System.out.println("FERROUS_SHADER is null");
            return;
        }
        System.out.println("==== Shader Debug Info ====");
        for (Field f : EntityModel.class.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                System.out.println(f.getName() + " = " + f.get(shader));
            } catch (Exception e) {
                System.out.println(f.getName() + " = <error: " + e.getMessage() + ">");
            }
        }
    }

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

        if (true) { // entity.hasStatusEffect(TOKEffects.FERROUS)

            Identifier texture = getTexture(entity);
            // VertexConsumer consumer = ItemRenderer.getArmorGlintConsumer(
            // vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), true
            // );
            IdiotsSaccharineTotems.LOGGER.info("[FERROUS] Using Texture: " + texture.toString());
            VertexConsumer consumer = vertexConsumers.getBuffer(FerrousRenderLayers.ferrous(texture));

            ShaderProgram shader = TOKShaders.FERROUS_SHADER;

            RenderSystem.setShader(() -> TOKShaders.FERROUS_SHADER);
            RenderSystem.setShaderTexture(0, texture);

            var tex = MinecraftClient.getInstance().getTextureManager().getTexture(texture);

            if (TOKShaders.FERROUS_SHADER != null) {
                var timeUniform = TOKShaders.FERROUS_SHADER.getUniform("Time");
                if (timeUniform != null)
                    timeUniform.set((MinecraftClient.getInstance().world.getTime() + tickDelta));

                var posUniform = TOKShaders.FERROUS_SHADER.getUniform("EntityPos");
                if (posUniform != null)
                    posUniform.set((float) entity.getX(), (float) entity.getY(), (float) entity.getZ());

                var screenUniform = TOKShaders.FERROUS_SHADER.getUniform("SSize");
                if (screenUniform != null) {
                    RenderSystem.bindTexture(tex.getGlId()); // bind the texture
                    int w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                    int h = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

                    screenUniform.set((float) w, (float) h);
                    IdiotsSaccharineTotems.LOGGER.info("Texture size via GL: " + w + "x" + h);
                }

                this.getContextModel().render(matrices, consumer, light,
                        OverlayTexture.DEFAULT_UV);

                IdiotsSaccharineTotems.LOGGER.error("I AM SUPPOSED TO RENDER RAHHHHHHHHHHH >:(((");
            } else {
                IdiotsSaccharineTotems.LOGGER.error("BITCH NOT LOADING MY SHADERS BITCH ASS FUCKING BITCH");
            }

            debugullah(this.getContextModel());

        }
    }
}
