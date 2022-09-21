package Terrain;

import SimulationEngine.Loaders.ModelLoader;

public class WaterSurface extends BaseTerrain{

    private float gridY;

    public WaterSurface(int gridX, int gridZ, int gridY, ModelLoader loader, TerrainTexturePack texturePack, TerrainTexture blendMap){
        super(gridX, gridZ, loader, texturePack, blendMap, "TerrainTextures/heightmap1");
        this.gridY = gridY;
    }

     public float getY(){
        return gridY;
    }

}
