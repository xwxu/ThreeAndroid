package three.renderers.shaders.ShaderChunks;

public class LightsFragmentEnd {
    public static final String code =
            "#if defined( RE_IndirectDiffuse )\n" +
            "\n" +
            "\tRE_IndirectDiffuse( irradiance, geometry, material, reflectedLight );\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#if defined( RE_IndirectSpecular )\n" +
            "\n" +
            "\tRE_IndirectSpecular( radiance, clearCoatRadiance, geometry, material, reflectedLight );\n" +
            "\n" +
            "#endif\n";
}
