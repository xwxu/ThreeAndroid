package three.renderers.shaders.ShaderChunks;

public class AomapFragment {
    public static final String code =
            "#ifdef USE_AOMAP\n" +
            "\n" +
            "\t// reads channel R, compatible with a combined OcclusionRoughnessMetallic (RGB) texture\n" +
            "\tfloat ambientOcclusion = ( texture2D( aoMap, vUv2 ).r - 1.0 ) * aoMapIntensity + 1.0;\n" +
            "\n" +
            "\treflectedLight.indirectDiffuse *= ambientOcclusion;\n" +
            "\n" +
            "\t#if defined( USE_ENVMAP ) && defined( PHYSICAL )\n" +
            "\n" +
            "\t\tfloat dotNV = saturate( dot( geometry.normal, geometry.viewDir ) );\n" +
            "\n" +
            "\t\treflectedLight.indirectSpecular *= computeSpecularOcclusion( dotNV, ambientOcclusion, material.specularRoughness );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
