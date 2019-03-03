package three.renderers.shaders.ShaderChunks;

public class EnvmapVertex {
    public static final String code =
            "#ifdef USE_ENVMAP\n" +
            "\n" +
            "\t#if defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG )\n" +
            "\n" +
            "\t\tvWorldPosition = worldPosition.xyz;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tvec3 cameraToVertex = normalize( worldPosition.xyz - cameraPosition );\n" +
            "\n" +
            "\t\tvec3 worldNormal = inverseTransformDirection( transformedNormal, viewMatrix );\n" +
            "\n" +
            "\t\t#ifdef ENVMAP_MODE_REFLECTION\n" +
            "\n" +
            "\t\t\tvReflect = reflect( cameraToVertex, worldNormal );\n" +
            "\n" +
            "\t\t#else\n" +
            "\n" +
            "\t\t\tvReflect = refract( cameraToVertex, worldNormal, refractionRatio );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
