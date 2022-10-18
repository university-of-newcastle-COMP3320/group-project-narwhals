package SimulationEngine.DisplayEngine;

import SimulationEngine.BaseShaders.NormalMapShader;
import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.ProjectEntities.Camera;
import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.BaseShaders.StaticShader;
import SimulationEngine.Skybox.CubeMap;
import Terrain.TerrainShader;
import Water.WaterShader;
import SimulationEngine.Shadows.ShadowMapRenderController;
import Terrain.BaseTerrain;
import Water.WaterFrameBuffers;
import Water.WaterSurface;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderController {

    private static final Vector3f DEFAULT_WATER_COLOR = new Vector3f(0.004f,0.65f, 0.87f);
    private Matrix4f projectionMatrix;
    private StaticShader eShader = new StaticShader();
    private TerrainShader tShader = new TerrainShader();
    private WaterShader wShader = new WaterShader();
    private NormalMapShader nShader = new NormalMapShader();
    private EntityRenderer eRenderer;
    private TerrainRenderer tRenderer;
    private WaterRenderer wRenderer;
    private SkyboxRenderer sRenderer;
    private NormalMapRenderer nRenderer;
    private Map<ModeledEntity, List<ModeledEntity>> entities = new HashMap<>();
    private Map<ModeledEntity, List<ModeledEntity>> normalEntities = new HashMap<>();
    private List<BaseTerrain> terrains = new ArrayList<>();
    private List<WaterSurface> waters = new ArrayList<>();

    private ShadowMapRenderController shadowMapRenderer;
    private CubeMap enviroMap;
    private static final String[] SKYBOX = {"SkyboxTextures/right", "SkyboxTextures/left" , "SkyboxTextures/top", "SkyboxTextures/bottom", "SkyboxTextures/back", "SkyboxTextures/front"};

    public RenderController(ModelLoader loader, ViewFrustrum camera, WaterFrameBuffers fbos) {
        glClearColor(DEFAULT_WATER_COLOR.x,DEFAULT_WATER_COLOR.y,DEFAULT_WATER_COLOR.z,1);
        projectionMatrix = camera.getProjectionMatrix();

        enviroMap = new CubeMap(SKYBOX, loader);
        eRenderer = new EntityRenderer(eShader, projectionMatrix);
        tRenderer = new TerrainRenderer(tShader,projectionMatrix);
        wRenderer = new WaterRenderer(wShader, projectionMatrix, fbos);
        sRenderer = new SkyboxRenderer(enviroMap, projectionMatrix);
        nRenderer = new NormalMapRenderer(nShader, projectionMatrix);
        this.shadowMapRenderer = new ShadowMapRenderController(camera);
    }

    public void renderScene(List<ModeledEntity> entityBatch, List<BaseTerrain> terrainBatch, List<LightSource> lights, Camera camera, Vector4f clipPlane){
        for(ModeledEntity model: entityBatch){
            if(model.getMaterial().hasNormalMapping()){
                processNormalEntities(model);
            }
            else{
                processEntity(model);
            }
        }
        for(BaseTerrain terrain: terrainBatch){
            processTerrain(terrain);
        }
        render(lights, camera, clipPlane);
    }


    public void render(List<LightSource> lights, Camera camera, Vector4f clipPlane){
        GL11.glEnable(GL11.GL_DEPTH_TEST);//Zbuffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);//Clear Color and depth buffers
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
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

        nShader.start();
        nShader.loadWaterColor(DEFAULT_WATER_COLOR.x,DEFAULT_WATER_COLOR.y,DEFAULT_WATER_COLOR.z);
        nShader.loadLights(lights);
        nShader.loadViewMatrix(camera);
        nShader.loadClippingPlane(clipPlane);
        nShader.loadShadowDistance(shadowMapRenderer.getShadowDistance());
        nShader.bindShadowMap();
        nRenderer.render(normalEntities, shadowMapRenderer.getToShadowMapSpaceMatrix());

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

        //For the skybox, necessary
//        sRenderer.render(camera);
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

    public void processNormalEntities(ModeledEntity entity){
        List<ModeledEntity> batch = entities.get(entity);
        if(batch != null){
            batch.add(entity);
        }
        else{
            List<ModeledEntity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            normalEntities.put(entity, newBatch);
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
        sRenderer.cleanUp();
        shadowMapRenderer.cleanUp();
    }
}
