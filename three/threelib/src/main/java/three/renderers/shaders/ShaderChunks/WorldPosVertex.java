package three.renderers.shaders.ShaderChunks;

public class WorldPosVertex {
    public static final String code =
            "#if defined( USE_ENVMAP ) || defined( DISTANCE ) || defined ( USE_SHADOWMAP )\n" +
            "\n" +
            "\tvec4 worldPosition = modelMatrix * vec4( transformed, 1.0 );\n" +
            "\n" +
            "#endif\n";
}
