package three.renderers.gl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import three.core.Object3D;
import three.materials.Material;
import three.materials.ShaderMaterial;
import three.renderers.GLRenderTarget;
import three.renderers.GLRenderer;
import three.renderers.shaders.ShaderObject;
import three.scenes.Fog;
import three.textures.Texture;
import three.util.Parameters;

import static three.constants.BackSide;
import static three.constants.DoubleSide;
import static three.constants.GammaEncoding;
import static three.constants.LinearEncoding;
import static three.constants.ObjectSpaceNormalMap;

public class GLPrograms {

    public ArrayList<GLProgram> programs = new ArrayList<>();
    private final HashMap<String, String> shaderIDs = new HashMap<>();
    private String[] parameterNames =  new String[]
            {"precision", "supportsVertexTextures", "map", "mapEncoding", "matcap", "matcapEncoding", "envMap", "envMapMode", "envMapEncoding",
            "lightMap", "aoMap", "emissiveMap", "emissiveMapEncoding", "bumpMap", "normalMap", "objectSpaceNormalMap", "displacementMap", "specularMap",
            "roughnessMap", "metalnessMap", "gradientMap",
            "alphaMap", "combine", "vertexColors", "fog", "useFog", "fogExp",
            "flatShading", "sizeAttenuation", "logarithmicDepthBuffer", "skinning",
            "maxBones", "useVertexTexture", "morphTargets", "morphNormals",
            "maxMorphTargets", "maxMorphNormals", "premultipliedAlpha",
            "numDirLights", "numPointLights", "numSpotLights", "numHemiLights", "numRectAreaLights",
            "shadowMapEnabled", "shadowMapType", "toneMapping", "physicallyCorrectLights",
            "alphaTest", "doubleSided", "flipSided", "numClippingPlanes", "numClipIntersection", "depthPacking", "dithering"};

    private GLRenderer renderer;
    private GLCapabilities capabilities;

    public GLPrograms(GLRenderer renderer, GLCapabilities capabilities){
        shaderIDs.put("MeshDepthMaterial", "depth");
        shaderIDs.put("MeshDistanceMaterial", "distanceRGBA");
        shaderIDs.put("MeshNormalMaterial", "normal");
        shaderIDs.put("MeshBasicMaterial", "basic");
        shaderIDs.put("MeshLambertMaterial", "lambert");
        shaderIDs.put("MeshPhongMaterial", "phong");
        shaderIDs.put("MeshToonMaterial", "phong");
        shaderIDs.put("MeshStandardMaterial", "physical");
        shaderIDs.put("MeshPhysicalMaterial", "physical");
        shaderIDs.put("MeshMatcapMaterial", "matcap");
        shaderIDs.put("LineBasicMaterial", "basic");
        shaderIDs.put("LineDashedMaterial", "dashed");
        shaderIDs.put("PointsMaterial", "points");
        shaderIDs.put("ShadowMaterial", "shadow");
        shaderIDs.put("SpriteMaterial", "sprite");

        this.renderer = renderer;
        this.capabilities = capabilities;
    }

    public Parameters getParameters(Material material, GLLights lights, ArrayList shadows,
                                    Fog fog, int nClipPlanes, int nClipIntersection, Object3D object){

        int maxBones = 0;
        GLRenderTarget currentRenderTarget = renderer.getRenderTarget();

        Parameters  parameters = new Parameters();
        String shaderId = shaderIDs.get(material.type);
        parameters.shaderID = shaderId;
        parameters.precision = material.precision != null ? material.precision : capabilities.maxPrecision;
        parameters.supportsVertexTextures = capabilities.vertexTextures;
        parameters.outputEncoding = getTextureEncodingFromMap(material, "", renderer.gammaOutput);

        parameters.map = material.checkFieldValid("map");
        parameters.mapEncoding = getTextureEncodingFromMap(material, "map", renderer.gammaInput);

        parameters.matcap = material.checkFieldValid("matcap");

        parameters.matcapEncoding = getTextureEncodingFromMap(material, "matcap", renderer.gammaInput);
        parameters.envMap = material.envMap != null;
        //parameters.envMapMode = CheckFieldExist(material, "envMap") ? (material.envMap.mapping > 0) : false;
        parameters.envMapEncoding = getTextureEncodingFromMap(material, "envMap", renderer.gammaInput);
        //parameters.envMapCubeUv =  CheckFieldExist(material, "envMap") &&
        //        ((material.envMap.mapping == CubeUVReflectionMapping) || (material.envMap.mapping == CubeUVRefractionMapping));

        parameters.lightMap = material.checkFieldValid("lightMap");
        parameters.aoMap = material.checkFieldValid("aoMap");
        parameters.emissiveMap = material.checkFieldValid("emissiveMap");
        parameters.emissiveMapEncoding = getTextureEncodingFromMap(material, "emissiveMap", renderer.gammaInput);
        Field bumpMapField = material.getProperty("bumpMap");
        parameters.bumpMap = material.checkFieldValid("bumpMap");
        parameters.normalMap = material.checkFieldValid("normalMap");
        parameters.objectSpaceNormalMap = material.normalMapType == ObjectSpaceNormalMap;

        parameters.displacementMap = material.checkFieldValid("displacementMap");
        parameters.roughnessMap = material.checkFieldValid("roughnessMap");
        parameters.metalnessMap = material.checkFieldValid("metalnessMap");
        parameters.specularMap = material.checkFieldValid("specularMap");
        parameters.alphaMap = material.checkFieldValid("alphaMap");
        parameters.gradientMap = material.checkFieldValid("gradientMap");
        parameters.combine = material.combine;
        parameters.vertexColors = material.vertexColors;
        parameters.fog = fog != null;
        Field useFogField = material.getProperty("fog");
        try {
            parameters.useFog = useFogField != null && useFogField.getBoolean(material);
        } catch (IllegalAccessException e) {
        }
        parameters.flatShading = material.flatShading;
        parameters.sizeAttenuation = material.sizeAttenuation;
        parameters.logarithmicDepthBuffer = capabilities.logarithmicDepthBuffer;
        parameters.skinning = material.skinning && maxBones > 0;
        parameters.maxBones = maxBones;
        parameters.useVertexTexture = capabilities.floatVertexTextures;
        parameters.morphTargets = material.morphTargets;
        parameters.morphNormals = material.morphNormals;
        parameters.maxMorphTargets = material.maxMorphTargets;
        parameters.maxMorphNormals = material.maxMorphNormals;
        parameters.numDirLights = lights.directional.size();
        parameters.numPointLights = lights.point.size();
        parameters.numSpotLights = lights.spot.size();
        parameters.numRectAreaLights = lights.rectArea.size();
        parameters.numHemiLights = lights.hemi.size();
        parameters.numClippingPlanes = nClipPlanes;
        parameters.numClipIntersection = nClipIntersection;
        parameters.dithering = material.dithering;
        parameters.shadowMapEnabled = renderer.shadowMap.enabled && object.receiveShadow && shadows.size() > 0;
        parameters.shadowMapType = renderer.shadowMap.type;
        parameters.toneMapping = renderer.toneMapping;
        parameters.physicallyCorrectLights = renderer.physicallyCorrectLights;
        parameters.premultipliedAlpha = material.premultipliedAlpha;
        parameters.alphaTest = material.alphaTest;
        parameters.doubleSided = material.side == DoubleSide;
        parameters.flipSided = material.side == BackSide;
        parameters.depthPacking = material.depthPacking;

        return parameters;
    }

    private int getTextureEncodingFromMap(Material material, String map, boolean gammaOverrideLinear){
        int encoding = LinearEncoding;

        Class cls = material.getClass();
        Field f = null;
        try {
            f = cls.getField(map);
            Texture texmap = (Texture) f.get(material);

            if ( texmap == null ) {
                encoding = LinearEncoding;
            } else {
                encoding = texmap.encoding;
            }

        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

        // add backwards compatibility for WebGLRenderer.gammaInput/gammaOutput parameter, should probably be removed at some point.
        if ( encoding == LinearEncoding && gammaOverrideLinear ) {
            encoding = GammaEncoding;
        }

        return encoding;
    }

    public String getProgramCode(Material material, Parameters parameters){

        String code = "";
        String delimeter = ",";

        if(parameters.shaderID != null){
            code += parameters.shaderID + delimeter;
        }else{
            code += ((ShaderMaterial)material).fragmentShader + delimeter;
            code += ((ShaderMaterial)material).vertexShader + delimeter;
        }

        if(material.defines != null){
            // TODO
        }

        code += parameters.ToCode();

        code += renderer.gammaOutput;
        code += renderer.gammaFactor;

        return code;
    }

    public GLProgram acquireProgram(Material material, ShaderObject shader, Parameters parameters, String code) {
        GLProgram program = null;

        // Check if code has been already compiled
        for ( int p = 0, pl = programs.size(); p < pl; p ++ ) {
            GLProgram programInfo = programs.get(p);

            if (programInfo.code.equals(code)) {
                program = programInfo;
                ++ program.usedTimes;
                break;
            }
        }

        if ( program == null ) {
            program = new GLProgram( renderer, code, material, shader, parameters );
            programs.add( program );
        }

        return program;
    }
}
