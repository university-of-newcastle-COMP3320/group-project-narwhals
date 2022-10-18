package SimulationEngine.PostProcessing.BloomEffect;

import SimulationEngine.BaseShaders.ShaderProgram;

public class BrightFilterShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/SimulationEngine/PostProcessing/BloomEffect/simpleVertex.glsl";
	private static final String FRAGMENT_FILE = "src/SimulationEngine/PostProcessing/BloomEffect/brightFilterFragment.glsl";
	
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
