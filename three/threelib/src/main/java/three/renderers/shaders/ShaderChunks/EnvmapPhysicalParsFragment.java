package three.renderers.shaders.ShaderChunks;

public class EnvmapPhysicalParsFragment {
    public static final String code =
            "#if defined( USE_ENVMAP ) && defined( PHYSICAL )\n" +
            "\n" +
            "\tvec3 getLightProbeIndirectIrradiance( /*const in SpecularLightProbe specularLightProbe,*/ const in GeometricContext geometry, const in int maxMIPLevel ) {\n" +
            "\n" +
            "\t\tvec3 worldNormal = inverseTransformDirection( geometry.normal, viewMatrix );\n" +
            "\n" +
            "\t\t#ifdef ENVMAP_TYPE_CUBE\n" +
            "\n" +
            "\t\t\tvec3 queryVec = vec3( flipEnvMap * worldNormal.x, worldNormal.yz );\n" +
            "\n" +
            "\t\t\t// TODO: replace with properly filtered cubemaps and access the irradiance LOD level, be it the last LOD level\n" +
            "\t\t\t// of a specular cubemap, or just the default level of a specially created irradiance cubemap.\n" +
            "\n" +
            "\t\t\t#ifdef TEXTURE_LOD_EXT\n" +
            "\n" +
            "\t\t\t\tvec4 envMapColor = textureCubeLodEXT( envMap, queryVec, float( maxMIPLevel ) );\n" +
            "\n" +
            "\t\t\t#else\n" +
            "\n" +
            "\t\t\t\t// force the bias high to get the last LOD level as it is the most blurred.\n" +
            "\t\t\t\tvec4 envMapColor = textureCube( envMap, queryVec, float( maxMIPLevel ) );\n" +
            "\n" +
            "\t\t\t#endif\n" +
            "\n" +
            "\t\t\tenvMapColor.rgb = envMapTexelToLinear( envMapColor ).rgb;\n" +
            "\n" +
            "\t\t#elif defined( ENVMAP_TYPE_CUBE_UV )\n" +
            "\n" +
            "\t\t\tvec3 queryVec = vec3( flipEnvMap * worldNormal.x, worldNormal.yz );\n" +
            "\t\t\tvec4 envMapColor = textureCubeUV( envMap, queryVec, 1.0 );\n" +
            "\n" +
            "\t\t#else\n" +
            "\n" +
            "\t\t\tvec4 envMapColor = vec4( 0.0 );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\treturn PI * envMapColor.rgb * envMapIntensity;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\t// taken from here: http://casual-effects.blogspot.ca/2011/08/plausible-environment-lighting-in-two.html\n" +
            "\tfloat getSpecularMIPLevel( const in float blinnShininessExponent, const in int maxMIPLevel ) {\n" +
            "\n" +
            "\t\t//float envMapWidth = pow( 2.0, maxMIPLevelScalar );\n" +
            "\t\t//float desiredMIPLevel = log2( envMapWidth * sqrt( 3.0 ) ) - 0.5 * log2( pow2( blinnShininessExponent ) + 1.0 );\n" +
            "\n" +
            "\t\tfloat maxMIPLevelScalar = float( maxMIPLevel );\n" +
            "\t\tfloat desiredMIPLevel = maxMIPLevelScalar + 0.79248 - 0.5 * log2( pow2( blinnShininessExponent ) + 1.0 );\n" +
            "\n" +
            "\t\t// clamp to allowable LOD ranges.\n" +
            "\t\treturn clamp( desiredMIPLevel, 0.0, maxMIPLevelScalar );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\tvec3 getLightProbeIndirectRadiance( /*const in SpecularLightProbe specularLightProbe,*/ const in GeometricContext geometry, const in float blinnShininessExponent, const in int maxMIPLevel ) {\n" +
            "\n" +
            "\t\t#ifdef ENVMAP_MODE_REFLECTION\n" +
            "\n" +
            "\t\t\tvec3 reflectVec = reflect( -geometry.viewDir, geometry.normal );\n" +
            "\n" +
            "\t\t#else\n" +
            "\n" +
            "\t\t\tvec3 reflectVec = refract( -geometry.viewDir, geometry.normal, refractionRatio );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\treflectVec = inverseTransformDirection( reflectVec, viewMatrix );\n" +
            "\n" +
            "\t\tfloat specularMIPLevel = getSpecularMIPLevel( blinnShininessExponent, maxMIPLevel );\n" +
            "\n" +
            "\t\t#ifdef ENVMAP_TYPE_CUBE\n" +
            "\n" +
            "\t\t\tvec3 queryReflectVec = vec3( flipEnvMap * reflectVec.x, reflectVec.yz );\n" +
            "\n" +
            "\t\t\t#ifdef TEXTURE_LOD_EXT\n" +
            "\n" +
            "\t\t\t\tvec4 envMapColor = textureCubeLodEXT( envMap, queryReflectVec, specularMIPLevel );\n" +
            "\n" +
            "\t\t\t#else\n" +
            "\n" +
            "\t\t\t\tvec4 envMapColor = textureCube( envMap, queryReflectVec, specularMIPLevel );\n" +
            "\n" +
            "\t\t\t#endif\n" +
            "\n" +
            "\t\t\tenvMapColor.rgb = envMapTexelToLinear( envMapColor ).rgb;\n" +
            "\n" +
            "\t\t#elif defined( ENVMAP_TYPE_CUBE_UV )\n" +
            "\n" +
            "\t\t\tvec3 queryReflectVec = vec3( flipEnvMap * reflectVec.x, reflectVec.yz );\n" +
            "\t\t\tvec4 envMapColor = textureCubeUV( envMap, queryReflectVec, BlinnExponentToGGXRoughness(blinnShininessExponent ));\n" +
            "\n" +
            "\t\t#elif defined( ENVMAP_TYPE_EQUIREC )\n" +
            "\n" +
            "\t\t\tvec2 sampleUV;\n" +
            "\t\t\tsampleUV.y = asin( clamp( reflectVec.y, - 1.0, 1.0 ) ) * RECIPROCAL_PI + 0.5;\n" +
            "\t\t\tsampleUV.x = atan( reflectVec.z, reflectVec.x ) * RECIPROCAL_PI2 + 0.5;\n" +
            "\n" +
            "\t\t\t#ifdef TEXTURE_LOD_EXT\n" +
            "\n" +
            "\t\t\t\tvec4 envMapColor = texture2DLodEXT( envMap, sampleUV, specularMIPLevel );\n" +
            "\n" +
            "\t\t\t#else\n" +
            "\n" +
            "\t\t\t\tvec4 envMapColor = texture2D( envMap, sampleUV, specularMIPLevel );\n" +
            "\n" +
            "\t\t\t#endif\n" +
            "\n" +
            "\t\t\tenvMapColor.rgb = envMapTexelToLinear( envMapColor ).rgb;\n" +
            "\n" +
            "\t\t#elif defined( ENVMAP_TYPE_SPHERE )\n" +
            "\n" +
            "\t\t\tvec3 reflectView = normalize( ( viewMatrix * vec4( reflectVec, 0.0 ) ).xyz + vec3( 0.0,0.0,1.0 ) );\n" +
            "\n" +
            "\t\t\t#ifdef TEXTURE_LOD_EXT\n" +
            "\n" +
            "\t\t\t\tvec4 envMapColor = texture2DLodEXT( envMap, reflectView.xy * 0.5 + 0.5, specularMIPLevel );\n" +
            "\n" +
            "\t\t\t#else\n" +
            "\n" +
            "\t\t\t\tvec4 envMapColor = texture2D( envMap, reflectView.xy * 0.5 + 0.5, specularMIPLevel );\n" +
            "\n" +
            "\t\t\t#endif\n" +
            "\n" +
            "\t\t\tenvMapColor.rgb = envMapTexelToLinear( envMapColor ).rgb;\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\treturn envMapColor.rgb * envMapIntensity;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n";
}
