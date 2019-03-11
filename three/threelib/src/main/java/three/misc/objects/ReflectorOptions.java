package three.misc.objects;

import three.math.Color;
import three.renderers.shaders.ShaderObject;

public class ReflectorOptions {
    public Color color = new Color(0x7f7f7f);
    public int textureWidth = 512;
    public int textureHeight = 512;
    public float clipBias = 0.0f;
    public int recursion = 0;
}
