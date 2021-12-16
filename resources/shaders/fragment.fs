#version 440

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = texture(texture_sampler, outTexCoord);

    float mipmapLevel = textureQueryLod(texture_sampler, outTexCoord).x;

    fragColor.a *= 1 + max(0, mipmapLevel) * 0.0;

    fragColor.a = (fragColor.a - 0.2) / max(fwidth(fragColor.a), 0.0001) + 0.5;
}