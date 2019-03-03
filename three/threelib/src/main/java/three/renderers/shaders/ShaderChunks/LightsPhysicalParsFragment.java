package three.renderers.shaders.ShaderChunks;

public class LightsPhysicalParsFragment {
    public static final String code =
            "struct PhysicalMaterial {\n" +
            "\n" +
            "\tvec3\tdiffuseColor;\n" +
            "\tfloat\tspecularRoughness;\n" +
            "\tvec3\tspecularColor;\n" +
            "\n" +
            "\t#ifndef STANDARD\n" +
            "\t\tfloat clearCoat;\n" +
            "\t\tfloat clearCoatRoughness;\n" +
            "\t#endif\n" +
            "\n" +
            "};\n" +
            "\n" +
            "#define MAXIMUM_SPECULAR_COEFFICIENT 0.16\n" +
            "#define DEFAULT_SPECULAR_COEFFICIENT 0.04\n" +
            "\n" +
            "// Clear coat directional hemishperical reflectance (this approximation should be improved)\n" +
            "float clearCoatDHRApprox( const in float roughness, const in float dotNL ) {\n" +
            "\n" +
            "\treturn DEFAULT_SPECULAR_COEFFICIENT + ( 1.0 - DEFAULT_SPECULAR_COEFFICIENT ) * ( pow( 1.0 - dotNL, 5.0 ) * pow( 1.0 - roughness, 2.0 ) );\n" +
            "\n" +
            "}\n" +
            "\n" +
            "#if NUM_RECT_AREA_LIGHTS > 0\n" +
            "\n" +
            "\tvoid RE_Direct_RectArea_Physical( const in RectAreaLight rectAreaLight, const in GeometricContext geometry, const in PhysicalMaterial material, inout ReflectedLight reflectedLight ) {\n" +
            "\n" +
            "\t\tvec3 normal = geometry.normal;\n" +
            "\t\tvec3 viewDir = geometry.viewDir;\n" +
            "\t\tvec3 position = geometry.position;\n" +
            "\t\tvec3 lightPos = rectAreaLight.position;\n" +
            "\t\tvec3 halfWidth = rectAreaLight.halfWidth;\n" +
            "\t\tvec3 halfHeight = rectAreaLight.halfHeight;\n" +
            "\t\tvec3 lightColor = rectAreaLight.color;\n" +
            "\t\tfloat roughness = material.specularRoughness;\n" +
            "\n" +
            "\t\tvec3 rectCoords[ 4 ];\n" +
            "\t\trectCoords[ 0 ] = lightPos + halfWidth - halfHeight; // counterclockwise; light shines in local neg z direction\n" +
            "\t\trectCoords[ 1 ] = lightPos - halfWidth - halfHeight;\n" +
            "\t\trectCoords[ 2 ] = lightPos - halfWidth + halfHeight;\n" +
            "\t\trectCoords[ 3 ] = lightPos + halfWidth + halfHeight;\n" +
            "\n" +
            "\t\tvec2 uv = LTC_Uv( normal, viewDir, roughness );\n" +
            "\n" +
            "\t\tvec4 t1 = texture2D( ltc_1, uv );\n" +
            "\t\tvec4 t2 = texture2D( ltc_2, uv );\n" +
            "\n" +
            "\t\tmat3 mInv = mat3(\n" +
            "\t\t\tvec3( t1.x, 0, t1.y ),\n" +
            "\t\t\tvec3(    0, 1,    0 ),\n" +
            "\t\t\tvec3( t1.z, 0, t1.w )\n" +
            "\t\t);\n" +
            "\n" +
            "\t\t// LTC Fresnel Approximation by Stephen Hill\n" +
            "\t\t// http://blog.selfshadow.com/publications/s2016-advances/s2016_ltc_fresnel.pdf\n" +
            "\t\tvec3 fresnel = ( material.specularColor * t2.x + ( vec3( 1.0 ) - material.specularColor ) * t2.y );\n" +
            "\n" +
            "\t\treflectedLight.directSpecular += lightColor * fresnel * LTC_Evaluate( normal, viewDir, position, mInv, rectCoords );\n" +
            "\n" +
            "\t\treflectedLight.directDiffuse += lightColor * material.diffuseColor * LTC_Evaluate( normal, viewDir, position, mat3( 1.0 ), rectCoords );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "void RE_Direct_Physical( const in IncidentLight directLight, const in GeometricContext geometry, const in PhysicalMaterial material, inout ReflectedLight reflectedLight ) {\n" +
            "\n" +
            "\tfloat dotNL = saturate( dot( geometry.normal, directLight.direction ) );\n" +
            "\n" +
            "\tvec3 irradiance = dotNL * directLight.color;\n" +
            "\n" +
            "\t#ifndef PHYSICALLY_CORRECT_LIGHTS\n" +
            "\n" +
            "\t\tirradiance *= PI; // punctual light\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#ifndef STANDARD\n" +
            "\t\tfloat clearCoatDHR = material.clearCoat * clearCoatDHRApprox( material.clearCoatRoughness, dotNL );\n" +
            "\t#else\n" +
            "\t\tfloat clearCoatDHR = 0.0;\n" +
            "\t#endif\n" +
            "\n" +
            "\treflectedLight.directSpecular += ( 1.0 - clearCoatDHR ) * irradiance * BRDF_Specular_GGX( directLight, geometry, material.specularColor, material.specularRoughness );\n" +
            "\n" +
            "\treflectedLight.directDiffuse += ( 1.0 - clearCoatDHR ) * irradiance * BRDF_Diffuse_Lambert( material.diffuseColor );\n" +
            "\n" +
            "\t#ifndef STANDARD\n" +
            "\n" +
            "\t\treflectedLight.directSpecular += irradiance * material.clearCoat * BRDF_Specular_GGX( directLight, geometry, vec3( DEFAULT_SPECULAR_COEFFICIENT ), material.clearCoatRoughness );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "}\n" +
            "\n" +
            "void RE_IndirectDiffuse_Physical( const in vec3 irradiance, const in GeometricContext geometry, const in PhysicalMaterial material, inout ReflectedLight reflectedLight ) {\n" +
            "\n" +
            "\treflectedLight.indirectDiffuse += irradiance * BRDF_Diffuse_Lambert( material.diffuseColor );\n" +
            "\n" +
            "}\n" +
            "\n" +
            "void RE_IndirectSpecular_Physical( const in vec3 radiance, const in vec3 clearCoatRadiance, const in GeometricContext geometry, const in PhysicalMaterial material, inout ReflectedLight reflectedLight ) {\n" +
            "\n" +
            "\t#ifndef STANDARD\n" +
            "\t\tfloat dotNV = saturate( dot( geometry.normal, geometry.viewDir ) );\n" +
            "\t\tfloat dotNL = dotNV;\n" +
            "\t\tfloat clearCoatDHR = material.clearCoat * clearCoatDHRApprox( material.clearCoatRoughness, dotNL );\n" +
            "\t#else\n" +
            "\t\tfloat clearCoatDHR = 0.0;\n" +
            "\t#endif\n" +
            "\n" +
            "\treflectedLight.indirectSpecular += ( 1.0 - clearCoatDHR ) * radiance * BRDF_Specular_GGX_Environment( geometry, material.specularColor, material.specularRoughness );\n" +
            "\n" +
            "\t#ifndef STANDARD\n" +
            "\n" +
            "\t\treflectedLight.indirectSpecular += clearCoatRadiance * material.clearCoat * BRDF_Specular_GGX_Environment( geometry, vec3( DEFAULT_SPECULAR_COEFFICIENT ), material.clearCoatRoughness );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "}\n" +
            "\n" +
            "#define RE_Direct\t\t\t\tRE_Direct_Physical\n" +
            "#define RE_Direct_RectArea\t\tRE_Direct_RectArea_Physical\n" +
            "#define RE_IndirectDiffuse\t\tRE_IndirectDiffuse_Physical\n" +
            "#define RE_IndirectSpecular\t\tRE_IndirectSpecular_Physical\n" +
            "\n" +
            "#define Material_BlinnShininessExponent( material )   GGXRoughnessToBlinnExponent( material.specularRoughness )\n" +
            "#define Material_ClearCoat_BlinnShininessExponent( material )   GGXRoughnessToBlinnExponent( material.clearCoatRoughness )\n" +
            "\n" +
            "// ref: https://seblagarde.files.wordpress.com/2015/07/course_notes_moving_frostbite_to_pbr_v32.pdf\n" +
            "float computeSpecularOcclusion( const in float dotNV, const in float ambientOcclusion, const in float roughness ) {\n" +
            "\n" +
            "\treturn saturate( pow( dotNV + ambientOcclusion, exp2( - 16.0 * roughness - 1.0 ) ) - 1.0 + ambientOcclusion );\n" +
            "\n" +
            "}\n";
}
