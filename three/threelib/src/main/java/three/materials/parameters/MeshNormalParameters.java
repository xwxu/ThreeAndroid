package three.materials.parameters;

import three.math.Vector2;
import three.textures.Texture;

import static three.constants.TangentSpaceNormalMap;

public class MeshNormalParameters extends MeshParameters {

    public Texture bumpMap = null;
    public float bumpScale = 1.0f;
    public Texture normalMap = null;
    public int normalMapType = TangentSpaceNormalMap;
    public Vector2 normalScale = new Vector2(1,1);
    public Texture displacementMap = null;
    public float displacementScale = 1.0f;
    public float displacementBias = 0.0f;
    public boolean skinning = false;
    public boolean morphTargets = false;
    public boolean morphNormals = false;

    public MeshNormalParameters(){
        super();
        fog = false;
        lights = false;
    }
}
