package three.lights;

import three.math.Color;

public class RectAreaLight extends Light {

    public float width;
    public float height;

    public RectAreaLight(Color color, float intensity){
        super(color, intensity);
        this.type = "RectAreaLight";
        this.width = 10;
        this.height = 10;
    }

    public RectAreaLight(Color color, float intensity, float width, float height){
        super(color, intensity);
        this.type = "RectAreaLight";
        this.width = width;
        this.height = height;
    }

    public RectAreaLight Copy(RectAreaLight source){
        super.Copy(source);
        this.width = source.width;
        this.height = source.height;

        return this;
    }

}
