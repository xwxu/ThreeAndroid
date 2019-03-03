package three.renderers.shaders.ShaderChunks;

public class MetalnessMapParsFragment {
    public static final String code =
            "#ifdef USE_METALNESSMAP\n" +
            "\n" +
            "\tuniform sampler2D metalnessMap;\n" +
            "\n" +
            "#endif\n";
}
