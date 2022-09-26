import SimulationEngine.DisplayEngine.Display;
import SimulationEngine.DisplayEngine.RenderController;
import SimulationEngine.Loaders.AssimpLoader;
import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.Models.Material;
import SimulationEngine.Models.Model;
import SimulationEngine.Models.ModelTexture;
import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Shaders.StaticShader;
import Terrain.BaseTerrain;
import Terrain.TerrainTexture;
import Terrain.TerrainTexturePack;
import Water.WaterFrameBuffers;
import Water.WaterSurface;
import Water.WaterTexture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glGetError;

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
        //Very important following line
        GL.createCapabilities();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        ViewFrustrum camera = new ViewFrustrum(window);
        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader();
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        RenderController renderer = new RenderController(camera, fbos);

        //Load models
        ModeledEntity[] models = AssimpLoader.loadModel("ProjectResources/Coral1/1a.obj", loader, "/Coral1/coral1");
        ModeledEntity[] models2 = AssimpLoader.loadModel("ProjectResources/Coral2/Coral2.obj", loader, "/Coral2/coral2");
        ModeledEntity[] models3 = AssimpLoader.loadModel("ProjectResources/Coral3/3a.obj", loader, "/Coral3/coral3");
        ModeledEntity[] models4 = AssimpLoader.loadModel("ProjectResources/Coral4/4.obj", loader, "/Coral4/coral4");
        ModeledEntity[] models5 = AssimpLoader.loadModel("ProjectResources/Coral5/coral5.obj", loader, "/Coral5/coral5");
        ModeledEntity[] divingBell= AssimpLoader.loadModel("ProjectResources/DivingBell/Diving_Bell.obj", loader, "/DivingBell/Copper");
        ModeledEntity[] narwhal = AssimpLoader.loadModel("ProjectResources/Narwhal/narwhal.obj", loader, "Narwhal/whiteColor");
        ModeledEntity[] orca = AssimpLoader.loadModel("ProjectResources/Orca/orca.obj", loader, "/Orca/orcaColor");
        ModeledEntity[] iceChunk1 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic1.obj", loader, "/IceChunks/ice-texture");
        ModeledEntity[] iceChunk2 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic2.obj", loader, "/IceChunks/ice-texture");
        ModeledEntity[] iceChunk3 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic3.obj", loader, "/IceChunks/ice-texture");
        ModeledEntity[] iceChunk4 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic4.obj", loader, "/IceChunks/ice-texture");

        //Set the initial position and orientation of models
        narwhal[0].setPosition(new Vector3f(0, 10, -50));

        Random rand = new Random();
        List<ModeledEntity> entities = new ArrayList<>();
        narwhal[0].setPosition( new Vector3f(0,40,-20));
        narwhal[0].setRY(270);
        narwhal[0].setScale(3);


        divingBell[0].setPosition(new Vector3f(45,40,-220));
        divingBell[0].setScale(3);
        divingBell[0].setRY(270);
        entities.add(divingBell[0]);

        ModeledEntity divingBell2 = new ModeledEntity(divingBell[0].getModel(), new Vector3f(-55,40,-30), 0 ,270, 0, 3);
        divingBell2.setMaterial(divingBell[0].getMaterial());
        entities.add(divingBell2);
        ModeledEntity orca1 = new ModeledEntity(orca[0].getModel(), new Vector3f(-60,60,-220), 0 ,270, 0, 6);
        orca1.setMaterial(orca[0].getMaterial());
        entities.add(orca1);


        //Random Positioning of IceChunks
        for(int i=0; i<200; i++){
            float x = rand.nextFloat()* 800 - 400;
            float z = rand.nextFloat()* 800 - 400;
            float scale = rand.nextInt(4,15);
            int rotation = (int) (x+z % 360);
            ModeledEntity newEntity;
            switch ((int) (rand.nextFloat()*4+1)) {
                case 1:
                    newEntity = new ModeledEntity(iceChunk1[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 6);
                    newEntity.setMaterial(iceChunk1[0].getMaterial());
                    newEntity.setScale(scale);
                    break;
                case 2:
                    newEntity = new ModeledEntity(iceChunk2[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 6);
                    newEntity.setMaterial(iceChunk2[0].getMaterial());
                    newEntity.setScale(scale);
                    break;
                case 3:
                    newEntity = new ModeledEntity(iceChunk3[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 12);
                    newEntity.setMaterial(iceChunk3[0].getMaterial());
                    newEntity.setScale(scale);
                    break;
                default:
                    newEntity = new ModeledEntity(iceChunk4[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 6);
                    newEntity.setMaterial(iceChunk4[0].getMaterial());
                    newEntity.setScale(scale);
                    break;

            }
            entities.add(newEntity);
        }

        //Random positioning of coral
        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 1000 - 500;
            float z = rand.nextFloat()* 1000 - 500;
            ModeledEntity newEntity = new ModeledEntity(models3[0].getModel());
            newEntity.setMaterial(models3[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat() * 3);
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 1000 - 500;
            float z = rand.nextFloat()* 1000 - 500;
            ModeledEntity newEntity = new ModeledEntity(models2[0].getModel());
            newEntity.setMaterial(models2[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat() * 3);
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 1000 - 500;
            float z = rand.nextFloat()* 1000 - 500;
            ModeledEntity newEntity = new ModeledEntity(models4[0].getModel());
            newEntity.setMaterial(models4[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat() * 3);
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 1000 - 500;
            float z = rand.nextFloat()* 1000 - 500;
            ModeledEntity newEntity = new ModeledEntity(models5[0].getModel());
            newEntity.setMaterial(models5[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat() * 3);
            entities.add(newEntity);
        }

        for(int i=0; i<50; i++){
            float x = rand.nextFloat()* 1000 - 500;
            float z = rand.nextFloat()* 1000 - 500;
            ModeledEntity newEntity = new ModeledEntity(models[0].getModel());
            newEntity.setMaterial(models[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,0,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat());
            entities.add(newEntity);
        }

        //Load Terrain Textures
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/seabed"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/coralBase"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/sand"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/stones"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("TerrainTextures/blendMap"));


        List<WaterSurface> waters = new ArrayList<>();
        //Load water surface tiles
        waters.add(new WaterSurface(0,125,0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(0,125,-1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-1,125,-1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-1,125,0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));

        List<BaseTerrain> terrains = new ArrayList<>();
        //Load ground surface tiles
        terrains.add(new BaseTerrain(0,0,loader, texturePack, blendMap, "TerrainTextures/heightmap"));
        terrains.add(new BaseTerrain(0,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap"));
        terrains.add(new BaseTerrain(-1,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap"));
        terrains.add(new BaseTerrain(-1,0,loader, texturePack, blendMap, "TerrainTextures/heightmap"));


        LightSource sun = new LightSource(new Vector3f(100000,100000,100000), new Vector3f(1f,1f,1f));
        //Sun light source
        List<LightSource> lights = new ArrayList<>();
        lights.add(sun);
        lights.add(new LightSource(new Vector3f(46,21,-221), new Vector3f(1f,1f, 0.8235f), new Vector3f(1,0.01f, 0.002f)));
        lights.add(new LightSource(new Vector3f(-56,21,-31), new Vector3f(1f,1f, 0.8235f), new Vector3f(1,0.01f, 0.002f)));


        boolean circle = false;
        boolean circle2 = false;

        int i = 0;
        for(WaterSurface water: waters){
            waters.get(i).setTexture(new WaterTexture(fbos.getReflectionTexture()));
            i++;
        }

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            renderer.renderShadowMap(entities, sun);
            camera.move();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            //Reflections
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getLocation().y - waters.get(0).getY());
            camera.getLocation().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1 , 0 , waters.get(0).getY() + 5));
            camera.getLocation().y += distance;
            camera.invertPitch();
            fbos.unbindCurrentFrameBuffer();

            //Refractions
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1 , 0 , -waters.get(0).getY() + 5));
            fbos.unbindCurrentFrameBuffer();


            for(WaterSurface water: waters){
                renderer.processWater(water);
            }
            //clip distance set to 130 to cull polygons
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1 , 0 , 130));

            glfwSwapBuffers(window); // swap the buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        fbos.cleanUp();
        renderer.cleanUp();
        shader.cleanUp();
    }

    public long getWindow() {
        return window;
    }

}