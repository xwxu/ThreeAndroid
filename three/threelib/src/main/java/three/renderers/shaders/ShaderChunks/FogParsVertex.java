package three.renderers.shaders.ShaderChunks;

public class FogParsVertex {
    public static final String code =
            "#ifdef USE_FOG\n" +
            "\n" +
            "\tvarying float fogDepth;\n" +
            "\n" +
            "#endif\n";
}
