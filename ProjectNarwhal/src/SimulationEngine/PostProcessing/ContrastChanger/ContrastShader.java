package SimulationEngine.PostProcessing.ContrastChanger;

import SimulationEngine.BaseShaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "ProjectResources/ExternalShaders/contrastVertex.glsl";
	private static final String FRAGMENT_FILE = "ProjectResources/ExternalShaders/contrastFragment.glsl";
	
	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
