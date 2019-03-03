package three.renderers.shaders.ShaderChunks;

public class LightsFragmentBegin {
    public static final String code = "GeometricContext geometry;\n" +
            "\n" +
            "geometry.position = - vViewPosition;\n" +
            "geometry.normal = normal;\n" +
            "geometry.viewDir = normalize( vViewPosition );\n" +
            "\n" +
            "IncidentLight directLight;\n" +
            "\n" +
            "#if ( NUM_POINT_LIGHTS > 0 ) && defined( RE_Direct )\n" +
            "\n" +
            "\tPointLight pointLight;\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_POINT_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tpointLight = pointLights[ i ];\n" +
            "\n" +
            "\t\tgetPointDirectLightIrradiance( pointLight, geometry, directLight );\n" +
            "\n" +
            "\t\t#ifdef USE_SHADOWMAP\n" +
            "\t\tdirectLight.color *= all( bvec2( pointLight.shadow, directLight.visible ) ) ? getPointShadow( pointShadowMap[ i ], pointLight.shadowMapSize, pointLight.shadowBias, pointLight.shadowRadius, vPointShadowCoord[ i ], pointLight.shadowCameraNear, pointLight.shadowCameraFar ) : 1.0;\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\tRE_Direct( directLight, geometry, material, reflectedLight );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if ( NUM_SPOT_LIGHTS > 0 ) && defined( RE_Direct )\n" +
            "\n" +
            "\tSpotLight spotLight;\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_SPOT_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tspotLight = spotLights[ i ];\n" +
            "\n" +
            "\t\tgetSpotDirectLightIrradiance( spotLight, geometry, directLight );\n" +
            "\n" +
            "\t\t#ifdef USE_SHADOWMAP\n" +
            "\t\tdirectLight.color *= all( bvec2( spotLight.shadow, directLight.visible ) ) ? getShadow( spotShadowMap[ i ], spotLight.shadowMapSize, spotLight.shadowBias, spotLight.shadowRadius, vSpotShadowCoord[ i ] ) : 1.0;\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\tRE_Direct( directLight, geometry, material, reflectedLight );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if ( NUM_DIR_LIGHTS > 0 ) && defined( RE_Direct )\n" +
            "\n" +
            "\tDirectionalLight directionalLight;\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_DIR_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tdirectionalLight = directionalLights[ i ];\n" +
            "\n" +
            "\t\tgetDirectionalDirectLightIrradiance( directionalLight, geometry, directLight );\n" +
            "\n" +
            "\t\t#ifdef USE_SHADOWMAP\n" +
            "\t\tdirectLight.color *= all( bvec2( directionalLight.shadow, directLight.visible ) ) ? getShadow( directionalShadowMap[ i ], directionalLight.shadowMapSize, directionalLight.shadowBias, directionalLight.shadowRadius, vDirectionalShadowCoord[ i ] ) : 1.0;\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\tRE_Direct( directLight, geometry, material, reflectedLight );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if ( NUM_RECT_AREA_LIGHTS > 0 ) && defined( RE_Direct_RectArea )\n" +
            "\n" +
            "\tRectAreaLight rectAreaLight;\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_RECT_AREA_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\trectAreaLight = rectAreaLights[ i ];\n" +
            "\t\tRE_Direct_RectArea( rectAreaLight, geometry, material, reflectedLight );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if defined( RE_IndirectDiffuse )\n" +
            "\n" +
            "\tvec3 irradiance = getAmbientLightIrradiance( ambientLightColor );\n" +
            "\n" +
            "\t#if ( NUM_HEMI_LIGHTS > 0 )\n" +
            "\n" +
            "\t\t#pragma unroll_loop\n" +
            "\t\tfor ( int i = 0; i < NUM_HEMI_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\t\tirradiance += getHemisphereLightIrradiance( hemisphereLights[ i ], geometry );\n" +
            "\n" +
            "\t\t}\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if defined( RE_IndirectSpecular )\n" +
            "\n" +
            "\tvec3 radiance = vec3( 0.0 );\n" +
            "\tvec3 clearCoatRadiance = vec3( 0.0 );\n" +
            "\n" +
            "#endif\n";
}
