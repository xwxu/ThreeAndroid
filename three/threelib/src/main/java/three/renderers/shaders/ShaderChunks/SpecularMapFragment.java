package three.renderers.shaders.ShaderChunks;

public class SpecularMapFragment {
    public static final String code =
            "float specularStrength;\n" +
            "\n" +
            "#ifdef USE_SPECULARMAP\n" +
            "\n" +
            "\tvec4 texelSpecular = texture2D( specularMap, vUv );\n" +
            "\tspecularStrength = texelSpecular.r;\n" +
            "\n" +
            "#else\n" +
            "\n" +
            "\tspecularStrength = 1.0;\n" +
            "\n" +
            "#endif\n";
}
