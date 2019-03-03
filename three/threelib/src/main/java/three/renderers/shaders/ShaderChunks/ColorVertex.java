package three.renderers.shaders.ShaderChunks;

public class ColorVertex {
    public static final String code =
            "#ifdef USE_COLOR\n" +
            "\n" +
            "\tvColor.xyz = color.xyz;\n" +
            "\n" +
            "#endif\n";
}
