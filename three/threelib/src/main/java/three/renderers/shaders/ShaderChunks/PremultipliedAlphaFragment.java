package three.renderers.shaders.ShaderChunks;

public class PremultipliedAlphaFragment {
    public static final String code =
            "#ifdef PREMULTIPLIED_ALPHA\n" +
            "\n" +
            "\t// get get normal blending with premultipled, use with CustomBlending, OneFactor, OneMinusSrcAlphaFactor, AddEquation.\n" +
            "\tgl_FragColor.rgb *= gl_FragColor.a;\n" +
            "\n" +
            "#endif\n";
}
