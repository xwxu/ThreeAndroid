package three.renderers.shaders.ShaderChunks;

public class DisplacementMapVertex {
    public static final String code =
            "#ifdef USE_DISPLACEMENTMAP\n" +
            "\n" +
            "\ttransformed += normalize( objectNormal ) * ( texture2D( displacementMap, uv ).x * displacementScale + displacementBias );\n" +
            "\n" +
            "#endif\n";
}
