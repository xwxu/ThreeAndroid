package three.renderers.shaders.ShaderChunks;

public class LightmapParsFragment {
    public static final String code =
            "#ifdef USE_LIGHTMAP\n" +
            "\n" +
            "\tuniform sampler2D lightMap;\n" +
            "\tuniform float lightMapIntensity;\n" +
            "\n" +
            "#endif\n";
}
