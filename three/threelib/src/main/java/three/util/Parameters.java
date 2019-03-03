package three.util;

import java.util.ArrayList;

import three.materials.Material;
import three.lights.Light;

import static three.constants.CubeUVReflectionMapping;
import static three.constants.CubeUVRefractionMapping;

public class Parameters {
    public String shaderID;

    public String precision;
    public boolean map;
    public int mapEncoding;
    public boolean matcap;
    public int matcapEncoding;
    public boolean envMap;
    public boolean envMapMode;
    public int envMapEncoding;
    public boolean envMapCubeUv;
    public boolean lightMap;
    public boolean aoMap;
    public boolean emissiveMap;
    public int emissiveMapEncoding;
    public boolean bumpMap;
    public boolean normalMap;
    public boolean objectSpaceNormalMap;
    public boolean displacementMap;
    public boolean roughnessMap;
    public boolean metalnessMap;
    public boolean specularMap;
    public boolean alphaMap;
    public boolean gradientMap;

    public int combine;
    public int vertexColors;
    public boolean fog;
    public boolean useFog;
    public boolean fogExp;

    public boolean flatShading;
    public boolean sizeAttenuation;
    public boolean skinning;
    public int maxBones;

    public boolean morphTargets;
    public boolean morphNormals;
    public int maxMorphTargets;
    public int maxMorphNormals;

    public int numDirLights;
    public int numPointLights;
    public int numSpotLights;
    public int numRectAreaLights;
    public int numHemiLights;

    public int numClippingPlanes;
    public int numClipIntersection;

    public boolean dithering;

    public boolean shadowMapEnabled;
    public int shadowMapType;
    public int toneMapping;
    public boolean physicallyCorrectLights;

    public boolean premultipliedAlpha;
    public int alphaTest;
    public boolean doubleSided;
    public boolean flipSided;
    public boolean depthPacking;
    public boolean supportsVertexTextures;

    public boolean useVertexTexture;
    public boolean logarithmicDepthBuffer;
    public int outputEncoding;

    public String ToCode(){
        String delimiter = ",";
        return precision + delimiter +
                map + delimiter +
                matcap + delimiter +
                envMap + delimiter +
                lightMap + delimiter +
                aoMap + delimiter +
                emissiveMap + delimiter +
                bumpMap + delimiter +
                normalMap + delimiter +
                roughnessMap + delimiter +
                metalnessMap + delimiter +
                specularMap + delimiter +
                alphaMap + delimiter +
                gradientMap + delimiter +
                combine + delimiter +
                vertexColors + delimiter +
                fog + delimiter +
                flatShading + delimiter +
                sizeAttenuation + delimiter +
                skinning + delimiter +
                numDirLights + delimiter +
                numSpotLights + delimiter +
                numPointLights + delimiter +
                numHemiLights + delimiter +
                numRectAreaLights + delimiter +
                numClipIntersection + delimiter +
                numClippingPlanes + delimiter +
                dithering + delimiter +
                shadowMapEnabled + delimiter +
                toneMapping + delimiter +
                physicallyCorrectLights + delimiter +
                premultipliedAlpha + delimiter +
                alphaTest + delimiter +
                doubleSided + delimiter +
                flipSided + delimiter +
                depthPacking + delimiter;

    }
}
