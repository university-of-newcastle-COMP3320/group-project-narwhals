package SimulationEngine.ProjectEntities;

import SimulationEngine.Tools.Keyboard;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import java.nio.FloatBuffer;



public class ViewFrustrum {
    private Vector3f location = new Vector3f(0,10,10);
    private float pitch;
    private float yaw;
    private float roll;
    private FloatBuffer x = FloatBuffer.allocate(2);
    private FloatBuffer y = FloatBuffer.allocate(2);
    private float currentSpeed;
    private long window;

    //default constructor
    public ViewFrustrum(long window){
        this.window = window;
        Keyboard keyboard = new Keyboard();
        GLFW.glfwSetKeyCallback(window, keyboard::invoke);
        GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallbackI() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                x.put(1,x.get(0));
                y.put(1,y.get(0));
                x.put(0,(float)xpos);
                y.put(0,(float)ypos);
            }
        }::invoke);
    };

    public void move(){
        checkInputs();
        calculatePitch(y.get(1)- y.get(0));
        calculateYaw(x.get(1) - x.get(0));
        System.out.println(yaw);
        float dx = (float) (currentSpeed * Math.sin(Math.toRadians(-getYaw())));
        float dz = (float) (currentSpeed * Math.cos(Math.toRadians(-getYaw())));
        location.x += dx;
        location.z += dz;
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
        //not sure about these
//        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)){
//            currentSpeed = 0.6f;
//        }
//        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)){
//            currentSpeed = 0.6f;
//        }
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
        if(y > 1 || y < -1){
            float pitchChange = y * 0.1f;
            pitch-= pitchChange;
            if(pitch > 360){
                pitch -= 360;
            }
            if(pitch < 0){
                pitch += 360;
            }
        }
    }

    public void calculateYaw(float x){
        if(x > 1 || x < -1){
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
}
