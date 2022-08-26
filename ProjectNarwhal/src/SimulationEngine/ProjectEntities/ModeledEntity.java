package SimulationEngine.ProjectEntities;

import SimulationEngine.Models.Material;
import SimulationEngine.Models.Model;
import org.joml.Vector3f;

public class ModeledEntity {

    private Material material;
    private Model model;
    private Vector3f position;
    private float rX, rY, rZ, scale;

    //Creates a modeled entity with a position, rotation and scale
    public ModeledEntity(Model model, Vector3f position, float rX, float rY, float rZ, float scale){
        this.model = model;
        this.position = position;
        this.rX = rX;
        this.rY = rY;
        this.rZ = rZ;
        this.scale = scale;
    }

    //increases a models postion on one of the axes
    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    //increases a models rotation on one of the axes
    public void increaseRotation(float rx, float ry, float rz){
        this.rX += rx;
        this.rY += ry;
        this.rZ += rz;
    }

    //returns the stored textured model
    public Model getModel() {
        return model;
    }

    //getters and setters
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position){
        this.position = position;
    }

    public float getRX() {
        return rX;
    }

    public void setRX(float rX) {
        this.rX = rX;
    }

    public float getRY() {
        return rY;
    }

    public void setRY(float rY) {
        this.rY = rY;
    }

    public float getRZ() {
        return rZ;
    }

    public void setRZ(float rZ) {
        this.rZ = rZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setMaterial(Material material){
        this.material = material;
    }

    public Material getMaterial(){
        return material;
    }

}
