package three.materials.parameters;

import three.math.Color;
import three.math.Vector2;
import three.textures.CubeTexture;
import three.textures.Texture;

import static three.constants.MultiplyOperation;
import static three.constants.TangentSpaceNormalMap;

public class MeshPhongParameters extends MeshParameters {

    public Color color = new Color( 0xffffff );;
    public Color specular = new Color(0x111111);
    public float shininess = 30.0f;
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
    public Texture alphaMap = null;
    public Texture specularMap = null;
    public CubeTexture envMap = null;
    public int combine = MultiplyOperation;
    public float reflectivity = 1.0f;
    public float refractionRatio = 0.98f;
    public String wireframeLinecap = "round";
    public String wireframeLinejoin = "round";
    public boolean skinning = false;
    public boolean morphTargets = false;
    public boolean morphNormals = false;

    public MeshPhongParameters(){
        super();
    }
}
