package three.renderers.shaders.ShaderChunks;

public class RoughnessMapParsFragment {
    public static final String code =
            "#ifdef USE_ROUGHNESSMAP\n" +
            "\n" +
            "\tuniform sampler2D roughnessMap;\n" +
            "\n" +
            "#endif\n";
}
