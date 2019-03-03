package three.math;

import three.core.Object3D;

public class Frustum {
    public boolean IntersectsSprite(Object3D object) {
        return false;
    }

    public boolean IntersectsObject(Object3D object) {
        return true;
    }

    public void SetFromMatrix(Matrix4 projScreenMatrix) {
    }
}
