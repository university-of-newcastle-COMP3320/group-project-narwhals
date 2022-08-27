package SimulationEngine.DisplayEngine;

import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.Loaders.AssimpLoader;
import SimulationEngine.Models.Material;
import SimulationEngine.Models.Model;
import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Shaders.StaticShader;
import SimulationEngine.Models.ModelTexture;
import Terrain.BaseTerrain;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Display {

    //stores the window handle
    private long window;

    public void run() {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1990, 1080, "Secret Project Narwhal", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        ViewFrustrum camera = new ViewFrustrum(window);
        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader();
        RenderController renderer = new RenderController();

        ModeledEntity[] models = AssimpLoader.loadModel("ProjectResources/dragon.obj", loader, "whiteColor");

        models[0].setPosition(new Vector3f(0, 10, 0));

//        uncomment for performance test
        Random rand = new Random();
        List<ModeledEntity> entities = new ArrayList<>();

        for(int i=0; i<100; i++){
            float x = rand.nextFloat()* 100 - 50;
            float y = rand.nextFloat()* 100 - 50;
            float z = rand.nextFloat()* 100 - 50;
            ModeledEntity newEntity = new ModeledEntity(models[0].getModel());
            newEntity.setMaterial(models[0].getMaterial());
            newEntity.setPosition(new Vector3f(x,y,z));
            entities.add(newEntity);
        }

        BaseTerrain terrain = new BaseTerrain(0,0,loader, new ModelTexture(loader.loadTexture("grass")));
        BaseTerrain terrain2 = new BaseTerrain(-1,0,loader, new ModelTexture(loader.loadTexture("grass")));
        BaseTerrain terrain3 = new BaseTerrain(0,-1,loader, new ModelTexture(loader.loadTexture("grass")));
        BaseTerrain terrain4 = new BaseTerrain(-1,-1,loader, new ModelTexture(loader.loadTexture("grass")));




        LightSource light = new LightSource(new Vector3f(20000,20000,2000), new Vector3f(1.5f,1.5f,1.5f));

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            camera.move();


            for(ModeledEntity model: entities){
                renderer.processEntity(model);
                model.increaseRotation(0f,0.2f,0f);
            }
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);

            renderer.render(light, camera);


            glfwSwapBuffers(window); // swap the buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        renderer.cleanUp();
        shader.cleanUp();
    }

    public long getWindow() {
        return window;
    }
}
