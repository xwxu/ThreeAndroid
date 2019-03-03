package three.renderers.shaders.ShaderChunks;

public class AomapParsFragment {
    public static final String code = "#ifdef USE_AOMAP\n" +
            "\n" +
            "\tuniform sampler2D aoMap;\n" +
            "\tuniform float aoMapIntensity;\n" +
            "\n" +
            "#endif\n";
}
