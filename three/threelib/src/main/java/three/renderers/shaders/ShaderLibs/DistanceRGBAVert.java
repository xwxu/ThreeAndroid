package three.renderers.shaders.ShaderLibs;

public class DistanceRGBAVert {
    public static final String code = "#define DISTANCE\n" +
            "\n" +
            "varying vec3 vWorldPosition;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <uv_pars_vertex>\n" +
            "#include <displacementmap_pars_vertex>\n" +
            "#include <morphtarget_pars_vertex>\n" +
            "#include <skinning_pars_vertex>\n" +
            "#include <clipping_planes_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <uv_vertex>\n" +
            "\n" +
            "\t#include <skinbase_vertex>\n" +
            "\n" +
            "\t#ifdef USE_DISPLACEMENTMAP\n" +
            "\n" +
            "\t\t#include <beginnormal_vertex>\n" +
            "\t\t#include <morphnormal_vertex>\n" +
            "\t\t#include <skinnormal_vertex>\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#include <begin_vertex>\n" +
            "\t#include <morphtarget_vertex>\n" +
            "\t#include <skinning_vertex>\n" +
            "\t#include <displacementmap_vertex>\n" +
            "\t#include <project_vertex>\n" +
            "\t#include <worldpos_vertex>\n" +
            "\t#include <clipping_planes_vertex>\n" +
            "\n" +
            "\tvWorldPosition = worldPosition.xyz;\n" +
            "\n" +
            "}\n";
}
