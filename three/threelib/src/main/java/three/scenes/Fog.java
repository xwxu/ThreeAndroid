package three.scenes;

import three.math.Color;

public class Fog {
    public String name = "";
    public Color color;
    public float near;
    public float far;

    public Fog(Color color, float near, float far){
        this.color = color;
        this.near = near;
        this.far = far;
    }

    public Fog(Color color){
        this.color = color;
        this.near = 1;
        this.far = 1000;
    }

}
