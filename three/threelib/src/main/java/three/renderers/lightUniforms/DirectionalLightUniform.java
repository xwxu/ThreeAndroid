package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector2;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class DirectionalLightUniform extends UniformsObject {

    public DirectionalLightUniform(){
        super();
        this.put("direction", new Vector3());
        this.put("color", new Color());
        this.put("shadow", false);
        this.put("shadowBias", 0.0f);
        this.put("shadowRadius", 1.0f);
        this.put("shadowMapSize", new Vector2());
    }

}
