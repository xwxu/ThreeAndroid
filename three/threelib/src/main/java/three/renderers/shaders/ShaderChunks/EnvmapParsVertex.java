package three.renderers.shaders.ShaderChunks;

public class EnvmapParsVertex {
    public static final String code =
            "#ifdef USE_ENVMAP\n" +
            "\n" +
            "\t#if defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG )\n" +
            "\t\tvarying vec3 vWorldPosition;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tvarying vec3 vReflect;\n" +
            "\t\tuniform float refractionRatio;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
