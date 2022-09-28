package SimulationEngine.Skybox;


import SimulationEngine.BaseShaders.ShaderProgram;
import org.joml.Matrix4f;

	public class SkyboxShader extends ShaderProgram{

		private static final String VERTEX_FILE = "src/SimulationEngine/Skybox/skyboxVertexShader.glsl";
		private static final String FRAGMENT_FILE = "src/SimulationEngine/Skybox/skyboxFragmentShader.glsl";

		private int location_projectionViewMatrix;

		public SkyboxShader() {
			super(VERTEX_FILE, FRAGMENT_FILE);
		}

		public void loadProjectionViewMatrix(Matrix4f matrix){
			super.loadMatrix(location_projectionViewMatrix, matrix);
		}

		@Override
		protected void getAllUniformLocations() {
			location_projectionViewMatrix = super.getUniformLocation("projectionViewMatrix");
		}

		@Override
		protected void bindAttributes() {
			super.bindAttribute(0, "position");
		}

	}
