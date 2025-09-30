#version 150
#define PI 3.14159265359

in vec2 vUv;
in vec4 vColor;

uniform sampler2D Sampler0;
uniform float Time;
uniform vec3 EntityPos;

out vec4 fragColor;

void main() {
    vec4 base = texture(Sampler0, vUv);

    float val = max(max(base.r, base.g), base.b);
    float rs = round(cos(val*PI*6.0 + Time + EntityPos.x + EntityPos.y)*val*4.0)/4.0;
    float s =  0.8 > rs && rs > 0.6 ? 0.99 : 0.60 + 0.30 * rs;

    fragColor = vec4(vec3(s).rgb, s);
}
