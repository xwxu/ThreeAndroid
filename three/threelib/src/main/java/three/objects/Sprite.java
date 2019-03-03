package three.objects;

import three.core.Object3D;
import three.materials.Material;
import three.math.Vector2;

public class Sprite extends Object3D {
    public Material material;

    public Vector2 center = new Vector2(0.5f, 0.5f);
}
