package SimulationEngine.Skybox;


import SimulationEngine.BaseShaders.ShaderProgram;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Matrix4f;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/SimulationEngine/Skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/SimulationEngine/Skybox/skyboxFragmentShader.glsl";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(ViewFrustrum camera){
		Matrix4f matrix = ProjectMaths.createViewMatrix(camera);
		matrix.m30(0);
		matrix.m31(0);
		matrix.m32(0);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
