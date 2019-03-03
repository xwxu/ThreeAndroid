package three.renderers.shaders.ShaderChunks;

public class LightsFragmentMaps {
    public static final String code =
            "#if defined( RE_IndirectDiffuse )\n" +
            "\n" +
            "\t#ifdef USE_LIGHTMAP\n" +
            "\n" +
            "\t\tvec3 lightMapIrradiance = texture2D( lightMap, vUv2 ).xyz * lightMapIntensity;\n" +
            "\n" +
            "\t\t#ifndef PHYSICALLY_CORRECT_LIGHTS\n" +
            "\n" +
            "\t\t\tlightMapIrradiance *= PI; // factor of PI should not be present; included here to prevent breakage\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\tirradiance += lightMapIrradiance;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#if defined( USE_ENVMAP ) && defined( PHYSICAL ) && defined( ENVMAP_TYPE_CUBE_UV )\n" +
            "\n" +
            "\t\tirradiance += getLightProbeIndirectIrradiance( /*lightProbe,*/ geometry, maxMipLevel );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if defined( USE_ENVMAP ) && defined( RE_IndirectSpecular )\n" +
            "\n" +
            "\tradiance += getLightProbeIndirectRadiance( /*specularLightProbe,*/ geometry, Material_BlinnShininessExponent( material ), maxMipLevel );\n" +
            "\n" +
            "\t#ifndef STANDARD\n" +
            "\t\tclearCoatRadiance += getLightProbeIndirectRadiance( /*specularLightProbe,*/ geometry, Material_ClearCoat_BlinnShininessExponent( material ), maxMipLevel );\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
