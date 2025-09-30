#version 150

// Match POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL
in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;   // overlay
in ivec2 UV2;   // light
in vec3 Normal;

// Standard matrices Minecraft provides for entity draws
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 vUv;
out vec4 vColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vUv = UV0;
    vColor = Color; // passed through (even if unused)
}
