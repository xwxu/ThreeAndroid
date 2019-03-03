package three.renderers.shaders.ShaderChunks;

public class UvVertex {
    public static final String code =
            "#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP ) || defined( USE_EMISSIVEMAP ) || defined( USE_ROUGHNESSMAP ) || defined( USE_METALNESSMAP )\n" +
            "\n" +
            "\tvUv = ( uvTransform * vec3( uv, 1 ) ).xy;\n" +
            "\n" +
            "#endif\n";
}
