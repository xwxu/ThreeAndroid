package three.renderers.shaders.ShaderChunks;

public class ColorParsFragment {
    public static final String code =
            "#ifdef USE_COLOR\n" +
            "\n" +
            "\tvarying vec3 vColor;\n" +
            "\n" +
            "#endif\n";
}
