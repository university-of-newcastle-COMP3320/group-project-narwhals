#version 400 core

in vec2 TextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[10];
in vec3 toCameraVector;
in float visibility;
in float NumberOfLights;
in vec4 shadowCoords;
in vec3 reflectedVector;
in vec3 refractedVector;

uniform sampler2D textureSampler;
uniform vec3 lightColor[10];
uniform vec3 attenuation[10];
uniform float shineDamper;
uniform float reflectance;
uniform vec3 waterColor;
uniform sampler2D shadowMap;
uniform samplerCube enviroMap;
uniform float reflectivity;

out vec4 outColor;

const int pcfCount = 3;
const float totalTexels = (pcfCount *2.0 + 1.0) * (pcfCount *2.0 + 1.0);

void main(){

       float mapSize = 4096.0;
       float texelSize = 1.0/mapSize;
       float total = 0.0;

       for(int x = -pcfCount; x <= pcfCount; x ++){
        for(int y = -pcfCount; y <= pcfCount; y ++){
            float objectNearestLight = texture(shadowMap, shadowCoords.xy + vec2(x,y) * texelSize).r;
               if (shadowCoords.z > objectNearestLight + 0.002){
                total += 1.0;
               }
            }
        }
       total /= totalTexels;
       float lightFactor = 1.0 - (total * shadowCoords.w);

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    for(int i = 0 ; i < NumberOfLights; i++){
       float distance = length(toLightVector[i]);
       float attFactor = (attenuation[i].x) + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
       if(attFactor == 0){attFactor = 1;}
       vec3 unitLightVector = normalize(toLightVector[i]);
       float nDot1 = dot(unitNormal, unitLightVector);
       float brightness = max(nDot1, 0.0);
       vec3 lightDirection = -unitLightVector;
       vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
       float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
       specularFactor = max(specularFactor,0.0);
       float dampedFactor = pow(specularFactor,shineDamper);
        if(i == 0){
            totalDiffuse = totalDiffuse + ((brightness * lightColor[i]) / attFactor) * lightFactor;
            totalSpecular = totalSpecular + ((dampedFactor * reflectance * lightColor[i]) / attFactor) * lightFactor;
        }
        else{
            totalDiffuse = totalDiffuse + ((brightness * lightColor[i]) / attFactor);
            totalSpecular = totalSpecular + ((dampedFactor * reflectance * lightColor[i]) / attFactor);
        }
      }
   totalDiffuse = max(totalDiffuse, 0.1);
   totalSpecular = totalSpecular;

    vec4 reflectedColor = texture(enviroMap, reflectedVector);
//    vec4 refractedColor = texture(enviroMap, refractedVector);
//    vec4 enviroColor = mix(reflectedColor, refractedColor, 0.5);

    outColor = vec4(totalDiffuse,1.0) * texture(textureSampler, TextureCoords) + vec4(totalSpecular, 1.0);
    outColor = mix(outColor, reflectedColor, reflectivity);
    outColor = mix(vec4(waterColor, 1.0), outColor, visibility);
    }
