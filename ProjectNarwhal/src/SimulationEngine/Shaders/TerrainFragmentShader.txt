#version 400 core

in vec2 TextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[10];
in vec3 toCameraVector;
in float visibility;
in float NumberOfLights;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor[10];
uniform vec3 attenuation[10];
uniform float shineDamper;
uniform float reflectance;
uniform vec3 waterColor;

out vec4 outColor;

void main()
  {
   vec4 blendMapColour = texture(blendMap, TextureCoords);
   float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
   vec2 tiledCoords = TextureCoords * 40.0;

   vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords) * backTextureAmount;
   vec4 rTextureColour = texture(rTexture, tiledCoords) * blendMapColour.r;
   vec4 gTextureColour = texture(gTexture, tiledCoords) * blendMapColour.g;
   vec4 bTextureColour = texture(bTexture, tiledCoords) * blendMapColour.b;

   vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

   vec3 unitNormal = normalize(surfaceNormal);
   vec3 totalDiffuse = vec3(0.0);
   vec3 totalSpecular = vec3(0.0);
   vec3 unitVectorToCamera = normalize(toCameraVector);

      for(int i = 0 ; i < NumberOfLights; i++){
              float distance = length(toLightVector[i]);
              float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
              if(attFactor == 0){attFactor = 1;}
              vec3 unitLightVector = normalize(toLightVector[i]);
              float nDot1 = dot(unitNormal, unitLightVector);
              float brightness = max(nDot1, 0.0);
              vec3 lightDirection = -unitLightVector;
              vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
              totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;
              float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
              specularFactor = max(specularFactor,0.0);
              float dampedFactor = pow(specularFactor,shineDamper);
              totalSpecular = totalSpecular + (dampedFactor * reflectance * lightColor[i]) / attFactor;
      }
   totalDiffuse = max(totalDiffuse, 0.1);

   outColor = vec4(totalDiffuse,1.0) * totalColour + vec4(totalSpecular, 1.0);
   outColor = mix(vec4(waterColor, 1.0), outColor, visibility);
  }
