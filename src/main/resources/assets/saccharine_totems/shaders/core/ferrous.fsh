#version 150

uniform sampler2D Sampler0;
uniform float Time;
uniform vec3 EntityPos;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 lightCoord;

out vec4 fragColor;

void main() {
    vec4 base = texture(Sampler0, texCoord0) * vertexColor;

    float wave = sin(Time * 2.0 + texCoord0.y * 10.0 + EntityPos.x) * 0.5 + 0.5;
    vec3 tinted = mix(base.rgb, vec3(0.7, 0.8, 1.0), wave);

    fragColor = vec4(tinted, base.a);
}
