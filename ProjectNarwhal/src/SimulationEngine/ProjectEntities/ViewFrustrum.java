package SimulationEngine.ProjectEntities;

import SimulationEngine.Tools.Keyboard;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class ViewFrustrum {

    private Vector3f location = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;

    //default constructor
//    public ViewFrustrum{
//        Keyboard keyboard = new Keyboard();
//    };

    public void move(){
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
}
