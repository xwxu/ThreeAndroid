package three.renderers.shaders.ShaderLibs;

public class MeshMatcapFrag {
    public static final String code = "#define MATCAP\n" +
            "\n" +
            "uniform vec3 diffuse;\n" +
            "uniform float opacity;\n" +
            "uniform sampler2D matcap;\n" +
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
            "#include <uv_pars_fragment>\n" +
            "#include <map_pars_fragment>\n" +
            "#include <alphamap_pars_fragment>\n" +
            "\n" +
            "#include <fog_pars_fragment>\n" +
            "#include <bumpmap_pars_fragment>\n" +
            "#include <normalmap_pars_fragment>\n" +
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
            "\t#include <alphamap_fragment>\n" +
            "\t#include <alphatest_fragment>\n" +
            "\t#include <normal_fragment_begin>\n" +
            "\t#include <normal_fragment_maps>\n" +
            "\n" +
            "\tvec3 viewDir = normalize( vViewPosition );\n" +
            "\tvec3 x = normalize( vec3( viewDir.z, 0.0, - viewDir.x ) );\n" +
            "\tvec3 y = cross( viewDir, x );\n" +
            "\tvec2 uv = vec2( dot( x, normal ), dot( y, normal ) ) * 0.495 + 0.5; // 0.495 to remove artifacts caused by undersized matcap disks\n" +
            "\n" +
            "\t#ifdef USE_MATCAP\n" +
            "\n" +
            "\t\tvec4 matcapColor = texture2D( matcap, uv );\n" +
            "\t\tmatcapColor = matcapTexelToLinear( matcapColor );\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tvec4 matcapColor = vec4( 1.0 );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\tvec3 outgoingLight = diffuseColor.rgb * matcapColor.rgb;\n" +
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
