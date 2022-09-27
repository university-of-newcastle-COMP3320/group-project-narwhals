package SimulationEngine.Shadows;


import SimulationEngine.DisplayEngine.RenderController;
import SimulationEngine.ProjectEntities.ViewFrustrum;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;

public class ShadowBox {

	private static final float OFFSET = 300;
	private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
	private static final Vector4f FORWARD = new Vector4f(0, 0, -1, 0);
	private static final float SHADOW_DISTANCE = 300;

	private float minX, maxX;
	private float minY, maxY;
	private float minZ, maxZ;
	private Matrix4f lightViewMatrix;
	private ViewFrustrum cam;

	private float farHeight, farWidth, nearHeight, nearWidth;

	protected ShadowBox(Matrix4f lightViewMatrix, ViewFrustrum camera) {
		this.lightViewMatrix = lightViewMatrix;
		this.cam = camera;
		calculateWidthsAndHeights();
	}

	protected void update() {
		Matrix4f rotation = calculateCameraRotationMatrix();
		Vector4f vector = new Vector4f();
		rotation.transform(FORWARD, vector);
		Vector3f forwardVector = new Vector3f(vector.x, vector.y, vector.z);
		Vector3f toFar = new Vector3f(forwardVector);
		toFar.mul(SHADOW_DISTANCE);
		Vector3f toNear = new Vector3f(forwardVector);
		toNear.mul(RenderController.NEAR_PLANE);

		Vector3f centerNear = new Vector3f();
		toNear.add(cam.getLocation(), centerNear);
		Vector3f centerFar = new Vector3f();
		toFar.add(cam.getLocation(), centerFar);

		Vector4f[] points = calculateFrustumVertices(rotation, forwardVector, centerNear,
				centerFar);

		boolean first = true;
		for (Vector4f point : points) {
			if (first) {
				minX = point.x;
				maxX = point.x;
				minY = point.y;
				maxY = point.y;
				minZ = point.z;
				maxZ = point.z;
				first = false;
				continue;
			}
			if (point.x > maxX) {
				maxX = point.x;
			} else if (point.x < minX) {
				minX = point.x;
			}
			if (point.y > maxY) {
				maxY = point.y;
			} else if (point.y < minY) {
				minY = point.y;
			}
			if (point.z > maxZ) {
				maxZ = point.z;
			} else if (point.z < minZ) {
				minZ = point.z;
			}
		}
		maxZ += OFFSET;

	}

	protected Vector3f getCenter() {
		float x = (minX + maxX) / 2f;
		float y = (minY + maxY) / 2f;
		float z = (minZ + maxZ) / 2f;
		Vector4f cen = new Vector4f(x, y, z, 1);
		Matrix4f invertedLight = new Matrix4f();
		lightViewMatrix.invert(invertedLight);
		Vector4f vector = new Vector4f();
		invertedLight.transform(cen, vector);
		return new Vector3f(vector.x, vector.y, vector.z);
	}

	protected float getWidth() {
		return maxX - minX;
	}

	protected float getHeight() {
		return maxY - minY;
	}

	protected float getLength() {
		return maxZ - minZ;
	}

	private Vector4f[] calculateFrustumVertices(Matrix4f rotation, Vector3f forwardVector,
												Vector3f centerNear, Vector3f centerFar) {
		Vector4f vector = new Vector4f();
		rotation.transform(UP, vector);
		Vector3f upVector = new Vector3f(vector.x, vector.y, vector.z);
		Vector3f rightVector = new Vector3f();
		forwardVector.cross(upVector, rightVector);
		Vector3f downVector = new Vector3f(-upVector.x, -upVector.y, -upVector.z);
		Vector3f leftVector = new Vector3f(-rightVector.x, -rightVector.y, -rightVector.z);
		Vector3f farTop = new Vector3f();
		centerFar.add( new Vector3f(upVector.x * farHeight, upVector.y * farHeight, upVector.z * farHeight), farTop);
		Vector3f farBottom = new Vector3f();
		centerFar.add(new Vector3f(downVector.x * farHeight, downVector.y * farHeight, downVector.z * farHeight), farBottom);
		Vector3f nearTop = new Vector3f();
		centerNear.add( new Vector3f(upVector.x * nearHeight, upVector.y * nearHeight, upVector.z * nearHeight), nearTop);
		Vector3f nearBottom = new Vector3f();
		centerNear.add(new Vector3f(downVector.x * nearHeight, downVector.y * nearHeight, downVector.z * nearHeight), nearBottom);
		Vector4f[] points = new Vector4f[8];
		points[0] = calculateLightSpaceFrustumCorner(farTop, rightVector, farWidth);
		points[1] = calculateLightSpaceFrustumCorner(farTop, leftVector, farWidth);
		points[2] = calculateLightSpaceFrustumCorner(farBottom, rightVector, farWidth);
		points[3] = calculateLightSpaceFrustumCorner(farBottom, leftVector, farWidth);
		points[4] = calculateLightSpaceFrustumCorner(nearTop, rightVector, nearWidth);
		points[5] = calculateLightSpaceFrustumCorner(nearTop, leftVector, nearWidth);
		points[6] = calculateLightSpaceFrustumCorner(nearBottom, rightVector, nearWidth);
		points[7] = calculateLightSpaceFrustumCorner(nearBottom, leftVector, nearWidth);
		return points;
	}

	private Vector4f calculateLightSpaceFrustumCorner(Vector3f startPoint, Vector3f direction,
													  float width) {
		Vector3f point = new Vector3f();
		startPoint.add(new Vector3f(direction.x * width, direction.y * width, direction.z * width), point);
		Vector4f point4f = new Vector4f(point.x, point.y, point.z, 1f);
		lightViewMatrix.transform(point4f, point4f);
		return point4f;
	}

	private Matrix4f calculateCameraRotationMatrix() {
		Matrix4f rotation = new Matrix4f();
		rotation.rotate((float) Math.toRadians(-cam.getYaw()), new Vector3f(0, 1, 0));
		rotation.rotate((float) Math.toRadians(-cam.getPitch()), new Vector3f(1, 0, 0));
		return rotation;
	}

	private void calculateWidthsAndHeights() {
		farWidth = (float) (SHADOW_DISTANCE * Math.tan(Math.toRadians(RenderController.FOV_ANGLE)));
		nearWidth = (float) (RenderController.FOV_ANGLE
				* Math.tan(Math.toRadians(RenderController.FOV_ANGLE)));
		farHeight = farWidth / getAspectRatio();
		nearHeight = nearWidth / getAspectRatio();
	}

	private float getAspectRatio() {
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		return (float) vidmode.width() / (float) vidmode.height();
	}

	public float getShadowDistance(){
		return SHADOW_DISTANCE;
	}

}
