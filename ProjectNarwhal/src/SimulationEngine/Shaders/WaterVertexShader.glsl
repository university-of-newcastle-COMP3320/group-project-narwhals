 #version 400 core

  in vec3 position;
  in vec2 textureCoords;
  in vec3 normal;

  out vec2 TextureCoords;
  out vec3 surfaceNormal;
  out vec3 toLightVector[10];
  out vec3 toCameraVector;
  out float visibility;
  out float NumberOfLights;
  out vec4 clipSpace;
  out vec3 reflectionVector;

  uniform float numberOfLights;
  uniform mat4 transformationMatrix;
  uniform mat4 projectionMatrix;
  uniform mat4 viewMatrix;
  uniform vec3 lightPosition[10];
  uniform float waveTime;
  uniform vec3 cameraLocation;

  const float density = 0.005;
  const float gradient = 1.5;
  const float PI = 3.1415926535897932384626433832795;
  const float waveLength = 8.0;
  const float waveAmplitude = 2;

  float generateOffset(float x, float z)
  {
      float radiansX = (x / waveLength + waveTime) * 2.0 * PI;
      float radiansZ = (z / waveLength + waveTime) * 2.0 * PI;
      return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
  }

  vec3 applyDistortion(vec3 vertex)
  {
  	float xDistortion = generateOffset(vertex.x, vertex.z);
  	float yDistortion = generateOffset(vertex.x, vertex.z);
  	float zDistortion = generateOffset(vertex.x, vertex.z);
  	return vertex + vec3(xDistortion, yDistortion, zDistortion);
  }

  void main()
  {
      vec3 currentVertex = position;
      currentVertex = applyDistortion(currentVertex);

      vec4 worldPosition = transformationMatrix * vec4(currentVertex,1.0);
      vec4 positionRelativeToCam = viewMatrix * worldPosition;
      clipSpace = projectionMatrix * positionRelativeToCam;
      gl_Position = clipSpace;
      TextureCoords = textureCoords * 20.0;
      reflectionVector = cameraLocation - worldPosition.xyz;


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