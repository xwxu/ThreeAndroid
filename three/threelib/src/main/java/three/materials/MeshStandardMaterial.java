package three.materials;

import three.materials.parameters.MeshParameters;
import three.materials.parameters.MeshStandardParameters;
import three.math.Color;
import three.math.Vector2;
import three.textures.CubeTexture;
import three.textures.Texture;

public class MeshStandardMaterial extends MeshMaterial{

    public Color color;
    public float roughness;
    public float metalness;
    public Texture map;
    public Texture lightMap;
    public float lightMapIntensity;
    public Texture aoMap;
    public float aoMapIntensity;
    public Color emissive;
    public float emissiveIntensity;
    public Texture emissiveMap;
    public Texture bumpMap;
    public float bumpScale;
    public Texture normalMap;
    public int normalMapType;
    public Vector2 normalScale;
    public Texture displacementMap;
    public float displacementScale;
    public float displacementBias;
    public Texture roughnessMap;
    public Texture metalnessMap;
    public Texture alphaMap;
    public CubeTexture envMap;
    public float envMapIntensity;
    public float refractionRatio;
    public String wireframeLinecap;
    public String wireframeLinejoin;
    public boolean skinning;
    public boolean morphTargets;
    public boolean morphNormals;

    public MeshStandardMaterial(MeshStandardParameters parameters){
        super(parameters);
        color = parameters.color;
        roughness = parameters.roughness;
        metalness = parameters.metalness;
        map = parameters.map;
        lightMap = parameters.lightMap;
        lightMapIntensity = parameters.lightMapIntensity;
        aoMap = parameters.aoMap;
        aoMapIntensity = parameters.aoMapIntensity;
        emissive = parameters.emissive;
        emissiveIntensity = parameters.emissiveIntensity;
        emissiveMap = parameters.emissiveMap;
        bumpMap = parameters.bumpMap;
        bumpScale = parameters.bumpScale;
        normalMap = parameters.normalMap;
        normalMapType = parameters.normalMapType;
        normalScale = parameters.normalScale;
        displacementMap = parameters.displacementMap;
        displacementScale = parameters.displacementScale;
        displacementBias = parameters.displacementBias;
        envMap = parameters.envMap;
        envMapIntensity = parameters.envMapIntensity;
        refractionRatio = parameters.refractionRatio;
        wireframeLinecap = parameters.wireframeLinecap;
        wireframeLinejoin = parameters.wireframeLinejoin;
        skinning = parameters.skinning;
        morphTargets = parameters.morphTargets;
        morphNormals = parameters.morphNormals;
    }
}
