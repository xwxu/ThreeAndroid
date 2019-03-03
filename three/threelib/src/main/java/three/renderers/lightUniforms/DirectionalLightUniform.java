package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector2;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class DirectionalLightUniform extends UniformsObject {

    public DirectionalLightUniform(){
        super();
        this.Put("direction", new Vector3());
        this.Put("color", new Color());
        this.Put("shadow", false);
        this.Put("shadowBias", 0.0f);
        this.Put("shadowRadius", 1.0f);
        this.Put("shadowMapSize", new Vector2());
    }

}
