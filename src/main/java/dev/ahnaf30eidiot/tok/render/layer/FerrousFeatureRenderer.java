package dev.ahnaf30eidiot.tok.render.layer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.api.TOKTrackedEntity;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import dev.ahnaf30eidiot.tok.render.TOKShaders;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class FerrousFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    public FerrousFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    public static final Identifier FERROUS_TEXTURE = Identifier.of(IdiotsSaccharineTotems.MOD_ID,
            "textures/misc/ferrous_glint_entity.png");
    
    public static boolean irisNotLoaded = false;

    private boolean isIrisShaderLoaded() {
        if (irisNotLoaded) return false;
        try {
            return IrisApi.getInstance().isShaderPackInUse();
        } catch (NoClassDefFoundError e) {
            irisNotLoaded = true;
            return false;
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

        if (entity.hasStatusEffect(TOKEffects.FERROUS) || ((TOKTrackedEntity) entity).isFerrous()) {

            boolean isIrisActive = isIrisShaderLoaded();
            // boolean isIrisActive = true;
            IdiotsSaccharineTotems.LOGGER.info(String.valueOf(isIrisActive));

            // Identifier texture = getTexture(entity);
            Identifier texture = isIrisActive ? FERROUS_TEXTURE : getTexture(entity);
            // VertexConsumer consumer = ItemRenderer.getArmorGlintConsumer(
            // vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), true
            // );
            VertexConsumer consumer = isIrisActive ? vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(texture,
                    (entity.age + tickDelta) * 0.01F, (entity.age + tickDelta) * 0.005F)) : vertexConsumers.getBuffer(FerrousRenderLayers.ferrous(texture));

            ShaderProgram shader = TOKShaders.FERROUS_SHADER;

            RenderSystem.setShader(() -> shader);
            RenderSystem.setShaderTexture(0, texture);

            var tex = MinecraftClient.getInstance().getTextureManager().getTexture(texture);

            if (shader != null) {
                var timeUniform = shader.getUniform("Time");
                if (timeUniform != null)
                    timeUniform.set((entity.age + tickDelta) % 60F);

                var posUniform = shader.getUniform("EntityPos");
                if (posUniform != null)
                    posUniform.set((float) entity.getX(), (float) entity.getY(), (float) entity.getZ());

                var screenUniform = shader.getUniform("SSize");
                if (screenUniform != null) {
                    RenderSystem.bindTexture(tex.getGlId()); // bind the texture
                    int w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                    int h = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

                    screenUniform.set((float) w, (float) h);
                }

                this.getContextModel().render(matrices, consumer, light,
                        OverlayTexture.DEFAULT_UV);

            } else {
                IdiotsSaccharineTotems.LOGGER.error("Hey man, the saccharine shaders didn't load? Huh.");
            }

        }
    }
}
