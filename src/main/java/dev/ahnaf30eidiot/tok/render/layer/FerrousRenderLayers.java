package dev.ahnaf30eidiot.tok.render.layer;

import org.joml.Matrix4f;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.ahnaf30eidiot.tok.render.TOKShaders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderPhase.ShaderProgram;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class FerrousRenderLayers {

    public static ShaderProgram FERROUS_PROGRAM = new RenderPhase.ShaderProgram(() -> TOKShaders.FERROUS_SHADER);

    @Environment(EnvType.CLIENT)
    public static final class FerrousTexturing extends RenderPhase.Texturing {
        public FerrousTexturing(float x, float y) {
            super("offset_texturing", () -> {
                // RenderSystem.setTextureMatrix(new Matrix4f().translation(x, y, 0.0F));
                long l = (long) (Util.getMeasuringTimeMs()
                        * MinecraftClient.getInstance().options.getGlintSpeed().getValue() * 8.0);
                float f = (float) (l % 110000L) / 110000.0F;
                float g = (float) (l % 30000L) / 30000.0F;
                Matrix4f matrix4f = new Matrix4f().translation(-f, g, 0.0F);
                RenderSystem.setTextureMatrix(matrix4f);
            }, () -> RenderSystem.resetTextureMatrix());
        }
    }

    public static RenderLayer animatedFerrous(Identifier texture) {
        // new RenderPhase.Texture(texture, false, false)
        return RenderLayer.of(
                "ferrous_layer",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(new RenderPhase.ShaderProgram(() -> TOKShaders.FERROUS_SHADER))
                        .texture(new RenderPhase.Texture(texture, false, false))
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .cull(RenderPhase.DISABLE_CULLING)
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                        .writeMaskState(RenderPhase.COLOR_MASK) // important: ensures color + depth write
                        .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                        .build(false));
    }

    public static RenderLayer irisFerrous(Identifier texture, float x, float y) {
        // new RenderPhase.Texture(texture, false, false)
        return RenderLayer.of(
                "ferrous_iris_layer",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.ENTITY_DECAL_PROGRAM)
                        .texture(new RenderPhase.Texture(texture, false, false))
                        .texturing(new FerrousTexturing(x, y))
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .cull(RenderPhase.DISABLE_CULLING)
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
                        .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                        .writeMaskState(RenderPhase.COLOR_MASK)
                        .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                        .build(false));
    }
}
