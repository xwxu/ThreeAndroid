package three.renderers.shaders.ShaderChunks;

public class MorphTargetVertex {
    public static final String code =
            "#ifdef USE_MORPHTARGETS\n" +
            "\n" +
            "\ttransformed += ( morphTarget0 - position ) * morphTargetInfluences[ 0 ];\n" +
            "\ttransformed += ( morphTarget1 - position ) * morphTargetInfluences[ 1 ];\n" +
            "\ttransformed += ( morphTarget2 - position ) * morphTargetInfluences[ 2 ];\n" +
            "\ttransformed += ( morphTarget3 - position ) * morphTargetInfluences[ 3 ];\n" +
            "\n" +
            "\t#ifndef USE_MORPHNORMALS\n" +
            "\n" +
            "\ttransformed += ( morphTarget4 - position ) * morphTargetInfluences[ 4 ];\n" +
            "\ttransformed += ( morphTarget5 - position ) * morphTargetInfluences[ 5 ];\n" +
            "\ttransformed += ( morphTarget6 - position ) * morphTargetInfluences[ 6 ];\n" +
            "\ttransformed += ( morphTarget7 - position ) * morphTargetInfluences[ 7 ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
