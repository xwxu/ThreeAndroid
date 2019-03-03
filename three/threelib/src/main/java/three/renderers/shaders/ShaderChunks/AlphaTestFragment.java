package three.renderers.shaders.ShaderChunks;

public class AlphaTestFragment {
    public static final String code =
            "#ifdef ALPHATEST\n" +
            "\n" +
            "\tif ( diffuseColor.a < ALPHATEST ) discard;\n" +
            "\n" +
            "#endif\n";
}
