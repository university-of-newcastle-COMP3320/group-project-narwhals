package SimulationEngine.DisplayEngine;

import SimulationEngine.Loaders.ModelLoader;
import Water.WaterShader;
import SimulationEngine.Tools.ProjectMaths;
import Water.WaterFrameBuffers;
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
    private float time = 0;
    private static final float WAVE_SPEED = 0.002f;
    private WaterFrameBuffers fbos;
    private int distortionTexture;

    public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.shader = shader;
        this.fbos = fbos;
        ModelLoader loader = new ModelLoader();
        distortionTexture = loader.loadTexture("/WaterTextures/DUDV");
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<WaterSurface> waters){
        for(WaterSurface water: waters){
            prepareWater(water);
            loadModelMatrix(water);
            GL11.glDrawElements(GL11.GL_TRIANGLES, water.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindWater();
            updateTime();
        }
    }

    private void prepareWater(WaterSurface water){
        GL30.glBindVertexArray(water.getModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        shader.loadShineVariables(1, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL13.GL_TEXTURE_2D, distortionTexture);
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

    private void updateTime() {
        time += WAVE_SPEED;
        shader.loadWaveTime(time / 5);
        shader.loadMoveFactor(time / 10);
    }

    public WaterShader getShader(){
        return shader;
    }
}