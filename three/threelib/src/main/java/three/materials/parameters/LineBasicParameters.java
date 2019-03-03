package three.materials.parameters;

import three.math.Color;

public class LineBasicParameters extends LineParameters {
    public Color color = new Color(0xffffff);
    public String linecap = "round";
    public String linejoin = "round";

    public LineBasicParameters(){
        super();
        this.lights = false;
    }
}
