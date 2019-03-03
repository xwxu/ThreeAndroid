package three.renderers.shaders.ShaderChunks;

public class MapParticleFragment {
    public static final String code =
            "#ifdef USE_MAP\n" +
            "\n" +
            "\tvec2 uv = ( uvTransform * vec3( gl_PointCoord.x, 1.0 - gl_PointCoord.y, 1 ) ).xy;\n" +
            "\tvec4 mapTexel = texture2D( map, uv );\n" +
            "\tdiffuseColor *= mapTexelToLinear( mapTexel );\n" +
            "\n" +
            "#endif\n";
}
