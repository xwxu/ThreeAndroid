package three.lights;

import three.math.Color;

public class AmbientLight extends Light {

    public boolean castShadow;

    public AmbientLight(Color color, float intensity){
        super(color, intensity);
        this.type = "AmbientLight";
        this.castShadow = false;
    }
}
