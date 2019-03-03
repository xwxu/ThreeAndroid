package three.renderers.shaders.ShaderChunks;

public class ShadowMapVertex {
    public static final String code =
            "#ifdef USE_SHADOWMAP\n" +
            "\n" +
            "\t#if NUM_DIR_LIGHTS > 0\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_DIR_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tvDirectionalShadowCoord[ i ] = directionalShadowMatrix[ i ] * worldPosition;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#if NUM_SPOT_LIGHTS > 0\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_SPOT_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tvSpotShadowCoord[ i ] = spotShadowMatrix[ i ] * worldPosition;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#if NUM_POINT_LIGHTS > 0\n" +
            "\n" +
            "\t#pragma unroll_loop\n" +
            "\tfor ( int i = 0; i < NUM_POINT_LIGHTS; i ++ ) {\n" +
            "\n" +
            "\t\tvPointShadowCoord[ i ] = pointShadowMatrix[ i ] * worldPosition;\n" +
            "\n" +
            "\t}\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t/*\n" +
            "\t#if NUM_RECT_AREA_LIGHTS > 0\n" +
            "\n" +
            "\t\t// TODO (abelnation): update vAreaShadowCoord with area light info\n" +
            "\n" +
            "\t#endif\n" +
            "\t*/\n" +
            "\n" +
            "#endif\n";
}
