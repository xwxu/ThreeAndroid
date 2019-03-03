package three.renderers.shaders.ShaderLibs;

public class MeshMatcapVert {
    public static final String code = "#define MATCAP\n" +
            "\n" +
            "varying vec3 vViewPosition;\n" +
            "\n" +
            "#ifndef FLAT_SHADED\n" +
            "\n" +
            "\tvarying vec3 vNormal;\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#include <common>\n" +
            "#include <uv_pars_vertex>\n" +
            "#include <displacementmap_pars_vertex>\n" +
            "#include <fog_pars_vertex>\n" +
            "#include <morphtarget_pars_vertex>\n" +
            "#include <skinning_pars_vertex>\n" +
            "\n" +
            "#include <logdepthbuf_pars_vertex>\n" +
            "#include <clipping_planes_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <uv_vertex>\n" +
            "\n" +
            "\t#include <beginnormal_vertex>\n" +
            "\t#include <morphnormal_vertex>\n" +
            "\t#include <skinbase_vertex>\n" +
            "\t#include <skinnormal_vertex>\n" +
            "\t#include <defaultnormal_vertex>\n" +
            "\n" +
            "\t#ifndef FLAT_SHADED // Normal computed with derivatives when FLAT_SHADED\n" +
            "\n" +
            "\t\tvNormal = normalize( transformedNormal );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#include <begin_vertex>\n" +
            "\t#include <morphtarget_vertex>\n" +
            "\t#include <skinning_vertex>\n" +
            "\t#include <displacementmap_vertex>\n" +
            "\t#include <project_vertex>\n" +
            "\n" +
            "\t#include <logdepthbuf_vertex>\n" +
            "\t#include <clipping_planes_vertex>\n" +
            "\t#include <fog_vertex>\n" +
            "\n" +
            "\tvViewPosition = - mvPosition.xyz;\n" +
            "\n" +
            "}\n";
}
