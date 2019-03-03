package three.renderers.shaders.ShaderChunks;

public class EmissiveMapFragment {
    public static final String code =
            "#ifdef USE_EMISSIVEMAP\n" +
            "\n" +
            "\tvec4 emissiveColor = texture2D( emissiveMap, vUv );\n" +
            "\n" +
            "\temissiveColor.rgb = emissiveMapTexelToLinear( emissiveColor ).rgb;\n" +
            "\n" +
            "\ttotalEmissiveRadiance *= emissiveColor.rgb;\n" +
            "\n" +
            "#endif\n";
}
