package three.renderers.shaders.ShaderChunks;

public class LightmapFragment {
    public static final String code =
            "#ifdef USE_LIGHTMAP\n" +
            "\n" +
            "\treflectedLight.indirectDiffuse += PI * texture2D( lightMap, vUv2 ).xyz * lightMapIntensity; // factor of PI should not be present; included here to prevent breakage\n" +
            "\n" +
            "#endif\n";
}
