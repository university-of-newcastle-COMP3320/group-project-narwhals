package SimulationEngine.Shadows;


import SimulationEngine.ProjectEntities.LightSource;
import SimulationEngine.ProjectEntities.ModeledEntity;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ShadowMapRenderController {

	private static final int SHADOW_MAP_SIZE = 8192;

	private ShadowFrameBuffer shadowFbo;
	private ShadowShader shader;
	private ShadowBox shadowBox;
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f lightViewMatrix = new Matrix4f();
	private Matrix4f projectionViewMatrix = new Matrix4f();
	private Matrix4f offset = createOffset();

	private ShadowMapEntityRenderer entityRenderer;


	public ShadowMapRenderController(ViewFrustrum camera) {
		shader = new ShadowShader();
		shadowBox = new ShadowBox(lightViewMatrix, camera);
		shadowFbo = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
		entityRenderer = new ShadowMapEntityRenderer(shader, projectionViewMatrix);
	}

	public void render(List<ModeledEntity> entities, LightSource sun) {
		shadowBox.update();
		Vector3f sunPosition = sun.getPosition();
		Vector3f lightDirection = new Vector3f(-sunPosition.x, -sunPosition.y, -sunPosition.z);
		prepare(lightDirection, shadowBox);
		entityRenderer.render(entities);
		finish();
	}

	public Matrix4f getToShadowMapSpaceMatrix() {
		Matrix4f returnMatrix = new Matrix4f();
		offset.mul(projectionViewMatrix, returnMatrix);
		return returnMatrix;
	}

	public void cleanUp() {
		shader.cleanUp();
		shadowFbo.cleanUp();
	}

	public int getShadowMap() {
		return shadowFbo.getShadowMap();
	}

	protected Matrix4f getLightSpaceTransform() {
		return lightViewMatrix;
	}

	private void prepare(Vector3f lightDirection, ShadowBox box) {
		updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
		updateLightViewMatrix(lightDirection, box.getCenter());
		projectionMatrix.mul(lightViewMatrix, projectionViewMatrix);
		shadowFbo.bindFrameBuffer();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		shader.start();
	}

	private void finish() {
		shader.stop();
		shadowFbo.unbindFrameBuffer();
	}

	private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
		direction.normalize(direction);
		center.negate(center);
		lightViewMatrix.identity();
		float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
		lightViewMatrix.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix);
		float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
		yaw = direction.z > 0 ? yaw - 180 : yaw;
		lightViewMatrix.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0),
				lightViewMatrix);
		lightViewMatrix.translate(center, lightViewMatrix);
	}

	private void updateOrthoProjectionMatrix(float width, float height, float length) {
		projectionMatrix.identity();
		projectionMatrix.m00(2f / width);
		projectionMatrix.m11(2f / height);
		projectionMatrix.m22(-2f / length);
		projectionMatrix.m33(1);
	}

	private static Matrix4f createOffset() {
		Matrix4f offset = new Matrix4f();
		offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
		offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
		return offset;
	}

	public float getShadowDistance(){
		return shadowBox.getShadowDistance();
	}
}
