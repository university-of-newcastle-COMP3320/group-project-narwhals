package SimulationEngine.DisplayEngine;

import SimulationEngine.Loaders.ModelLoader;
import SimulationEngine.Models.Model;
import SimulationEngine.ProjectEntities.ViewFrustrum;
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

        public void render(ViewFrustrum camera){
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            shader.start();
            loadProjectionViewMatrix(camera);
            bindCubeVao();
            bindTexture();
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
            unbindCubeVao();
            shader.stop();
            GL11.glDepthFunc(GL11.GL_LESS);
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
            GL13.glActiveTexture(GL13.GL_TEXTURE2);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubeMap.getTexture());
        }

        private void loadProjectionViewMatrix(ViewFrustrum camera){
            Matrix4f viewMatrix = ProjectMaths.createViewMatrix(camera);
            Matrix4f projectionViewMatrix = new Matrix4f();
            projectionMatrix.mul(viewMatrix, projectionViewMatrix);
            shader.loadProjectionViewMatrix(projectionViewMatrix);
        }

}
