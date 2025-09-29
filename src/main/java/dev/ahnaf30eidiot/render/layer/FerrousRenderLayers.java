package dev.ahnaf30eidiot.render.layer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import dev.ahnaf30eidiot.render.TOKShaders;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class FerrousRenderLayers {
    public static RenderLayer ferrous(Identifier texture) {
        return RenderLayer.of(
            "ferrous_layer",
            VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS,
            256,
            RenderLayer.MultiPhaseParameters.builder()
                .program(new RenderPhase.ShaderProgram(() -> TOKShaders.FERROUS_SHADER))
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                .build(false)
        );
    }
}

