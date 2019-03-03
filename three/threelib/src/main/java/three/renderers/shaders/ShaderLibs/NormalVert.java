package three.renderers.shaders.ShaderLibs;

public class NormalVert {
    public static final String code = "#define NORMAL\n" +
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
            "#include <uv_pars_vertex>\n" +
            "#include <displacementmap_pars_vertex>\n" +
            "#include <morphtarget_pars_vertex>\n" +
            "#include <skinning_pars_vertex>\n" +
            "#include <logdepthbuf_pars_vertex>\n" +
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
            "\n" +
            "#if defined( FLAT_SHADED ) || defined( USE_BUMPMAP ) || ( defined( USE_NORMALMAP ) && ! defined( OBJECTSPACE_NORMALMAP ) )\n" +
            "\n" +
            "\tvViewPosition = - mvPosition.xyz;\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "}\n";
}
