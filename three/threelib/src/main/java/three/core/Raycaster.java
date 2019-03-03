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
    public Ray ray;
    public float near;
    public float far;
    public float linePrecision;

    public Raycaster(Vector3 origin, Vector3 direction, float near, float far){
        this.ray = new Ray(origin, direction);
        this.near = near;
        this.far = far;
        this.linePrecision = 1;
    }

    private void IntersectObject_(Object3D object, Raycaster raycaster,
                                  ArrayList<Intersect> intersects, boolean recursive){

        if ( object.visible == false ) return;
        object.Raycast( raycaster, intersects );

        if ( recursive == true ) {
            ArrayList<Object3D> children = object.children;
            for ( int i = 0, l = children.size(); i < l; i ++ ) {
                IntersectObject_( children.get(i), raycaster, intersects, true );
            }
        }
    }


    public void Set(Vector3 origin, Vector3 direction){
        this.ray.Set(origin, direction);
    }

    public void SetFromCamera(Vector2 coords, Camera camera){
        if(camera instanceof PerspectiveCamera){
            this.ray.origin.SetFromMatrixPosition( camera.matrixWorld );
            this.ray.direction.Set( coords.x, coords.y, 0.5f ).Unproject( camera ).Sub( this.ray.origin ).Normalize();
        }else if(camera instanceof OrthographicCamera){
            OrthographicCamera orthographicCamera = (OrthographicCamera)camera;
            this.ray.origin.Set( coords.x, coords.y, ( orthographicCamera.near + orthographicCamera.far ) / ( orthographicCamera.near - orthographicCamera.far ) ).Unproject( camera ); // set origin in plane of camera
            this.ray.direction.Set( 0, 0, - 1 ).TransformDirection( camera.matrixWorld );
        }
    }

    public ArrayList<Intersect> IntersectObject(Object3D object, boolean recursive){
        ArrayList<Intersect> intersects = new ArrayList<Intersect>();
        IntersectObject_( object, this, intersects, recursive );
        // sort
        Comparator c = new IntersectComparator();
        Collections.sort(intersects, c);
        return intersects;
    }

    public ArrayList<Intersect> IntersectObjects(ArrayList<Object3D> objects, boolean recursive){
        ArrayList<Intersect> intersects = new ArrayList<Intersect>();

        for ( int i = 0, l = objects.size(); i < l; i ++ ) {
            IntersectObject_( objects.get(i), this, intersects, recursive );
        }
        // sort
        Comparator c = new IntersectComparator();
        Collections.sort(intersects, c);
        return intersects;
    }
}
