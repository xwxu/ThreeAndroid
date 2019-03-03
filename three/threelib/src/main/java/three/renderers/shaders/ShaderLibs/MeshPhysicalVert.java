package three.renderers.shaders.ShaderLibs;

public class MeshPhysicalVert {
    public static final String code = "#define PHYSICAL\n" +
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
            "#include <uv2_pars_vertex>\n" +
            "#include <displacementmap_pars_vertex>\n" +
            "#include <color_pars_vertex>\n" +
            "#include <fog_pars_vertex>\n" +
            "#include <morphtarget_pars_vertex>\n" +
            "#include <skinning_pars_vertex>\n" +
            "#include <shadowmap_pars_vertex>\n" +
            "#include <logdepthbuf_pars_vertex>\n" +
            "#include <clipping_planes_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <uv_vertex>\n" +
            "\t#include <uv2_vertex>\n" +
            "\t#include <color_vertex>\n" +
            "\n" +
            "\t#include <beginnormal_vertex>\n" +
            "\t#include <morphnormal_vertex>\n" +
            "\t#include <skinbase_vertex>\n" +
            "\t#include <skinnormal_vertex>\n" +
            "\t#include <defaultnormal_vertex>\n" +
            "\n" +
            "#ifndef FLAT_SHADED // Normal computed with derivatives when FLAT_SHADED\n" +
            "\n" +
            "\tvNormal = normalize( transformedNormal );\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "\t#include <begin_vertex>\n" +
            "\t#include <morphtarget_vertex>\n" +
            "\t#include <skinning_vertex>\n" +
            "\t#include <displacementmap_vertex>\n" +
            "\t#include <project_vertex>\n" +
            "\t#include <logdepthbuf_vertex>\n" +
            "\t#include <clipping_planes_vertex>\n" +
            "\n" +
            "\tvViewPosition = - mvPosition.xyz;\n" +
            "\n" +
            "\t#include <worldpos_vertex>\n" +
            "\t#include <shadowmap_vertex>\n" +
            "\t#include <fog_vertex>\n" +
            "\n" +
            "}\n";
}
