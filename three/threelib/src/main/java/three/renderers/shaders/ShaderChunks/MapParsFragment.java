package three.renderers.shaders.ShaderChunks;

public class MapParsFragment {
    public static final String code = "#ifdef USE_MAP\n" +
            "\n" +
            "\tuniform sampler2D map;\n" +
            "\n" +
            "#endif\n";
}
