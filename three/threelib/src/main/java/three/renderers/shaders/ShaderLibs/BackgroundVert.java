package three.renderers.shaders.ShaderLibs;

public class BackgroundVert {
    public static final String code = "varying vec2 vUv;\n" +
            "uniform mat3 uvTransform;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\tvUv = ( uvTransform * vec3( uv, 1 ) ).xy;\n" +
            "\n" +
            "\tgl_Position = vec4( position.xy, 1.0, 1.0 );\n" +
            "\n" +
            "}\n";
}
