package SimulationEngine.PostProcessing;

import SimulationEngine.PostProcessing.BloomEffect.BrightFilter;
import SimulationEngine.PostProcessing.BloomEffect.CombineFilter;
import SimulationEngine.PostProcessing.ContrastChanger.ContrastChanger;
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

	private static HorizontalBlur horizontalBlur2;
	private static VerticalBlur verticalBlur2;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;

	public static void init(ModelLoader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		horizontalBlur = new HorizontalBlur(width/4, height/4);
		verticalBlur = new VerticalBlur(width/4, height/4);
		horizontalBlur2 = new HorizontalBlur(width/2, height/2);
		verticalBlur2 = new VerticalBlur(width/2, height/2);
		brightFilter = new BrightFilter(width/2, height/2);
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
		brightFilter.render(colourTexture);
		horizontalBlur2.render(brightFilter.getOutputTexture());
		verticalBlur2.render(horizontalBlur2.getOutputTexture());
		horizontalBlur.render(verticalBlur2.getOutputTexture());
		verticalBlur.render(horizontalBlur.getOutputTexture());
		combineFilter.render(colourTexture, verticalBlur.getOutputTexture());
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		horizontalBlur.cleanUp();
		verticalBlur.cleanUp();
		brightFilter.cleanUp();
		combineFilter.cleanUp();
		horizontalBlur2.cleanUp();
		verticalBlur2.cleanUp();
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
