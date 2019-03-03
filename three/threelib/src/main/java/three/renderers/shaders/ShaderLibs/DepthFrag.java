package three.renderers.shaders.ShaderLibs;

public class DepthFrag {
    public static final String code = "#if DEPTH_PACKING == 3200\n" +
            "\n" +
            "\tuniform float opacity;\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#include <common>\n" +
            "#include <packing>\n" +
            "#include <uv_pars_fragment>\n" +
            "#include <map_pars_fragment>\n" +
            "#include <alphamap_pars_fragment>\n" +
            "#include <logdepthbuf_pars_fragment>\n" +
            "#include <clipping_planes_pars_fragment>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <clipping_planes_fragment>\n" +
            "\n" +
            "\tvec4 diffuseColor = vec4( 1.0 );\n" +
            "\n" +
            "\t#if DEPTH_PACKING == 3200\n" +
            "\n" +
            "\t\tdiffuseColor.a = opacity;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#include <map_fragment>\n" +
            "\t#include <alphamap_fragment>\n" +
            "\t#include <alphatest_fragment>\n" +
            "\n" +
            "\t#include <logdepthbuf_fragment>\n" +
            "\n" +
            "\t#if DEPTH_PACKING == 3200\n" +
            "\n" +
            "\t\tgl_FragColor = vec4( vec3( 1.0 - gl_FragCoord.z ), opacity );\n" +
            "\n" +
            "\t#elif DEPTH_PACKING == 3201\n" +
            "\n" +
            "\t\tgl_FragColor = packDepthToRGBA( gl_FragCoord.z );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "}\n";
}
