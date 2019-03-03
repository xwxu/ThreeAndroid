package three.renderers.shaders.ShaderChunks;

public class BumpmapParsFragment {
    public static final String code =
            "#ifdef USE_BUMPMAP\n" +
            "\n" +
            "\tuniform sampler2D bumpMap;\n" +
            "\tuniform float bumpScale;\n" +
            "\n" +
            "\t// Bump Mapping Unparametrized Surfaces on the GPU by Morten S. Mikkelsen\n" +
            "\t// http://api.unrealengine.com/attachments/Engine/Rendering/LightingAndShadows/BumpMappingWithoutTangentSpace/mm_sfgrad_bump.pdf\n" +
            "\n" +
            "\t// Evaluate the derivative of the height w.r.t. screen-space using forward differencing (listing 2)\n" +
            "\n" +
            "\tvec2 dHdxy_fwd() {\n" +
            "\n" +
            "\t\tvec2 dSTdx = dFdx( vUv );\n" +
            "\t\tvec2 dSTdy = dFdy( vUv );\n" +
            "\n" +
            "\t\tfloat Hll = bumpScale * texture2D( bumpMap, vUv ).x;\n" +
            "\t\tfloat dBx = bumpScale * texture2D( bumpMap, vUv + dSTdx ).x - Hll;\n" +
            "\t\tfloat dBy = bumpScale * texture2D( bumpMap, vUv + dSTdy ).x - Hll;\n" +
            "\n" +
            "\t\treturn vec2( dBx, dBy );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\tvec3 perturbNormalArb( vec3 surf_pos, vec3 surf_norm, vec2 dHdxy ) {\n" +
            "\n" +
            "\t\t// Workaround for Adreno 3XX dFd*( vec3 ) bug. See #9988\n" +
            "\n" +
            "\t\tvec3 vSigmaX = vec3( dFdx( surf_pos.x ), dFdx( surf_pos.y ), dFdx( surf_pos.z ) );\n" +
            "\t\tvec3 vSigmaY = vec3( dFdy( surf_pos.x ), dFdy( surf_pos.y ), dFdy( surf_pos.z ) );\n" +
            "\t\tvec3 vN = surf_norm;\t\t// normalized\n" +
            "\n" +
            "\t\tvec3 R1 = cross( vSigmaY, vN );\n" +
            "\t\tvec3 R2 = cross( vN, vSigmaX );\n" +
            "\n" +
            "\t\tfloat fDet = dot( vSigmaX, R1 );\n" +
            "\n" +
            "\t\tfDet *= ( float( gl_FrontFacing ) * 2.0 - 1.0 );\n" +
            "\n" +
            "\t\tvec3 vGrad = sign( fDet ) * ( dHdxy.x * R1 + dHdxy.y * R2 );\n" +
            "\t\treturn normalize( abs( fDet ) * surf_norm - vGrad );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n";
}
