package three.materials.parameters;

import three.math.Color;
import three.textures.Texture;

public class PointsParameters extends MaterialParameters {
    public Color color = new Color(0xffffff);
    public Texture map = null;
    public float size = 1.0f;
    public boolean sizeAttenuation = true;
    public boolean morphTargets = false;

    public PointsParameters(){
        super();
        lights = false;
    }
}
