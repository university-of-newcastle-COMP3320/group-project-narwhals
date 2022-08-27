package SimulationEngine.Shaders;

import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TerrainShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/SimulationEngine/Shaders/TerrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/SimulationEngine/Shaders/TerrainFragmentShader.txt";
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectance;
    private int location_waterColor;

    //Constructor
    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        //uniforms will not work if this is not called
        getAllUniformLocations();
    }

    //binds the attributes declared in vbo to shader in's
    @Override
    protected void bindAttributes() {
        //the shader attribute number references the number in the vbo, so to define color separately, create new v with color information
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2,"normal");
    }

    //This needs to be implemented for each uniform provided in the shaders and will have the name of the shader as the param in getUniformLocation
    //This location will need to be stored as an int somewhere, here it is stored globally.
    protected void getAllUniformLocations(){
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightColor = super.getUniformLocation("lightColor");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectance = super.getUniformLocation("reflectance");
        location_waterColor = super.getUniformLocation("waterColor");
    }

    public void loadShineVariables(float damper, float reflectance){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectance, reflectance);
    }

    public void loadWaterColor(float r, float g, float b){
        super.loadVec3(location_waterColor, new Vector3f(r,g,b));
    }

    //Loads a provided transformation matrix to the shader
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(ViewFrustrum camera){
        Matrix4f viewMatrix = ProjectMaths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadLight(LightSource light){
        super.loadVec3(location_lightPosition, light.getPosition());
        super.loadVec3(location_lightColor, light.getColour());
    }
}
