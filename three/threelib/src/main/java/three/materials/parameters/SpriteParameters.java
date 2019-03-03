package three.materials.parameters;

import three.math.Color;
import three.textures.Texture;

public class SpriteParameters extends MaterialParameters {
    public Color color = new Color(0xffffff);
    public Texture map = null;
    public float rotation = 0.0f;
    public boolean sizeAttenuation = true;

    public SpriteParameters(){
        super();
        lights = false;
        transparent = true;
    }
}
