package three.math;

import three.core.Object3D;

public class Frustum {
    public boolean intersectsSprite(Object3D object) {
        return false;
    }

    public boolean intersectsObject(Object3D object) {
        return true;
    }

    public void setFromMatrix(Matrix4 projScreenMatrix) {
    }
}
