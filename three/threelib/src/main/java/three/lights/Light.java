package three.lights;

import three.core.Object3D;
import three.math.Color;

public class Light extends Object3D {
    public Color color;
    public float intensity;
    public boolean receiveShadow;

    public float distance;
    public LightShadow shadow = null;


    public Light(Color color, float intensity){
        super();
        this.type = "Light";
        this.color = color;
        this.intensity = intensity;
        this.receiveShadow = false;
    }

    public Light copy(Light source){
        super.copy(source, false);
        this.color.copy(source.color);
        this.intensity = source.intensity;
        return this;
    }
}
