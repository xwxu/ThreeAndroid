package three.lights;

import three.core.Object3D;
import three.math.Color;

public class HemisphereLight extends Light {

    public boolean castShadow = false;
    public Color groundColor;

    public HemisphereLight(Color skyColor, Color groundColor, float intensity){
        super(skyColor, intensity);
        this.type = "HemisphereLight";
        this.position.copy(Object3D.DefaultUp);
        this.updateMatrix();
        this.groundColor = new Color(groundColor);
    }

    public HemisphereLight copy(HemisphereLight source){
        super.copy(source);
        this.groundColor.copy( source.groundColor );
        return this;
    }
}
