package three.renderers.shaders.ShaderLibs;

public class LineDashedVert {
    public static final String code = "uniform float scale;\n" +
            "attribute float lineDistance;\n" +
            "\n" +
            "varying float vLineDistance;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <color_pars_vertex>\n" +
            "#include <fog_pars_vertex>\n" +
            "#include <logdepthbuf_pars_vertex>\n" +
            "#include <clipping_planes_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <color_vertex>\n" +
            "\n" +
            "\tvLineDistance = scale * lineDistance;\n" +
            "\n" +
            "\tvec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
            "\tgl_Position = projectionMatrix * mvPosition;\n" +
            "\n" +
            "\t#include <logdepthbuf_vertex>\n" +
            "\t#include <clipping_planes_vertex>\n" +
            "\t#include <fog_vertex>\n" +
            "\n" +
            "}\n";
}
