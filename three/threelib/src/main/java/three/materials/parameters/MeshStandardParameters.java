package three.materials.parameters;

import three.math.Color;
import three.math.Vector2;
import three.textures.CubeTexture;
import three.textures.Texture;

import static three.constants.TangentSpaceNormalMap;

public class MeshStandardParameters extends MeshParameters {

    public Color color = new Color(0xffffff);
    public float roughness = 0.5f;
    public float metalness = 0.5f;
    public Texture map = null;
    public Texture lightMap = null;
    public float lightMapIntensity = 1.0f;
    public Texture aoMap = null;
    public float aoMapIntensity = 1.0f;
    public Color emissive = new Color(0x000000);
    public float emissiveIntensity = 1.0f;
    public Texture emissiveMap = null;
    public Texture bumpMap = null;
    public float bumpScale = 1.0f;
    public Texture normalMap = null;
    public int normalMapType = TangentSpaceNormalMap;
    public Vector2 normalScale = new Vector2(1,1);
    public Texture displacementMap = null;
    public float displacementScale = 1.0f;
    public float displacementBias = 0.0f;
    public Texture roughnessMap = null;
    public Texture metalnessMap = null;
    public Texture alphaMap = null;
    public CubeTexture envMap = null;
    public float envMapIntensity = 1.0f;
    public float refractionRatio = 0.98f;
    public String wireframeLinecap = "round";
    public String wireframeLinejoin = "round";
    public boolean skinning = false;
    public boolean morphTargets = false;
    public boolean morphNormals = false;

    public MeshStandardParameters(){
        super();
    }
}
