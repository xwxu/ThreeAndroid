package three.materials;

import three.materials.parameters.MeshLambertParameters;
import three.math.Color;
import three.textures.CubeTexture;
import three.textures.Texture;

public class MeshLambertMaterial extends MeshMaterial {

    public Color color;
    public Texture map;
    public Texture lightMap;
    public float lightMapIntensity;
    public Texture aoMap;
    public float aoMapIntensity;
    public Color emissive;
    public float emissiveIntensity;
    public Texture emissiveMap;
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

    public MeshLambertMaterial(MeshLambertParameters parameters){
        super(parameters);
        type = "MeshLambertMaterial";
        color = parameters.color;
        map = parameters.map;
        lightMap = parameters.lightMap;
        lightMapIntensity = parameters.lightMapIntensity;
        aoMap = parameters.aoMap;
        aoMapIntensity = parameters.aoMapIntensity;
        emissive = parameters.emissive;
        emissiveIntensity = parameters.emissiveIntensity;
        emissiveMap = parameters.emissiveMap;
        specularMap = parameters.specularMap;
        envMap = parameters.envMap;
        combine = parameters.combine;
        reflectivity = parameters.reflectivity;
        refractionRatio = parameters.refractionRatio;
        wireframe = parameters.wireframe;
        wireframeLinewidth = parameters.wireframeLinewidth;
        wireframeLinecap = parameters.wireframeLinecap;
        wireframeLinejoin = parameters.wireframeLinejoin;
        skinning = parameters.skinning;
        morphTargets = parameters.morphTargets;
        morphNormals = parameters.morphNormals;
    }

    public MeshLambertMaterial copy(MeshLambertMaterial source){
        super.copy(source);
        this.color.copy( source.color );

        this.map = source.map;

        this.lightMap = source.lightMap;
        this.lightMapIntensity = source.lightMapIntensity;

        this.aoMap = source.aoMap;
        this.aoMapIntensity = source.aoMapIntensity;

        this.emissive.copy( source.emissive );
        this.emissiveMap = source.emissiveMap;
        this.emissiveIntensity = source.emissiveIntensity;

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
