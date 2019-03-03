package three.renderers.shaders.ShaderChunks;

public class SkinNormalVertex {
    public static final String code =
            "#ifdef USE_SKINNING\n" +
            "\n" +
            "\tmat4 skinMatrix = mat4( 0.0 );\n" +
            "\tskinMatrix += skinWeight.x * boneMatX;\n" +
            "\tskinMatrix += skinWeight.y * boneMatY;\n" +
            "\tskinMatrix += skinWeight.z * boneMatZ;\n" +
            "\tskinMatrix += skinWeight.w * boneMatW;\n" +
            "\tskinMatrix  = bindMatrixInverse * skinMatrix * bindMatrix;\n" +
            "\n" +
            "\tobjectNormal = vec4( skinMatrix * vec4( objectNormal, 0.0 ) ).xyz;\n" +
            "\n" +
            "#endif\n";
}
