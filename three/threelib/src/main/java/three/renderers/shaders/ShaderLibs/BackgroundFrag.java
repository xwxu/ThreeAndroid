package three.renderers.shaders.ShaderLibs;

public class BackgroundFrag {
    public static final String code = "uniform sampler2D t2D;\n" +
            "\n" +
            "varying vec2 vUv;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\tvec4 texColor = texture2D( t2D, vUv );\n" +
            "\n" +
            "\tgl_FragColor = mapTexelToLinear( texColor );\n" +
            "\n" +
            "\t#include <tonemapping_fragment>\n" +
            "\t#include <encodings_fragment>\n" +
            "\n" +
            "}\n";
}
