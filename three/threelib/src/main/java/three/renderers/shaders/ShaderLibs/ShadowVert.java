package three.renderers.shaders.ShaderLibs;

public class ShadowVert {
    public static final String code = "#include <fog_pars_vertex>\n" +
            "#include <shadowmap_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <begin_vertex>\n" +
            "\t#include <project_vertex>\n" +
            "\t#include <worldpos_vertex>\n" +
            "\t#include <shadowmap_vertex>\n" +
            "\t#include <fog_vertex>\n" +
            "\n" +
            "}\n";
}
