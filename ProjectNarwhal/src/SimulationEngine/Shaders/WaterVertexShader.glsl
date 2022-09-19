 #version 400 core

  const float PI = 3.1415926535897932384626433832795;

  const float waveLength = 4.0;
  const float waveAmplitude = 0.2;

  in vec3 position;
  in vec2 textureCoords;
  in vec3 normal;

  out vec2 TextureCoords;
  out vec3 surfaceNormal;
  out vec3 toLightVector[10];
  out vec3 toCameraVector;
  out float visibility;
  out float NumberOfLights;

  uniform float numberOfLights;
  uniform mat4 transformationMatrix;
  uniform mat4 projectionMatrix;
  uniform mat4 viewMatrix;
  uniform vec3 lightPosition[10];
  uniform float waveTime;

  const float density = 0.005;
  const float gradient = 1.5;

  float generateOffset(float x, float z)
  {
      float radiansX = (x / waveLength + waveTime) * 2.0 * PI;
      float radiansZ = (z / waveLength + waveTime) * 2.0 * PI;
      return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
  }

  vec3 applyDistortion(vec3 vertex)
  {
  	float xDistortion = generateOffset(vertex.x, vertex.z, 0.2, 0.1);
  	float yDistortion = generateOffset(vertex.x, vertex.z, 0.1, 0.3);
  	float zDistortion = generateOffset(vertex.x, vertex.z, 0.15, 0.2);
  	return vertex + vec3(xDistortion, yDistortion, zDistortion);
  }

  void main()
  {
      //need to use applyDistortion() on each vertex of the water surface
      vec4 worldPosition = transformationMatrix * vec4(position,1.0);
      vec4 positionRelativeToCam = viewMatrix * worldPosition;
      gl_Position = projectionMatrix * positionRelativeToCam;
      TextureCoords = textureCoords;

      surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
      for(int i = 0; i < numberOfLights; i ++){
              toLightVector[i] = lightPosition[i] - worldPosition.xyz;
      }
      toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

      float distance = length(positionRelativeToCam.xyz);
      visibility = exp(-pow((distance*density),gradient));
      visibility = clamp(visibility,0.0,1.0);
      NumberOfLights = numberOfLights;
  }