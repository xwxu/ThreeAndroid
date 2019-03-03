package three.renderers.shaders.ShaderChunks;

public class LightsLambertVertex {
    public static final String code =
            "vec3 diffuse = vec3( 1.0 );\n" +
            "\n" +
            "GeometricContext geometry;\n" +
            "geometry.position = mvPosition.xyz;\n" +
            "geometry.normal = normalize( transformedNormal );\n" +
            "geometry.viewDir = normalize( -mvPosition.xyz );\n" +
            "\n" +
            "GeometricContext backGeometry;\n" +
            "backGeometry.position = geometry.position;\n" +
            "backGeometry.normal = -geometry.normal;\n" +
            "backGeometry.viewDir = geometry.viewDir;\n" +
            "\n" +
            "vLightFront = vec3( 0.0 );\n" +
            "\n" +
            "#ifdef DOUBLE_SIDED\n" +
            "\tvLightBack = vec3( 0.0 );\n" +
            "#endif\n" +
            "\n" +
            "IncidentLight directLight;\n" +
            "float dotNL;\n" +
            "vec3 directLightColor_Diffuse;\n" +
            "\n" +
            "#if NUM_POINT_LIGHTS > 0\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_POINT_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tgetPointDirectLightIrradiance( pointLights[ i ], geometry, directLight );\n" +
            "\n" +
            "\t\tdotNL = dot( geometry.normal, directLight.direction );\n" +
            "\t\tdirectLightColor_Diffuse = PI * directLight.color;\n" +
            "\n" +
            "\t\tvLightFront += saturate( dotNL ) * directLightColor_Diffuse;\n" +
            "\n" +
            "\t\t#ifdef DOUBLE_SIDED\n" +
            "\n" +
            "\t\t\tvLightBack += saturate( -dotNL ) * directLightColor_Diffuse;\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if NUM_SPOT_LIGHTS > 0\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_SPOT_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tgetSpotDirectLightIrradiance( spotLights[ i ], geometry, directLight );\n" +
            "\n" +
            "\t\tdotNL = dot( geometry.normal, directLight.direction );\n" +
            "\t\tdirectLightColor_Diffuse = PI * directLight.color;\n" +
            "\n" +
            "\t\tvLightFront += saturate( dotNL ) * directLightColor_Diffuse;\n" +
            "\n" +
            "\t\t#ifdef DOUBLE_SIDED\n" +
            "\n" +
            "\t\t\tvLightBack += saturate( -dotNL ) * directLightColor_Diffuse;\n" +
            "\n" +
            "\t\t#endif\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "/*\n" +
            "#if NUM_RECT_AREA_LIGHTS > 0\n" +
            "\n" +
            "\tfor ( int i = 0; i < NUM_RECT_AREA_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\t// TODO (abelnation): implement\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "*/\n" +
            "\n" +
            "#if NUM_DIR_LIGHTS > 0\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_DIR_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tgetDirectionalDirectLightIrradiance( directionalLights[ i ], geometry, directLight );\n" +
            "\n" +
            "\t\tdotNL = dot( geometry.normal, directLight.direction );\n" +
            "\t\tdirectLightColor_Diffuse = PI * directLight.color;\n" +
            "\n" +
            "\t\tvLightFront += saturate( dotNL ) * directLightColor_Diffuse;\n" +
            "\n" +
            "\t\t#ifdef DOUBLE_SIDED\n" +
            "\n" +
            "\t\t\tvLightBack += saturate( -dotNL ) * directLightColor_Diffuse;\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if NUM_HEMI_LIGHTS > 0\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_HEMI_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tvLightFront += getHemisphereLightIrradiance( hemisphereLights[ i ], geometry );\n" +
            "\n" +
            "\t\t#ifdef DOUBLE_SIDED\n" +
            "\n" +
            "\t\t\tvLightBack += getHemisphereLightIrradiance( hemisphereLights[ i ], backGeometry );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n";
}
