package three.renderers.shaders.ShaderLibs;

public class CubeFrag {
    public static final String code = "uniform samplerCube tCube;\n" +
            "uniform float tFlip;\n" +
            "uniform float opacity;\n" +
            "\n" +
            "varying vec3 vWorldDirection;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\tvec4 texColor = textureCube( tCube, vec3( tFlip * vWorldDirection.x, vWorldDirection.yz ) );\n" +
            "\n" +
            "\tgl_FragColor = mapTexelToLinear( texColor );\n" +
            "\tgl_FragColor.a *= opacity;\n" +
            "\n" +
            "\t#include <tonemapping_fragment>\n" +
            "\t#include <encodings_fragment>\n" +
            "\n" +
            "}\n";
}
