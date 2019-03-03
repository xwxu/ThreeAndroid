package three.math;

import three.objects.Line;

public class Plane {
    public Vector3 normal;
    public float constant;

    public Plane(){
        this.normal = new Vector3(1,0,0);
        this.constant = 0;
    }

    public Plane(Vector3 normal, float constant){
        this.normal = normal;
        this.constant = constant;
    }

    public Plane Set(Vector3 normal, float constant){
        this.normal = normal;
        this.constant = constant;
        return this;
    }

    public Plane SetFromNormalAndCoplanarPoint(Vector3 normal, Vector3 point){
        this.normal.Copy(normal);
        this.constant = - point.Dot(this.normal);
        return this;
    }

    public Plane Clone(){
        return new Plane().Copy(this);
    }

    public Plane Copy(Plane plane){
        this.normal.Copy( plane.normal );
        this.constant = plane.constant;
        return this;
    }

    public Plane Normalize(){
        float inverseNormalLength = 1.0f / this.normal.Length();
        this.normal.MultiplyScalar( inverseNormalLength );
        this.constant *= inverseNormalLength;
        return this;
    }

    public Plane Negate(){
        this.constant *= - 1;
        this.normal.Negate();
        return this;
    }

    public float DistanceToPoint(Vector3 point){
        return this.normal.Dot( point ) + this.constant;
    }

    public float DistanceToSphere(Sphere sphere){
        return this.DistanceToPoint( sphere.center ) - sphere.radius;
    }

    public Vector3 ProjectPoint(Vector3 point, Vector3 target){
        return target.Copy( this.normal ).MultiplyScalar( - this.DistanceToPoint( point ) ).Add( point );
    }

    public Vector3 IntersectLine(Line3 line, Vector3 target){
        Vector3 v1 = new Vector3();
        Vector3 direction = line.Delta( v1 );

        float denominator = this.normal.Dot( direction );

        if ( denominator == 0 ) {
            // line is coplanar, return origin
            if ( this.DistanceToPoint( line.start ) == 0 ) {
                return target.Copy( line.start );
            }
            // Unsure if this is the correct method to handle this case.
            return null;
        }

        float t = - ( line.start.Dot( this.normal ) + this.constant ) / denominator;

        if ( t < 0 || t > 1 ) {
            return null;
        }

        return target.Copy( direction ).MultiplyScalar( t ).Add( line.start );
    }

    public boolean IntersectsLine(Line3 line){
        // Note: this tests if a line intersects the plane, not whether it (or its end-points) are coplanar with it.
        float startSign = this.DistanceToPoint( line.start );
        float endSign = this.DistanceToPoint( line.end );

        return ( startSign < 0 && endSign > 0 ) || ( endSign < 0 && startSign > 0 );
    }

    public boolean IntersectsBox(Box3 box){
        return box.IntersectsPlane(this);
    }

    public boolean IntersectsSphere(Sphere sphere){
        return sphere.IntersectsPlane(this);
    }

    public Vector3 CoplanarPoint(Vector3 target){
        return target.Copy( this.normal ).MultiplyScalar( - this.constant );
    }

    public Plane ApplyMatrix4(Matrix4 matrix, Matrix3 optionalNormalMatrix){
        Vector3 v1 = new Vector3();
        Matrix3 m1 = new Matrix3();
        Matrix3 normalMatrix = optionalNormalMatrix != null ? optionalNormalMatrix : m1.GetNormalMatrix( matrix );
        Vector3 referencePoint = this.CoplanarPoint( v1 ).ApplyMatrix4( matrix );
        Vector3 normal = this.normal.ApplyMatrix3( normalMatrix ).Normalize();
        this.constant = - referencePoint.Dot( normal );

        return this;
    }

    public Plane Translate(Vector3 offset){
        this.constant -= offset.Dot( this.normal );
        return this;
    }

    public boolean Equals(Plane plane){
        return plane.normal.Equals( this.normal ) && ( plane.constant == this.constant );
    }
}
