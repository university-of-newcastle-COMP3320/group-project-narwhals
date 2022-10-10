package SimulationEngine.Reflections;

import SimulationEngine.ProjectEntities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CubeMapCamera implements Camera {

    private static final float NEAR_PLANE = 0.01f;
    private static final float FAR_PLANE = 200f;
    private static final float FOV = 90.0f;// don't change!
    private static final float ASPECT_RATIO = 1;

    private final Vector3f center;
    private float pitch = 0;
    private float yaw = 0;

    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();

    public CubeMapCamera(Vector3f center) {
        this.center = center;
        createProjectionMatrix();
    }

    public void switchToFace(int faceIndex) {
        switch (faceIndex) {
            case 0:
                pitch = -180;
                yaw = -90;
                break;
            case 1:
                pitch = -180;
                yaw = 90;
                break;
            case 2:
                pitch = -90;
                yaw = 0;
                break;
            case 3:
                pitch = 90;
                yaw = 0;
                break;
            case 4:
                pitch = -180;
                yaw = 0;
                break;
            case 5:
                pitch = -180;
                yaw = 180;
                break;
        }
        this.updateViewMatrix();
    }

    @Override
    public Vector3f getLocation() {
        return center;
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public void reflect(float height) {

    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4f getProjectionViewMatrix() {
        return projectionViewMatrix;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    private void createProjectionMatrix() {
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / ASPECT_RATIO;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.identity();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22 (-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }

    private void updateViewMatrix() {
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(180), new Vector3f(0, 0, 1), viewMatrix);
        viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix);
        viewMatrix.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix);
        Vector3f negativeCameraPos = new Vector3f(-center.x, -center.y, -center.z);
        viewMatrix.translate(negativeCameraPos, viewMatrix);

        projectionMatrix.mul(viewMatrix, projectionViewMatrix);
    }
}
