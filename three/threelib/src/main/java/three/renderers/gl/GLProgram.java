package three.renderers.gl;

import android.opengl.GLES20;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import three.materials.Material;
import three.materials.RawShaderMaterial;
import three.renderers.GLRenderer;
import three.renderers.shaders.ShaderChunk;
import three.util.Parameters;
import three.renderers.shaders.ShaderObject;

import static three.constants.ACESFilmicToneMapping;
import static three.constants.AddOperation;
import static three.constants.CineonToneMapping;
import static three.constants.CubeReflectionMapping;
import static three.constants.CubeRefractionMapping;
import static three.constants.CubeUVReflectionMapping;
import static three.constants.CubeUVRefractionMapping;
import static three.constants.EquirectangularReflectionMapping;
import static three.constants.EquirectangularRefractionMapping;
import static three.constants.GammaEncoding;
import static three.constants.LinearEncoding;
import static three.constants.LinearToneMapping;
import static three.constants.MixOperation;
import static three.constants.MultiplyOperation;
import static three.constants.NoToneMapping;
import static three.constants.PCFShadowMap;
import static three.constants.PCFSoftShadowMap;
import static three.constants.RGBDEncoding;
import static three.constants.RGBEEncoding;
import static three.constants.RGBM16Encoding;
import static three.constants.RGBM7Encoding;
import static three.constants.ReinhardToneMapping;
import static three.constants.SphericalReflectionMapping;
import static three.constants.Uncharted2ToneMapping;
import static three.constants.VertexColors;
import static three.constants.sRGBEncoding;

public class GLProgram {
    private static int programIdCount = 0;
    private static ShaderChunk chunks = new ShaderChunk();

    public int id = programIdCount ++;
    public String name;
    private GLRenderer renderer;
    public String code;
    public int usedTimes = 1;
    public int program;
    public String vertexShader;
    public String fragmentShader;

    HashMap<String, Boolean> defines;

    private HashMap<String, Integer> cachedAttributes = null;
    private GLUniforms cachedUniforms = null;

    public GLProgram(GLRenderer renderer, String code, Material material, ShaderObject shader, Parameters parameters ){
        this.renderer = renderer;
        this.code = code;
        defines = material.defines;
        vertexShader = shader.vertexShader;
        fragmentShader = shader.fragmentShader;

        String shadowMapTypeDefine = "SHADOWMAP_TYPE_BASIC";

        if ( parameters.shadowMapType == PCFShadowMap ) {
            shadowMapTypeDefine = "SHADOWMAP_TYPE_PCF";
        } else if ( parameters.shadowMapType == PCFSoftShadowMap ) {
            shadowMapTypeDefine = "SHADOWMAP_TYPE_PCF_SOFT";
        }

        String envMapTypeDefine = "ENVMAP_TYPE_CUBE";
        String envMapModeDefine = "ENVMAP_MODE_REFLECTION";
        String envMapBlendingDefine = "ENVMAP_BLENDING_MULTIPLY";

        if ( parameters.envMap ) {
            switch ( material.envMap.mapping ) {
                case CubeReflectionMapping:
                case CubeRefractionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_CUBE";
                    break;
                case CubeUVReflectionMapping:
                case CubeUVRefractionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_CUBE_UV";
                    break;
                case EquirectangularReflectionMapping:
                case EquirectangularRefractionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_EQUIREC";
                    break;
                case SphericalReflectionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_SPHERE";
                    break;
            }

            switch ( material.envMap.mapping ) {
                case CubeRefractionMapping:
                case EquirectangularRefractionMapping:
                    envMapModeDefine = "ENVMAP_MODE_REFRACTION";
                    break;
            }

            switch ( material.combine ) {
                case MultiplyOperation:
                    envMapBlendingDefine = "ENVMAP_BLENDING_MULTIPLY";
                    break;
                case MixOperation:
                    envMapBlendingDefine = "ENVMAP_BLENDING_MIX";
                    break;
                case AddOperation:
                    envMapBlendingDefine = "ENVMAP_BLENDING_ADD";
                    break;
            }
        }

        float gammaFactorDefine = ( renderer.gammaFactor > 0 ) ? renderer.gammaFactor : 1.0f;

        String customDefines = GenerateDefines( defines );

        program = GLES20.glCreateProgram();

        String prefixVertex, prefixFragment;

        if ( material instanceof RawShaderMaterial) {
            ArrayList<String> vertex = new ArrayList();
            vertex.add(customDefines);
            prefixVertex = String.join("\n", vertex);

            if ( !prefixVertex.isEmpty() ) {
                prefixVertex += "\n";
            }

            ArrayList<String> fragment = new ArrayList();
            fragment.add(customDefines);
            prefixFragment = String.join("\n", fragment);

            if ( !prefixFragment.isEmpty() ) {
                prefixFragment += "\n";
            }

        } else {
            ArrayList<String> vertex = new ArrayList();
            vertex.add("precision " + parameters.precision + " float;");
            vertex.add("precision " + parameters.precision + " int;");
            vertex.add("#define SHADER_NAME " + shader.name);
            vertex.add(customDefines);
            vertex.add(parameters.supportsVertexTextures ? "#define VERTEX_TEXTURES" : "");
            vertex.add("#define GAMMA_FACTOR " + gammaFactorDefine);
            vertex.add("#define MAX_BONES " + parameters.maxBones);
            vertex.add(( parameters.useFog && parameters.fog ) ? "#define USE_FOG" : "");
            vertex.add(( parameters.useFog && parameters.fogExp ) ? "#define FOG_EXP2" : "");
            vertex.add(parameters.map ? "#define USE_MAP" : "");
            vertex.add(parameters.envMap ? "#define USE_ENVMAP" : "");
            vertex.add(parameters.envMap ? "#define " + envMapModeDefine : "");
            vertex.add(parameters.lightMap ? "#define USE_LIGHTMAP" : "");
            vertex.add(parameters.aoMap ? "#define USE_AOMAP" : "");
            vertex.add(parameters.emissiveMap ? "#define USE_EMISSIVEMAP" : "");
            vertex.add(parameters.bumpMap ? "#define USE_BUMPMAP" : "");
            vertex.add(parameters.normalMap ? "#define USE_NORMALMAP" : "");
            vertex.add(( parameters.normalMap && parameters.objectSpaceNormalMap ) ? "#define OBJECTSPACE_NORMALMAP" : "");
            vertex.add(parameters.displacementMap && parameters.supportsVertexTextures ? "#define USE_DISPLACEMENTMAP" : "");
            vertex.add(parameters.specularMap ? "#define USE_SPECULARMAP" : "");
            vertex.add(parameters.roughnessMap ? "#define USE_ROUGHNESSMAP" : "");
            vertex.add(parameters.metalnessMap ? "#define USE_METALNESSMAP" : "");
            vertex.add(parameters.alphaMap ? "#define USE_ALPHAMAP" : "");
            vertex.add(parameters.vertexColors == VertexColors ? "#define USE_COLOR" : "");
            vertex.add(parameters.flatShading ? "#define FLAT_SHADED" : "");
            vertex.add(parameters.skinning ? "#define USE_SKINNING" : "");
            vertex.add(parameters.useVertexTexture ? "#define BONE_TEXTURE" : "");
            vertex.add(parameters.morphTargets ? "#define USE_MORPHTARGETS" : "");
            vertex.add(parameters.doubleSided ? "#define DOUBLE_SIDED" : "");
            vertex.add(parameters.flipSided ? "#define FLIP_SIDED" : "");
            vertex.add(parameters.shadowMapEnabled ? "#define USE_SHADOWMAP" : "");
            vertex.add(parameters.shadowMapEnabled ? "#define " + shadowMapTypeDefine : "");
            vertex.add(parameters.sizeAttenuation ? "#define USE_SIZEATTENUATION" : "");
            vertex.add(parameters.logarithmicDepthBuffer ? "#define USE_LOGDEPTHBUF" : "");
            vertex.add("uniform mat4 modelMatrix;");
            vertex.add("uniform mat4 modelViewMatrix;");
            vertex.add("uniform mat4 projectionMatrix;");
            vertex.add("uniform mat4 viewMatrix;");
            vertex.add("uniform mat3 normalMatrix;");
            vertex.add("uniform vec3 cameraPosition;");
            vertex.add("attribute vec3 position;");
            vertex.add("attribute vec3 normal;");
            vertex.add("attribute vec2 uv;");
            vertex.add("#ifdef USE_COLOR");
            vertex.add("	attribute vec3 color;");
            vertex.add("#endif");
            vertex.add("#ifdef USE_MORPHTARGETS");
            vertex.add("	attribute vec3 morphTarget0;");
            vertex.add("	attribute vec3 morphTarget1;");
            vertex.add("	attribute vec3 morphTarget2;");
            vertex.add("	attribute vec3 morphTarget3;");
            vertex.add("	#ifdef USE_MORPHNORMALS");
            vertex.add("		attribute vec3 morphNormal0;");
            vertex.add("		attribute vec3 morphNormal1;");
            vertex.add("		attribute vec3 morphNormal2;");
            vertex.add("		attribute vec3 morphNormal3;");
            vertex.add("	#else");
            vertex.add("		attribute vec3 morphTarget4;");
            vertex.add("		attribute vec3 morphTarget5;");
            vertex.add("		attribute vec3 morphTarget6;");
            vertex.add("		attribute vec3 morphTarget7;");
            vertex.add("	#endif");
            vertex.add("#endif");
            vertex.add("#ifdef USE_SKINNING");
            vertex.add("	attribute vec4 skinIndex;");
            vertex.add("	attribute vec4 skinWeight;");
            vertex.add("#endif");
            vertex.add("\n");

            prefixVertex = String.join("\n", vertex);

            ArrayList<String> fragment = new ArrayList();
            fragment.add("#extension GL_OES_standard_derivatives : enable");
            fragment.add("precision " + parameters.precision + " float;");
            fragment.add("precision " + parameters.precision + " int;");
            fragment.add("#define SHADER_NAME " + shader.name);
            fragment.add(parameters.alphaTest != 0 ? "#define ALPHATEST " + parameters.alphaTest + ( parameters.alphaTest % 1 != 0 ? "" : ".0" ) : "");
            fragment.add("#define GAMMA_FACTOR " + gammaFactorDefine);
            fragment.add(( parameters.useFog && parameters.fog ) ? "#define USE_FOG" : "");
            fragment.add(( parameters.useFog && parameters.fogExp ) ? "#define FOG_EXP2" : "");
            fragment.add(parameters.map ? "#define USE_MAP" : "");
            fragment.add(parameters.matcap ? "#define USE_MATCAP" : "");
            fragment.add(parameters.envMap ? "#define USE_ENVMAP" : "");
            fragment.add(parameters.envMap ? "#define " + envMapTypeDefine : "");
            fragment.add(parameters.envMap ? "#define " + envMapModeDefine : "");
            fragment.add(parameters.envMap ? "#define " + envMapBlendingDefine : "");
            fragment.add(parameters.lightMap ? "#define USE_LIGHTMAP" : "");
            fragment.add(parameters.aoMap ? "#define USE_AOMAP" : "");
            fragment.add(parameters.emissiveMap ? "#define USE_EMISSIVEMAP" : "");
            fragment.add(parameters.bumpMap ? "#define USE_BUMPMAP" : "");
            fragment.add(parameters.normalMap ? "#define USE_NORMALMAP" : "");
            fragment.add(( parameters.normalMap && parameters.objectSpaceNormalMap ) ? "#define OBJECTSPACE_NORMALMAP" : "");
            fragment.add(parameters.specularMap ? "#define USE_SPECULARMAP" : "");
            fragment.add(parameters.roughnessMap ? "#define USE_ROUGHNESSMAP" : "");
            fragment.add(parameters.metalnessMap ? "#define USE_METALNESSMAP" : "");
            fragment.add(parameters.alphaMap ? "#define USE_ALPHAMAP" : "");
            fragment.add(parameters.vertexColors == VertexColors ? "#define USE_COLOR" : "");
            fragment.add(parameters.gradientMap ? "#define USE_GRADIENTMAP" : "");
            fragment.add(parameters.flatShading ? "#define FLAT_SHADED" : "");
            fragment.add(parameters.doubleSided ? "#define DOUBLE_SIDED" : "");
            fragment.add(parameters.flipSided ? "#define FLIP_SIDED" : "");
            fragment.add(parameters.shadowMapEnabled ? "#define USE_SHADOWMAP" : "");
            fragment.add(parameters.shadowMapEnabled ? "#define " + shadowMapTypeDefine : "");
            fragment.add(parameters.premultipliedAlpha ? "#define PREMULTIPLIED_ALPHA" : "");
            fragment.add(parameters.physicallyCorrectLights ? "#define PHYSICALLY_CORRECT_LIGHTS" : "");
            fragment.add(parameters.logarithmicDepthBuffer ? "#define USE_LOGDEPTHBUF" : "");
            fragment.add("uniform mat4 viewMatrix;");
            fragment.add("uniform vec3 cameraPosition;");
            fragment.add(( parameters.toneMapping != NoToneMapping ) ? "#define TONE_MAPPING" : "");
            fragment.add(( parameters.toneMapping != NoToneMapping ) ? chunks.Get("tonemapping_pars_fragment"): "");
            fragment.add(( parameters.toneMapping != NoToneMapping ) ? GetToneMappingFunction( "toneMapping", parameters.toneMapping ) : "");
            fragment.add(parameters.dithering ? "#define DITHERING" : "");
            fragment.add(( parameters.outputEncoding != 0 || parameters.mapEncoding != 0 || parameters.matcapEncoding != 0 ||
                    parameters.envMapEncoding != 0 || parameters.emissiveMapEncoding != 0) ?
                    chunks.Get("encodings_pars_fragment"): "");
            fragment.add(parameters.mapEncoding != 0 ? GetTexelDecodingFunction( "mapTexelToLinear", parameters.mapEncoding ) : "");
            fragment.add(parameters.matcapEncoding != 0 ? GetTexelDecodingFunction( "matcapTexelToLinear", parameters.matcapEncoding ) : "");
            fragment.add(parameters.envMapEncoding != 0 ? GetTexelDecodingFunction( "envMapTexelToLinear", parameters.envMapEncoding ) : "");
            fragment.add(parameters.emissiveMapEncoding != 0 ? GetTexelDecodingFunction( "emissiveMapTexelToLinear", parameters.emissiveMapEncoding ) : "");
            fragment.add(parameters.outputEncoding != 0 ? GetTexelEncodingFunction( "linearToOutputTexel", parameters.outputEncoding ) : "");
            fragment.add(parameters.depthPacking ? "#define DEPTH_PACKING " + material.depthPacking : "");
            fragment.add("\n");

            prefixFragment = String.join("\n", fragment);
        }

        vertexShader = ParseIncludes( vertexShader );
        vertexShader = ReplaceLightNums( vertexShader, parameters );
        vertexShader = ReplaceClippingPlaneNums( vertexShader, parameters );

        fragmentShader = ParseIncludes( fragmentShader );
        fragmentShader = ReplaceLightNums( fragmentShader, parameters );
        fragmentShader = ReplaceClippingPlaneNums( fragmentShader, parameters );

        vertexShader = UnrollLoops( vertexShader );
        fragmentShader = UnrollLoops( fragmentShader );

        String vertexGlsl = prefixVertex + vertexShader;
        String fragmentGlsl = prefixFragment + fragmentShader;

        int glVertexShader = GLShader.CreateShader( GLES20.GL_VERTEX_SHADER, vertexGlsl );
        int glFragmentShader = GLShader.CreateShader( GLES20.GL_FRAGMENT_SHADER, fragmentGlsl );

        GLES20.glAttachShader( program, glVertexShader );
        GLES20.glAttachShader( program, glFragmentShader );

        // Force a particular attribute to index 0.
        if ( material.index0AttributeName != null ) {
            GLES20.glBindAttribLocation( program, 0, material.index0AttributeName );
        } else if ( parameters.morphTargets == true ) {
            // programs with morphTargets displace position out of attribute 0
            GLES20.glBindAttribLocation( program, 0, "position" );
        }

        GLES20.glLinkProgram( program );

        final int[] link = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, link, 0);
        if(link[0] == 0){
            System.err.println("link failed\n");
        }

        // clean up
        GLES20.glDeleteShader( glVertexShader );
        GLES20.glDeleteShader( glFragmentShader );
    }

    public GLUniforms GetUniforms(){
        if ( cachedUniforms == null ) {
            cachedUniforms = new GLUniforms( program, renderer );
        }

        return cachedUniforms;
    }

    public HashMap<String, Integer> GetAttributes() {
        if ( cachedAttributes == null ) {
            cachedAttributes = FetchAttributeLocations( program );
        }

        return cachedAttributes;
    }

    public void Destroy(){
        GLES20.glDeleteProgram(program);
        this.program = -1;
    }

    private ArrayList<String> GetEncodingComponents(int encoding){
        ArrayList<String> list = new ArrayList<>();
        switch (encoding){
            case LinearEncoding:
                list.add("Linear");
                list.add("(value)");
                break;
            case sRGBEncoding:
                list.add("sRGB");
                list.add("(value)");
                break;
            case RGBEEncoding:
                list.add("RGBE");
                list.add("(value)");
                break;
            case RGBM7Encoding:
                list.add("RGBM");
                list.add("( value, 7.0 )");
                break;
            case RGBM16Encoding:
                list.add("RGBM");
                list.add("( value, 16.0 )");
                break;
            case RGBDEncoding:
                list.add("RGBD");
                list.add("( value, 256.0 )");
                break;
            case GammaEncoding:
                list.add("Gamma");
                list.add("( value, float( GAMMA_FACTOR ) )");
                break;
        }
        return list;
    }

    private String GetTexelDecodingFunction(String functionName, int encoding){
        ArrayList components = GetEncodingComponents( encoding );
        return "vec4 " + functionName + "( vec4 value ) { return " + components.get(0) + "ToLinear" + components.get(1) + "; }";
    }

    private String GetTexelEncodingFunction(String functionName, int encoding) {
        ArrayList components = GetEncodingComponents( encoding );
        return "vec4 " + functionName + "( vec4 value ) { return LinearTo" + components.get(0) + components.get(1) + "; }";
    }

    private String GetToneMappingFunction(String functionName, int toneMapping){
        String toneMappingName = "";

        switch ( toneMapping ) {
            case LinearToneMapping:
                toneMappingName = "Linear";
                break;
            case ReinhardToneMapping:
                toneMappingName = "Reinhard";
                break;
            case Uncharted2ToneMapping:
                toneMappingName = "Uncharted2";
                break;
            case CineonToneMapping:
                toneMappingName = "OptimizedCineon";
                break;
            case ACESFilmicToneMapping:
                toneMappingName = "ACESFilmic";
                break;
            default:
                break;
        }

        return "vec3 " + functionName + "( vec3 color ) { return " + toneMappingName + "ToneMapping( color ); }";
    }

    private String GenerateDefines(HashMap<String, Boolean> defines){
        if(defines == null){
            return "";
        }

        ArrayList<String> chunks = new ArrayList();

        for (Object o : defines.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            String name = (String) pair.getKey();
            boolean value = (boolean) pair.getValue();
            if (!value) continue;
            chunks.add("#define " + name + " " + value);
        }

        String result = String.join("\n", chunks); // min sdk version: 26
        return result;
    }

    private HashMap<String, Integer> FetchAttributeLocations(int program){
        HashMap<String,Integer> attributes = new HashMap<>();
        IntBuffer buf = IntBuffer.allocate(1);
        GLES20.glGetProgramiv(program, GLES20.GL_ACTIVE_ATTRIBUTES, buf);
        int n = buf.get(0);

        for ( int i = 0; i < n; i ++ ) {
            byte[] nameBuf = new byte[20];
            int[] size = new int[1];
            int[] type = new int[1];
            GLES20.glGetActiveAttrib(program, i, 20, null, 0, size, 0, type, 0, nameBuf, 0);
            byte[] trimed = GLUtils.TrimZero(nameBuf);
            String name = new String(trimed);
            attributes.put(name, GLES20.glGetAttribLocation(program, name));
        }

        return attributes;
    }

    private boolean FilterEmptyline(String string){
        return !string.equals("");
    }

    private String ReplaceLightNums(String string, Parameters parameters){
        String dirlights = "NUM_DIR_LIGHTS";
        string = string.replaceAll(dirlights, String.valueOf(parameters.numDirLights));
        String spotlights = "NUM_SPOT_LIGHTS";
        string = string.replaceAll(spotlights, String.valueOf(parameters.numSpotLights));
        String rectArealights = "NUM_RECT_AREA_LIGHTS";
        string = string.replaceAll(rectArealights, String.valueOf(parameters.numRectAreaLights));
        String pointlights = "NUM_POINT_LIGHTS";
        string = string.replaceAll(pointlights, String.valueOf(parameters.numPointLights));
        String hemilights = "NUM_HEMI_LIGHTS";
        string = string.replaceAll(hemilights, String.valueOf(parameters.numHemiLights));
        return string;
    }

    private String ReplaceClippingPlaneNums(String string, Parameters parameters){
        String clippingPlanes = "NUM_CLIPPING_PLANES";
        string = string.replaceAll(clippingPlanes, String.valueOf(parameters.numClippingPlanes));
        String union_clipping_planes = "UNION_CLIPPING_PLANES";
        string = string.replaceAll(union_clipping_planes, String.valueOf(parameters.numClipIntersection));
        return string;
    }

    public static String ParseIncludes(String string){

        String regex = "[\t]*#include[\\s]*<([\\w\\d]+)>";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(string);
        while (m.find()){
            String s =  m.group();
            int start = s.indexOf("<");
            int end = s.indexOf(">");
            String chunkName = s.substring(start+1, end);
            String chunkCode = chunks.Get(chunkName);
            string = string.replace(s, chunkCode);
            m.reset(string);
        }

        return string;
    }

    private String UnrollLoops(String string){
        String regex = "#pragma unroll_loop[\\s]+?for \\( int i \\= (\\d+)\\; i < (\\d+)\\; i \\+\\+ \\) \\{([\\s\\S]+?)(?=\\})\\}";
        Pattern p = Pattern.compile(regex);
        Matcher  matcher = p.matcher(string);

        String cond = "i \\= (\\d+); i < (\\d+);";
        Pattern patternCond = Pattern.compile(cond);

        String bracket = "\\{[\\w\\W]+\\}";
        Pattern patternBracket = Pattern.compile(bracket);

        while(matcher.find()){
            String newContent = "";
            String group = matcher.group();

            Matcher matcherCond = patternCond.matcher(group);
            if(matcherCond.find()){
                String condition = matcherCond.group();

                int equal = condition.indexOf("=");
                int semi1 = condition.indexOf(";");
                String start = condition.substring(equal+1, semi1).trim();
                int startIndex = Integer.parseInt(start);

                int less = condition.indexOf("<");
                int semi2 = condition.lastIndexOf(";");
                String end = condition.substring(less+1, semi2).trim();
                int endIndex = Integer.parseInt(end);

                Matcher matcherBracket = patternBracket.matcher(group);
                if(matcherBracket.find()){
                    String bracketContent = matcherBracket.group();
                    for(int i = startIndex; i < endIndex; ++i){
                        String content = bracketContent.substring(1, bracketContent.length() - 1);
                        newContent += content.replaceAll("\\[ i \\]", "\\[" + i + "\\]");
                    }
                }

                string = matcher.replaceFirst(newContent);
            }
            matcher.reset(string);
        }

        return string;
    }




}
