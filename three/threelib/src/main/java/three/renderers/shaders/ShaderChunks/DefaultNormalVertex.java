package three.renderers.shaders.ShaderChunks;

public class DefaultNormalVertex {
    public static final String code =
            "vec3 transformedNormal = normalMatrix * objectNormal;\n" +
            "\n" +
            "#ifdef FLIP_SIDED\n" +
            "\n" +
            "\ttransformedNormal = - transformedNormal;\n" +
            "\n" +
            "#endif\n";
}
