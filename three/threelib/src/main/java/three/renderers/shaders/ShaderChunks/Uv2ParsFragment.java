package three.renderers.shaders.ShaderChunks;

public class Uv2ParsFragment {
    public static final String code =
            "#if defined( USE_LIGHTMAP ) || defined( USE_AOMAP )\n" +
            "\n" +
            "\tvarying vec2 vUv2;\n" +
            "\n" +
            "#endif\n";
}
