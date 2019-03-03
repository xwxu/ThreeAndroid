package three.renderers.shaders.ShaderChunks;

public class GradientMapParsFragment {
    public static final String code =
            "#ifdef TOON\n" +
            "\n" +
            "\tuniform sampler2D gradientMap;\n" +
            "\n" +
            "\tvec3 getGradientIrradiance( vec3 normal, vec3 lightDirection ) {\n" +
            "\n" +
            "\t\t// dotNL will be from -1.0 to 1.0\n" +
            "\t\tfloat dotNL = dot( normal, lightDirection );\n" +
            "\t\tvec2 coord = vec2( dotNL * 0.5 + 0.5, 0.0 );\n" +
            "\n" +
            "\t\t#ifdef USE_GRADIENTMAP\n" +
            "\n" +
            "\t\t\treturn texture2D( gradientMap, coord ).rgb;\n" +
            "\n" +
            "\t\t#else\n" +
            "\n" +
            "\t\t\treturn ( coord.x < 0.7 ) ? vec3( 0.7 ) : vec3( 1.0 );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n";
}
