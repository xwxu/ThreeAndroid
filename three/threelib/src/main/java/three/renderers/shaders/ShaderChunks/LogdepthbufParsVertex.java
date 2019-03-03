package three.renderers.shaders.ShaderChunks;

public class LogdepthbufParsVertex {
    public static final String code =
            "#ifdef USE_LOGDEPTHBUF\n" +
            "\n" +
            "\t#ifdef USE_LOGDEPTHBUF_EXT\n" +
            "\n" +
            "\t\tvarying float vFragDepth;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tuniform float logDepthBufFC;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
