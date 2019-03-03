package three.renderers.shaders.ShaderChunks;

public class FogParsFragment {
    public static final String code =
            "#ifdef USE_FOG\n" +
            "\n" +
            "\tuniform vec3 fogColor;\n" +
            "\tvarying float fogDepth;\n" +
            "\n" +
            "\t#ifdef FOG_EXP2\n" +
            "\n" +
            "\t\tuniform float fogDensity;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tuniform float fogNear;\n" +
            "\t\tuniform float fogFar;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
