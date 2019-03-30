package three.materials;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import three.materials.parameters.MaterialParameters;
import three.math.Math_;
import three.math.Plane;
import three.renderers.gl.GLProgram;
import three.textures.Texture;

public class Material {
    private static int materialId = 0;

    public int id;
    public String uuid;
    public String name;
    public String type = "Material";
    public boolean fog;
    public boolean lights;
    public int blending;
    public int side;
    public boolean flatShading;
    public int vertexColors;
    public float opacity;
    public boolean transparent;
    public int blendSrc;
	public int blendDst;
	public int blendEquation;
	public int blendSrcAlpha;
	public int blendDstAlpha;
	public int blendEquationAlpha;
	public int depthFunc;
	public boolean depthTest;
	public boolean depthWrite;
	public ArrayList<Plane> clippingPlanes;
	public boolean clipIntersection;
	public boolean clipShadows;
	public int shadowSide;
	public boolean colorWrite;
	public String precision; // override the renderer's default precision for this material
	public boolean polygonOffset;
	public float polygonOffsetFactor;
	public float polygonOffsetUnits;
	public boolean dithering;
	public int alphaTest;
	public boolean premultipliedAlpha;
	public boolean visible;
    public boolean wireframe;
	public boolean needsUpdate;

    public GLProgram program;
    public boolean clipping = false;

    public HashMap<String,Boolean> defines;
    public Texture envMap = null;
    public int combine;
    public int normalMapType;
    public boolean sizeAttenuation;
    public boolean skinning;
    public boolean morphTargets;
    public boolean morphNormals;
    public int maxMorphTargets;
    public int maxMorphNormals;
    public boolean depthPacking;
    public String index0AttributeName;


    public Material(MaterialParameters parameters){
        this.id = materialId ++;
        this.uuid = Math_.generateUUID();
        this.name = "";
        this.fog = parameters.fog;
        this.lights = parameters.lights;

        this.blending = parameters.blending;
        this.side = parameters.side;
        this.flatShading = parameters.flatShading;
        this.vertexColors = parameters.vertexColors;

        this.opacity = parameters.opacity;
        this.transparent = parameters.transparent;

        this.blendSrc = parameters.blendSrc;
        this.blendDst = parameters.blendDst;
        this.blendEquation = parameters.blendEquation;
        this.blendSrcAlpha = parameters.blendSrcAlpha;
        this.blendDstAlpha = parameters.blendDstAlpha;
        this.blendEquationAlpha = parameters.blendEquationAlpha;

        this.depthFunc = parameters.depthFunc;
        this.depthTest = parameters.depthTest;
        this.depthWrite = parameters.depthWrite;

        this.clippingPlanes = null;
        this.clipIntersection = parameters.clipIntersection;
        this.clipShadows = parameters.clipShadows;

        this.shadowSide = parameters.shadowSide;

        this.colorWrite = parameters.colorWrite;

        this.precision = parameters.precision;

        this.polygonOffset = parameters.polygonOffset;
        this.polygonOffsetFactor = parameters.polygonOffsetFactor;
        this.polygonOffsetUnits = parameters.polygonOffsetUnits;

        this.dithering = parameters.dithering;
        this.alphaTest = parameters.alphaTest;
        this.premultipliedAlpha = parameters.premultipliedAlpha;
        this.visible = parameters.visible;
        this.wireframe = parameters.wireframe;
        this.needsUpdate = true;
    }

    public Material clone(){
        return new Material(new MaterialParameters()).copy(this);
    }

    public Material copy(Material source){
        this.name = source.name;

        this.fog = source.fog;
        this.lights = source.lights;

        this.blending = source.blending;
        this.side = source.side;
        this.flatShading = source.flatShading;
        this.vertexColors = source.vertexColors;

        this.opacity = source.opacity;
        this.transparent = source.transparent;

        this.blendSrc = source.blendSrc;
        this.blendDst = source.blendDst;
        this.blendEquation = source.blendEquation;
        this.blendSrcAlpha = source.blendSrcAlpha;
        this.blendDstAlpha = source.blendDstAlpha;
        this.blendEquationAlpha = source.blendEquationAlpha;

        this.depthFunc = source.depthFunc;
        this.depthTest = source.depthTest;
        this.depthWrite = source.depthWrite;

        this.colorWrite = source.colorWrite;

        this.precision = source.precision;

        this.polygonOffset = source.polygonOffset;
        this.polygonOffsetFactor = source.polygonOffsetFactor;
        this.polygonOffsetUnits = source.polygonOffsetUnits;

        this.dithering = source.dithering;

        this.alphaTest = source.alphaTest;
        this.premultipliedAlpha = source.premultipliedAlpha;

        this.visible = source.visible;

        this.clipShadows = source.clipShadows;
        this.clipIntersection = source.clipIntersection;

        ArrayList<Plane> srcPlanes = source.clippingPlanes,
                dstPlanes = null;

        if ( srcPlanes != null ) {
            dstPlanes = new ArrayList<Plane>();
            for ( int i = 0; i < srcPlanes.size(); ++ i ){
                dstPlanes.set(i, srcPlanes.get(i).clone_());
            }
        }

        this.clippingPlanes = dstPlanes;

        this.shadowSide = source.shadowSide;

        return this;
    }

    public Field getProperty(String name){
        Class cls = this.getClass();
        Field f = null;
        try {
            f = cls.getField(name);
        } catch (NoSuchFieldException e) {
        }
        return f;
    }

    public boolean checkFieldValid(String name){
        boolean valid = false;
        Class cls = this.getClass();
        Field f = null;
        try {
            f = cls.getField(name);
        } catch (NoSuchFieldException e) {
        }
        try {
            if(f != null &&  f.get(this) != null ){
                valid = true;
            }
        } catch (IllegalAccessException e) {
        }

        return valid;
    }
}
