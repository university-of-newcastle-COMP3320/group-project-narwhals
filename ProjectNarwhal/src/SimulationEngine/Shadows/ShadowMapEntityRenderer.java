package SimulationEngine.Shadows;

import SimulationEngine.Models.Model;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class ShadowMapEntityRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;

	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	protected void render(List<ModeledEntity> entities) {
		for (ModeledEntity entity : entities) {
			Model rawModel = entity.getModel();
			bindModel(rawModel);
			prepareInstance(entity);
			GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	private void bindModel(Model rawModel) {
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}

	private void prepareInstance(ModeledEntity entity) {
		Matrix4f modelMatrix = ProjectMaths.createTransformationMatrix(entity.getPosition(),
				entity.getRX(), entity.getRY(), entity.getRZ(), entity.getScale());
		Matrix4f mvpMatrix = new Matrix4f();
		projectionViewMatrix.mul(modelMatrix, mvpMatrix);
		shader.loadMvpMatrix(mvpMatrix);
	}

}
