package three.renderers.shaders.ShaderChunks;

public class LightsPhongFragment {
    public static final String code =
            "BlinnPhongMaterial material;\n" +
            "material.diffuseColor = diffuseColor.rgb;\n" +
            "material.specularColor = specular;\n" +
            "material.specularShininess = shininess;\n" +
            "material.specularStrength = specularStrength;\n";
}
