package three.math;

import java.util.ArrayList;

public class Sphere {
    public Vector3 center;
    public float radius;

    public Sphere(){
        this.center = new Vector3();
        this.radius = 0;
    }

    public Sphere(Vector3 center, float radius){
        this.center = center;
        this.radius = radius;
    }

    public Sphere set(Vector3 center, float radius){
        this.center = center;
        this.radius = radius;
        return this;
    }

    public Sphere setFromPoints(ArrayList<Vector3> points){
        Box3 box = new Box3();
        Vector3 center = this.center;
        box.setFromPoints( points ).getCenter( center );
        float maxRadiusSq = 0;

        for ( int i = 0, il = points.size(); i < il; i ++ ) {
            maxRadiusSq = Math.max( maxRadiusSq, center.distanceToSquared( points.get(i) ) );
        }

        this.radius = (float)Math.sqrt( maxRadiusSq );

        return this;
    }

    public Sphere Clone(){

        return this;
    }

    public Sphere copy(Sphere s){

        return this;
    }

    public boolean empty(){
        return ( this.radius <= 0 );
    }


    public boolean containsPoint(Vector3 point){
        return ( point.distanceToSquared( this.center ) <= ( this.radius * this.radius ) );
    }


    public boolean intersectsBox(Box3 box){
        return box.intersectsSphere( this );
    }

    public boolean intersectsSphere(Sphere sphere){
        float radiusSum = this.radius + sphere.radius;
        return sphere.center.distanceToSquared( this.center ) <= ( radiusSum * radiusSum );
    }

    public boolean intersectsPlane(Plane plane){
        return Math.abs( plane.DistanceToPoint( this.center ) ) <= this.radius;
    }


    public Vector3 clampPoint(Vector3 point, Vector3 target){
        float deltaLengthSq = this.center.distanceToSquared( point );
        target.copy( point );

        if ( deltaLengthSq > ( this.radius * this.radius ) ) {
            target.sub( this.center ).normalize();
            target.multiplyScalar( this.radius ).add( this.center );
        }

        return target;
    }

    public float distanceToPoint(Vector3 point){
        return ( point.distanceTo( this.center ) - this.radius );
    }

    public Box3 getBoundingBox(Box3 target){
        target.set( this.center, this.center );
        target.expandByScalar( this.radius );

        return target;
    }

    public Sphere applyMatrix4(Matrix4 matrix){
        this.center.applyMatrix4( matrix );
        this.radius = this.radius * matrix.getMaxScaleOnAxis();
        return this;
    }

    public Sphere translate(Vector3 offset){
        this.center.add( offset );
        return this;
    }

    public boolean equals(Sphere sphere){
        return sphere.center.equals( this.center ) && ( sphere.radius == this.radius );
    }
}
