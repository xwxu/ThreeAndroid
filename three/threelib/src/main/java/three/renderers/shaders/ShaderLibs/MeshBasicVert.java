package three.renderers.shaders.ShaderLibs;

public class MeshBasicVert {
    public static final String code = "#include <common>\n" +
            "#include <uv_pars_vertex>\n" +
            "#include <uv2_pars_vertex>\n" +
            "#include <envmap_pars_vertex>\n" +
            "#include <color_pars_vertex>\n" +
            "#include <fog_pars_vertex>\n" +
            "#include <morphtarget_pars_vertex>\n" +
            "#include <skinning_pars_vertex>\n" +
            "#include <logdepthbuf_pars_vertex>\n" +
            "#include <clipping_planes_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <uv_vertex>\n" +
            "\t#include <uv2_vertex>\n" +
            "\t#include <color_vertex>\n" +
            "\t#include <skinbase_vertex>\n" +
            "\n" +
            "\t#ifdef USE_ENVMAP\n" +
            "\n" +
            "\t#include <beginnormal_vertex>\n" +
            "\t#include <morphnormal_vertex>\n" +
            "\t#include <skinnormal_vertex>\n" +
            "\t#include <defaultnormal_vertex>\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#include <begin_vertex>\n" +
            "\t#include <morphtarget_vertex>\n" +
            "\t#include <skinning_vertex>\n" +
            "\t#include <project_vertex>\n" +
            "\t#include <logdepthbuf_vertex>\n" +
            "\n" +
            "\t#include <worldpos_vertex>\n" +
            "\t#include <clipping_planes_vertex>\n" +
            "\t#include <envmap_vertex>\n" +
            "\t#include <fog_vertex>\n" +
            "\n" +
            "}\n";
}
