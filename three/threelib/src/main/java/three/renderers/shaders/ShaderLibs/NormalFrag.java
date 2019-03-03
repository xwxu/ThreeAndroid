package three.renderers.shaders.ShaderLibs;

public class NormalFrag {
    public static final String code = "#define NORMAL\n" +
            "\n" +
            "uniform float opacity;\n" +
            "\n" +
            "#if defined( FLAT_SHADED ) || defined( USE_BUMPMAP ) || ( defined( USE_NORMALMAP ) && ! defined( OBJECTSPACE_NORMALMAP ) )\n" +
            "\n" +
            "\tvarying vec3 vViewPosition;\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#ifndef FLAT_SHADED\n" +
            "\n" +
            "\tvarying vec3 vNormal;\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#include <packing>\n" +
            "#include <uv_pars_fragment>\n" +
            "#include <bumpmap_pars_fragment>\n" +
            "#include <normalmap_pars_fragment>\n" +
            "#include <logdepthbuf_pars_fragment>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <logdepthbuf_fragment>\n" +
            "\t#include <normal_fragment_begin>\n" +
            "\t#include <normal_fragment_maps>\n" +
            "\n" +
            "\tgl_FragColor = vec4( packNormalToRGB( normal ), opacity );\n" +
            "\n" +
            "}\n";
}
