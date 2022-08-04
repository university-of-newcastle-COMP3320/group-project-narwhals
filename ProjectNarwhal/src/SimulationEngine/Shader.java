package SimulationEngine;

import org.lwjgl.opengl.GL20;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Shader {

    private String vertexFile = "../Shaders/vertex.glsl";
    private String fragmentFile = "../Shaders/FragmentShader";
    private int programID, vertexID, fragmentID;

    public Shader(){};

    public Shader (String vertexFile, String fragmentFile){
        this.vertexFile = vertexFile;
        this.fragmentFile = fragmentFile;
    }

    private String readFile(String shaderFile){
        File file = new File(this.getClass().getResource(shaderFile).getFile());
        Scanner scan = null;
        try{
            scan = new Scanner(file);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        String string = "";
        while (scan.hasNextLine()){
            string += scan.nextLine() + "\n";
        }
        return string;
    }

    private int loadShader(int type, String file){
        int id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, readFile(file));
        GL20.glCompileShader(id);

        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE){
            System.out.println("Could not compile" + file);
        }
        return id;
    }

    public void create(){
        programID = GL20.glCreateProgram();

        vertexID = loadShader(GL20.GL_VERTEX_SHADER, vertexFile);
        fragmentID = loadShader(GL20.GL_FRAGMENT_SHADER, fragmentFile);

        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);
        GL20.glLinkProgram(programID);
        GL20.glDeleteShader(fragmentID);

    }

    public void delete(){
        stop();
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);
        GL20.glDeleteProgram(programID);
    }

    public void use(){
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }
}
