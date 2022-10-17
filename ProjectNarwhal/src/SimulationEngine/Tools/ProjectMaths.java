package SimulationEngine.Tools;

import SimulationEngine.ProjectEntities.Camera;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import org.joml.Matrix4f;
import org.joml.Vector3f;


//Is a class containing the majority of the mathematical operations we are going to be calling on regularly.
//JOML also contains classes for quaternions and vector translations which will be very useful in the future for simulation design.
//JOML will handle the maths and where it is not able to do the math LWJGL should have functions to cover it.
public class ProjectMaths {

    //Takes a vector, adds the rotation values shown by rX,rY,rY and applies scale to them.
    //returns the transformation matrix
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rX, float rY, float rZ, float scale){
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation.x, translation.y, translation.z);
        matrix.rotate((float)Math.toRadians(rX), new Vector3f(1.0f, 0.0f, 0.0f), matrix);
        matrix.rotate((float)Math.toRadians(rY), new Vector3f(0.0f, 1.0f, 0.0f), matrix);
        matrix.rotate((float)Math.toRadians(rZ), new Vector3f(0.0f, 0.0f, 1.0f), matrix);
        matrix.scale(scale, matrix);
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix);
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix);
        //if we want to add roll rotation it needs to be added here
        Vector3f cameraPos = camera.getLocation();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        viewMatrix.translate(negativeCameraPos, viewMatrix);
        return viewMatrix;
    }
}
