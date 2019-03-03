package three.materials.parameters;

import three.math.Color;
import three.textures.CubeTexture;
import three.textures.Texture;

import static three.constants.MultiplyOperation;

public class MeshBasicParameters extends MeshParameters {
    public Color color = new Color( 0xffffff );;
    public Texture map = null;
    public Texture lightMap = null;
    public float lightMapIntensity = 1.0f;
    public Texture aoMap = null;
    public float aoMapIntensity = 1.0f;
    public Texture specularMap = null;
    public Texture alphaMap = null;
    public CubeTexture envMap = null;
    public int combine = MultiplyOperation;
    public float reflectivity = 1.0f;
    public float refractionRatio = 0.98f;
    public String wireframeLinecap = "round";
    public String wireframeLinejoin = "round";
    public boolean skinning = false;
    public boolean morphTargets = false;

    public MeshBasicParameters(){
        super();
    }
}
