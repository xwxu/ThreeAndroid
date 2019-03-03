package three.renderers.shaders.ShaderChunks;

public class NormalFragmentMaps {
    public static final String code =
            "#ifdef USE_NORMALMAP\n" +
            "\n" +
            "\t#ifdef OBJECTSPACE_NORMALMAP\n" +
            "\n" +
            "\t\tnormal = texture2D( normalMap, vUv ).xyz * 2.0 - 1.0; // overrides both flatShading and attribute normals\n" +
            "\n" +
            "\t\t#ifdef FLIP_SIDED\n" +
            "\n" +
            "\t\t\tnormal = - normal;\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\t#ifdef DOUBLE_SIDED\n" +
            "\n" +
            "\t\t\tnormal = normal * ( float( gl_FrontFacing ) * 2.0 - 1.0 );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\tnormal = normalize( normalMatrix * normal );\n" +
            "\n" +
            "\t#else // tangent-space normal map\n" +
            "\n" +
            "\t\tnormal = perturbNormal2Arb( -vViewPosition, normal );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#elif defined( USE_BUMPMAP )\n" +
            "\n" +
            "\tnormal = perturbNormalArb( -vViewPosition, normal, dHdxy_fwd() );\n" +
            "\n" +
            "#endif\n";
}
