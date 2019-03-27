package three.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import three.cameras.Camera;
import three.cameras.OrthographicCamera;
import three.cameras.PerspectiveCamera;
import three.math.Ray;
import three.math.Vector2;
import three.math.Vector3;
import three.util.Intersect;
import three.util.IntersectComparator;

public class Raycaster {
    public Ray set;
    public float near;
    public float far;
    public float linePrecision;

    public Raycaster(Vector3 origin, Vector3 direction, float near, float far){
        this.set = new Ray(origin, direction);
        this.near = near;
        this.far = far;
        this.linePrecision = 1;
    }

    private void intersectObject_(Object3D object, Raycaster raycaster,
                                  ArrayList<Intersect> intersects, boolean recursive){

        if ( object.visible == false ) return;
        object.raycast( raycaster, intersects );

        if (recursive) {
            ArrayList<Object3D> children = object.children;
            for ( int i = 0, l = children.size(); i < l; i ++ ) {
                intersectObject_( children.get(i), raycaster, intersects, true );
            }
        }
    }


    public void set(Vector3 origin, Vector3 direction){
        this.set.set(origin, direction);
    }

    public void setFromCamera(Vector2 coords, Camera camera){
        if(camera instanceof PerspectiveCamera){
            this.set.origin.setFromMatrixPosition( camera.matrixWorld );
            this.set.direction.set( coords.x, coords.y, 0.5f ).unproject( camera ).sub( this.set.origin ).normalize();
        }else if(camera instanceof OrthographicCamera){
            OrthographicCamera orthographicCamera = (OrthographicCamera)camera;
            this.set.origin.set( coords.x, coords.y, ( orthographicCamera.near + orthographicCamera.far ) / ( orthographicCamera.near - orthographicCamera.far ) ).unproject( camera ); // set origin in plane of camera
            this.set.direction.set( 0, 0, - 1 ).transformDirection( camera.matrixWorld );
        }
    }

    public ArrayList<Intersect> intersectObject(Object3D object, boolean recursive){
        ArrayList<Intersect> intersects = new ArrayList<Intersect>();
        intersectObject_( object, this, intersects, recursive );
        // sort
        Comparator c = new IntersectComparator();
        Collections.sort(intersects, c);
        return intersects;
    }

    public ArrayList<Intersect> intersectObjects(ArrayList<Object3D> objects, boolean recursive){
        ArrayList<Intersect> intersects = new ArrayList<Intersect>();

        for ( int i = 0, l = objects.size(); i < l; i ++ ) {
            intersectObject_( objects.get(i), this, intersects, recursive );
        }
        // sort
        Comparator c = new IntersectComparator();
        Collections.sort(intersects, c);
        return intersects;
    }
}
