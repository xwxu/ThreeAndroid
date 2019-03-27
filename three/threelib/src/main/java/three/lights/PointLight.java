package three.lights;

import three.cameras.PerspectiveCamera;
import three.math.Color;

public class PointLight extends Light {

    public float distance;
    public float decay;
    public LightShadow shadow = new LightShadow(new PerspectiveCamera( 90, 1, 0.5f, 500 ));

    public PointLight(Color color, float intensity){
        super(color, intensity);
        this.type = "PointLight";
        this.distance = 0;
        this.decay = 1;
    }

    public PointLight(Color color, float intensity, float distance, float decay){
        super(color, intensity);
        this.type = "PointLight";
        this.distance = distance;
        this.decay = decay;
    }

    public PointLight copy(PointLight source){
        super.copy(source);
        this.distance = source.distance;
        this.decay = source.decay;
        this.shadow = source.shadow.clone();

        return this;
    }
}
