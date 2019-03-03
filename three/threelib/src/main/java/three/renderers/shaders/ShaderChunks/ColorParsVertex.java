package three.renderers.shaders.ShaderChunks;

public class ColorParsVertex {
    public static final String code =
            "#ifdef USE_COLOR\n" +
            "\n" +
            "\tvarying vec3 vColor;\n" +
            "\n" +
            "#endif\n";
}
