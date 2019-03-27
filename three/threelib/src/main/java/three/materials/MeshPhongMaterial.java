package three.materials;

import three.materials.parameters.MeshPhongParameters;
import three.math.Color;
import three.math.Vector2;
import three.textures.CubeTexture;
import three.textures.Texture;

public class MeshPhongMaterial extends MeshMaterial{

    public Color color;
    public Color specular;
    public float shininess;
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
    public Texture specularMap;
    public Texture alphaMap;
    public CubeTexture envMap;
    public int combine;
    public float reflectivity;
    public float refractionRatio;
    public String wireframeLinecap;
    public String wireframeLinejoin;
    public boolean skinning;
    public boolean morphTargets;
    public boolean morphNormals;

    public MeshPhongMaterial(MeshPhongParameters parameters){
        super(parameters);
        type = "MeshPhongMaterial";
        color = parameters.color;
        specular = parameters.specular;
        shininess = parameters.shininess;
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
        specularMap = parameters.specularMap;
        envMap = parameters.envMap;
        combine = parameters.combine;
        reflectivity = parameters.reflectivity;
        refractionRatio = parameters.refractionRatio;
        wireframeLinecap = parameters.wireframeLinecap;
        wireframeLinejoin = parameters.wireframeLinejoin;
        skinning = parameters.skinning;
        morphTargets = parameters.morphTargets;
        morphNormals = parameters.morphNormals;
    }

    public MeshPhongMaterial copy(MeshPhongMaterial source) {
        super.copy(source);
        this.color.copy( source.color );
        this.specular.copy( source.specular );
        this.shininess = source.shininess;

        this.map = source.map;

        this.lightMap = source.lightMap;
        this.lightMapIntensity = source.lightMapIntensity;

        this.aoMap = source.aoMap;
        this.aoMapIntensity = source.aoMapIntensity;

        this.emissive.copy( source.emissive );
        this.emissiveMap = source.emissiveMap;
        this.emissiveIntensity = source.emissiveIntensity;

        this.bumpMap = source.bumpMap;
        this.bumpScale = source.bumpScale;

        this.normalMap = source.normalMap;
        this.normalMapType = source.normalMapType;
        this.normalScale.copy( source.normalScale );

        this.displacementMap = source.displacementMap;
        this.displacementScale = source.displacementScale;
        this.displacementBias = source.displacementBias;

        this.specularMap = source.specularMap;

        this.alphaMap = source.alphaMap;

        this.envMap = source.envMap;
        this.combine = source.combine;
        this.reflectivity = source.reflectivity;
        this.refractionRatio = source.refractionRatio;

        this.wireframe = source.wireframe;
        this.wireframeLinewidth = source.wireframeLinewidth;
        this.wireframeLinecap = source.wireframeLinecap;
        this.wireframeLinejoin = source.wireframeLinejoin;

        this.skinning = source.skinning;
        this.morphTargets = source.morphTargets;
        this.morphNormals = source.morphNormals;

        return this;
    }
}
