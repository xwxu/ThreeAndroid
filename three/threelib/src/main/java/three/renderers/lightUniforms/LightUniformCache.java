package three.renderers.lightUniforms;

import java.util.HashMap;

import three.lights.Light;
import three.math.Color;
import three.math.Vector2;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class LightUniformCache {

    HashMap<Integer, UniformsObject> lights = new HashMap<>();

    public UniformsObject get(Light light){

        UniformsObject uniforms = lights.get(light.id);
        if ( uniforms != null ) {
            return uniforms;
        }

        switch ( light.type ) {
            case "DirectionalLight":
                DirectionalLightUniform directionalLightUniform = new DirectionalLightUniform();
                uniforms = directionalLightUniform;
                break;

            case "SpotLight":
                SpotLightUniform spotLightUniform = new SpotLightUniform();
                uniforms = spotLightUniform;
                break;

            case "PointLight":
                PointLightUniform pointLightUniform = new PointLightUniform();
                uniforms = pointLightUniform;
                break;

            case "HemisphereLight":
                HemisphereLightUniform hemisphereLightUniform = new HemisphereLightUniform();
                uniforms = hemisphereLightUniform;
                break;

            case "RectAreaLight":
                RectAreaLightUniform rectAreaLightUniform = new RectAreaLightUniform();
                uniforms = rectAreaLightUniform;
                break;

        }

        lights.put(light.id, uniforms);

        return uniforms;
    }
}
