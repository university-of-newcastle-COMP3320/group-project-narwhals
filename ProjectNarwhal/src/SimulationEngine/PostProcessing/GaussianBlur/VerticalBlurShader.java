package SimulationEngine.PostProcessing.GaussianBlur;

import SimulationEngine.BaseShaders.ShaderProgram;
public class VerticalBlurShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/SimulationEngine/PostProcessing/GaussianBlur/verticalBlurVertex.glsl";
	private static final String FRAGMENT_FILE = "src/SimulationEngine/PostProcessing/GaussianBlur/blurFragment.glsl";
	
	private int location_targetHeight;
	
	protected VerticalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	protected void loadTargetHeight(float height){
		super.loadFloat(location_targetHeight, height);
	}

	@Override
	protected void getAllUniformLocations() {	
		location_targetHeight = super.getUniformLocation("targetHeight");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
