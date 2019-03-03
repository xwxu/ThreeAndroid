package three.renderers.shaders.ShaderChunks;

public class ClippingPlanesParsVertex {
    public static final String code = "#if NUM_CLIPPING_PLANES > 0 && ! defined( PHYSICAL ) && ! defined( PHONG ) && ! defined( MATCAP )\n" +
            "\tvarying vec3 vViewPosition;\n" +
            "#endif\n";
}
