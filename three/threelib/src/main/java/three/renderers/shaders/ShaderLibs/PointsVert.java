package three.renderers.shaders.ShaderLibs;

public class PointsVert {
    public static final String code = "uniform float size;\n" +
            "uniform float scale;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <color_pars_vertex>\n" +
            "#include <fog_pars_vertex>\n" +
            "#include <morphtarget_pars_vertex>\n" +
            "#include <logdepthbuf_pars_vertex>\n" +
            "#include <clipping_planes_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <color_vertex>\n" +
            "\t#include <begin_vertex>\n" +
            "\t#include <morphtarget_vertex>\n" +
            "\t#include <project_vertex>\n" +
            "\n" +
            "\tgl_PointSize = size;\n" +
            "\n" +
            "\t#ifdef USE_SIZEATTENUATION\n" +
            "\n" +
            "\t\tbool isPerspective = ( projectionMatrix[ 2 ][ 3 ] == - 1.0 );\n" +
            "\n" +
            "\t\tif ( isPerspective ) gl_PointSize *= ( scale / - mvPosition.z );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#include <logdepthbuf_vertex>\n" +
            "\t#include <clipping_planes_vertex>\n" +
            "\t#include <worldpos_vertex>\n" +
            "\t#include <fog_vertex>\n" +
            "\n" +
            "}\n";
}
