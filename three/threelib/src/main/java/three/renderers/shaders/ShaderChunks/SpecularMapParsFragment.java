package three.renderers.shaders.ShaderChunks;

public class SpecularMapParsFragment {
    public static final String code =
            "#ifdef USE_SPECULARMAP\n" +
            "\n" +
            "\tuniform sampler2D specularMap;\n" +
            "\n" +
            "#endif\n";
}
