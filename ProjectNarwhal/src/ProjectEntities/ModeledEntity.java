package ProjectEntities;

import models.TexturedModel;
import org.joml.Vector3f;

public class ModeledEntity {

    private TexturedModel model;
    private Vector3f position;
    private float rX, rY, rZ, scale;

    public ModeledEntity(TexturedModel model, Vector3f position, float rX, float rY, float rZ, float scale){
        this.model = model;
        this.position = position;
        this.rX = rX;
        this.rY = rY;
        this.rZ = rZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float rx, float ry, float rz){
        this.rX += rx;
        this.rY += ry;
        this.rZ += rz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position){
        this.position = position;
    }

    public float getRX() {
        return rX;
    }

    public float getRY() {
        return rY;
    }

    public float getRZ() {
        return rZ;
    }

    public float getScale() {
        return scale;
    }
}
