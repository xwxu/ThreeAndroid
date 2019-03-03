package three.renderers.shaders.ShaderChunks;

public class EnvmapParsFragment {
    public static final String code =
            "#if defined( USE_ENVMAP ) || defined( PHYSICAL )\n" +
            "\tuniform float reflectivity;\n" +
            "\tuniform float envMapIntensity;\n" +
            "#endif\n" +
            "\n" +
            "#ifdef USE_ENVMAP\n" +
            "\n" +
            "\t#if ! defined( PHYSICAL ) && ( defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG ) )\n" +
            "\t\tvarying vec3 vWorldPosition;\n" +
            "\t#endif\n" +
            "\n" +
            "\t#ifdef ENVMAP_TYPE_CUBE\n" +
            "\t\tuniform samplerCube envMap;\n" +
            "\t#else\n" +
            "\t\tuniform sampler2D envMap;\n" +
            "\t#endif\n" +
            "\tuniform float flipEnvMap;\n" +
            "\tuniform int maxMipLevel;\n" +
            "\n" +
            "\t#if defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG ) || defined( PHYSICAL )\n" +
            "\t\tuniform float refractionRatio;\n" +
            "\t#else\n" +
            "\t\tvarying vec3 vReflect;\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
