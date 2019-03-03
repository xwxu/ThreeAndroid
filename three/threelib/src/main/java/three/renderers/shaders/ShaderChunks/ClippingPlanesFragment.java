package three.renderers.shaders.ShaderChunks;

public class ClippingPlanesFragment {
    public static final String code =
            "#if NUM_CLIPPING_PLANES > 0\n" +
            "\n" +
            "\tvec4 plane;\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < UNION_CLIPPING_PLANES; i ++ ) {\n" +
            "\n" +
            "\t\tplane = clippingPlanes[ i ];\n" +
            "\t\tif ( dot( vViewPosition, plane.xyz ) > plane.w ) discard;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\t#if UNION_CLIPPING_PLANES < NUM_CLIPPING_PLANES\n" +
            "\n" +
            "\t\tbool clipped = true;\n" +
            "\n" +
            "\t\t#pragma unroll_loop\n" +
            "\t\tfor ( int i = UNION_CLIPPING_PLANES; i < NUM_CLIPPING_PLANES; i ++ ) {\n" +
            "\n" +
            "\t\t\tplane = clippingPlanes[ i ];\n" +
            "\t\t\tclipped = ( dot( vViewPosition, plane.xyz ) > plane.w ) && clipped;\n" +
            "\n" +
            "\t\t}\n" +
            "\n" +
            "\t\tif ( clipped ) discard;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
