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

    public Light Copy(Light source){
        super.Copy(source, false);
        this.color.Copy(source.color);
        this.intensity = source.intensity;
        return this;
    }
}
