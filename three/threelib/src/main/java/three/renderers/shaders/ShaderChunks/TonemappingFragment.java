package three.renderers.shaders.ShaderChunks;

public class TonemappingFragment {
    public static final String code =
            "#if defined( TONE_MAPPING )\n" +
            "\n" +
            "  gl_FragColor.rgb = toneMapping( gl_FragColor.rgb );\n" +
            "\n" +
            "#endif\n";
}
