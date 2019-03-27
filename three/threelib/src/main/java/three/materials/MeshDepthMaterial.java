package three.materials;

import three.materials.parameters.MeshDepthParameters;
import three.textures.Texture;

public class MeshDepthMaterial extends MeshMaterial {
    public int depthPacking;
    public boolean skinning;
    public boolean morphTargets;
    public Texture map;
    public Texture alphaMap;
    public Texture displacementMap;
    public float displacementScale;
    public float displacementBias;
    public boolean fog;
    public boolean lights;

    public MeshDepthMaterial(MeshDepthParameters parameters) {
        super(parameters);
        this.type = "MeshDepthMaterial";
        depthPacking = parameters.depthPacking;
        skinning = parameters.skinning;
        morphTargets = parameters.morphTargets;
        map = parameters.map;
        alphaMap = parameters.alphaMap;
        displacementMap = parameters.displacementMap;
        displacementScale = parameters.displacementScale;
        displacementBias = parameters.displacementBias;

    }

    public MeshDepthMaterial copy(MeshDepthMaterial source){
        super.copy(source);
        this.depthPacking = source.depthPacking;

        this.skinning = source.skinning;
        this.morphTargets = source.morphTargets;

        this.map = source.map;

        this.alphaMap = source.alphaMap;

        this.displacementMap = source.displacementMap;
        this.displacementScale = source.displacementScale;
        this.displacementBias = source.displacementBias;

        this.wireframe = source.wireframe;
        this.wireframeLinewidth = source.wireframeLinewidth;
        return this;
    }
}
