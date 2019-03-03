package three.materials;

import three.materials.parameters.MeshDistanceParameters;
import three.math.Vector3;
import three.textures.Texture;

public class MeshDistanceMaterial extends MeshMaterial{

    public Vector3 referencePosition;
    public float nearDistance;
    public float farDistance;
    public boolean skinning;
    public boolean morphTargets;
    public Texture map;
    public Texture alphaMap;
    public Texture displacementMap;
    public float displacementScale;
    public float displacementBias;

    public MeshDistanceMaterial(MeshDistanceParameters parameters){
        super(parameters);
        type = "MeshDistanceMaterial";
        referencePosition = parameters.referencePosition;
        nearDistance = parameters.nearDistance;
        farDistance = parameters.farDistance;
        skinning = parameters.skinning;
        morphTargets = parameters.morphTargets;
        map = parameters.map;
        alphaMap = parameters.alphaMap;
        displacementMap = parameters.displacementMap;
        displacementScale = parameters.displacementScale;
        displacementBias = parameters.displacementBias;

    }

    public MeshDistanceMaterial Copy(MeshDistanceMaterial source){
        super.Copy(source);

        this.referencePosition.Copy( source.referencePosition );
        this.nearDistance = source.nearDistance;
        this.farDistance = source.farDistance;

        this.skinning = source.skinning;
        this.morphTargets = source.morphTargets;

        this.map = source.map;
        this.alphaMap = source.alphaMap;

        this.displacementMap = source.displacementMap;
        this.displacementScale = source.displacementScale;
        this.displacementBias = source.displacementBias;

        return this;
    }
}
