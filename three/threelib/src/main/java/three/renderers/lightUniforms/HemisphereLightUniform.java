package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class HemisphereLightUniform extends UniformsObject {

    public HemisphereLightUniform(){
        super();
        this.Put("direction", new Vector3());
        this.Put("skyColor", new Color());
        this.Put("groundColor", new Color());
    }
}
