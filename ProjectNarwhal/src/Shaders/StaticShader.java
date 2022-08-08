package Shaders;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/Shaders/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader.txt";
    private int locTransMat;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        //this implementation needs some work otherwise it will require heaps of custom shaders
        //the shader attribute number references the number in the vbo, so to define color separately, create new vbo with color information
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    //This needs to be implemented for each uniform provided in the shaders and will have the name of the shader as the param in getUniformLocation
    //This location will need to be stored as an int somewhere, here it is stored globally.
    protected void getAllUniformLocations(){
        locTransMat = super.getUniformLocation("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(locTransMat, matrix);
    }

}
