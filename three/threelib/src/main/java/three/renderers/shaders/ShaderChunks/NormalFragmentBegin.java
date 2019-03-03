package three.renderers.shaders.ShaderChunks;

public class NormalFragmentBegin {
    public static final String code =
            "#ifdef FLAT_SHADED\n" +
            "\n" +
            "\t// Workaround for Adreno/Nexus5 not able able to do dFdx( vViewPosition ) ...\n" +
            "\n" +
            "\tvec3 fdx = vec3( dFdx( vViewPosition.x ), dFdx( vViewPosition.y ), dFdx( vViewPosition.z ) );\n" +
            "\tvec3 fdy = vec3( dFdy( vViewPosition.x ), dFdy( vViewPosition.y ), dFdy( vViewPosition.z ) );\n" +
            "\tvec3 normal = normalize( cross( fdx, fdy ) );\n" +
            "\n" +
            "#else\n" +
            "\n" +
            "\tvec3 normal = normalize( vNormal );\n" +
            "\n" +
            "\t#ifdef DOUBLE_SIDED\n" +
            "\n" +
            "\t\tnormal = normal * ( float( gl_FrontFacing ) * 2.0 - 1.0 );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
