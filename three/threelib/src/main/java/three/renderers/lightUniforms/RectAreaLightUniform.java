package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class RectAreaLightUniform extends UniformsObject {

    public RectAreaLightUniform(){
        super();
        this.put("color", new Color());
        this.put("position", new Vector3());
        this.put("halfWidth", new Vector3());
        this.put("halfHeight", new Vector3());
    }
}
