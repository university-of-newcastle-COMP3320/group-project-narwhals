package Scene;

import SimulationEngine.DisplayEngine.RenderController;
import SimulationEngine.Loaders.AssimpLoader;
import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.Models.Texture;
import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.Reflections.EnvironmentMapRenderer;
import SimulationEngine.Skybox.CubeMap;
import Terrain.BaseTerrain;
import Terrain.TerrainTexture;
import Terrain.TerrainTexturePack;
import TerrainGeneration.TerrainGeneration;
import Water.WaterFrameBuffers;
import Water.WaterSurface;
import Water.WaterTexture;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scene{

    private ModelLoader loader;
    private WaterFrameBuffers fbos;
    private List<ModeledEntity> entities;
    private List<WaterSurface> waters;
    private List<BaseTerrain> terrains;
    private List<LightSource> lights;
    private LightSource sun;

    private Texture environmentMap;

    public Scene(ModelLoader loader, WaterFrameBuffers fbos, RenderController renderer) {
        this.loader = loader;
        this.fbos = fbos;
        entities = new ArrayList<>();

        environmentMap = Texture.newEmptyCubeMap(512);

        //Create the scene
        //Load models
        ModeledEntity[] models = AssimpLoader.loadModel("ProjectResources/Coral1/1a.obj", loader, "/Coral1/coral1");
        ModeledEntity[] models2 = AssimpLoader.loadModel("ProjectResources/Coral2/Coral2.obj", loader, "/Coral2/coral2");
        ModeledEntity[] models3 = AssimpLoader.loadModel("ProjectResources/Coral3/3a.obj", loader, "/Coral3/coral3");
        ModeledEntity[] models4 = AssimpLoader.loadModel("ProjectResources/Coral4/4.obj", loader, "/Coral4/coral4");
        ModeledEntity[] models5 = AssimpLoader.loadModel("ProjectResources/Coral5/coral5.obj", loader, "/Coral5/coral5");
        ModeledEntity[] divingBell = AssimpLoader.loadModel("ProjectResources/DivingBell/Diving_Bell.obj", loader, "/DivingBell/Copper");
        ModeledEntity[] narwhal = AssimpLoader.loadModel("ProjectResources/Narwhal/new-narwhal.obj", loader, "Narwhal/new-narwhalTexture");
        ModeledEntity[] orca = AssimpLoader.loadModel("ProjectResources/Orca/orca.obj", loader, "/Orca/orcaColor");
        ModeledEntity[] iceChunk1 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic1.obj", loader, "/IceChunks/ice-texture");
        ModeledEntity[] iceChunk2 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic2.obj", loader, "/IceChunks/ice-texture");
        ModeledEntity[] iceChunk3 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic3.obj", loader, "/IceChunks/ice-texture");
        ModeledEntity[] iceChunk4 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic4.obj", loader, "/IceChunks/ice-texture");
        ModeledEntity[] cube = AssimpLoader.loadModel("ProjectResources/Cube/cube.obj", loader, "/Narwhal/whiteColor");


        Random rand = new Random();

        //Random Positioning of IceChunks
//        for(int i=0; i<200; i++){
//            float x = rand.nextFloat()* 800 - 400;
//            float z = rand.nextFloat()* 800 - 400;
//            float scale = rand.nextInt(11) + 4;
//            int rotation = (int) (x+z % 360);
//            ModeledEntity newEntity;
//            switch ((int) (rand.nextFloat()*4+1)) {
//                case 1:
//                    newEntity = new ModeledEntity(iceChunk1[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 6);
//                    newEntity.setMaterial(iceChunk1[0].getMaterial());
//                    newEntity.setScale(scale);
//                    break;
//                case 2:
//                    newEntity = new ModeledEntity(iceChunk2[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 6);
//                    newEntity.setMaterial(iceChunk2[0].getMaterial());
//                    newEntity.setScale(scale);
//                    break;
//                case 3:
//                    newEntity = new ModeledEntity(iceChunk3[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 12);
//                    newEntity.setMaterial(iceChunk3[0].getMaterial());
//                    newEntity.setScale(scale);
//                    break;
//                default:
//                    newEntity = new ModeledEntity(iceChunk4[0].getModel(), new Vector3f(x,120,z), 0 ,rotation, 0, 6);
//                    newEntity.setMaterial(iceChunk4[0].getMaterial());
//                    newEntity.setScale(scale);
//                    break;
//
//            }
//            entities.add(newEntity);
//        }
//
//        //Random positioning of coral
//        for(int i=0; i<40; i++){
//            float x = rand.nextFloat()* 1000 - 500;
//            float z = rand.nextFloat()* 1000 - 500;
//            ModeledEntity newEntity = new ModeledEntity(models3[0].getModel());
//            newEntity.setMaterial(models3[0].getMaterial());
//            newEntity.setPosition(new Vector3f(x,0,z));
//            newEntity.setScale(rand.nextInt(10) + 1);
//            newEntity.setScale(rand.nextFloat() * 3);
//            entities.add(newEntity);
//        }
//
//
//        for(int i=0; i<40; i++){
//            float x = rand.nextFloat()* 1000 - 500;
//            float z = rand.nextFloat()* 1000 - 500;
//            ModeledEntity newEntity = new ModeledEntity(models2[0].getModel());
//            newEntity.setMaterial(models2[0].getMaterial());
//            newEntity.setPosition(new Vector3f(x,0,z));
//            newEntity.setScale(rand.nextInt(10) + 1);
//            newEntity.setScale(rand.nextFloat() * 3);
//            entities.add(newEntity);
//        }
//
//        for(int i=0; i<40; i++){
//            float x = rand.nextFloat()* 1000 - 500;
//            float z = rand.nextFloat()* 1000 - 500;
//            ModeledEntity newEntity = new ModeledEntity(models4[0].getModel());
//            newEntity.setMaterial(models4[0].getMaterial());
//            newEntity.setPosition(new Vector3f(x,0,z));
//            newEntity.setScale(rand.nextInt(10) + 1);
//            newEntity.setScale(rand.nextFloat() * 3);
//            entities.add(newEntity);
//        }
//
//        for(int i=0; i<40; i++){
//            float x = rand.nextFloat()* 1000 - 500;
//            float z = rand.nextFloat()* 1000 - 500;
//            ModeledEntity newEntity = new ModeledEntity(models5[0].getModel());
//            newEntity.setMaterial(models5[0].getMaterial());
//            newEntity.setPosition(new Vector3f(x,0,z));
//            newEntity.setScale(rand.nextInt(10) + 1);
//            newEntity.setScale(rand.nextFloat() * 3);
//            entities.add(newEntity);
//        }
//
//        for(int i=0; i<40; i++){
//            float x = rand.nextFloat()* 1000 - 500;
//            float z = rand.nextFloat()* 1000 - 500;
//            ModeledEntity newEntity = new ModeledEntity(models[0].getModel());
//            newEntity.setMaterial(models[0].getMaterial());
//            newEntity.setPosition(new Vector3f(x,0,z));
//            newEntity.setScale(rand.nextInt(10) + 1);
//            newEntity.setScale(rand.nextFloat());
//            entities.add(newEntity);
//        }
//
        //Load Terrain Textures
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/seabed"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/coralBase"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/sand"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/stones"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("TerrainTextures/blendMap"));

//
//        waters = new ArrayList<>();
//        //Load water surface tiles
//        //middle 4
//        waters.add(new WaterSurface(0, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(0, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(-1, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(-1, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        //top
//        waters.add(new WaterSurface(-2, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(-2, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(-2, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(-2, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        //sides
//        waters.add(new WaterSurface(-1, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(0, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(-1, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(0, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        //bottom
//        waters.add(new WaterSurface(1, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(1, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(1, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
//        waters.add(new WaterSurface(1, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));

        new TerrainGeneration();
        terrains = new ArrayList<>();
        //Load ground surface tiles
        terrains.add(new BaseTerrain(0,0,loader, texturePack, blendMap, "TerrainTextures/heightmap4"));
        terrains.add(new BaseTerrain(0,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap3"));
        terrains.add(new BaseTerrain(-1,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap1"));
        terrains.add(new BaseTerrain(-1,0,loader, texturePack, blendMap, "TerrainTextures/heightmap2"));
        terrains.add(new BaseTerrain(-2,0,loader, texturePack, blendMap, "TerrainTextures/heightmap12"));
        terrains.add(new BaseTerrain(-2,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap11"));
        terrains.add(new BaseTerrain(-3,0,loader, texturePack, blendMap, "TerrainTextures/heightmap9"));
        terrains.add(new BaseTerrain(-3,-1,loader, texturePack, blendMap, "TerrainTextures/heightmap10"));
        terrains.add(new BaseTerrain(0,-2,loader, texturePack, blendMap, "TerrainTextures/heightmap8"));
        terrains.add(new BaseTerrain(0,-3,loader, texturePack, blendMap, "TerrainTextures/heightmap7"));
        terrains.add(new BaseTerrain(-1,-2,loader, texturePack, blendMap, "TerrainTextures/heightmap5"));
        terrains.add(new BaseTerrain(-1,-3,loader, texturePack, blendMap, "TerrainTextures/heightmap6"));
        terrains.add(new BaseTerrain(-2,-2,loader, texturePack, blendMap, "TerrainTextures/heightmap16"));
        terrains.add(new BaseTerrain(-2,-3,loader, texturePack, blendMap, "TerrainTextures/heightmap15"));
        terrains.add(new BaseTerrain(-3,-2,loader, texturePack, blendMap, "TerrainTextures/heightmap13"));
        terrains.add(new BaseTerrain(-3,-3,loader, texturePack, blendMap, "TerrainTextures/heightmap14"));

        sun = new LightSource(new Vector3f(100000, 100000, 100000), new Vector3f(1f, 1f, 1f));
        //Sun light source
        lights = new ArrayList<>();
        lights.add(sun);
        lights.add(new LightSource(new Vector3f(46, 21, -221), new Vector3f(1f, 1f, 0.8235f), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new LightSource(new Vector3f(-56, 21, -31), new Vector3f(1f, 1f, 0.8235f), new Vector3f(1, 0.01f, 0.002f)));


        boolean circle = false;
        boolean circle2 = false;

//        int i = 0;
//        for (WaterSurface water : waters) {
//            waters.get(i).setTexture(new WaterTexture(fbos.getReflectionTexture()));
//            i++;
//        }

//        divingBell[0].setPosition(new Vector3f(45, 40, -220));
//        divingBell[0].setScale(3);
//        divingBell[0].setRY(270);
//        divingBell[0].getMaterial().setReflectivity(new Vector4f(0.0f));
//        divingBell[0].setEnvironmentMap(this.getEnvironmentMap());
//        entities.add(divingBell[0]);
//
//        ModeledEntity divingBell2 = new ModeledEntity(divingBell[0].getModel(), new Vector3f(-55, 40, -30), 0, 270, 0, 3);
//        divingBell2.setMaterial(divingBell[0].getMaterial());
//        entities.add(divingBell2);

//        renderer.renderShadowMap(this.getEntities(), this.getSun());
//        //glass cubes
//        for(int j = 0; j < 10; j ++){
//            float x = rand.nextFloat()* 200 + 50;
//            float z = rand.nextFloat()* 200 + 50;
//            float scale = rand.nextInt(6) + 3;
//            ModeledEntity cubes = new ModeledEntity(cube[0].getModel(), new Vector3f(x, 20, z), 0, 0, 0, scale);
//            cubes.setMaterial(cube[0].getMaterial());
//            cubes.getMaterial().setReflectance(1f);
//            cubes.getMaterial().setShineDamper(10f);
//            cubes.getMaterial().setReflectivity(new Vector4f(1f, 0.1f, 0.1f, 1f));
//            environmentMap = Texture.newEmptyCubeMap(1024);
//            EnvironmentMapRenderer.renderEnvironmentMap(environmentMap, this, new Vector3f(x, 20, z), renderer);
//            cubes.setEnvironmentMap(environmentMap);
//            entities.add(cubes);
//        }
//
//        ModeledEntity orca1 = new ModeledEntity(orca[0].getModel(), new Vector3f(-60, 60, -220), 0, 270, 0, 6);
//        orca1.setMaterial(orca[0].getMaterial());
//        orca1.getMaterial().setReflectance(1f);
//        orca1.getMaterial().setShineDamper(10f);
//        orca1.getMaterial().setReflectivity(new Vector4f(0.0f));
//        orca1.setEnvironmentMap(this.getEnvironmentMap());
//        entities.add(orca1);
//
//        narwhal[0].setPosition(new Vector3f(0, 40, -20));
//        narwhal[0].setRY(270);
//        narwhal[0].setScale(3);
//        entities.add(narwhal[0]);

    }

    public List<ModeledEntity> getEntities(){
        return entities;
    }

    public List<WaterSurface> getWaters(){
        return waters;
    }

    public List<BaseTerrain> getTerrains(){
        return terrains;
    }

    public List<LightSource> getLights(){
        return lights;
    }

    public LightSource getSun(){
        return sun;
    }

    public Texture getEnvironmentMap(){
        return environmentMap;
    }
}
