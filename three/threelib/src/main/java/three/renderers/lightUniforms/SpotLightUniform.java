package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector2;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class SpotLightUniform extends UniformsObject {

    public SpotLightUniform(){
        super();
        this.put("position", new Vector3());
        this.put("direction", new Vector3());
        this.put("color", new Color());
        this.put("distance", 0.0f);
        this.put("coneCos", 0.0f);
        this.put("penumbraCos", 0.0f);
        this.put("decay", 0.0f);
        this.put("shadow", false);
        this.put("shadowBias", 0.0f);
        this.put("shadowRadius", 1.0f);
        this.put("shadowMapSize", new Vector2());
    }
}
