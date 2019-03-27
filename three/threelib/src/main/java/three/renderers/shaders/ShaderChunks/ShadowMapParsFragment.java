package three.renderers.shaders.ShaderChunks;

public class ShadowMapParsFragment {
    public static final String code =
            "#ifdef USE_SHADOWMAP\n" +
            "\n" +
            "\t#if NUM_DIR_LIGHTS > 0\n" +
            "\n" +
            "\t\tuniform sampler2D directionalShadowMap[ NUM_DIR_LIGHTS ];\n" +
            "\t\tvarying vec4 vDirectionalShadowCoord[ NUM_DIR_LIGHTS ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#if NUM_SPOT_LIGHTS > 0\n" +
            "\n" +
            "\t\tuniform sampler2D spotShadowMap[ NUM_SPOT_LIGHTS ];\n" +
            "\t\tvarying vec4 vSpotShadowCoord[ NUM_SPOT_LIGHTS ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#if NUM_POINT_LIGHTS > 0\n" +
            "\n" +
            "\t\tuniform sampler2D pointShadowMap[ NUM_POINT_LIGHTS ];\n" +
            "\t\tvarying vec4 vPointShadowCoord[ NUM_POINT_LIGHTS ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t/*\n" +
            "\t#if NUM_RECT_AREA_LIGHTS > 0\n" +
            "\n" +
            "\t\t// TODO (abelnation): create uniforms for area light shadows\n" +
            "\n" +
            "\t#endif\n" +
            "\t*/\n" +
            "\n" +
            "\tfloat texture2DCompare( sampler2D depths, vec2 uv, float compare ) {\n" +
            "\n" +
            "\t\treturn step( compare, unpackRGBAToDepth( texture2D( depths, uv ) ) );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\tfloat texture2DShadowLerp( sampler2D depths, vec2 size, vec2 uv, float compare ) {\n" +
            "\n" +
            "\t\tconst vec2 offset = vec2( 0.0, 1.0 );\n" +
            "\n" +
            "\t\tvec2 texelSize = vec2( 1.0 ) / size;\n" +
            "\t\tvec2 centroidUV = floor( uv * size + 0.5 ) / size;\n" +
            "\n" +
            "\t\tfloat lb = texture2DCompare( depths, centroidUV + texelSize * offset.xx, compare );\n" +
            "\t\tfloat lt = texture2DCompare( depths, centroidUV + texelSize * offset.xy, compare );\n" +
            "\t\tfloat rb = texture2DCompare( depths, centroidUV + texelSize * offset.yx, compare );\n" +
            "\t\tfloat rt = texture2DCompare( depths, centroidUV + texelSize * offset.yy, compare );\n" +
            "\n" +
            "\t\tvec2 f = fract( uv * size + 0.5 );\n" +
            "\n" +
            "\t\tfloat a = mix( lb, lt, f.y );\n" +
            "\t\tfloat b = mix( rb, rt, f.y );\n" +
            "\t\tfloat c = mix( a, b, f.x );\n" +
            "\n" +
            "\t\treturn c;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\tfloat getShadow( sampler2D shadowMap, vec2 shadowMapSize, float shadowBias, float shadowRadius, vec4 shadowCoord ) {\n" +
            "\n" +
            "\t\tfloat shadow = 1.0;\n" +
            "\n" +
            "\t\tshadowCoord.xyz /= shadowCoord.w;\n" +
            "\t\tshadowCoord.z += shadowBias;\n" +
            "\n" +
            "\t\t// if ( something && something ) breaks ATI OpenGL shader compiler\n" +
            "\t\t// if ( all( something, something ) ) using this instead\n" +
            "\n" +
            "\t\tbvec4 inFrustumVec = bvec4 ( shadowCoord.x >= 0.0, shadowCoord.x <= 1.0, shadowCoord.y >= 0.0, shadowCoord.y <= 1.0 );\n" +
            "\t\tbool inFrustum = all( inFrustumVec );\n" +
            "\n" +
            "\t\tbvec2 frustumTestVec = bvec2( inFrustum, shadowCoord.z <= 1.0 );\n" +
            "\n" +
            "\t\tbool frustumTest = all( frustumTestVec );\n" +
            "\n" +
            "\t\tif ( frustumTest ) {\n" +
            "\n" +
            "\t\t#if defined( SHADOWMAP_TYPE_PCF )\n" +
            "\n" +
            "\t\t\tvec2 texelSize = vec2( 1.0 ) / shadowMapSize;\n" +
            "\n" +
            "\t\t\tfloat dx0 = - texelSize.x * shadowRadius;\n" +
            "\t\t\tfloat dy0 = - texelSize.y * shadowRadius;\n" +
            "\t\t\tfloat dx1 = + texelSize.x * shadowRadius;\n" +
            "\t\t\tfloat dy1 = + texelSize.y * shadowRadius;\n" +
            "\n" +
            "\t\t\tshadow = (\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( dx0, dy0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( 0.0, dy0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( dx1, dy0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( dx0, 0.0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy, shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( dx1, 0.0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( dx0, dy1 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( 0.0, dy1 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, shadowCoord.xy + vec2( dx1, dy1 ), shadowCoord.z )\n" +
            "\t\t\t) * ( 1.0 / 9.0 );\n" +
            "\n" +
            "\t\t#elif defined( SHADOWMAP_TYPE_PCF_SOFT )\n" +
            "\n" +
            "\t\t\tvec2 texelSize = vec2( 1.0 ) / shadowMapSize;\n" +
            "\n" +
            "\t\t\tfloat dx0 = - texelSize.x * shadowRadius;\n" +
            "\t\t\tfloat dy0 = - texelSize.y * shadowRadius;\n" +
            "\t\t\tfloat dx1 = + texelSize.x * shadowRadius;\n" +
            "\t\t\tfloat dy1 = + texelSize.y * shadowRadius;\n" +
            "\n" +
            "\t\t\tshadow = (\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( dx0, dy0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( 0.0, dy0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( dx1, dy0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( dx0, 0.0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy, shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( dx1, 0.0 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( dx0, dy1 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( 0.0, dy1 ), shadowCoord.z ) +\n" +
            "\t\t\t\ttexture2DShadowLerp( shadowMap, shadowMapSize, shadowCoord.xy + vec2( dx1, dy1 ), shadowCoord.z )\n" +
            "\t\t\t) * ( 1.0 / 9.0 );\n" +
            "\n" +
            "\t\t#else // no percentage-closer filtering:\n" +
            "\n" +
            "\t\t\tshadow = texture2DCompare( shadowMap, shadowCoord.xy, shadowCoord.z );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t\t}\n" +
            "\n" +
            "\t\treturn shadow;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\t// cubeToUV() maps a 3D direction vector suitable for cube texture mapping to a 2D\n" +
            "\t// vector suitable for 2D texture mapping. This code uses the following layout for the\n" +
            "\t// 2D texture:\n" +
            "\t//\n" +
            "\t// xzXZ\n" +
            "\t//  y Y\n" +
            "\t//\n" +
            "\t// Y - Positive y direction\n" +
            "\t// y - Negative y direction\n" +
            "\t// X - Positive x direction\n" +
            "\t// x - Negative x direction\n" +
            "\t// Z - Positive z direction\n" +
            "\t// z - Negative z direction\n" +
            "\t//\n" +
            "\t// Source and test bed:\n" +
            "\t// https://gist.github.com/tschw/da10c43c467ce8afd0c4\n" +
            "\n" +
            "\tvec2 cubeToUV( vec3 v, float texelSizeY ) {\n" +
            "\n" +
            "\t\t// Number of texels to avoid at the edge of each square\n" +
            "\n" +
            "\t\tvec3 absV = abs( v );\n" +
            "\n" +
            "\t\t// intersect unit cube\n" +
            "\n" +
            "\t\tfloat scaleToCube = 1.0 / max( absV.x, max( absV.y, absV.z ) );\n" +
            "\t\tabsV *= scaleToCube;\n" +
            "\n" +
            "\t\t// Apply scale to avoid seams\n" +
            "\n" +
            "\t\t// two texels less per square (one texel will do for NEAREST)\n" +
            "\t\tv *= scaleToCube * ( 1.0 - 2.0 * texelSizeY );\n" +
            "\n" +
            "\t\t// Unwrap\n" +
            "\n" +
            "\t\t// space: -1 ... 1 range for each square\n" +
            "\t\t//\n" +
            "\t\t// #X##\t\tdim    := ( 4 , 2 )\n" +
            "\t\t//  # #\t\tcenter := ( 1 , 1 )\n" +
            "\n" +
            "\t\tvec2 planar = v.xy;\n" +
            "\n" +
            "\t\tfloat almostATexel = 1.5 * texelSizeY;\n" +
            "\t\tfloat almostOne = 1.0 - almostATexel;\n" +
            "\n" +
            "\t\tif ( absV.z >= almostOne ) {\n" +
            "\n" +
            "\t\t\tif ( v.z > 0.0 )\n" +
            "\t\t\t\tplanar.x = 4.0 - v.x;\n" +
            "\n" +
            "\t\t} else if ( absV.x >= almostOne ) {\n" +
            "\n" +
            "\t\t\tfloat signX = sign( v.x );\n" +
            "\t\t\tplanar.x = v.z * signX + 2.0 * signX;\n" +
            "\n" +
            "\t\t} else if ( absV.y >= almostOne ) {\n" +
            "\n" +
            "\t\t\tfloat signY = sign( v.y );\n" +
            "\t\t\tplanar.x = v.x + 2.0 * signY + 2.0;\n" +
            "\t\t\tplanar.y = v.z * signY - 2.0;\n" +
            "\n" +
            "\t\t}\n" +
            "\n" +
            "\t\t// Transform to UV space\n" +
            "\n" +
            "\t\t// scale := 0.5 / dim\n" +
            "\t\t// translate := ( center + 0.5 ) / dim\n" +
            "\t\treturn vec2( 0.125, 0.25 ) * planar + vec2( 0.375, 0.75 );\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\tfloat getPointShadow( sampler2D shadowMap, vec2 shadowMapSize, float shadowBias, float shadowRadius, vec4 shadowCoord, float shadowCameraNear, float shadowCameraFar ) {\n" +
            "\n" +
            "\t\tvec2 texelSize = vec2( 1.0 ) / ( shadowMapSize * vec2( 4.0, 2.0 ) );\n" +
            "\n" +
            "\t\t// for point lights, the uniform @vShadowCoord is re-purposed to hold\n" +
            "\t\t// the vector from the light to the world-space position of the fragment.\n" +
            "\t\tvec3 lightToPosition = shadowCoord.xyz;\n" +
            "\n" +
            "\t\t// dp = normalized distance from light to fragment position\n" +
            "\t\tfloat dp = ( length( lightToPosition ) - shadowCameraNear ) / ( shadowCameraFar - shadowCameraNear ); // need to clamp?\n" +
            "\t\tdp += shadowBias;\n" +
            "\n" +
            "\t\t// bd3D = base direction 3D\n" +
            "\t\tvec3 bd3D = normalize( lightToPosition );\n" +
            "\n" +
            "\t\t#if defined( SHADOWMAP_TYPE_PCF ) || defined( SHADOWMAP_TYPE_PCF_SOFT )\n" +
            "\n" +
            "\t\t\tvec2 offset = vec2( - 1, 1 ) * shadowRadius * texelSize.y;\n" +
            "\n" +
            "\t\t\treturn (\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.xyy, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.yyy, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.xyx, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.yyx, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.xxy, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.yxy, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.xxx, texelSize.y ), dp ) +\n" +
            "\t\t\t\ttexture2DCompare( shadowMap, cubeToUV( bd3D + offset.yxx, texelSize.y ), dp )\n" +
            "\t\t\t) * ( 1.0 / 9.0 );\n" +
            "\n" +
            "\t\t#else // no percentage-closer filtering\n" +
            "\n" +
            "\t\t\treturn texture2DCompare( shadowMap, cubeToUV( bd3D, texelSize.y ), dp );\n" +
            "\n" +
            "\t\t#endif\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "#endif\n";
}
