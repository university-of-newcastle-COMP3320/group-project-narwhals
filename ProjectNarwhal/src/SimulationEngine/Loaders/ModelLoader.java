package SimulationEngine.Loaders;

import SimulationEngine.Models.Model;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class ModelLoader {


    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    //loads positional data and texture data to the relevant VAO, so they can be applied to models from the VBO
    public Model loadToVAO(float[] positions,float[] textureCoords, int[] indicies) {
        int vaoID = createVAO();
        bindIndicesBuffer(indicies);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1,2, textureCoords);
        unbindVAO();
        return new Model(vaoID, indicies.length);
    }

    //loads a texture from an image and returns its id
    public int loadTexture(String filename){
        int textureID;
        BufferedImage image = TextureLoader.loadImage(filename);
        textureID = TextureLoader.loadTexture(image);
        textures.add(textureID);
        return textureID;
    }

    //removes all leftover information stored in VAO's VBO's and textures arrays
    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture: textures){
            GL11.glDeleteTextures(texture);
        }
    }

    //Creates a and binds a VAO (Vertex array)
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    //Creates a VBO and binds it to vboID
    private void bindIndicesBuffer(int [] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    //Stores information in the attribute list, ie, texture information, vector information
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        //coordinate size determines the size of the list being stored, e.g vectors are generally 3 coords, textures are 2 etc
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    //unbinds vertex arrays
    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    //Stores the vertex information in a buffer that can be stored in the vbo
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    //stores information about VBO in an integer buffer
    private IntBuffer storeDataInIntBuffer(int [] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}