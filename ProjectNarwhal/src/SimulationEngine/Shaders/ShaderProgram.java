package SimulationEngine.Shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
    //for 4 by 4 matrices
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    //Connstructor, creates shaders and program and attches shaders and attributes to program
    public ShaderProgram(String vertexFile, String fragmentFile){
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
    }

    //Returns the integer location of the uniform value for shaders
    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    //Returns the integer values of all uniform locations
    protected abstract void getAllUniformLocations();

    //Links up the inputs of the shader programs to the aspects of the VAO, e.g. colour, texture, position
    protected abstract void bindAttributes();

    //To bind an attribute to the program using a variable name
    protected void bindAttribute (int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    //starts the shader
    public void start(){
        GL20.glUseProgram(programID);
    }

    //stops the shader
    public void stop(){
        GL20.glUseProgram(0);
    }

    //detaches and deletes the shader after use
    public void cleanUp(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    //loads the shader from the provided text file into a format that can be used by opengl
    private static int loadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }

    //loads a float value to a specified location
    protected void loadFloat (int location, float value){
        GL20.glUniform1f(location, value);
    }

    //loads a vector3 to a specified location
    protected void loadVec3 (int location, Vector3f vector){
        GL20.glUniform3f(location, vector.x, vector.y , vector.z );
    }

    protected void loadVec4 (int location, Vector4f vector){
        GL20.glUniform4f(location, vector.x, vector.y , vector.z, vector.w);
    }

    protected void loadInt(int location, int value){
        GL20.glUniform1i(location,value);
    }

    //loads a boolean to a specified location
    protected void loadBool (int location, boolean value){
        int boolVal = 0;
        if(value){
            boolVal = 1;
        }
        GL20.glUniform1f(location, boolVal);
    }

    //loads a matrix to a specified location
    protected void loadMatrix(int location, Matrix4f matrix){
        matrix.get(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }


}
