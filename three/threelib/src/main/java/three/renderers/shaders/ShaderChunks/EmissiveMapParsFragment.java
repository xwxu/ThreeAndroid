package three.renderers.shaders.ShaderChunks;

public class EmissiveMapParsFragment {
    public static final String code =
            "#ifdef USE_EMISSIVEMAP\n" +
            "\n" +
            "\tuniform sampler2D emissiveMap;\n" +
            "\n" +
            "#endif\n";
}
