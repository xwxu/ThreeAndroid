package three.renderers.lightUniforms;

import three.math.Color;
import three.math.Vector3;
import three.renderers.shaders.UniformsObject;

public class HemisphereLightUniform extends UniformsObject {

    public HemisphereLightUniform(){
        super();
        this.put("direction", new Vector3());
        this.put("skyColor", new Color());
        this.put("groundColor", new Color());
    }
}
