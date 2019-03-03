package three.renderers.shaders.ShaderChunks;

public class LightsPhysicalFragment {
    public static final String code =
            "PhysicalMaterial material;\n" +
            "material.diffuseColor = diffuseColor.rgb * ( 1.0 - metalnessFactor );\n" +
            "material.specularRoughness = clamp( roughnessFactor, 0.04, 1.0 );\n" +
            "#ifdef STANDARD\n" +
            "\tmaterial.specularColor = mix( vec3( DEFAULT_SPECULAR_COEFFICIENT ), diffuseColor.rgb, metalnessFactor );\n" +
            "#else\n" +
            "\tmaterial.specularColor = mix( vec3( MAXIMUM_SPECULAR_COEFFICIENT * pow2( reflectivity ) ), diffuseColor.rgb, metalnessFactor );\n" +
            "\tmaterial.clearCoat = saturate( clearCoat ); // Burley clearcoat model\n" +
            "\tmaterial.clearCoatRoughness = clamp( clearCoatRoughness, 0.04, 1.0 );\n" +
            "#endif\n";
}
