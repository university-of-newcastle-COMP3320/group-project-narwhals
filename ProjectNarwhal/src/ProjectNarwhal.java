import SimulationEngine.DisplayEngine.Display;
import SimulationEngine.DisplayEngine.RenderController;
import SimulationEngine.Loaders.AssimpLoader;
import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Shaders.StaticShader;
import Terrain.BaseTerrain;
import Terrain.TerrainTexture;
import Terrain.TerrainTexturePack;
import Water.WaterSurface;
import Water.WaterTexture;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class ProjectNarwhal {

    //stores the window handle
    private long window;

    public static void main(String[] args) {
        ProjectNarwhal simulation = new ProjectNarwhal();
        simulation.start();
    }

    public void start(){
        Display disp = new Display();
        window = disp.run();

        //main game loop
        loop();
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        ViewFrustrum camera = new ViewFrustrum(window);
        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader();
        RenderController renderer = new RenderController();

        ModeledEntity[] models = AssimpLoader.loadModel("ProjectResources/Coral1/1a.obj", loader, "/Coral1/coral1");
        ModeledEntity[] models2 = AssimpLoader.loadModel("ProjectResources/Coral2/Coral2.obj", loader, "/Coral2/coral2");
        ModeledEntity[] models3 = AssimpLoader.loadModel("ProjectResources/Coral3/3a.obj", loader, "/Coral3/coral3");
        ModeledEntity[] models4 = AssimpLoader.loadModel("ProjectResources/Coral4/4.obj", loader, "/Coral4/coral4");
        ModeledEntity[] models5 = AssimpLoader.loadModel("ProjectResources/Coral5/coral5.obj", loader, "/Coral5/coral5");
        ModeledEntity[] divingBell= AssimpLoader.loadModel("ProjectResources/DivingBell/Diving_Bell.obj", loader, "/DivingBell/Copper");
        ModeledEntity[] narwhal = AssimpLoader.loadModel("ProjectResources/Narwhal/narwhal.obj", loader, "Narwhal/whiteColor");



        narwhal[0].setPosition(new Vector3f(0, 10, -50));

        Random rand = new Random();
        List<ModeledEntity> entities = new ArrayList<>();

        //I've realised I'm already returning model and this is creating new models and assigning the old models to the new ones, fix
        ModeledEntity narwhal1 = new ModeledEntity(narwhal[0].getModel(), new Vector3f(0,40,-20), 0 ,270, 0, 3);
        narwhal1.setMaterial(narwhal[0].getMaterial());
        //back face culling will need to be disabled for the diving bells at this point
        ModeledEntity divingBell1 = new ModeledEntity(divingBell[0].getModel(), new Vector3f(45,40,-220), 0 ,270, 0, 3);
        divingBell1.setMaterial(divingBell[0].getMaterial());
        entities.add(divingBell1);
        ModeledEntity divingBell2 = new ModeledEntity(divingBell[0].getModel(), new Vector3f(-55,40,-30), 0 ,270, 0, 3);
        divingBell2.setMaterial(divingBell[0].getMaterial());
        entities.add(divingBell2);

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 500 - 250;
            float z = rand.nextFloat()* 500 - 250;
            ModeledEntity newEntity = new ModeledEntity(models3[0].getModel());
            newEntity.setMaterial(models3[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 500 - 250;
            float z = rand.nextFloat()* 500 - 250;
            ModeledEntity newEntity = new ModeledEntity(models2[0].getModel());
            newEntity.setMaterial(models2[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 500 - 250;
            float z = rand.nextFloat()* 500 - 250;
            ModeledEntity newEntity = new ModeledEntity(models4[0].getModel());
            newEntity.setMaterial(models4[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 500 - 250;
            float z = rand.nextFloat()* 500 - 250;
            ModeledEntity newEntity = new ModeledEntity(models5[0].getModel());
            newEntity.setMaterial(models5[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 500 - 250;
            float z = rand.nextFloat()* 500 - 250;
            ModeledEntity newEntity = new ModeledEntity(models[0].getModel());
            newEntity.setMaterial(models[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            entities.add(newEntity);
        }

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/seabed"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/coral"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/sand"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/stones"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("TerrainTextures/blendMap"));


        //water surface tiles
        WaterSurface water = new WaterSurface(0,200,0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder")));
        WaterSurface water2 = new WaterSurface(0,200,-1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder")));
        WaterSurface water3 = new WaterSurface(-1,200,-1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder")));
        WaterSurface water4 = new WaterSurface(-1,200,0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder")));

        //ground surface tiles
        BaseTerrain terrain = new BaseTerrain(0,0,loader, texturePack, blendMap, "TerrainTextures/heightmap");
        BaseTerrain terrain2 = new BaseTerrain(0,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap");
        BaseTerrain terrain3 = new BaseTerrain(-1,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap");
        BaseTerrain terrain4 = new BaseTerrain(-1,0,loader, texturePack, blendMap, "TerrainTextures/heightmap");

//        LightSource light = new LightSource(new Vector3f(0,1000,-7000), new Vector3f(0.4f,0.4f,0.4f));
        //Sun light source
        List<LightSource> lights = new ArrayList<>();
        lights.add(new LightSource(new Vector3f(10,10,10), new Vector3f(0.1f,0.1f,0.1f)));
        lights.add(new LightSource(new Vector3f(46,21,-221), new Vector3f(0f,0f, 2f), new Vector3f(1,0.01f, 0.002f)));
        lights.add(new LightSource(new Vector3f(-56,21,-31), new Vector3f(2f,0f, 0f), new Vector3f(1,0.01f, 0.002f)));
//        lights.add(light);


        boolean circle = false;
        boolean circle2 = false;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            camera.move();

            for(ModeledEntity model: entities){
                renderer.processEntity(model);
            }
            renderer.processEntity(narwhal1);
            if(narwhal1.getPosition().z <= -100 && !circle){
                circle = true;
                narwhal1.increaseRotation(0,180,0);
            }
            if(narwhal1.getPosition().z >= 100 && circle){
                circle = false;
                narwhal1.increaseRotation(0,180,0);
            }
            if(narwhal1.getPosition().x > -100 && !circle){
                narwhal1.setPosition(new Vector3f(narwhal1.getPosition().x, narwhal1.getPosition().y, narwhal1.getPosition().z - 0.05f));
            }

            if(narwhal1.getPosition().x < 100 && circle){
                narwhal1.setPosition(new Vector3f(narwhal1.getPosition().x, narwhal1.getPosition().y, narwhal1.getPosition().z  + 0.05f));
            }


            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);
            renderer.processWater(water);
            renderer.processWater(water2);
            renderer.processWater(water3);
            renderer.processWater(water4);

            renderer.render(lights, camera);


            glfwSwapBuffers(window); // swap the buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        renderer.cleanUp();
        shader.cleanUp();
    }

    public long getWindow() {
        return window;
    }

}