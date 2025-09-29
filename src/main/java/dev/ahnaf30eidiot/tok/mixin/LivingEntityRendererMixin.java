package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.ahnaf30eidiot.render.layer.FerrousFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addFerrousLayer(EntityRendererFactory.Context ctx, M model, float shadowRadius, CallbackInfo ci) {
        LivingEntityRenderer<T, M> self = (LivingEntityRenderer<T, M>)(Object)this;
        try {
            java.lang.reflect.Method addFeatureMethod = LivingEntityRenderer.class.getDeclaredMethod("addFeature", Class.forName("net.minecraft.client.render.entity.FeatureRenderer"));
            addFeatureMethod.setAccessible(true);
            addFeatureMethod.invoke(self, new FerrousFeatureRenderer<>(self));
        } catch (Exception e) {
            throw new RuntimeException("Failed to add FerrousFeatureRenderer via reflection", e);
        }
    }
}
