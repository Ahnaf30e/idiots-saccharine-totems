#version 150
#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2; // lightmap
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;
uniform vec3 Light0_Direction;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(Position, FogShape);

    // 💡 vanilla-style light
    vec4 lightColor = minecraft_mix_light(Light0_Direction, Light1_Direction, Normal, Color);
    vec4 lightmapSample = texelFetch(Sampler2, UV2 / 16, 0);

    vertexColor = lightColor * lightmapSample;
    texCoord0 = UV0;
}
