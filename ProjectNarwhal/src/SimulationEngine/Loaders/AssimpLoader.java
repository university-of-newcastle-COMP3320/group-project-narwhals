package SimulationEngine.Loaders;

import SimulationEngine.Models.Material;
import SimulationEngine.Models.Model;
import SimulationEngine.Models.Texture;
import SimulationEngine.ProjectEntities.ModeledEntity;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

//returns a model that the main game loop can use
public class AssimpLoader {
    //import a scene using assimp and then use that scene to get the relevant information about the meshes etc.

    public static ModeledEntity[] loadModel(String filePath, ModelLoader loader, String texturePath) {
        return loadModel(filePath, loader, texturePath, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace
        );
    }

    public static ModeledEntity[] loadModel (String filePath, ModelLoader loader, String texturePath, int flags){
        AIScene aiScene = aiImportFile(filePath, flags);
        if(aiScene == null) {
            System.out.println("ERROR LOADING FILE");
        }

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<Material> materials = new ArrayList<Material>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials, texturePath);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        ModeledEntity[] entities = new ModeledEntity[numMeshes];
        for (int i = 0; i < numMeshes; i++) {

            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Model model = processMesh(aiMesh, materials, loader);
            entities[i] = new ModeledEntity(model);
            int materialIDx = aiMesh.mMaterialIndex();
            if(materialIDx >= 0 && materialIDx < materials.size()){
                entities[i].setMaterial(materials.get(materialIDx));
            }
        }
        return entities;
    }

    private static void processMaterial(AIMaterial aiMaterial, List<Material> materials, String texturePath){
        AIColor4D colour = AIColor4D.create();

        Vector4f ambient = Material.DEFAULT_COLOUR;
        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4f diffuse = Material.DEFAULT_COLOUR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4f specular = Material.DEFAULT_COLOUR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4f reflectivity = new Vector4f(0.0f);
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_REFLECTIVITY, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            reflectivity = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        ModelLoader loader = new ModelLoader();
        Texture texture =  new Texture(loader.loadTexture(texturePath));
        //will need to find a way to include the normal map here later

        Material material = new Material(ambient, diffuse, specular, reflectivity, 0, 1, texture);
        materials.add(material);
    }

    private static Model processMesh(AIMesh aiMesh, List<Material> materials, ModelLoader loader) {
        float [] tangents = processTangents(aiMesh);
        float[] vertices = processVertices(aiMesh);
        float[] textures = processTextCoords(aiMesh);
        float[] normals = processNormals(aiMesh);
        int[] indices = processIndices(aiMesh);

        return loader.loadToVAO(vertices,textures,normals,tangents,indices);
    }

    //functions to convert assimp data into the arrays I need.
    private static float[] processVertices(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mVertices();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D textCoord = buffer.get();
            data[pos++] = textCoord.x();
            data[pos++] = textCoord.y();
            data[pos++] = textCoord.z();
        }
        return data;
    }


    private static float[] processTangents(AIMesh aiMesh){
        AIVector3D.Buffer buffer = aiMesh.mTangents();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D tangent = buffer.get();
            data[pos++] = tangent.x();
            data[pos++] = tangent.y();
            data[pos++] = tangent.z();
        }
        return data;
    }


    private static float[] processNormals(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mNormals();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D normal = buffer.get();
            data[pos++] = normal.x();
            data[pos++] = normal.y();
            data[pos++] = normal.z();
        }
        return data;
    }

    private static float[] processTextCoords(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);
        if (buffer == null) {
            return new float[]{};
        }
        float[] data = new float[buffer.remaining() * 2];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D textCoord = buffer.get();
            data[pos++] = textCoord.x();
            data[pos++] = 1 - textCoord.y();
        }
        return data;
    }

    private static int[] processIndices(AIMesh aiMesh) {
        List<Integer> indices = new ArrayList<>();
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
        return indices.stream().mapToInt(Integer::intValue).toArray();
    }
}


