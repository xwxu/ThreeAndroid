package three.renderers.shaders.ShaderChunks;

public class DisplacementMapParsVertex {
    public static final String code =
            "#ifdef USE_DISPLACEMENTMAP\n" +
            "\n" +
            "\tuniform sampler2D displacementMap;\n" +
            "\tuniform float displacementScale;\n" +
            "\tuniform float displacementBias;\n" +
            "\n" +
            "#endif\n";
}
