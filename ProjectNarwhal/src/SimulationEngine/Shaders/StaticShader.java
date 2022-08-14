package SimulationEngine.Shaders;

import SimulationEngine.ProjectEntities.ViewFrustrum;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/SimulationEngine/Shaders/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/SimulationEngine/Shaders/FragmentShader.txt";
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    //Constructor
    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        //uniforms will not work if this is not called
        getAllUniformLocations();
    }

    //Abstract implementation of bind attributes
    @Override
    protected void bindAttributes() {
        //the shader attribute number references the number in the vbo, so to define color separately, create new v with color information
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    //This needs to be implemented for each uniform provided in the shaders and will have the name of the shader as the param in getUniformLocation
    //This location will need to be stored as an int somewhere, here it is stored globally.
    protected void getAllUniformLocations(){
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
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

}
