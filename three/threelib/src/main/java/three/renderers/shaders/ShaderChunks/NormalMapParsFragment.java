package three.renderers.shaders.ShaderChunks;

public class NormalMapParsFragment {
    public static final String code =
            "#ifdef USE_NORMALMAP\n" +
            "\n" +
            "\tuniform sampler2D normalMap;\n" +
            "\tuniform vec2 normalScale;\n" +
            "\n" +
            "\t#ifdef OBJECTSPACE_NORMALMAP\n" +
            "\n" +
            "\t\tuniform mat3 normalMatrix;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\t// Per-Pixel Tangent Space Normal Mapping\n" +
            "\t\t// http://hacksoflife.blogspot.ch/2009/11/per-pixel-tangent-space-normal-mapping.html\n" +
            "\n" +
            "\t\tvec3 perturbNormal2Arb( vec3 eye_pos, vec3 surf_norm ) {\n" +
            "\n" +
            "\t\t\t// Workaround for Adreno 3XX dFd*( vec3 ) bug. See #9988\n" +
            "\n" +
            "\t\t\tvec3 q0 = vec3( dFdx( eye_pos.x ), dFdx( eye_pos.y ), dFdx( eye_pos.z ) );\n" +
            "\t\t\tvec3 q1 = vec3( dFdy( eye_pos.x ), dFdy( eye_pos.y ), dFdy( eye_pos.z ) );\n" +
            "\t\t\tvec2 st0 = dFdx( vUv.st );\n" +
            "\t\t\tvec2 st1 = dFdy( vUv.st );\n" +
            "\n" +
            "\t\t\tfloat scale = sign( st1.t * st0.s - st0.t * st1.s ); // we do not care about the magnitude\n" +
            "\n" +
            "\t\t\tvec3 S = normalize( ( q0 * st1.t - q1 * st0.t ) * scale );\n" +
            "\t\t\tvec3 T = normalize( ( - q0 * st1.s + q1 * st0.s ) * scale );\n" +
            "\t\t\tvec3 N = normalize( surf_norm );\n" +
            "\t\t\tmat3 tsn = mat3( S, T, N );\n" +
            "\n" +
            "\t\t\tvec3 mapN = texture2D( normalMap, vUv ).xyz * 2.0 - 1.0;\n" +
            "\n" +
            "\t\t\tmapN.xy *= normalScale;\n" +
            "\t\t\tmapN.xy *= ( float( gl_FrontFacing ) * 2.0 - 1.0 );\n" +
            "\n" +
            "\t\t\treturn normalize( tsn * mapN );\n" +
            "\n" +
            "\t\t}\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
