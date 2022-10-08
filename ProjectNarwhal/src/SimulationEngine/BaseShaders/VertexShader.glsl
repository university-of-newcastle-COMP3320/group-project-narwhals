 #version 400 core

  in vec3 position;
  in vec2 textureCoords;
  in vec3 normal;
  //these should be the tangents
  in vec3 tangent;

  out vec2 TextureCoords;
  out vec3 surfaceNormal;
  out vec3 toLightVector[10];
  out vec3 toCameraVector;
  out float visibility;
  out float NumberOfLights;
  out vec4 shadowCoords;
  out vec3 reflectedVector;
  out vec3 refractedVector;

  uniform float numberOfLights;
  uniform mat4 transformationMatrix;
  uniform mat4 projectionMatrix;
  uniform mat4 viewMatrix;
  uniform vec3 lightPosition[10];
  uniform mat4 toShadowMapSpace;
  uniform float shadowDistance;
  uniform vec4 plane;
  uniform vec3 cameraPosition;

  const float density = 0.003;
  const float gradient = 1.5;
  const float transitionDistance = 10.0;

  void main()
  {
      vec4 worldPosition = transformationMatrix * vec4(position,1.0);
      shadowCoords = toShadowMapSpace * worldPosition;
      vec4 positionRelativeToCam = viewMatrix * worldPosition;
      gl_Position = projectionMatrix * positionRelativeToCam;
      TextureCoords = textureCoords;

      gl_ClipDistance[0] = dot(worldPosition, plane);

      surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
      for(int i = 0; i < numberOfLights; i ++){
              toLightVector[i] = lightPosition[i] - worldPosition.xyz;
      }
      toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
      vec3 viewVector = normalize(worldPosition.xyz - cameraPosition);
      vec3 unitNormal = normalize(normal);
      reflectedVector = reflect(viewVector, unitNormal);
      refractedVector = refract(viewVector, unitNormal, 1.0/1.33);

      float distance = length(positionRelativeToCam.xyz);
      visibility = exp(-pow((distance*density),gradient));
      visibility = clamp(visibility,0.0,1.0);
      NumberOfLights = numberOfLights;

      distance = distance - shadowDistance - transitionDistance;
      distance = distance / transitionDistance;
      shadowCoords.w = clamp(1.0-distance, 0.0,1.0);
  }