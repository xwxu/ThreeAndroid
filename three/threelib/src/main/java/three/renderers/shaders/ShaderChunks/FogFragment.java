package three.renderers.shaders.ShaderChunks;

public class FogFragment {
    public static final String code =
            "#ifdef USE_FOG\n" +
            "\n" +
            "\t#ifdef FOG_EXP2\n" +
            "\n" +
            "\t\tfloat fogFactor = whiteCompliment( exp2( - fogDensity * fogDensity * fogDepth * fogDepth * LOG2 ) );\n" +
            "\n" +
            "\t#else\n" +
            "\n" +
            "\t\tfloat fogFactor = smoothstep( fogNear, fogFar, fogDepth );\n" +
            "\n" +
            "\t#endif\n" +
            "\n" +
            "\tgl_FragColor.rgb = mix( gl_FragColor.rgb, fogColor, fogFactor );\n" +
            "\n" +
            "#endif\n";
}
