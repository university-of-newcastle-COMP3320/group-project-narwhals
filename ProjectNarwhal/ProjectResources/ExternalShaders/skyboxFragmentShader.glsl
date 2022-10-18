#version 400 core

in vec3 textureCoords;
out vec4 outColor;

uniform samplerCube cubeMap;

void main(void){
 outColor = texture(cubeMap, textureCoords) ;
}