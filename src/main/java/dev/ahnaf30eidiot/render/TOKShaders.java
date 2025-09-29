package dev.ahnaf30eidiot.render;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class TOKShaders {
    public static ShaderProgram FERROUS_SHADER;

    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of(IdiotsSaccharineTotems.MOD_ID, "saccharine_totems_shaders");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        try {
                            ShaderNamespaceContext.set(IdiotsSaccharineTotems.MOD_ID); // Allows for ShaderProgramMixin to get the MOD_ID
                            FERROUS_SHADER = new ShaderProgram(
                                    manager,
                                    "ferrous",
                                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to load ferrous shader", e);
                        }
                    }
                });
    }
}
