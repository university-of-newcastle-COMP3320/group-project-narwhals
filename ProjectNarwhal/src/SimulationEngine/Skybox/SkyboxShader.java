package SimulationEngine.Skybox;


import SimulationEngine.BaseShaders.ShaderProgram;
import org.joml.Matrix4f;

	public class SkyboxShader extends ShaderProgram{

		private static final String VERTEX_FILE = "src/SimulationEngine/Skybox/skyboxVertexShader.glsl";
		private static final String FRAGMENT_FILE = "src/SimulationEngine/Skybox/skyboxFragmentShader.glsl";

		private int location_projectionViewMatrix;
		private int location_cubeMap;

		public SkyboxShader() {
			super(VERTEX_FILE, FRAGMENT_FILE);
		}

		public void loadProjectionViewMatrix(Matrix4f matrix){
			super.loadMatrix(location_projectionViewMatrix, matrix);
		}

		public void connectTextureUnits(){
			super.loadInt(location_cubeMap, 0);
		}


		@Override
		protected void getAllUniformLocations() {
			location_projectionViewMatrix = super.getUniformLocation("projectionViewMatrix");
			location_cubeMap = super.getUniformLocation("cubeMap");
		}

		@Override
		protected void bindAttributes() {
			super.bindAttribute(0, "position");
		}

	}
