package three.renderers.shaders.ShaderChunks;

public class ClippingPlanesVertex {
    public static final String code =
            "#if NUM_CLIPPING_PLANES > 0 && ! defined( PHYSICAL ) && ! defined( PHONG ) && ! defined( MATCAP )\n" +
            "\tvViewPosition = - mvPosition.xyz;\n" +
            "#endif\n";
}
