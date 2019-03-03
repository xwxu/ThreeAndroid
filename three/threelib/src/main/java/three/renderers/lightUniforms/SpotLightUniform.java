package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector2;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class SpotLightUniform extends UniformsObject {

    public SpotLightUniform(){
        super();
        this.Put("position", new Vector3());
        this.Put("direction", new Vector3());
        this.Put("color", new Color());
        this.Put("distance", 0.0f);
        this.Put("coneCos", 0.0f);
        this.Put("penumbraCos", 0.0f);
        this.Put("decay", 0.0f);
        this.Put("shadow", false);
        this.Put("shadowBias", 0.0f);
        this.Put("shadowRadius", 1.0f);
        this.Put("shadowMapSize", new Vector2());
    }
}
