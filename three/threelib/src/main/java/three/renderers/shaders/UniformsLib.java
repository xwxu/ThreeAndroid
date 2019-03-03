package three.renderers.shaders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

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
        common.Put("diffuse", new Color(0xeeeeee));
        common.Put("opacity", 1.0f);
        common.Put("map", null);
        common.Put("uvTransform", new Matrix3());
        common.Put("alphamap", null);

        specularmap.Put("specularMap", null);

        envmap.Put("envMap", null);
        envmap.Put("flipEnvMap", -1);
        envmap.Put("reflectivity", 1.0f);
        envmap.Put("refractionRatio", 0.98f);
        envmap.Put("maxMipLevel", 0);

        aomap.Put("aoMap", null);
        aomap.Put("aoMapIntensity", 1.0f);

        lightmap.Put("maxMipLevel", null);
        lightmap.Put("maxMipLevel", 1);

        emissivemap.Put("emissiveMap", null);

        bumpmap.Put("bumpMap", null);
        bumpmap.Put("bumpScale", 1.0f);

        normalmap.Put("normalMap", null);
        normalmap.Put("normalScale", new Vector2( 1, 1 ));

        displacementmap.Put("displacementMap", null);
        displacementmap.Put("displacementScale", 1.0f);
        displacementmap.Put("displacementBias", 0.0f);

        roughnessmap.Put("roughnessMap", null);

        metalnessmap.Put("metalnessMap", null);

        gradientmap.Put("gradientMap", null);

        fog.Put("fogDensity", 0.00025f);
        fog.Put("fogNear", 1.0f);
        fog.Put("fogFar", 2000.0f);
        fog.Put("fogColor", new Color( 0xffffff ));

        lights.Put("ambientLightColor", new ArrayList<>());
        lights.Put("directionalLights", new ArrayList<DirectionalLightUniform>());
        lights.Put("directionalShadowMap", new ArrayList<>());
        lights.Put("directionalShadowMatrix", new ArrayList<>());
        lights.Put("spotLights", new ArrayList<SpotLightUniform>());
        lights.Put("spotShadowMap", new ArrayList<>());
        lights.Put("spotShadowMatrix", new ArrayList<>());
        lights.Put("pointLights", new ArrayList<PointLightUniform>());
        lights.Put("pointShadowMap", new ArrayList<>());
        lights.Put("pointShadowMatrix", new ArrayList<>());
        lights.Put("hemisphereLights", new ArrayList<HemisphereLightUniform>());
        lights.Put("rectAreaLights", new ArrayList<RectAreaLightUniform>());

        points.Put("diffuse", new Color( 0xeeeeee ));
        points.Put("opacity", 1.0f);
        points.Put("size", 1.0f);
        points.Put("scale", 1.0f);
        points.Put("map", null);
        points.Put("uvTransform", new Matrix3());

        sprite.Put("diffuse", new Color( 0xeeeeee ));
        sprite.Put("opacity", 1.0f);
        sprite.Put("center", new Vector2( 0.5f, 0.5f ));
        sprite.Put("rotation", 0.0f);
        sprite.Put("map", null);
        sprite.Put("uvTransform", new Matrix3());

    }

}
