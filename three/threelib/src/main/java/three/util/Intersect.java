package three.util;

import java.util.Comparator;

import three.core.Face3;
import three.core.Object3D;
import three.math.Vector3;
import three.math.Vector2;

public class Intersect{
    public double distance;
    public Object3D object;
    public Vector3 point;
    public Vector2 uv;
    public Face3 face;
    public int faceIndex;

    public Intersect(double distance, Vector3 point, Object3D object){
        this.distance = distance;
        this.object = object;
        this.point = point;
    }
}
