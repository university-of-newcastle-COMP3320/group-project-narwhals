package SimulationEngine.ProjectEntities;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface Camera {
    public Vector3f getLocation();
    public Matrix4f getViewMatrix();
    public void reflect(float height);
    public Matrix4f getProjectionMatrix();
    public Matrix4f getProjectionViewMatrix();
}
