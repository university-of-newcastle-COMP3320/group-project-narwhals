import Scene.Scene;
import SimulationEngine.DisplayEngine.Display;
import SimulationEngine.DisplayEngine.RenderController;
import SimulationEngine.Loaders.AssimpLoader;
import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.Models.Material;
import SimulationEngine.Models.Model;
import SimulationEngine.Models.Texture;
import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.BaseShaders.StaticShader;
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

        ViewFrustrum camera = new ViewFrustrum(window, new Vector3f(0f,0f,0f));
        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader();
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        RenderController renderer = new RenderController(loader, camera, fbos);

        Scene scene = new Scene(loader, fbos);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            renderer.renderShadowMap(scene.getEntities(), scene.getSun());
            camera.move();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            //Reflections
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getLocation().y - scene.getWaters().get(0).getY());
            camera.getLocation().y -= distance;
            camera.invertPitch();
            renderer.renderScene(scene.getEntities(), scene.getTerrains(), scene.getLights(), camera, new Vector4f(0, -1 , 0 , scene.getWaters().get(0).getY() - 25));
            camera.getLocation().y += distance;
            camera.invertPitch();
            fbos.unbindCurrentFrameBuffer();

            //Refractions
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(scene.getEntities(), scene.getTerrains(), scene.getLights(), camera, new Vector4f(0, 1 , 0 , -scene.getWaters().get(0).getY() + 5));
            fbos.unbindCurrentFrameBuffer();


            for(WaterSurface water:scene.getWaters()){
                renderer.processWater(water);
            }
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            //clip distance set to 130 to cull polygons
            renderer.renderScene(scene.getEntities(), scene.getTerrains(), scene.getLights(), camera, new Vector4f(0, -1 , 0 , 130));

            glfwSwapBuffers(window); // swap the buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        loader.cleanUp();
        fbos.cleanUp();
        renderer.cleanUp();
        shader.cleanUp();
    }

    public long getWindow() {
        return window;
    }

}