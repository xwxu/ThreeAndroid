package three.renderers.shaders.ShaderChunks;

public class LogdepthbufVertex {
    public static final String code =
            "#ifdef USE_LOGDEPTHBUF\n" +
            "\n" +
            "\t#ifdef USE_LOGDEPTHBUF_EXT\n" +
            "\n" +
            "\t\tvFragDepth = 1.0 + gl_Position.w;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tgl_Position.z = log2( max( EPSILON, gl_Position.w + 1.0 ) ) * logDepthBufFC - 1.0;\n" +
            "\n" +
            "\t\tgl_Position.z *= gl_Position.w;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
