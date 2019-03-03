package three.renderers.shaders.ShaderChunks;

public class MapParticleParsFragment {
    public static final String code =
            "#ifdef USE_MAP\n" +
            "\n" +
            "\tuniform mat3 uvTransform;\n" +
            "\tuniform sampler2D map;\n" +
            "\n" +
            "#endif\n";
}
