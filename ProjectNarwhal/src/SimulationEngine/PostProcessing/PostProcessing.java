package SimulationEngine.PostProcessing;

import SimulationEngine.DisplayEngine.Display;
import SimulationEngine.PostProcessing.GaussianBlur.HorizontalBlur;
import SimulationEngine.PostProcessing.GaussianBlur.VerticalBlur;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import SimulationEngine.Models.Model;
import SimulationEngine.Loaders.ModelLoader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static Model quad;

	private static final int width = 1280;
	private static final int height = 720;

	private static ContrastChanger contrastChanger;
	private static HorizontalBlur horizontalBlur;
	private static VerticalBlur verticalBlur;

	public static void init(ModelLoader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		horizontalBlur = new HorizontalBlur(width, height);
		verticalBlur = new VerticalBlur(width, height);
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
		//horizontalBlur.render(colourTexture);
		//verticalBlur.render(horizontalBlur.getOutputTexture());
		contrastChanger.render(colourTexture);
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		horizontalBlur.cleanUp();
		verticalBlur.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
