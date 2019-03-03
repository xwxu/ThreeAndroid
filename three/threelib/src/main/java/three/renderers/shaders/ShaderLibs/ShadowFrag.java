package three.renderers.shaders.ShaderLibs;

public class ShadowFrag {
    public static final String code = "uniform vec3 color;\n" +
            "uniform float opacity;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <packing>\n" +
            "#include <fog_pars_fragment>\n" +
            "#include <bsdfs>\n" +
            "#include <lights_pars_begin>\n" +
            "#include <shadowmap_pars_fragment>\n" +
            "#include <shadowmask_pars_fragment>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\tgl_FragColor = vec4( color, opacity * ( 1.0 - getShadowMask() ) );\n" +
            "\n" +
            "\t#include <fog_fragment>\n" +
            "\n" +
            "}\n";
}
