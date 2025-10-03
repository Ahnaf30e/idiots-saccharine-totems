package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.ahnaf30eidiot.tok.render.ShaderNamespaceContext;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.util.Identifier;

@Mixin(ShaderProgram.class)
public class ShaderProgramMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
    private Identifier redirectShaderIdentifier(String path) {
        String ns = ShaderNamespaceContext.getAndClear();
        if (ns != null && !ns.isEmpty()) {
            return Identifier.of(ns, path); // custom namespace
        }
        return Identifier.ofVanilla(path); // fallback to vanilla
    }
}