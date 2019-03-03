package three.renderers.shaders.ShaderChunks;

public class SkinningParsVertex {
    public static final String code = "#ifdef USE_SKINNING\n" +
            "\n" +
            "\tuniform mat4 bindMatrix;\n" +
            "\tuniform mat4 bindMatrixInverse;\n" +
            "\n" +
            "\t#ifdef BONE_TEXTURE\n" +
            "\n" +
            "\t\tuniform sampler2D boneTexture;\n" +
            "\t\tuniform int boneTextureSize;\n" +
            "\n" +
            "\t\tmat4 getBoneMatrix( const in float i ) {\n" +
            "\n" +
            "\t\t\tfloat j = i * 4.0;\n" +
            "\t\t\tfloat x = mod( j, float( boneTextureSize ) );\n" +
            "\t\t\tfloat y = floor( j / float( boneTextureSize ) );\n" +
            "\n" +
            "\t\t\tfloat dx = 1.0 / float( boneTextureSize );\n" +
            "\t\t\tfloat dy = 1.0 / float( boneTextureSize );\n" +
            "\n" +
            "\t\t\ty = dy * ( y + 0.5 );\n" +
            "\n" +
            "\t\t\tvec4 v1 = texture2D( boneTexture, vec2( dx * ( x + 0.5 ), y ) );\n" +
            "\t\t\tvec4 v2 = texture2D( boneTexture, vec2( dx * ( x + 1.5 ), y ) );\n" +
            "\t\t\tvec4 v3 = texture2D( boneTexture, vec2( dx * ( x + 2.5 ), y ) );\n" +
            "\t\t\tvec4 v4 = texture2D( boneTexture, vec2( dx * ( x + 3.5 ), y ) );\n" +
            "\n" +
            "\t\t\tmat4 bone = mat4( v1, v2, v3, v4 );\n" +
            "\n" +
            "\t\t\treturn bone;\n" +
            "\n" +
            "\t\t}\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tuniform mat4 boneMatrices[ MAX_BONES ];\n" +
            "\n" +
            "\t\tmat4 getBoneMatrix( const in float i ) {\n" +
            "\n" +
            "\t\t\tmat4 bone = boneMatrices[ int(i) ];\n" +
            "\t\t\treturn bone;\n" +
            "\n" +
            "\t\t}\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
