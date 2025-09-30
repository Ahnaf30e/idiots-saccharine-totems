package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.ahnaf30eidiot.render.layer.FerrousFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    protected abstract boolean addFeature(FeatureRenderer<T, M> feature);

    @Shadow
    private java.util.List<FeatureRenderer<T, M>> features;

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addFerrousLayer(EntityRendererFactory.Context ctx, M model, float shadowRadius, CallbackInfo ci) {
        this.addFeature(new FerrousFeatureRenderer<>((LivingEntityRenderer<T, M>) (Object) this));
    }
}
