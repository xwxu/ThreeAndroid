package three.renderers.shaders.ShaderChunks;

public class MorphNormalVertex {
    public static final String code =
            "#ifdef USE_MORPHNORMALS\n" +
            "\n" +
            "\tobjectNormal += ( morphNormal0 - normal ) * morphTargetInfluences[ 0 ];\n" +
            "\tobjectNormal += ( morphNormal1 - normal ) * morphTargetInfluences[ 1 ];\n" +
            "\tobjectNormal += ( morphNormal2 - normal ) * morphTargetInfluences[ 2 ];\n" +
            "\tobjectNormal += ( morphNormal3 - normal ) * morphTargetInfluences[ 3 ];\n" +
            "\n" +
            "#endif\n";
}
