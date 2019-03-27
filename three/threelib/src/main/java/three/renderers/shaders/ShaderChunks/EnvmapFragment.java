package three.renderers.shaders.ShaderChunks;

public class EnvmapFragment {
    public static final String code =
            "#ifdef USE_ENVMAP\n" +
            "\n" +
            "\t#if defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG )\n" +
            "\n" +
            "\t\tvec3 cameraToVertex = normalize( vWorldPosition - cameraPosition );\n" +
            "\n" +
            "\t\t// Transforming Normal Vectors with the inverse Transformation\n" +
            "\t\tvec3 worldNormal = inverseTransformDirection( normal, viewMatrix );\n" +
            "\n" +
            "\t\t#ifdef ENVMAP_MODE_REFLECTION\n" +
            "\n" +
            "\t\t\tvec3 reflectVec = reflect( cameraToVertex, worldNormal );\n" +
            "\n" +
            "\t\t#else\n" +
            "\n" +
            "\t\t\tvec3 reflectVec = refract( cameraToVertex, worldNormal, refractionRatio );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tvec3 reflectVec = vReflect;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#ifdef ENVMAP_TYPE_CUBE\n" +
            "\n" +
            "\t\tvec4 envColor = textureCube( envMap, vec3( flipEnvMap * reflectVec.x, reflectVec.yz ) );\n" +
            "\n" +
            "\t#elif defined( ENVMAP_TYPE_EQUIREC )\n" +
            "\n" +
            "\t\tvec2 sampleUV;\n" +
            "\n" +
            "\t\treflectVec = normalize( reflectVec );\n" +
            "\n" +
            "\t\tsampleUV.y = asin( clamp( reflectVec.y, - 1.0, 1.0 ) ) * RECIPROCAL_PI + 0.5;\n" +
            "\n" +
            "\t\tsampleUV.x = atan( reflectVec.z, reflectVec.x ) * RECIPROCAL_PI2 + 0.5;\n" +
            "\n" +
            "\t\tvec4 envColor = texture2D( envMap, sampleUV );\n" +
            "\n" +
            "\t#elif defined( ENVMAP_TYPE_SPHERE )\n" +
            "\n" +
            "\t\treflectVec = normalize( reflectVec );\n" +
            "\n" +
            "\t\tvec3 reflectView = normalize( ( viewMatrix * vec4( reflectVec, 0.0 ) ).xyz + vec3( 0.0, 0.0, 1.0 ) );\n" +
            "\n" +
            "\t\tvec4 envColor = texture2D( envMap, reflectView.xy * 0.5 + 0.5 );\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tvec4 envColor = vec4( 0.0 );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\tenvColor = envMapTexelToLinear( envColor );\n" +
            "\n" +
            "\t#ifdef ENVMAP_BLENDING_MULTIPLY\n" +
            "\n" +
            "\t\toutgoingLight = mix( outgoingLight, outgoingLight * envColor.xyz, specularStrength * reflectivity );\n" +
            "\n" +
            "\t#elif defined( ENVMAP_BLENDING_MIX )\n" +
            "\n" +
            "\t\toutgoingLight = mix( outgoingLight, envColor.xyz, specularStrength * reflectivity );\n" +
            "\n" +
            "\t#elif defined( ENVMAP_BLENDING_ADD )\n" +
            "\n" +
            "\t\toutgoingLight += envColor.xyz * specularStrength * reflectivity;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
