#version 150
#define PI 3.14159265359
#moj_import <fog.glsl>

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float Time;
uniform vec3 EntityPos;
uniform vec2 SSize;

out vec4 fragColor;

void main() {
    vec2 texel = 1.0 / SSize;
    vec2 newCoord = (floor(texCoord0 * SSize) + 0.5) * texel;
    vec2 snapped = floor(newCoord / texel) * texel;

    vec4 c = texture(Sampler0, newCoord);
    if (c.a < 0.1) discard;
    vec4 u = texture(Sampler0, newCoord + vec2(0.0, texel.y));
    vec4 d = texture(Sampler0, newCoord - vec2(0.0, texel.y));
    vec4 l = texture(Sampler0, newCoord - vec2(texel.x, 0.0));
    vec4 r = texture(Sampler0, newCoord + vec2(texel.x, 0.0));

    // sharpen kernel
    vec4 sharpened = clamp((c * 1.5) - (u + d + l + r)*(0.5/4.0), 0.0, 1.0);

    float val = dot(sharpened.rgb, vec3(0.2126, 0.7152, 0.0722));
    // val = max(max(sharpened.r, sharpened.g), sharpened.b);
    // val = (sharpened.r + sharpened.g + sharpened.b) / 3.0;

    float off = Time * 0.1 + val + snapped.x * 8.0 + snapped.y * 8.0 + EntityPos.x + EntityPos.y;
    float ts = (cos(val * PI * 3.0 + off)+1.0)/2.0 * (val* 0.5 + 0.5) * 0.7 + (val*0.3);
    float rs = round(ts * 8.0) / 8.0;
    // float rs = val;


    float s = (0.8 > ts && ts > 0.7) ? 0.99 : (0.3 + 0.6 * rs);
    vec4 rColor = vec4(vec3(s), c.a) * vertexColor * ColorModulator;

    fragColor = linear_fog(rColor, vertexDistance, FogStart, FogEnd, FogColor);
}
