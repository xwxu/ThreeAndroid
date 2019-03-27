package three.renderers.shaders;

import java.util.ArrayList;

import three.math.Color;
import three.math.Matrix3;
import three.math.Vector2;
import three.renderers.lightUniforms.DirectionalLightUniform;
import three.renderers.lightUniforms.HemisphereLightUniform;
import three.renderers.lightUniforms.PointLightUniform;
import three.renderers.lightUniforms.RectAreaLightUniform;
import three.renderers.lightUniforms.SpotLightUniform;

public class UniformsLib {
    public UniformsObject common = new UniformsObject();
    public UniformsObject specularmap = new UniformsObject();
    public UniformsObject envmap = new UniformsObject();
    public UniformsObject aomap = new UniformsObject();
    public UniformsObject lightmap = new UniformsObject();
    public UniformsObject emissivemap = new UniformsObject();
    public UniformsObject bumpmap = new UniformsObject();
    public UniformsObject normalmap = new UniformsObject();
    public UniformsObject displacementmap = new UniformsObject();
    public UniformsObject roughnessmap = new UniformsObject();
    public UniformsObject metalnessmap = new UniformsObject();
    public UniformsObject gradientmap = new UniformsObject();
    public UniformsObject fog = new UniformsObject();
    public UniformsObject lights = new UniformsObject();
    public UniformsObject points = new UniformsObject();
    public UniformsObject sprite = new UniformsObject();

    public UniformsLib(){
        common.put("diffuse", new Color(0xeeeeee));
        common.put("opacity", 1.0f);
        common.put("map", null);
        common.put("uvTransform", new Matrix3());
        common.put("alphamap", null);

        specularmap.put("specularMap", null);

        envmap.put("envMap", null);
        envmap.put("flipEnvMap", -1);
        envmap.put("reflectivity", 1.0f);
        envmap.put("refractionRatio", 0.98f);
        envmap.put("maxMipLevel", 0);

        aomap.put("aoMap", null);
        aomap.put("aoMapIntensity", 1.0f);

        lightmap.put("maxMipLevel", null);
        lightmap.put("maxMipLevel", 1);

        emissivemap.put("emissiveMap", null);

        bumpmap.put("bumpMap", null);
        bumpmap.put("bumpScale", 1.0f);

        normalmap.put("normalMap", null);
        normalmap.put("normalScale", new Vector2( 1, 1 ));

        displacementmap.put("displacementMap", null);
        displacementmap.put("displacementScale", 1.0f);
        displacementmap.put("displacementBias", 0.0f);

        roughnessmap.put("roughnessMap", null);

        metalnessmap.put("metalnessMap", null);

        gradientmap.put("gradientMap", null);

        fog.put("fogDensity", 0.00025f);
        fog.put("fogNear", 1.0f);
        fog.put("fogFar", 2000.0f);
        fog.put("fogColor", new Color( 0xffffff ));

        lights.put("ambientLightColor", new ArrayList<>());
        lights.put("directionalLights", new ArrayList<DirectionalLightUniform>());
        lights.put("directionalShadowMap", new ArrayList<>());
        lights.put("directionalShadowMatrix", new ArrayList<>());
        lights.put("spotLights", new ArrayList<SpotLightUniform>());
        lights.put("spotShadowMap", new ArrayList<>());
        lights.put("spotShadowMatrix", new ArrayList<>());
        lights.put("pointLights", new ArrayList<PointLightUniform>());
        lights.put("pointShadowMap", new ArrayList<>());
        lights.put("pointShadowMatrix", new ArrayList<>());
        lights.put("hemisphereLights", new ArrayList<HemisphereLightUniform>());
        lights.put("rectAreaLights", new ArrayList<RectAreaLightUniform>());

        points.put("diffuse", new Color( 0xeeeeee ));
        points.put("opacity", 1.0f);
        points.put("size", 1.0f);
        points.put("scale", 1.0f);
        points.put("map", null);
        points.put("uvTransform", new Matrix3());

        sprite.put("diffuse", new Color( 0xeeeeee ));
        sprite.put("opacity", 1.0f);
        sprite.put("center", new Vector2( 0.5f, 0.5f ));
        sprite.put("rotation", 0.0f);
        sprite.put("map", null);
        sprite.put("uvTransform", new Matrix3());

    }

}
