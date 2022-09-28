package SimulationEngine.Loaders;

import SimulationEngine.Models.Model;
import SimulationEngine.Models.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;


public class ModelLoader {


    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    //loads positional data and texture data to the relevant VAO, so they can be applied to models from the VBO
    public Model loadToVAO(float[] positions,float[] textureCoords,float[] normals, int[] indicies) {
        int vaoID = createVAO();
        bindIndicesBuffer(indicies);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1,2, textureCoords);
        storeDataInAttributeList(2,3, normals);
        unbindVAO();
        return new Model(vaoID, indicies.length);
    }

    public Model loadToVAO(float[] positions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, 3, positions);
        unbindVAO();
        return new Model(vaoID, positions.length / 3);
    }


    public int loadCubeMap(String [] textureFiles){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
        for(int i = 0; i < textureFiles.length; i++){
            TextureData data = decodeTextureFile("ProjectResources/"+textureFiles[i]+".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        textures.add(texID);
        return texID;
    }

    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
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