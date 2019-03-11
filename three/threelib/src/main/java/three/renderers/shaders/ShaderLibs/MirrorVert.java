package three.renderers.shaders.ShaderLibs;

public class MirrorVert {
    public static final String code =
            "varying vec2 vUv;\n" +
            "void main() {\n" +
                "   vUv = uv;\n" +
                "   gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
                "}\n";
}
