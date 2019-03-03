package three.renderers.shaders.ShaderChunks;

public class ProjectVertex {
    public static final String code =
            "vec4 mvPosition = modelViewMatrix * vec4( transformed, 1.0 );\n" +
            "\n" +
            "gl_Position = projectionMatrix * mvPosition;\n";
}
