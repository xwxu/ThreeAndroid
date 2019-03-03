package three.renderers.shaders.ShaderLibs;

public class SpriteVert {
    public static final String code = "uniform float rotation;\n" +
            "uniform vec2 center;\n" +
            "\n" +
            "#include <common>\n" +
            "#include <uv_pars_vertex>\n" +
            "#include <fog_pars_vertex>\n" +
            "#include <logdepthbuf_pars_vertex>\n" +
            "#include <clipping_planes_pars_vertex>\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "\t#include <uv_vertex>\n" +
            "\n" +
            "\tvec4 mvPosition = modelViewMatrix * vec4( 0.0, 0.0, 0.0, 1.0 );\n" +
            "\n" +
            "\tvec2 scale;\n" +
            "\tscale.x = length( vec3( modelMatrix[ 0 ].x, modelMatrix[ 0 ].y, modelMatrix[ 0 ].z ) );\n" +
            "\tscale.y = length( vec3( modelMatrix[ 1 ].x, modelMatrix[ 1 ].y, modelMatrix[ 1 ].z ) );\n" +
            "\n" +
            "\t#ifndef USE_SIZEATTENUATION\n" +
            "\n" +
            "\t\tbool isPerspective = ( projectionMatrix[ 2 ][ 3 ] == - 1.0 );\n" +
            "\n" +
            "\t\tif ( isPerspective ) scale *= - mvPosition.z;\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\tvec2 alignedPosition = ( position.xy - ( center - vec2( 0.5 ) ) ) * scale;\n" +
            "\n" +
            "\tvec2 rotatedPosition;\n" +
            "\trotatedPosition.x = cos( rotation ) * alignedPosition.x - sin( rotation ) * alignedPosition.y;\n" +
            "\trotatedPosition.y = sin( rotation ) * alignedPosition.x + cos( rotation ) * alignedPosition.y;\n" +
            "\n" +
            "\tmvPosition.xy += rotatedPosition;\n" +
            "\n" +
            "\tgl_Position = projectionMatrix * mvPosition;\n" +
            "\n" +
            "\t#include <logdepthbuf_vertex>\n" +
            "\t#include <clipping_planes_vertex>\n" +
            "\t#include <fog_vertex>\n" +
            "\n" +
            "}\n";
}
