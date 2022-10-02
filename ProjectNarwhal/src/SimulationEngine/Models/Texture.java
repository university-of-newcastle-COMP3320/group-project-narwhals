package SimulationEngine.Models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;

public class Texture {

        public final int textureId;
        public final int size;
        private final int type;

        protected Texture(int textureId, int size) {
            this.textureId = textureId;
            this.size = size;
            this.type = GL11.GL_TEXTURE_2D;
        }

        protected Texture(int textureId, int type, int size) {
            this.textureId = textureId;
            this.size = size;
            this.type = type;
        }

        public void bindToUnit(int unit) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
            GL11.glBindTexture(type, textureId);
        }

        public void delete() {
            GL11.glDeleteTextures(textureId);
        }

//        public static TextureBuilder newTexture(MyFile textureFile) {
//            return new TextureBuilder(textureFile);
//        }
//
//        public static Texture newCubeMap(MyFile[] textureFiles) {
//            int cubeMapId = TextureUtils.loadCubeMap(textureFiles);
//            //TODO needs to know size!
//            return new Texture(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, 0);
//        }

        public static Texture newEmptyCubeMap(int size) {
            int cubeMapId = createEmptyCubeMap(size);
            return new Texture(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, size);
        }

        public static int createEmptyCubeMap(int size) {
            int texID = GL11.glGenTextures();
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
            for (int i = 0; i < 6; i++) {
                GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA8, size, size, 0, GL11.GL_RGBA,
                        GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
            }
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
            return texID;
        }
}
