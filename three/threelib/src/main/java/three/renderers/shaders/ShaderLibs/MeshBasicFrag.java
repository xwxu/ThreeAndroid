package three.renderers.shaders.ShaderLibs;

public class MeshBasicFrag {
    public static final String code = "uniform vec3 diffuse;\n" +
            "uniform float opacity;\n" +
            "\n" +
            "#ifndef FLAT_SHADED\n" +
            "\n" +
            "\tvarying vec3 vNormal;\n" +
            "\n" +
            "#endif\n" +
            "\n" +
            "#include <common>\n" +
            "#include <color_pars_fragment>\n" +
            "#include <uv_pars_fragment>\n" +
            "#include <uv2_pars_fragment>\n" +
            "#include <map_pars_fragment>\n" +
            "#include <alphamap_pars_fragment>\n" +
            "#include <aomap_pars_fragment>\n" +
            "#include <lightmap_pars_fragment>\n" +
            "#include <envmap_pars_fragment>\n" +
            "#include <fog_pars_fragment>\n" +
            "#include <specularmap_pars_fragment>\n" +
            "#include <logdepthbuf_pars_fragment>\n" +
            "#include <clipping_planes_pars_fragment>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <clipping_planes_fragment>\n" +
            "\n" +
            "\tvec4 diffuseColor = vec4( diffuse, opacity );\n" +
            "\n" +
            "\t#include <logdepthbuf_fragment>\n" +
            "\t#include <map_fragment>\n" +
            "\t#include <color_fragment>\n" +
            "\t#include <alphamap_fragment>\n" +
            "\t#include <alphatest_fragment>\n" +
            "\t#include <specularmap_fragment>\n" +
            "\n" +
            "\tReflectedLight reflectedLight = ReflectedLight( vec3( 0.0 ), vec3( 0.0 ), vec3( 0.0 ), vec3( 0.0 ) );\n" +
            "\n" +
            "\t// accumulation (baked indirect lighting only)\n" +
            "\t#ifdef USE_LIGHTMAP\n" +
            "\n" +
            "\t\treflectedLight.indirectDiffuse += texture2D( lightMap, vUv2 ).xyz * lightMapIntensity;\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\treflectedLight.indirectDiffuse += vec3( 1.0 );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t// modulation\n" +
            "\t#include <aomap_fragment>\n" +
            "\n" +
            "\treflectedLight.indirectDiffuse *= diffuseColor.rgb;\n" +
            "\n" +
            "\tvec3 outgoingLight = reflectedLight.indirectDiffuse;\n" +
            "\n" +
            "\t#include <envmap_fragment>\n" +
            "\n" +
            "\tgl_FragColor = vec4( outgoingLight, diffuseColor.a );\n" +
            "\n" +
            "\t#include <premultiplied_alpha_fragment>\n" +
            "\t#include <tonemapping_fragment>\n" +
            "\t#include <encodings_fragment>\n" +
            "\t#include <fog_fragment>\n" +
            "\n" +
            "}\n";
}
