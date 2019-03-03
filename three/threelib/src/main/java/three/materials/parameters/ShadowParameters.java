package three.materials.parameters;

import three.math.Color;

public class ShadowParameters extends MaterialParameters {
    public Color color = new Color(0x000000);

    public ShadowParameters(){
        super();
        transparent = true;
    }
}
