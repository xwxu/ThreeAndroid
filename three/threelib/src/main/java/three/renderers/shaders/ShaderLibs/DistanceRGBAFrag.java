package three.renderers.shaders.ShaderLibs;

public class DistanceRGBAFrag {
    public static final String code = "#define DISTANCE\n" +
            "\n" +
            "uniform vec3 referencePosition;\n" +
            "uniform float nearDistance;\n" +
            "uniform float farDistance;\n" +
            "varying vec3 vWorldPosition;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <packing>\n" +
            "#include <uv_pars_fragment>\n" +
            "#include <map_pars_fragment>\n" +
            "#include <alphamap_pars_fragment>\n" +
            "#include <clipping_planes_pars_fragment>\n" +
            "\n" +
            "void main () {\n" +
            "\n" +
            "\t#include <clipping_planes_fragment>\n" +
            "\n" +
            "\tvec4 diffuseColor = vec4( 1.0 );\n" +
            "\n" +
            "\t#include <map_fragment>\n" +
            "\t#include <alphamap_fragment>\n" +
            "\t#include <alphatest_fragment>\n" +
            "\n" +
            "\tfloat dist = length( vWorldPosition - referencePosition );\n" +
            "\tdist = ( dist - nearDistance ) / ( farDistance - nearDistance );\n" +
            "\tdist = saturate( dist ); // clamp to [ 0, 1 ]\n" +
            "\n" +
            "\tgl_FragColor = packDepthToRGBA( dist );\n" +
            "\n" +
            "}\n";
}
