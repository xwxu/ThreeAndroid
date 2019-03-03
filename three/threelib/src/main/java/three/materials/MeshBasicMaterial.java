package three.materials;

import three.materials.parameters.MeshBasicParameters;
import three.math.Color;
import three.textures.CubeTexture;
import three.textures.Texture;

public class MeshBasicMaterial extends MeshMaterial{

    public Color color;
    public Texture map;
    public Texture lightMap;
    public float lightMapIntensity;
    public Texture aoMap;
    public float aoMapIntensity;
    public Texture specularMap;
    public Texture alphaMap;
    public CubeTexture envMap;
    public int combine;
    public float reflectivity;
    public float refractionRatio;
    public boolean wireframe;
    public float wireframeLinewidth;
    public String wireframeLinecap;
    public String wireframeLinejoin;
    public boolean skinning;
    public boolean morphTargets;

    public MeshBasicMaterial(MeshBasicParameters parameters){
        super(parameters);
        this.type = "MeshBasicMaterial";
        this.color = parameters.color;
        this.map = parameters.map;
        this.lightMap = parameters.lightMap;
        this.lightMapIntensity = parameters.lightMapIntensity;
        this.aoMap = parameters.aoMap;
        this.aoMapIntensity = parameters.aoMapIntensity;
        this.specularMap = parameters.specularMap;
        this.alphaMap = parameters.alphaMap;
        this.envMap = parameters.envMap;
        this.combine = parameters.combine;
        this.reflectivity = parameters.reflectivity;
        this.refractionRatio = parameters.refractionRatio;
        this.wireframeLinecap = parameters.wireframeLinecap;
        this.wireframeLinejoin = parameters.wireframeLinejoin;
        this.skinning = parameters.skinning;
        this.morphTargets = parameters.morphTargets;
        this.lights = false;
    }

    public MeshBasicMaterial Copy(MeshBasicMaterial source){
        super.Copy(source);
        this.color.Copy( source.color );

        this.map = source.map;

        this.lightMap = source.lightMap;
        this.lightMapIntensity = source.lightMapIntensity;

        this.aoMap = source.aoMap;
        this.aoMapIntensity = source.aoMapIntensity;

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

        return this;
    }
}
