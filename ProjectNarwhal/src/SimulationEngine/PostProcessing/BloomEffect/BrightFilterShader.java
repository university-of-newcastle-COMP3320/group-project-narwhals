package SimulationEngine.PostProcessing.BloomEffect;

import SimulationEngine.BaseShaders.ShaderProgram;

public class BrightFilterShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "ProjectResources/ExternalShaders/simpleVertex.glsl";
	private static final String FRAGMENT_FILE = "ProjectResources/ExternalShaders/brightFilterFragment.glsl";
	
	
	public BrightFilterShader() {
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
