package SimulationEngine;

import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.Shaders.StaticShader;
import SimulationEngine.Textures.ModelTexture;
import SimulationEngine.Tools.ProjectMaths;
import SimulationEngine.Models.Model;

import SimulationEngine.Models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import static org.lwjgl.glfw.GLFW.*;


import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;


public class Renderer {

    //Can be later made dynamic or changed if performance is an issue
    private static final float FOV_ANGLE = 90.0f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private Matrix4f projectionMatrix;

    //Constructor, creates the view frustrum matrix and loads it
    public Renderer(StaticShader shader){
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    //handles the rendering of entities using the relevant shaders in their specified origin location and cleans up after they have been rendered
    public void render(ModeledEntity entity, StaticShader shader) {
        TexturedModel tModel = entity.getModel();
        Model model = tModel.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        Matrix4f transformationMatrix = ProjectMaths.createTransformationMatrix(entity.getPosition(), entity.getRX(), entity.getRY(), entity.getRZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadShineVariables(tModel.getMaterial().getShineDamper(), tModel.getMaterial().getReflectance());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tModel.getTexture().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    //creates a projection matrix representing a frustrum using static variables
    private void createProjectionMatrix(){
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        float aspectRatio = (float) vidmode.width()  / (float) vidmode.height() ;
        float y_scale = (float) ((1.0f / Math.tan(Math.toRadians(FOV_ANGLE / 2.0f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }

}
