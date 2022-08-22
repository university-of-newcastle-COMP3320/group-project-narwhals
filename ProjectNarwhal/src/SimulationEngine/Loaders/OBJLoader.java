package SimulationEngine.Loaders;

import SimulationEngine.Models.Model;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

//returns a model that the main game loop can use
public class OBJLoader {
    //import a scene using assimp and then use that scene to get the relevant information about the meshes etc.

    public static Model loadObjModel(String fileName, ModelLoader loader) {
        return loadObjModel(fileName, loader, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals);
    }

    public static Model loadObjModel (String fileName, ModelLoader loader, int flags){
        FileReader reader = null;
        try {
            reader = new FileReader(new File("ProjectResources/" +fileName+".obj"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bReader = new BufferedReader(reader);
        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;

        try {
            while (true) {
                line = bReader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v "))
                {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                }
                else if (line.startsWith("vt "))
                {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                }
                else if (line.startsWith("vn "))
                {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }
                else if (line.startsWith("f "))
                {
                    textureArray = new float[vertices.size() *2];
                    normalArray = new float[vertices.size() *3];
                    break;
                }
            }
            while(line != null){
                if( !line.startsWith("f ")){
                    line = bReader.readLine();
                    continue;
                }
                //split the faces into 3 seperate coords
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalArray);
                line = bReader.readLine();
            }
            bReader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex:vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i=0; i<indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray,textureArray,indicesArray);
    }

    //Sorts which vertex is related to which texture, normal vector and index
    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray){
        //I know we all love C++ so I had to put this in here
        int currentVertexPointer = Integer.parseInt(vertexData[0]) -1;
        indices.add(currentVertexPointer);
        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1])-1 );
        textureArray[currentVertexPointer*2] = currentTexture.x;
        textureArray[currentVertexPointer*2+1] = 1 - currentTexture.y; //1 - is required as openGL starts from the top left for UV mapping and blender starts at bottom left
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
    }

    //functions to convert assimp data into the arrays I need.
}
