package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class RectAreaLightUniform extends UniformsObject {

    public RectAreaLightUniform(){
        super();
        this.Put("color", new Color());
        this.Put("position", new Vector3());
        this.Put("halfWidth", new Vector3());
        this.Put("halfHeight", new Vector3());
    }
}
