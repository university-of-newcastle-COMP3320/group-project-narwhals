package SimulationEngine.DisplayEngine;

import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Shaders.StaticShader;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

public class RenderController {

    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<ModeledEntity, List<ModeledEntity>> entities = new HashMap<>();

    public void render(LightSource light, ViewFrustrum camera){
        GL11.glEnable(GL_DEPTH_TEST); //this is our z buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT); // clear the framebuffer and the depthbuffer

        shader.start(); //start the shaders
        shader.loadLight(light);
        shader.loadViewMatrix(camera);

        renderer.render(entities);

        shader.stop(); //stop the shaders
        entities.clear();
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

    public void cleanUp(){
        shader.cleanUp();
    }
}
