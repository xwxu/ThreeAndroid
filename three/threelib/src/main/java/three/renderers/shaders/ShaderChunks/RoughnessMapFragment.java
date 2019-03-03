package three.renderers.shaders.ShaderChunks;

public class RoughnessMapFragment {
    public static final String code =
            "float roughnessFactor = roughness;\n" +
            "\n" +
            "#ifdef USE_ROUGHNESSMAP\n" +
            "\n" +
            "\tvec4 texelRoughness = texture2D( roughnessMap, vUv );\n" +
            "\n" +
            "\t// reads channel G, compatible with a combined OcclusionRoughnessMetallic (RGB) texture\n" +
            "\troughnessFactor *= texelRoughness.g;\n" +
            "\n" +
            "#endif\n";
}
