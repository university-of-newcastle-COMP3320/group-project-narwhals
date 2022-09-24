package SimulationEngine.ProjectEntities;

import SimulationEngine.Tools.Keyboard;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;


public class ViewFrustrum {
    private Vector3f location = new Vector3f(0,25,40);

    private final float CONTROL_OFFSET = 10;
    private float pitch;
    private float yaw;
    private float roll;
    private double[] x = new double[2];
    private double[] y = new double[2];
    private float currentSpeed;
    private long window;
    private boolean move = true;

    //default constructor
    public ViewFrustrum(long window){
        x[0] = 0;
        x[1] = 0;
        y[0] = 0;
        y[1] = 0;
        this.window = window;
        Keyboard keyboard = new Keyboard();
        GLFW.glfwSetKeyCallback(window, keyboard::invoke);
//        GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallbackI() {
//            @Override
//            public void invoke(long window, double xpos, double ypos) {
//                x.put(2,x.get(1));
//                x.put(2,x.get(1));
//                x.put(1,x.get(0));
//                y.put(1,y.get(0));
//                x.put(0,(float)xpos);
//                y.put(0,(float)ypos);
//            }
//        }::invoke);
        GLFW.glfwSetCursorPos(window, 0, 0);
//        GLFW.glfwSetCursorEnterCallback(window, new GLFWCursorEnterCallbackI() {
//            @Override
//            public void invoke(long window, boolean entered) {
//                if(entered){
//                    move = true;
//                }
//                else{
//                    move = false;
//                }
//            }
//        });
    };

    public void move(){
        x[1] = x[0];
        y[1] = y[0];
        GLFW.glfwGetCursorPos(window,x,y);
        if(move == true) {
            checkInputs();
            calculatePitch((float)(y[1] - y[0]));
            calculateYaw((float)(x[1] - x[0]));
            float dx = (float) (currentSpeed * Math.sin(Math.toRadians(-getYaw() + CONTROL_OFFSET)));
            float dz = (float) (currentSpeed * Math.cos(Math.toRadians(-getYaw() + CONTROL_OFFSET)));
            location.x += dx;
            location.z += dz;
        }
    }

    public void checkInputs(){
        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_W)){
            currentSpeed = -0.6f;
        }
//        TO reimplement these, cross product
//        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_A)){
//            currentSpeed = 0.6f;
//        }
//        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_D)){
//            currentSpeed = 0.6f;
//        }
        else if(Keyboard.isKeyDown(GLFW.GLFW_KEY_S)){
            currentSpeed = 0.6f;
        }
        else{
            currentSpeed = 0;
        }
        if (Keyboard.isKeyDown(GLFW_KEY_ESCAPE )){
            glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        }
        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)){
            location.y -= 0.6f;
        }
        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)){
            location.y  += 0.6f;
        }
    }

    public void invertPitch(){
        this.pitch = -pitch;
    }

    //getters
    public Vector3f getLocation() {
        return location;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void calculatePitch(float y){
            float pitchChange = y * 0.1f;
            pitch-= pitchChange;
            if(pitch > 360){
                pitch -= 360;
            }
            if(pitch < 0){
                pitch += 360;
            }
    }

    public void calculateYaw(float x){
            float angleChange = x * 0.3f;
            yaw -= angleChange;
            if(yaw > 360){
                yaw -= 360;
            }

            if(yaw < 0){
                yaw += 360;
            }
    }
}
