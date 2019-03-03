package three.lights;

import three.core.Object3D;
import three.math.Color;

public class HemisphereLight extends Light {

    public boolean castShadow = false;
    public Color groundColor;

    public HemisphereLight(Color skyColor, Color groundColor, float intensity){
        super(skyColor, intensity);
        this.type = "HemisphereLight";
        this.position.Copy(Object3D.DefaultUp);
        this.UpdateMatrix();
        this.groundColor = new Color(groundColor);
    }

    public HemisphereLight Copy(HemisphereLight source){
        super.Copy(source);
        this.groundColor.Copy( source.groundColor );

        return this;
    }
}
