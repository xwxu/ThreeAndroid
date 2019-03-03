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

    public Sphere Set(Vector3 center, float radius){
        this.center = center;
        this.radius = radius;
        return this;
    }

    public Sphere SetFromPoints(ArrayList<Vector3> points){
        Box3 box = new Box3();
        Vector3 center = this.center;
        box.SetFromPoints( points ).GetCenter( center );
        float maxRadiusSq = 0;

        for ( int i = 0, il = points.size(); i < il; i ++ ) {
            maxRadiusSq = Math.max( maxRadiusSq, center.DistanceToSquared( points.get(i) ) );
        }

        this.radius = (float)Math.sqrt( maxRadiusSq );

        return this;
    }

    public Sphere Clone(){

        return this;
    }

    public Sphere Copy(Sphere s){

        return this;
    }

    public boolean Empty(){
        return ( this.radius <= 0 );
    }


    public boolean ContainsPoint(Vector3 point){
        return ( point.DistanceToSquared( this.center ) <= ( this.radius * this.radius ) );
    }


    public boolean IntersectsBox(Box3 box){
        return box.IntersectsSphere( this );
    }

    public boolean IntersectsSphere(Sphere sphere){
        float radiusSum = this.radius + sphere.radius;
        return sphere.center.DistanceToSquared( this.center ) <= ( radiusSum * radiusSum );
    }

    public boolean IntersectsPlane(Plane plane){
        return Math.abs( plane.DistanceToPoint( this.center ) ) <= this.radius;
    }


    public Vector3 ClampPoint(Vector3 point, Vector3 target){
        float deltaLengthSq = this.center.DistanceToSquared( point );
        target.Copy( point );

        if ( deltaLengthSq > ( this.radius * this.radius ) ) {
            target.Sub( this.center ).Normalize();
            target.MultiplyScalar( this.radius ).Add( this.center );
        }

        return target;
    }

    public float DistanceToPoint(Vector3 point){
        return ( point.DistanceTo( this.center ) - this.radius );
    }

    public Box3 GetBoundingBox(Box3 target){
        target.Set( this.center, this.center );
        target.ExpandByScalar( this.radius );

        return target;
    }

    public Sphere ApplyMatrix4(Matrix4 matrix){
        this.center.ApplyMatrix4( matrix );
        this.radius = this.radius * matrix.GetMaxScaleOnAxis();
        return this;
    }

    public Sphere Translate(Vector3 offset){
        this.center.Add( offset );
        return this;
    }

    public boolean Equals(Sphere sphere){
        return sphere.center.equals( this.center ) && ( sphere.radius == this.radius );
    }
}
