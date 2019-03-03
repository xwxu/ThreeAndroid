package three.renderers.shaders.ShaderChunks;

public class DitheringFragment {
    public static final String code =
            "#if defined( DITHERING )\n" +
            "\n" +
            "  gl_FragColor.rgb = dithering( gl_FragColor.rgb );\n" +
            "\n" +
            "#endif\n";
}
