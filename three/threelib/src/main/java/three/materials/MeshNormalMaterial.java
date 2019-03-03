package three.materials;

import three.materials.parameters.MeshNormalParameters;
import three.math.Vector2;
import three.textures.Texture;

import static three.constants.TangentSpaceNormalMap;

public class MeshNormalMaterial extends MeshMaterial{

    public Texture bumpMap;
    public float bumpScale;
    public Texture normalMap;
    public int normalMapType;
    public Vector2 normalScale;
    public Texture displacementMap;
    public float displacementScale;
    public float displacementBias;
    public boolean skinning;
    public boolean morphTargets;
    public boolean morphNormals;

    public MeshNormalMaterial(MeshNormalParameters parameters){
        super(parameters);
        type = "MeshNormalMaterial";
        bumpMap = parameters.bumpMap;
        bumpScale = parameters.bumpScale;
        normalMap = parameters.normalMap;
        normalMapType = parameters.normalMapType;
        displacementMap = parameters.displacementMap;
        displacementScale = parameters.displacementScale;
        displacementBias = parameters.displacementBias;
        skinning = parameters.skinning;
        morphTargets = parameters.morphTargets;
        morphNormals = parameters.morphNormals;
    }

    public MeshNormalMaterial Copy(MeshNormalMaterial source){
        super.Copy(source);
        type = "MeshNormalMaterial";
        this.bumpMap = source.bumpMap;
        this.bumpScale = source.bumpScale;

        this.normalMap = source.normalMap;
        this.normalMapType = source.normalMapType;
        this.normalScale.Copy( source.normalScale );

        this.displacementMap = source.displacementMap;
        this.displacementScale = source.displacementScale;
        this.displacementBias = source.displacementBias;

        this.wireframe = source.wireframe;
        this.wireframeLinewidth = source.wireframeLinewidth;

        this.skinning = source.skinning;
        this.morphTargets = source.morphTargets;
        this.morphNormals = source.morphNormals;

        return this;
    }
}
