package three.renderers.shaders.ShaderLibs;

public class PointsFrag {
    public static final String code = "uniform vec3 diffuse;\n" +
            "uniform float opacity;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <color_pars_fragment>\n" +
            "#include <map_particle_pars_fragment>\n" +
            "#include <fog_pars_fragment>\n" +
            "#include <logdepthbuf_pars_fragment>\n" +
            "#include <clipping_planes_pars_fragment>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <clipping_planes_fragment>\n" +
            "\n" +
            "\tvec3 outgoingLight = vec3( 0.0 );\n" +
            "\tvec4 diffuseColor = vec4( diffuse, opacity );\n" +
            "\n" +
            "\t#include <logdepthbuf_fragment>\n" +
            "\t#include <map_particle_fragment>\n" +
            "\t#include <color_fragment>\n" +
            "\t#include <alphatest_fragment>\n" +
            "\n" +
            "\toutgoingLight = diffuseColor.rgb;\n" +
            "\n" +
            "\tgl_FragColor = vec4( outgoingLight, diffuseColor.a );\n" +
            "\n" +
            "\t#include <premultiplied_alpha_fragment>\n" +
            "\t#include <tonemapping_fragment>\n" +
            "\t#include <encodings_fragment>\n" +
            "\t#include <fog_fragment>\n" +
            "\n" +
            "}\n";
}
