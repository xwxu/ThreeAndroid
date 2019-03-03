package three.renderers.shaders.ShaderChunks;

public class AlphamapFragment {
    public static final String code =
            "#ifdef USE_ALPHAMAP\n" +
            "\n" +
            "\tdiffuseColor.a *= texture2D( alphaMap, vUv ).g;\n" +
            "\n" +
            "#endif\n";
}
