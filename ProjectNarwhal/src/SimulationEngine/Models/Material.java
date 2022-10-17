package SimulationEngine.Models;

import org.joml.Vector4f;

//stores information about an object including normal maps and color and light values
public class Material {

    public static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;
    private Vector4f reflectivity;
    private float reflectance = 0;
    private float shineDamper = 1;
    private Texture texture;
    private Texture normalMap;

    public Material(){}

    public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, Vector4f reflectivity, float reflectance, float shineDamper, Texture texture, Texture normalMap) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.reflectance = reflectance;
        this.reflectivity = reflectivity;
        this.shineDamper = shineDamper;
        this.texture = texture;
        this.normalMap = normalMap;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public Vector4f getReflectivity(){
        return reflectivity;
    }

    public void setReflectivity(Vector4f reflectivity){
        this.reflectivity = reflectivity;
    }
}
