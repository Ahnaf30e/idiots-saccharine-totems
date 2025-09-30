#version 150
#define PI 3.14159265359

in vec2 vTexCoord;
in vec4 vColor;

uniform sampler2D Sampler0;
uniform float Time;
uniform vec3 EntityPos;
uniform vec2 SSize;

out vec4 fragColor;

void main() {
    vec2 texel = 1.0 / SSize;
    vec2 newCoord = (floor(vTexCoord * SSize) + 0.5) * texel;
    // 3x3 sample fetch
    vec4 c = texture(Sampler0, newCoord);
    vec4 u = texture(Sampler0, newCoord + vec2(0.0, texel.y));
    vec4 d = texture(Sampler0, newCoord - vec2(0.0, texel.y));
    vec4 l = texture(Sampler0, newCoord - vec2(texel.x, 0.0));
    vec4 r = texture(Sampler0, newCoord + vec2(texel.x, 0.0));

    // sharpen kernel
    vec4 sharpened = clamp((c * 5.0) - (u + d + l + r), 0.0, 1.0);

    float val = max(max(sharpened.r, sharpened.g), sharpened.b);
    float rs = round(cos(val * PI * 6.0 + Time * 0.1 + EntityPos.x + EntityPos.y + vTexCoord.x * 12.0 + vTexCoord.y * 12.0) * val * 4.0) / 4.0;
    // float rs = val;

    float s = (0.8 > rs && rs > 0.6) ? 0.99 : (0.60 + 0.30 * rs);

    fragColor = vec4(vec3(s), c.a);
}
