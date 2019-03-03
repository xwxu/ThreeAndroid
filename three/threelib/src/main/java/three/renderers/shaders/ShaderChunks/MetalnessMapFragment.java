package three.renderers.shaders.ShaderChunks;

public class MetalnessMapFragment {
    public static final String code =
            "float metalnessFactor = metalness;\n" +
            "\n" +
            "#ifdef USE_METALNESSMAP\n" +
            "\n" +
            "\tvec4 texelMetalness = texture2D( metalnessMap, vUv );\n" +
            "\n" +
            "\t// reads channel B, compatible with a combined OcclusionRoughnessMetallic (RGB) texture\n" +
            "\tmetalnessFactor *= texelMetalness.b;\n" +
            "\n" +
            "#endif\n";
}
