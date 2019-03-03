package three.renderers.shaders.ShaderChunks;

public class ShadowMapParsVertex {
    public static final String code =
            "#ifdef USE_SHADOWMAP\n" +
            "\n" +
            "\t#if NUM_DIR_LIGHTS > 0\n" +
            "\n" +
            "\t\tuniform mat4 directionalShadowMatrix[ NUM_DIR_LIGHTS ];\n" +
            "\t\tvarying vec4 vDirectionalShadowCoord[ NUM_DIR_LIGHTS ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#if NUM_SPOT_LIGHTS > 0\n" +
            "\n" +
            "\t\tuniform mat4 spotShadowMatrix[ NUM_SPOT_LIGHTS ];\n" +
            "\t\tvarying vec4 vSpotShadowCoord[ NUM_SPOT_LIGHTS ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t#if NUM_POINT_LIGHTS > 0\n" +
            "\n" +
            "\t\tuniform mat4 pointShadowMatrix[ NUM_POINT_LIGHTS ];\n" +
            "\t\tvarying vec4 vPointShadowCoord[ NUM_POINT_LIGHTS ];\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\t/*\n" +
            "\t#if NUM_RECT_AREA_LIGHTS > 0\n" +
            "\n" +
            "\t\t// TODO (abelnation): uniforms for area light shadows\n" +
            "\n" +
            "\t#endif\n" +
            "\t*/\n" +
            "\n" +
            "#endif\n";
}
