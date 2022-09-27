package SimulationEngine.DisplayEngine;

import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.BaseShaders.StaticShader;
import Terrain.TerrainShader;
import Water.WaterShader;
import SimulationEngine.Shadows.ShadowMapRenderController;
import Terrain.BaseTerrain;
import Water.WaterFrameBuffers;
import Water.WaterSurface;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.opengl.GL11.*;

public class RenderController {

    public static final float FOV_ANGLE = 70.0f;
    public static final float NEAR_PLANE = 0.01f;
    public static final float FAR_PLANE = 1000f;
    private static final Vector3f DEFAULT_WATER_COLOR = new Vector3f(0.004f,0.65f, 0.87f);
    private Matrix4f projectionMatrix;
    private StaticShader eShader = new StaticShader();
    private TerrainShader tShader = new TerrainShader();
    private WaterShader wShader = new WaterShader();
    private EntityRenderer eRenderer;
    private TerrainRenderer tRenderer;
    private WaterRenderer wRenderer;
    private Map<ModeledEntity, List<ModeledEntity>> entities = new HashMap<>();
    private List<BaseTerrain> terrains = new ArrayList<>();
    private List<WaterSurface> waters = new ArrayList<>();

    private ShadowMapRenderController shadowMapRenderer;

    public RenderController(ModelLoader loader, ViewFrustrum camera, WaterFrameBuffers fbos) {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        eRenderer = new EntityRenderer(eShader, projectionMatrix);
        tRenderer = new TerrainRenderer(tShader,projectionMatrix);
        wRenderer = new WaterRenderer(wShader, projectionMatrix, fbos);
        this.shadowMapRenderer = new ShadowMapRenderController(camera);
    }

    public void renderScene(List<ModeledEntity> entityBatch, List<BaseTerrain> terrainBatch, List<LightSource> lights, ViewFrustrum camera, Vector4f clipPlane){
        for(ModeledEntity model: entityBatch){
            processEntity(model);
        }
        for(BaseTerrain terrain: terrainBatch){
            processTerrain(terrain);
        }
        render(lights, camera, clipPlane);
    }


    public void render(List<LightSource> lights, ViewFrustrum camera, Vector4f clipPlane){
        glClearColor(DEFAULT_WATER_COLOR.x,DEFAULT_WATER_COLOR.y,DEFAULT_WATER_COLOR.z,1);
        GL11.glEnable(GL_DEPTH_TEST); //this is our z buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT); // clear the framebuffer and the depthbuffer
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL_TEXTURE_2D, getShadowMapTexture());

        eShader.start(); //start the shaders
        eShader.loadWaterColor(DEFAULT_WATER_COLOR.x,DEFAULT_WATER_COLOR.y,DEFAULT_WATER_COLOR.z);
        eShader.loadLights(lights);
        eShader.loadViewMatrix(camera);
        eShader.loadClippingPlane(clipPlane);
        eShader.loadShadowDistance(shadowMapRenderer.getShadowDistance());
        eShader.bindShadowMap();
        eRenderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());

        eShader.stop(); //stop the shaders

        tShader.start();
        tShader.loadWaterColor(DEFAULT_WATER_COLOR.x,DEFAULT_WATER_COLOR.y,DEFAULT_WATER_COLOR.z);
        tShader.loadLights(lights);
        tShader.loadViewMatrix(camera);
        tShader.loadClippingPlane(clipPlane);
        tShader.loadShadowDistance(shadowMapRenderer.getShadowDistance());
        tRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());

        tShader.stop();

        GL11.glDisable(GL11.GL_CULL_FACE);
        wShader.start();
        wShader.loadWaterColor(DEFAULT_WATER_COLOR.x,DEFAULT_WATER_COLOR.y,DEFAULT_WATER_COLOR.z);
        wShader.loadLights(lights);
        wShader.loadViewMatrix(camera);
        wRenderer.render(waters);

        wShader.stop();

        GL11.glEnable(GL11.GL_CULL_FACE);


        terrains.clear();
        entities.clear();
        waters.clear();
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

    public void processWater(WaterSurface water){ waters.add(water);}

    public void renderShadowMap(List<ModeledEntity> entities, LightSource sun) {
        for (ModeledEntity entity : entities){
            processEntity(entity);
        }
        shadowMapRenderer.render(entities, sun);
    }

    public int getShadowMapTexture(){
        return shadowMapRenderer.getShadowMap();
    }

    public void cleanUp(){
        eShader.cleanUp();
        tShader.cleanUp();
        wShader.cleanUp();
        shadowMapRenderer.cleanUp();
    }

    //creates a projection matrix representing a frustrum using static variables
    private void createProjectionMatrix(){
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) vidmode.width()  / (float) vidmode.height() ;
        float y_scale = (float) ((1.0f / Math.tan(Math.toRadians(FOV_ANGLE / 2.0f))));
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
