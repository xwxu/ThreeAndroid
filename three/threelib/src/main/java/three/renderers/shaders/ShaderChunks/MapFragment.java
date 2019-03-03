package three.renderers.shaders.ShaderChunks;

public class MapFragment {
    public static final String code = "#ifdef USE_MAP\n" +
            "\n" +
            "\tvec4 texelColor = texture2D( map, vUv );\n" +
            "\n" +
            "\ttexelColor = mapTexelToLinear( texelColor );\n" +
            "\tdiffuseColor *= texelColor;\n" +
            "\n" +
            "#endif\n";
}
