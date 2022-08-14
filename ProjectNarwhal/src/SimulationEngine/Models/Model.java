package SimulationEngine.Models;

public class Model {

    private int vaoID;
    private int vertexCount;

    //Constructor, stores vao with positional data and vertex count
    public Model (int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    //getters
    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
