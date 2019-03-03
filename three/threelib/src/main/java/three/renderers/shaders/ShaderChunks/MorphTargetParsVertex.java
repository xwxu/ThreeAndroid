package three.renderers.shaders.ShaderChunks;

public class MorphTargetParsVertex {
    public static final String code =
            "#ifdef USE_MORPHTARGETS\n" +
            "\n" +
            "\t#ifndef USE_MORPHNORMALS\n" +
            "\n" +
            "\tuniform float morphTargetInfluences[ 8 ];\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\tuniform float morphTargetInfluences[ 4 ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "#endif\n";
}
