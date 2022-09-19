package SimulationEngine.DisplayEngine;

import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.Shaders.TerrainShader;
import SimulationEngine.Shaders.WaterShader;
import SimulationEngine.Tools.ProjectMaths;
import Terrain.BaseTerrain;
import Water.WaterSurface;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class WaterRenderer {
    private WaterShader shader;

    public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix){
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<WaterSurface> waters){
        for(WaterSurface water: waters){
            prepareWater(water);
            loadModelMatrix(water);
            GL11.glDrawElements(GL11.GL_TRIANGLES, water.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindWater();
        }
    }

    private void prepareWater(WaterSurface water){
        GL30.glBindVertexArray(water.getModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        shader.loadShineVariables(1, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, water.getTexture().getTextureID());
    }

    private void unbindWater(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(WaterSurface water){
        Matrix4f transformationMatrix = ProjectMaths.createTransformationMatrix(new Vector3f(water.getX(), water.getY(), water.getZ()),0,0,0,1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}