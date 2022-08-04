package Shaders;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/Shaders/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader.txt";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        //this implementation needs some work otherwise it will require heaps of custom shaders
        super.bindAttribute(0, "position");
    }
}
