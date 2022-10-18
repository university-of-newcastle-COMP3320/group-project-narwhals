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
import Water.WaterFrameBuffers;
import Water.WaterSurface;
import Water.WaterTexture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import TerrainGeneration.TerrainGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scene{

    private ModelLoader loader;
    private WaterFrameBuffers fbos;
    private WaterFrameBuffers fbos2;
    private List<ModeledEntity> entities;
    private List<WaterSurface> waters;
    private List<BaseTerrain> terrains;
    private List<LightSource> lights;
    private LightSource sun;

    private Texture environmentMap;

    public Scene(ModelLoader loader, WaterFrameBuffers fbos, WaterFrameBuffers fbos2, RenderController renderer) {
        this.loader = loader;
        this.fbos = fbos;
        this.fbos2 = fbos2;
        entities = new ArrayList<>();

        environmentMap = Texture.newEmptyCubeMap(512);

        //Create the scene
        //Load models
        ModeledEntity[] cube = AssimpLoader.loadModel("ProjectResources/Cube/cube.obj", loader, "/Narwhal/whiteColor", null);
        ModeledEntity[] barrel = AssimpLoader.loadModel("ProjectResources/Barrel/barrel.obj", loader, "Barrel/barrel", "Barrel/barrelNormal");
        ModeledEntity[] models = AssimpLoader.loadModel("ProjectResources/Coral1/1a.obj", loader, "/Coral1/coral1", null);
        ModeledEntity[] models2 = AssimpLoader.loadModel("ProjectResources/Coral2/Coral2.obj", loader, "/Coral2/coral2", null);
        ModeledEntity[] models4 = AssimpLoader.loadModel("ProjectResources/Coral4/4.obj", loader, "/Coral4/coral4", null);
        ModeledEntity[] models5 = AssimpLoader.loadModel("ProjectResources/Coral5/coral5.obj", loader, "/Coral5/coral5", null);
        ModeledEntity[] divingBell = AssimpLoader.loadModel("ProjectResources/DivingBell/Diving_Bell.obj", loader, "/DivingBell/Copper", "DivingBell/Copper_Normal");
        ModeledEntity[] narwhal = AssimpLoader.loadModel("ProjectResources/Narwhal/new-narwhal.obj", loader, "Narwhal/new-narwhalTexture", null);
        ModeledEntity[] orca = AssimpLoader.loadModel("ProjectResources/Orca/orca.obj", loader, "/Orca/orcaColor", null);
        ModeledEntity[] iceChunk1 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic1.obj", loader, "/IceChunks/ice-texture", null);
        ModeledEntity[] iceChunk2 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic2.obj", loader, "/IceChunks/ice-texture", null);
        ModeledEntity[] iceChunk3 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic3.obj", loader, "/IceChunks/ice-texture", null);
        ModeledEntity[] iceChunk4 = AssimpLoader.loadModel("ProjectResources/IceChunks/ic4.obj", loader, "/IceChunks/ice-texture", null);
        ModeledEntity[] fish = AssimpLoader.loadModel("ProjectResources/Fish/fish5.obj", loader, "/Fish/fish-scale", "Fish/fish-scale-normal");
        ModeledEntity[] divingBellWater = AssimpLoader.loadModel("ProjectResources/DivingBellWater/divingBellWater.obj", loader, "/WaterTextures/placeholder", null);
        ModeledEntity[] kelp = AssimpLoader.loadModel("ProjectResources/Kelp/kelp.obj", loader, "/Kelp/kelp", null);

        Random rand = new Random();

        float x;
        float z;

        //Random Positioning of IceChunks
        for(int i=0; i<200; i++){
            x = rand.nextFloat()* 2000 - 1000;
            z = rand.nextFloat()* 2000 - 1000;
            float scale = rand.nextInt(11) + 4;
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


        //Load Terrain Textures
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/seabed"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/coralBase"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/sand"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("TerrainTextures/stones"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("TerrainTextures/blendMap"));

        waters = new ArrayList<>();
        //Load water surface tiles
        //middle 4
        waters.add(new WaterSurface(0, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(0, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-1, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-1, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        //top
        waters.add(new WaterSurface(-2, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-2, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-2, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-2, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        //sides
        waters.add(new WaterSurface(-1, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(0, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(-1, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(0, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        //bottom
        waters.add(new WaterSurface(1, 125, -2, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(1, 125, -1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(1, 125, 0, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));
        waters.add(new WaterSurface(1, 125, 1, loader, new WaterTexture(loader.loadTexture("WaterTextures/placeholder"))));

        new TerrainGeneration();
        terrains = new ArrayList<>();
        //Load ground surface tiles
        terrains.add(new BaseTerrain(-1f,-1f,loader, texturePack, blendMap, "TerrainTextures/heightmap"));

        ModeledEntity orca1 = new ModeledEntity(orca[0].getModel(), new Vector3f(-60, 60, -220), 0, 270, 0, 6);
        orca1.setMaterial(orca[0].getMaterial());
        orca1.getMaterial().setReflectivity(new Vector4f(0.0f));
        orca1.setEnvironmentMap(this.getEnvironmentMap());
        entities.add(orca1);

        narwhal[0].setPosition(new Vector3f(200, 40, 220));
        narwhal[0].setRY(310);
        narwhal[0].setScale(3);
        entities.add(narwhal[0]);

        //Random positioning of coral
        for(int i=0; i<40; i++){
            x = rand.nextFloat()* 2000 - 1000;
            z = rand.nextFloat()* 2000 - 1000;
            ModeledEntity newEntity = new ModeledEntity(models2[0].getModel());
            newEntity.setMaterial(models2[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,terrains.get(0).getHeightOfTerrain(x, z)-3,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat() * 3);
            entities.add(newEntity);
        }

        for(int i=0; i<40; i++){
            x = rand.nextFloat()* 2000 - 1000;
            z = rand.nextFloat()* 2000 - 1000;
            ModeledEntity newEntity = new ModeledEntity(models4[0].getModel());
            newEntity.setMaterial(models4[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,terrains.get(0).getHeightOfTerrain(x, z)-5,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat() * 3);
            entities.add(newEntity);
        }

        for(int i=0; i<40; i++){
            x = rand.nextFloat()* 2000 - 1000;
            z = rand.nextFloat()* 2000 - 1000;
            ModeledEntity newEntity = new ModeledEntity(models5[0].getModel());
            newEntity.setMaterial(models5[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,terrains.get(0).getHeightOfTerrain(x, z)-3,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat() * 3);
            entities.add(newEntity);
        }

        for(int i=0; i<40; i++){
            x = rand.nextFloat()* 2000 - 1000;
            z = rand.nextFloat()* 2000 - 1000;
            ModeledEntity newEntity = new ModeledEntity(models[0].getModel());
            newEntity.setMaterial(models[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,terrains.get(0).getHeightOfTerrain(x, z)-3,z));
            newEntity.setScale(rand.nextInt(10) + 1);
            newEntity.setScale(rand.nextFloat());
            entities.add(newEntity);
        }

        //Random positioning and rotation of kelp
        for(int i=0; i<120; i++){
            x = rand.nextFloat()* 1000 - 500;
            z = rand.nextFloat()* 1000 - 500;
            float rotation = rand.nextFloat()* 360;
            ModeledEntity newEntity = new ModeledEntity(kelp[0].getModel());
            newEntity.setMaterial(kelp[0].getMaterial());
            float terrainHeight = terrains.get(0).getHeightOfTerrain(x, z);
            newEntity.setPosition(new Vector3f(x,terrainHeight,z));
            newEntity.setScale(rand.nextFloat() * 5);
            newEntity.setRY(rotation);
            entities.add(newEntity);
        }

        sun = new LightSource(new Vector3f(100000, 100000, 100000), new Vector3f(1f, 1f, 1f));
        //Sun light source
        lights = new ArrayList<>();
        lights.add(sun);
        lights.add(new LightSource(new Vector3f(-700, 23, -720), new Vector3f(2f, 2f, 1.646f), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new LightSource(new Vector3f(200, 23, 220), new Vector3f(2f, 2f, 1.646f), new Vector3f(1, 0.01f, 0.002f)));


        int i = 0;
        for (WaterSurface water : waters) {
            waters.get(i).setTexture(new WaterTexture(fbos.getReflectionTexture()));
            i++;
        }

        divingBell[0].setPosition(new Vector3f(200, 40, 220));
        divingBell[0].setScale(3);
        divingBell[0].setRY(270);
        divingBell[0].getMaterial().setReflectance(1f);
        divingBell[0].getMaterial().setShineDamper(10f);
        divingBell[0].getMaterial().setReflectivity(new Vector4f(0.0f));
        divingBell[0].setEnvironmentMap(this.getEnvironmentMap());
        entities.add(divingBell[0]);

        ModeledEntity divingBell2 = new ModeledEntity(divingBell[0].getModel(), new Vector3f(-700, 40, -720), 0, 270, 0, 3);
        divingBell2.setMaterial(divingBell[0].getMaterial());
        divingBell2.getMaterial().setReflectance(1f);
        divingBell2.getMaterial().setShineDamper(10f);
        entities.add(divingBell2);

        divingBellWater[0].setPosition(new Vector3f(200, 40, 220));
        divingBellWater[0].setScale(3);
        divingBellWater[0].setRY(270);
        divingBellWater[0].getMaterial().setReflectivity(new Vector4f(0.0f));
        divingBellWater[0].getMaterial().setTexture(new Texture(fbos2.getReflectionTexture()));
        divingBellWater[0].setEnvironmentMap(this.getEnvironmentMap());
        entities.add(divingBellWater[0]);

        ModeledEntity divingBellWater2 = new ModeledEntity(divingBellWater[0].getModel(), new Vector3f(-700, 40, -720), 0, 270, 0, 3);
        divingBellWater2.setMaterial(divingBellWater[0].getMaterial());
        entities.add(divingBellWater2);

        for(int j = 0; j < 5; j++){
            x = rand.nextFloat()* 2000 - 1000;
            z = rand.nextFloat()* 2000 - 1000;
            int ry = rand.nextInt(360);
            float scale = rand.nextInt(3) + 1;
            ModeledEntity barrels = new ModeledEntity(barrel[0].getModel(), new Vector3f(x,terrains.get(0).getHeightOfTerrain(x, z) + 3, z), 0, 0, ry, scale);
            barrels.setMaterial(barrel[0].getMaterial());
            entities.add(barrels);
        }

        renderer.renderShadowMap(this.getEntities(), this.getSun());
        //glass cubes
        for(int j = 0; j < 10; j ++){
            x = rand.nextFloat()* -200 + 50;
            z = rand.nextFloat()* -200 + 50;
            float scale = rand.nextInt(6) + 3;
            ModeledEntity cubes = new ModeledEntity(cube[0].getModel(), new Vector3f(x, terrains.get(0).getHeightOfTerrain(x, z) + 5, z), 0, 0, 0, scale);
            cubes.setMaterial(cube[0].getMaterial());
            cubes.getMaterial().setReflectance(1f);
            cubes.getMaterial().setShineDamper(10f);
            cubes.getMaterial().setReflectivity(new Vector4f(1f, 0.1f, 0.1f, 1f));
            environmentMap = Texture.newEmptyCubeMap(1024);
            EnvironmentMapRenderer.renderEnvironmentMap(environmentMap, this, new Vector3f(x, terrains.get(0).getHeightOfTerrain(x, z) + 5, z), renderer);
            cubes.setEnvironmentMap(environmentMap);
            entities.add(cubes);
        }

        //school of fish
        for (int j = 0; j<80; j++) {
            int randX = rand.nextInt(100);
            int randZ = rand.nextInt(30);
            int randY = rand.nextInt(30);
            ModeledEntity fish1 = new ModeledEntity(fish[0].getModel(), new Vector3f(-20 + randX,50 + randY, -80+randZ), 0 ,0, 0, 2);
            fish1.setMaterial(fish[0].getMaterial());
            entities.add(fish1);
        }

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
