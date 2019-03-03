package three.renderers.shaders.ShaderChunks;

public class LightsParsBegin {
    public static final String code =
            "uniform vec3 ambientLightColor;\n" +
            "\n" +
            "vec3 getAmbientLightIrradiance( const in vec3 ambientLightColor ) {\n" +
            "\n" +
            "\tvec3 irradiance = ambientLightColor;\n" +
            "\n" +
            "\t#ifndef PHYSICALLY_CORRECT_LIGHTS\n" +
            "\n" +
            "\t\tirradiance *= PI;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\treturn irradiance;\n" +
            "\n" +
            "}\n" +
            "\n" +
            "#if NUM_DIR_LIGHTS > 0\n" +
            "\n" +
            "\tstruct DirectionalLight {\n" +
            "\t\tvec3 direction;\n" +
            "\t\tvec3 color;\n" +
            "\n" +
            "\t\tint shadow;\n" +
            "\t\tfloat shadowBias;\n" +
            "\t\tfloat shadowRadius;\n" +
            "\t\tvec2 shadowMapSize;\n" +
            "\t};\n" +
            "\n" +
            "\tuniform DirectionalLight directionalLights[ NUM_DIR_LIGHTS ];\n" +
            "\n" +
            "\tvoid getDirectionalDirectLightIrradiance( const in DirectionalLight directionalLight, const in GeometricContext geometry, out IncidentLight directLight ) {\n" +
            "\n" +
            "\t\tdirectLight.color = directionalLight.color;\n" +
            "\t\tdirectLight.direction = directionalLight.direction;\n" +
            "\t\tdirectLight.visible = true;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "\n" +
            "#if NUM_POINT_LIGHTS > 0\n" +
            "\n" +
            "\tstruct PointLight {\n" +
            "\t\tvec3 position;\n" +
            "\t\tvec3 color;\n" +
            "\t\tfloat distance;\n" +
            "\t\tfloat decay;\n" +
            "\n" +
            "\t\tint shadow;\n" +
            "\t\tfloat shadowBias;\n" +
            "\t\tfloat shadowRadius;\n" +
            "\t\tvec2 shadowMapSize;\n" +
            "\t\tfloat shadowCameraNear;\n" +
            "\t\tfloat shadowCameraFar;\n" +
            "\t};\n" +
            "\n" +
            "\tuniform PointLight pointLights[ NUM_POINT_LIGHTS ];\n" +
            "\n" +
            "\t// directLight is an out parameter as having it as a return value caused compiler errors on some devices\n" +
            "\tvoid getPointDirectLightIrradiance( const in PointLight pointLight, const in GeometricContext geometry, out IncidentLight directLight ) {\n" +
            "\n" +
            "\t\tvec3 lVector = pointLight.position - geometry.position;\n" +
            "\t\tdirectLight.direction = normalize( lVector );\n" +
            "\n" +
            "\t\tfloat lightDistance = length( lVector );\n" +
            "\n" +
            "\t\tdirectLight.color = pointLight.color;\n" +
            "\t\tdirectLight.color *= punctualLightIntensityToIrradianceFactor( lightDistance, pointLight.distance, pointLight.decay );\n" +
            "\t\tdirectLight.visible = ( directLight.color != vec3( 0.0 ) );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "\n" +
            "#if NUM_SPOT_LIGHTS > 0\n" +
            "\n" +
            "\tstruct SpotLight {\n" +
            "\t\tvec3 position;\n" +
            "\t\tvec3 direction;\n" +
            "\t\tvec3 color;\n" +
            "\t\tfloat distance;\n" +
            "\t\tfloat decay;\n" +
            "\t\tfloat coneCos;\n" +
            "\t\tfloat penumbraCos;\n" +
            "\n" +
            "\t\tint shadow;\n" +
            "\t\tfloat shadowBias;\n" +
            "\t\tfloat shadowRadius;\n" +
            "\t\tvec2 shadowMapSize;\n" +
            "\t};\n" +
            "\n" +
            "\tuniform SpotLight spotLights[ NUM_SPOT_LIGHTS ];\n" +
            "\n" +
            "\t// directLight is an out parameter as having it as a return value caused compiler errors on some devices\n" +
            "\tvoid getSpotDirectLightIrradiance( const in SpotLight spotLight, const in GeometricContext geometry, out IncidentLight directLight  ) {\n" +
            "\n" +
            "\t\tvec3 lVector = spotLight.position - geometry.position;\n" +
            "\t\tdirectLight.direction = normalize( lVector );\n" +
            "\n" +
            "\t\tfloat lightDistance = length( lVector );\n" +
            "\t\tfloat angleCos = dot( directLight.direction, spotLight.direction );\n" +
            "\n" +
            "\t\tif ( angleCos > spotLight.coneCos ) {\n" +
            "\n" +
            "\t\t\tfloat spotEffect = smoothstep( spotLight.coneCos, spotLight.penumbraCos, angleCos );\n" +
            "\n" +
            "\t\t\tdirectLight.color = spotLight.color;\n" +
            "\t\t\tdirectLight.color *= spotEffect * punctualLightIntensityToIrradianceFactor( lightDistance, spotLight.distance, spotLight.decay );\n" +
            "\t\t\tdirectLight.visible = true;\n" +
            "\n" +
            "\t\t} else {\n" +
            "\n" +
            "\t\t\tdirectLight.color = vec3( 0.0 );\n" +
            "\t\t\tdirectLight.visible = false;\n" +
            "\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "\n" +
            "#if NUM_RECT_AREA_LIGHTS > 0\n" +
            "\n" +
            "\tstruct RectAreaLight {\n" +
            "\t\tvec3 color;\n" +
            "\t\tvec3 position;\n" +
            "\t\tvec3 halfWidth;\n" +
            "\t\tvec3 halfHeight;\n" +
            "\t};\n" +
            "\n" +
            "\t// Pre-computed values of LinearTransformedCosine approximation of BRDF\n" +
            "\t// BRDF approximation Texture is 64x64\n" +
            "\tuniform sampler2D ltc_1; // RGBA Float\n" +
            "\tuniform sampler2D ltc_2; // RGBA Float\n" +
            "\n" +
            "\tuniform RectAreaLight rectAreaLights[ NUM_RECT_AREA_LIGHTS ];\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "\n" +
            "#if NUM_HEMI_LIGHTS > 0\n" +
            "\n" +
            "\tstruct HemisphereLight {\n" +
            "\t\tvec3 direction;\n" +
            "\t\tvec3 skyColor;\n" +
            "\t\tvec3 groundColor;\n" +
            "\t};\n" +
            "\n" +
            "\tuniform HemisphereLight hemisphereLights[ NUM_HEMI_LIGHTS ];\n" +
            "\n" +
            "\tvec3 getHemisphereLightIrradiance( const in HemisphereLight hemiLight, const in GeometricContext geometry ) {\n" +
            "\n" +
            "\t\tfloat dotNL = dot( geometry.normal, hemiLight.direction );\n" +
            "\t\tfloat hemiDiffuseWeight = 0.5 * dotNL + 0.5;\n" +
            "\n" +
            "\t\tvec3 irradiance = mix( hemiLight.groundColor, hemiLight.skyColor, hemiDiffuseWeight );\n" +
            "\n" +
            "\t\t#ifndef PHYSICALLY_CORRECT_LIGHTS\n" +
            "\n" +
            "\t\t\tirradiance *= PI;\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\treturn irradiance;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n";
}
