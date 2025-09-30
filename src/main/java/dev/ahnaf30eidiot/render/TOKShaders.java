package dev.ahnaf30eidiot.render;

import java.lang.reflect.Field;

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

    public static void debugShader(ShaderProgram shader) {
        if (shader == null) {
            System.out.println("FERROUS_SHADER is null");
            return;
        }
        System.out.println("==== Shader Debug Info ====");
        for (Field f : ShaderProgram.class.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                System.out.println(f.getName() + " = " + f.get(shader));
            } catch (Exception e) {
                System.out.println(f.getName() + " = <error: " + e.getMessage() + ">");
            }
        }
    }

    public static void init() {

        IdiotsSaccharineTotems.LOGGER.info("Registering Mod shaders for " + IdiotsSaccharineTotems.MOD_ID);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() { // FIXME: SUS
                        return Identifier.of(IdiotsSaccharineTotems.MOD_ID, "saccharine_totems_shaders");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        try {
                            ShaderNamespaceContext.set(IdiotsSaccharineTotems.MOD_ID); // Allows for ShaderProgramMixin
                                                                                       // to get the MOD_ID
                            String shaderName = "ferrous";
                            IdiotsSaccharineTotems.LOGGER.info("Loading shader: " + shaderName + ".fsh");
                            Identifier fragShaderId = Identifier.of(IdiotsSaccharineTotems.MOD_ID,
                                    "shaders/core/" + shaderName + ".fsh");
                            var resourceOpt = manager.getResource(fragShaderId);
                            if (resourceOpt.isPresent()) {
                                var resource = resourceOpt.get();
                                try (var reader = resource.getReader()) {
                                    String source = reader.lines().reduce("", (a, b) -> a + "\n" + b);
                                    IdiotsSaccharineTotems.LOGGER.info("=== Raw fragment shader source ===\n" + source);
                                }
                            }

                            FERROUS_SHADER = new ShaderProgram(
                                    manager,
                                    shaderName,
                                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
                            debugShader(FERROUS_SHADER);

                            if (TOKShaders.FERROUS_SHADER == null) {
                                IdiotsSaccharineTotems.LOGGER.error("FERROUS_SHADER is NULL");
                            } else {
                                IdiotsSaccharineTotems.LOGGER
                                        .info("FERROUS_SHADER name=" + TOKShaders.FERROUS_SHADER.getName() +
                                                " glRef=" + TOKShaders.FERROUS_SHADER.getGlRef());
                            }

                        } catch (Exception e) {
                            throw new RuntimeException("Failed to load ferrous shader", e);
                        }
                    }
                });
    }
}
