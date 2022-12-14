package SimulationEngine.DisplayEngine;

import SimulationEngine.BaseShaders.NormalMapShader;
import SimulationEngine.BaseShaders.StaticShader;
import SimulationEngine.Models.Texture;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.Skybox.CubeMap;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class NormalMapRenderer {

    private NormalMapShader shader;
    private CubeMap entityMap;

    //Constructor, creates the view frustrum matrix and loads it
    public NormalMapRenderer(NormalMapShader shader, Matrix4f projectionMatrix){
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    //handles rendering each batch of models
    public void render(Map<ModeledEntity, List<ModeledEntity>> entities, Matrix4f toShadowSpace){
        shader.loadToShadowSpaceMatrix(toShadowSpace);
        shader.connectTextureUnits();
        for(ModeledEntity model:entities.keySet()){
            prepareModel(model);
            List<ModeledEntity> batch = entities.get(model);
            for(ModeledEntity entity: batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindEntity();
        }
    }

    private void prepareModel(ModeledEntity entity){
        GL30.glBindVertexArray(entity.getModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        shader.loadShineVariables(entity.getMaterial().getShineDamper(), entity.getMaterial().getReflectance());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getMaterial().getTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getMaterial().getNormalMap().getID());
    }

    private void unbindEntity(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(ModeledEntity entity){
        Matrix4f transformationMatrix = ProjectMaths.createTransformationMatrix(entity.getPosition(), entity.getRX(), entity.getRY(), entity.getRZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadShineVariables(entity.getMaterial().getShineDamper(), entity.getMaterial().getReflectance());
        if(entity.hasReflection()){
            shader.loadReflectivity(entity.getMaterial().getReflectivity().x);
            bindEnvironmentMap(entity.getEnvironmentMap());
        }
        else{
            shader.loadReflectivity(0);
        }
    }

    private void bindEnvironmentMap(Texture entityMap){
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, entityMap.getID());
    }
}
