package three.renderers.shaders.ShaderChunks;

public class LogdepthbufFragment {
    public static final String code =
            "#if defined( USE_LOGDEPTHBUF ) && defined( USE_LOGDEPTHBUF_EXT )\n" +
            "\n" +
            "\tgl_FragDepthEXT = log2( vFragDepth ) * logDepthBufFC * 0.5;\n" +
            "\n" +
            "#endif\n";
}
