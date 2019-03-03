package three.renderers.shaders.ShaderLibs;

public class CubeVert {
    public static final String code = "varying vec3 vWorldDirection;\n" +
            "\n" +
            "#include <common>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\tvWorldDirection = transformDirection( position, modelMatrix );\n" +
            "\n" +
            "\t#include <begin_vertex>\n" +
            "\t#include <project_vertex>\n" +
            "\n" +
            "\tgl_Position.z = gl_Position.w; // set z to camera.far\n" +
            "\n" +
            "}\n";
}
