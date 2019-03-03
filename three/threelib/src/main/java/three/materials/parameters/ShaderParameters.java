package three.materials.parameters;

public class ShaderParameters extends MaterialParameters {

    public String vertexShader = "void main() {\\n\\tgl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\\n}";
    public String fragmentShader = "void main() {\\n\\tgl_FragColor = vec4( 1.0, 0.0, 0.0, 1.0 );\\n}";
    public float linewidth = 1.0f;
    public boolean wireframe = false;
    public float wireframeLinewidth = 1.0f;
    public boolean skinning = false;
    public boolean morphTargets = false;
    public boolean morphNormals = false;
    public boolean uniformsNeedUpdate = false;

    public ShaderParameters(){
        super();
    }
}
