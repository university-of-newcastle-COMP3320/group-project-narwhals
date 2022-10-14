package SimulationEngine.ProjectEntities;

import SimulationEngine.Tools.Keyboard;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;


public class ViewFrustrum implements Camera{
    private Vector3f location = new Vector3f(0,25,40);

    private final float CONTROL_OFFSET = 10.5f;
    private float pitch;
    private float yaw;
    private double[] x = new double[2];
    private double[] y = new double[2];
    public static final float FOV_ANGLE = 70.0f;
    public static final float NEAR_PLANE = 0.01f;
    public static final float FAR_PLANE = 100000f;
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private float currentSpeed;
    private long window;
    private Vector3f center;

    //default constructor
    public ViewFrustrum(long window, Vector3f center){
        x[0] = 0;
        x[1] = 0;
        y[0] = 0;
        y[1] = 0;
        this.window = window;
        Keyboard keyboard = new Keyboard();
        GLFW.glfwSetKeyCallback(window, keyboard::invoke);
        GLFW.glfwSetCursorPos(window, 0.5, 0.5);
        this.center = center;
        createProjectionMatrix();
        updateViewMatrix();
    };

    public void move(){
        x[1] = x[0];
        y[1] = y[0];
        GLFW.glfwGetCursorPos(window,x,y);
        checkInputs();
        calculatePitch((float)(y[1] - y[0]));
        calculateYaw((float)(x[1] - x[0]));
        float dx = (float) (currentSpeed * Math.sin(Math.toRadians(-getYaw() + CONTROL_OFFSET)));
        float dz = (float) (currentSpeed * Math.cos(Math.toRadians(-getYaw() + CONTROL_OFFSET)));
        location.x += dx;
        location.z += dz;
    }

    public void checkInputs(){
        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_W) && location.x >= -800 && location.x <=800 && location.z >= -800 && location.z <= 800){
            currentSpeed = -0.6f;
        }
        else if(Keyboard.isKeyDown(GLFW.GLFW_KEY_S) && location.x >= -800 && location.x <=800 && location.z >= -800 && location.z <= 800){
            currentSpeed = 0.6f;
        }
        else if(location.x <= -800){
            location.x += 1;
        }
        else if(location.z <= -800){
            location.z += 1;
        }
        else if(location.x >= 800) {
            location.x -= 1;
        }
        else if(location.z >= 800){
            location.z -= 1;
        }
        else{
            currentSpeed = 0;
        }
//        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_A)){
//            currentSpeed = 0.6f;
//        }
//        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_D)){
//            currentSpeed = 0.6f;
//        }
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

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public Vector3f getCenter(){
        return center;
    }

    public Matrix4f getViewMatrix(){
        return viewMatrix;
    }

    public Matrix4f getProjectionViewMatrix() {
        return projectionViewMatrix;
    }

    public void reflect(float height) {

    }

    //creates a projection matrix representing a frustrum using static variables
    private void createProjectionMatrix(){
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) vidmode.width()  / (float) vidmode.height() ;
        float y_scale = (float) ((1.0f / Math.tan(Math.toRadians(FOV_ANGLE / 2.0f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }

    public static Matrix4f createViewMatrix(ViewFrustrum camera){
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

    private void updateViewMatrix() {
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(180), new Vector3f(0, 0, 1));
        viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0));
        Vector3f negativeCameraPos = new Vector3f(-center.x, -center.y, -center.z);
        viewMatrix.translate(negativeCameraPos, viewMatrix);
        projectionMatrix.mul(viewMatrix, projectionViewMatrix);
    }
}
