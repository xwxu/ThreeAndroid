package three.renderers.shaders.ShaderChunks;

public class AlphamapParsFragment {
    public static final String code =
            "#ifdef USE_ALPHAMAP\n" +
            "\n" +
            "\tuniform sampler2D alphaMap;\n" +
            "\n" +
            "#endif\n";
}
