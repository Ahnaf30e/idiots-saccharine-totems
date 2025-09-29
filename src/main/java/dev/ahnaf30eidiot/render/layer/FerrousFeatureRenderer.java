package dev.ahnaf30eidiot.render.layer;

import dev.ahnaf30eidiot.effect.TOKEffects;
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

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
            float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
            float headPitch) {

                if (!entity.hasStatusEffect(TOKEffects.FERROUS)) return;

                Identifier texture  = getTexture(entity);
                VertexConsumer consumer = vertexConsumers.getBuffer(FerrousRenderLayers.ferrous(texture));

                this.getContextModel().render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }
}
