package three.renderers.shaders.ShaderChunks;

public class SkinbaseVertex {
    public static final String code =
            "#ifdef USE_SKINNING\n" +
            "\n" +
            "\tmat4 boneMatX = getBoneMatrix( skinIndex.x );\n" +
            "\tmat4 boneMatY = getBoneMatrix( skinIndex.y );\n" +
            "\tmat4 boneMatZ = getBoneMatrix( skinIndex.z );\n" +
            "\tmat4 boneMatW = getBoneMatrix( skinIndex.w );\n" +
            "\n" +
            "#endif\n";
}
