package three.materials.parameters;

import three.math.Vector3;
import three.textures.Texture;

public class MeshDistanceParameters extends MeshParameters
{
    public Vector3 referencePosition = new Vector3();
    public float nearDistance = 1.0f;
    public float farDistance = 1000.0f;
    public boolean skinning = false;
    public boolean morphTargets = false;
    public Texture map = null;
    public Texture alphaMap = null;
    public Texture displacementMap = null;
    public float displacementScale = 1.0f;
    public float displacementBias = 0.0f;

    public MeshDistanceParameters(){
        super();
        this.fog = false;
        this.lights = false;
    }
}
