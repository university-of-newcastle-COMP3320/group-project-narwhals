package SimulationEngine.Reflections;

import Scene.Scene;
import SimulationEngine.DisplayEngine.RenderController;
import SimulationEngine.Models.Texture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;

public class EnvironmentMapRenderer {

    public static void renderEnvironmentMap(Texture cubeMap, Scene scene, Vector3f center, RenderController renderer){

        CubeMapCamera camera = new CubeMapCamera(center);

        int fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);

        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, cubeMap.size, cubeMap.size);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        GL11.glViewport(0,0, cubeMap.size, cubeMap.size);

        for(int i =0; i < 6; i ++){
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, cubeMap.textureId, 0);
            camera.switchToFace(i);
            //create low quality renderer
            renderer.renderScene(scene.getEntities(), scene.getTerrains(), scene.getLights(), camera, new Vector4f(0, 1 , 0 , 110));
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        GL11.glViewport(0,0, vidmode.width(), vidmode.height());

        GL30.glDeleteRenderbuffers(depthBuffer);
        GL30.glDeleteFramebuffers(fbo);
    }
}
