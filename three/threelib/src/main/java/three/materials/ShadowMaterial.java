package three.materials;

import three.materials.parameters.ShadowParameters;
import three.math.Color;

public class ShadowMaterial extends Material{

    public Color color;

    public ShadowMaterial(ShadowParameters parameters){
        super(parameters);
        type = "ShadowMaterial";
        color = parameters.color;
    }


}
