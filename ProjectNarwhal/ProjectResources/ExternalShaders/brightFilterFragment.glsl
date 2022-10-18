#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;

void main(void){
    vec4 colour = texture(colourTexture, textureCoords);
    float brightness = (colour.r * 0.5) + (colour.g * 0.5);
    //out_Colour = colour * brightness * brightness;
    if (brightness>0.4) {
        out_Colour = colour;
    } else {
        out_Colour = vec4(0.0, 0.0, 0.0, 1.0);
    }

}