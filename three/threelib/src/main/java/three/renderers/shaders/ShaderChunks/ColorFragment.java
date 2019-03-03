package three.renderers.shaders.ShaderChunks;

public class ColorFragment {
    public static final String code =
            "#ifdef USE_COLOR\n" +
            "\n" +
            "\tdiffuseColor.rgb *= vColor;\n" +
            "\n" +
            "#endif\n";
}
