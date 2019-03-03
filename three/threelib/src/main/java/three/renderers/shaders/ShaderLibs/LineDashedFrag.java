package three.renderers.shaders.ShaderLibs;

public class LineDashedFrag {
    public static final String code = "uniform vec3 diffuse;\n" +
            "uniform float opacity;\n" +
            "\n" +
            "uniform float dashSize;\n" +
            "uniform float totalSize;\n" +
            "\n" +
            "varying float vLineDistance;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <color_pars_fragment>\n" +
            "#include <fog_pars_fragment>\n" +
            "#include <logdepthbuf_pars_fragment>\n" +
            "#include <clipping_planes_pars_fragment>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <clipping_planes_fragment>\n" +
            "\n" +
            "\tif ( mod( vLineDistance, totalSize ) > dashSize ) {\n" +
            "\n" +
            "\t\tdiscard;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\tvec3 outgoingLight = vec3( 0.0 );\n" +
            "\tvec4 diffuseColor = vec4( diffuse, opacity );\n" +
            "\n" +
            "\t#include <logdepthbuf_fragment>\n" +
            "\t#include <color_fragment>\n" +
            "\n" +
            "\toutgoingLight = diffuseColor.rgb; // simple shader\n" +
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
