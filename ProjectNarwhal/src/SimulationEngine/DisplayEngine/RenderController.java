package SimulationEngine.DisplayEngine;

import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Shaders.StaticShader;
import SimulationEngine.Shaders.TerrainShader;
import Terrain.BaseTerrain;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

public class RenderController {

    private static final float FOV_ANGLE = 90.0f;
    private static final float NEAR_PLANE = 0.01f;
    private static final float FAR_PLANE = 100000f;
    private Matrix4f projectionMatrix;
    private StaticShader eShader = new StaticShader();
    private EntityRenderer eRenderer;
    private TerrainRenderer tRenderer;
    private TerrainShader tShader = new TerrainShader();
    private Map<ModeledEntity, List<ModeledEntity>> entities = new HashMap<>();
    private List<BaseTerrain> terrains = new ArrayList<>();

    public RenderController() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        eRenderer = new EntityRenderer(eShader, projectionMatrix);
        tRenderer = new TerrainRenderer(tShader,projectionMatrix);
    }

    public void render(LightSource light, ViewFrustrum camera){
        GL11.glEnable(GL_DEPTH_TEST); //this is our z buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT); // clear the framebuffer and the depthbuffer
        eShader.start(); //start the shaders
        eShader.loadLight(light);
        eShader.loadViewMatrix(camera);

        eRenderer.render(entities);

        eShader.stop(); //stop the shaders

        tShader.start();
        tShader.loadLight(light);
        tShader.loadViewMatrix(camera);
        tRenderer.render(terrains);

        tShader.stop();

        terrains.clear();
        entities.clear();
    }

    public void processEntity(ModeledEntity entity){
        List<ModeledEntity> batch = entities.get(entity);
        if(batch != null){
            batch.add(entity);
        }
        else{
            List<ModeledEntity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entity, newBatch);
        }
    }

    public void processTerrain(BaseTerrain terrain){
        terrains.add(terrain);
    }

    public void cleanUp(){
        eShader.cleanUp();
        tShader.cleanUp();
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