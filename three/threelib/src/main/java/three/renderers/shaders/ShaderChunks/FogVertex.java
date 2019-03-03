package three.renderers.shaders.ShaderChunks;

public class FogVertex {
    public static final String code =
            "#ifdef USE_FOG\n" +
            "\n" +
            "\tfogDepth = -mvPosition.z;\n" +
            "\n" +
            "#endif\n";
}
