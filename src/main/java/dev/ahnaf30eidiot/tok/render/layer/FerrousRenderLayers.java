package dev.ahnaf30eidiot.tok.render.layer;

import dev.ahnaf30eidiot.tok.render.TOKShaders;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderPhase.ShaderProgram;
import net.minecraft.util.Identifier;

public class FerrousRenderLayers {

    public static ShaderProgram FERROUS_PROGRAM = new RenderPhase.ShaderProgram(() -> TOKShaders.FERROUS_SHADER);

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
                .writeMaskState(RenderPhase.ALL_MASK) // important: ensures color + depth write
                .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                .build(true));
    }

    public static RenderLayer irisFerrous(Identifier texture, float x, float y) {
        // new RenderPhase.Texture(texture, false, false)
        return RenderLayer.of(
                "ferrous_layer",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderLayer.ENERGY_SWIRL_PROGRAM)
                        .texture(new RenderPhase.Texture(texture, false, false))
                .texturing(RenderPhase.ENTITY_GLINT_TEXTURING)
                .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderPhase.DISABLE_CULLING)
                .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                .writeMaskState(RenderPhase.ALL_MASK) // important: ensures color + depth write
                .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                .build(true));
    }
}
