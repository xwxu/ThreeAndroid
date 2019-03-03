package three.materials.parameters;

import three.textures.Texture;

import static three.constants.BasicDepthPacking;

public class MeshDepthParameters extends MeshParameters {
    public int depthPacking = BasicDepthPacking;
    public boolean skinning = false;
    public boolean morphTargets = false;
    public Texture map = null;
    public Texture alphaMap = null;
    public Texture displacementMap = null;
    public float displacementScale = 1.0f;
    public float displacementBias = 0.0f;

    public MeshDepthParameters(){
        super();
        this.fog = false;
        this.lights = false;
    }
}
