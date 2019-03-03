package three.renderers.shaders.ShaderChunks;

public class ClippingPlanesParsFragment {
    public static final String code =
            "#if NUM_CLIPPING_PLANES > 0\n" +
                    "\n" +
                    "\t#if ! defined( PHYSICAL ) && ! defined( PHONG ) && ! defined( MATCAP )\n" +
                    "\t\tvarying vec3 vViewPosition;\n" +
                    "\t#endif\n" +
                    "\n" +
                    "\tuniform vec4 clippingPlanes[ NUM_CLIPPING_PLANES ];\n" +
                    "\n" +
                    "#endif\n";
}
