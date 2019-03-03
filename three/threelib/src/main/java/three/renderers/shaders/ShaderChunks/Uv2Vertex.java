package three.renderers.shaders.ShaderChunks;

public class Uv2Vertex {
    public static final String code =
            "#if defined( USE_LIGHTMAP ) || defined( USE_AOMAP )\n" +
            "\n" +
            "\tvUv2 = uv2;\n" +
            "\n" +
            "#endif\n";
}
