package three.renderers.shaders.ShaderChunks;

public class Uv2ParsVertex {
    public static final String code =
            "#if defined( USE_LIGHTMAP ) || defined( USE_AOMAP )\n" +
            "\n" +
            "\tattribute vec2 uv2;\n" +
            "\tvarying vec2 vUv2;\n" +
            "\n" +
            "#endif\n";
}
