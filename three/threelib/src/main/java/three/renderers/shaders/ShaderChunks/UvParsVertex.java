package three.renderers.shaders.ShaderChunks;

public class UvParsVertex {
    public static final String code =
            "#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP ) || defined( USE_EMISSIVEMAP ) || defined( USE_ROUGHNESSMAP ) || defined( USE_METALNESSMAP )\n" +
                    "\n" +
                    "\tvarying vec2 vUv;\n" +
                    "\tuniform mat3 uvTransform;\n" +
                    "\n" +
                    "#endif\n";
}
