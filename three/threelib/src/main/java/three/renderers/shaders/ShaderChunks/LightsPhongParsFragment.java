package three.renderers.shaders.ShaderChunks;

public class LightsPhongParsFragment {
    public static final String code =
            "varying vec3 vViewPosition;\n" +
            "\n" +
            "#ifndef FLAT_SHADED\n" +
            "\n" +
            "\tvarying vec3 vNormal;\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "\n" +
            "struct BlinnPhongMaterial {\n" +
            "\n" +
            "\tvec3\tdiffuseColor;\n" +
            "\tvec3\tspecularColor;\n" +
            "\tfloat\tspecularShininess;\n" +
            "\tfloat\tspecularStrength;\n" +
            "\n" +
            "};\n" +
            "\n" +
            "void RE_Direct_BlinnPhong( const in IncidentLight directLight, const in GeometricContext geometry, const in BlinnPhongMaterial material, inout ReflectedLight reflectedLight ) {\n" +
            "\n" +
            "\t#ifdef TOON\n" +
            "\n" +
            "\t\tvec3 irradiance = getGradientIrradiance( geometry.normal, directLight.direction ) * directLight.color;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tfloat dotNL = saturate( dot( geometry.normal, directLight.direction ) );\n" +
            "\t\tvec3 irradiance = dotNL * directLight.color;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#ifndef PHYSICALLY_CORRECT_LIGHTS\n" +
            "\n" +
            "\t\tirradiance *= PI; // punctual light\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\treflectedLight.directDiffuse += irradiance * BRDF_Diffuse_Lambert( material.diffuseColor );\n" +
            "\n" +
            "\treflectedLight.directSpecular += irradiance * BRDF_Specular_BlinnPhong( directLight, geometry, material.specularColor, material.specularShininess ) * material.specularStrength;\n" +
            "\n" +
            "}\n" +
            "\n" +
            "void RE_IndirectDiffuse_BlinnPhong( const in vec3 irradiance, const in GeometricContext geometry, const in BlinnPhongMaterial material, inout ReflectedLight reflectedLight ) {\n" +
            "\n" +
            "\treflectedLight.indirectDiffuse += irradiance * BRDF_Diffuse_Lambert( material.diffuseColor );\n" +
            "\n" +
            "}\n" +
            "\n" +
            "#define RE_Direct\t\t\t\tRE_Direct_BlinnPhong\n" +
            "#define RE_IndirectDiffuse\t\tRE_IndirectDiffuse_BlinnPhong\n" +
            "\n" +
            "#define Material_LightProbeLOD( material )\t(0)\n";
}
