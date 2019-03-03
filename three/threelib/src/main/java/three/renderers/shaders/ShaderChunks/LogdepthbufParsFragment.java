package three.renderers.shaders.ShaderChunks;

public class LogdepthbufParsFragment {
    public static final String code =
            "#if defined( USE_LOGDEPTHBUF ) && defined( USE_LOGDEPTHBUF_EXT )\n" +
            "\n" +
            "\tuniform float logDepthBufFC;\n" +
            "\tvarying float vFragDepth;\n" +
            "\n" +
            "#endif\n";
}
