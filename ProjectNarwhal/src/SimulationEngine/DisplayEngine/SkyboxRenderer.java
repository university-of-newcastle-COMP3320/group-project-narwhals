package SimulationEngine.DisplayEngine;

import SimulationEngine.ProjectEntities.Camera;
import SimulationEngine.Skybox.CubeMap;
import SimulationEngine.Skybox.SkyboxShader;
import SimulationEngine.Tools.ProjectMaths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SkyboxRenderer {


        private SkyboxShader shader;
        private Matrix4f projectionMatrix;
        private CubeMap cubeMap;

        public SkyboxRenderer(CubeMap cubeMap, Matrix4f projectionMatrix){
            this.projectionMatrix = projectionMatrix;
            shader = new SkyboxShader();
            this.cubeMap = cubeMap;
        }

        public void render(Camera camera){
            shader.start();
            shader.connectTextureUnits();
            loadProjectionViewMatrix(camera);
            bindCubeVao();
            bindTexture();
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36);
            unbindCubeVao();
            shader.stop();
        }

        public void cleanUp(){
            shader.cleanUp();
        }

        private void bindCubeVao(){
            GL30.glBindVertexArray(cubeMap.getCube().getVaoID());
            GL20.glEnableVertexAttribArray(0);
        }

        private void unbindCubeVao(){
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        }

        private void bindTexture(){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubeMap.getTexture());
        }

        private void loadProjectionViewMatrix(Camera camera){
            Matrix4f viewMatrix = ProjectMaths.createViewMatrix(camera);
            viewMatrix.m30(0);
            viewMatrix.m31(0);
            viewMatrix.m32(0);
            Matrix4f projectionViewMatrix = new Matrix4f();
            projectionMatrix.mul(viewMatrix, projectionViewMatrix);
            shader.loadProjectionViewMatrix(projectionViewMatrix);
        }

}
