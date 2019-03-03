package three.renderers.shaders.ShaderChunks;

public class TonemappingParsFragment {
    public static final String code =
            "#ifndef saturate\n" +
            "\t#define saturate(a) clamp( a, 0.0, 1.0 )\n" +
            "#endif\n" +
            "\n" +
            "uniform float toneMappingExposure;\n" +
            "uniform float toneMappingWhitePoint;\n" +
            "\n" +
            "// exposure only\n" +
            "vec3 LinearToneMapping( vec3 color ) {\n" +
            "\n" +
            "\treturn toneMappingExposure * color;\n" +
            "\n" +
            "}\n" +
            "\n" +
            "// source: https://www.cs.utah.edu/~reinhard/cdrom/\n" +
            "vec3 ReinhardToneMapping( vec3 color ) {\n" +
            "\n" +
            "\tcolor *= toneMappingExposure;\n" +
            "\treturn saturate( color / ( vec3( 1.0 ) + color ) );\n" +
            "\n" +
            "}\n" +
            "\n" +
            "// source: http://filmicgames.com/archives/75\n" +
            "#define Uncharted2Helper( x ) max( ( ( x * ( 0.15 * x + 0.10 * 0.50 ) + 0.20 * 0.02 ) / ( x * ( 0.15 * x + 0.50 ) + 0.20 * 0.30 ) ) - 0.02 / 0.30, vec3( 0.0 ) )\n" +
            "vec3 Uncharted2ToneMapping( vec3 color ) {\n" +
            "\n" +
            "\t// John Hable's filmic operator from Uncharted 2 video game\n" +
            "\tcolor *= toneMappingExposure;\n" +
            "\treturn saturate( Uncharted2Helper( color ) / Uncharted2Helper( vec3( toneMappingWhitePoint ) ) );\n" +
            "\n" +
            "}\n" +
            "\n" +
            "// source: http://filmicgames.com/archives/75\n" +
            "vec3 OptimizedCineonToneMapping( vec3 color ) {\n" +
            "\n" +
            "\t// optimized filmic operator by Jim Hejl and Richard Burgess-Dawson\n" +
            "\tcolor *= toneMappingExposure;\n" +
            "\tcolor = max( vec3( 0.0 ), color - 0.004 );\n" +
            "\treturn pow( ( color * ( 6.2 * color + 0.5 ) ) / ( color * ( 6.2 * color + 1.7 ) + 0.06 ), vec3( 2.2 ) );\n" +
            "\n" +
            "}\n" +
            "\n" +
            "// source: https://knarkowicz.wordpress.com/2016/01/06/aces-filmic-tone-mapping-curve/\n" +
            "vec3 ACESFilmicToneMapping( vec3 color ) {\n" +
            "\n" +
            "\tcolor *= toneMappingExposure;\n" +
            "\treturn saturate( ( color * ( 2.51 * color + 0.03 ) ) / ( color * ( 2.43 * color + 0.59 ) + 0.14 ) );\n" +
            "\n" +
            "}\n";
}
