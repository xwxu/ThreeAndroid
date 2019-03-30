package three.math;

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

    public Plane set(Vector3 normal, float constant){
        this.normal = normal;
        this.constant = constant;
        return this;
    }

    public Plane setFromNormalAndCoplanarPoint(Vector3 normal, Vector3 point){
        this.normal.copy(normal);
        this.constant = - point.dot(this.normal);
        return this;
    }

    public Plane clone_(){
        return new Plane().copy(this);
    }

    public Plane copy(Plane plane){
        this.normal.copy( plane.normal );
        this.constant = plane.constant;
        return this;
    }

    public Plane Normalize(){
        float inverseNormalLength = 1.0f / this.normal.length();
        this.normal.multiplyScalar( inverseNormalLength );
        this.constant *= inverseNormalLength;
        return this;
    }

    public Plane Negate(){
        this.constant *= - 1;
        this.normal.negate();
        return this;
    }

    public float DistanceToPoint(Vector3 point){
        return this.normal.dot( point ) + this.constant;
    }

    public float DistanceToSphere(Sphere sphere){
        return this.DistanceToPoint( sphere.center ) - sphere.radius;
    }

    public Vector3 ProjectPoint(Vector3 point, Vector3 target){
        return target.copy( this.normal ).multiplyScalar( - this.DistanceToPoint( point ) ).add( point );
    }

    public Vector3 IntersectLine(Line3 line, Vector3 target){
        Vector3 v1 = new Vector3();
        Vector3 direction = line.delta( v1 );

        float denominator = this.normal.dot( direction );

        if ( denominator == 0 ) {
            // line is coplanar, return origin
            if ( this.DistanceToPoint( line.start ) == 0 ) {
                return target.copy( line.start );
            }
            // Unsure if this is the correct method to handle this case.
            return null;
        }

        float t = - ( line.start.dot( this.normal ) + this.constant ) / denominator;

        if ( t < 0 || t > 1 ) {
            return null;
        }

        return target.copy( direction ).multiplyScalar( t ).add( line.start );
    }

    public boolean IntersectsLine(Line3 line){
        // Note: this tests if a line intersects the plane, not whether it (or its end-points) are coplanar with it.
        float startSign = this.DistanceToPoint( line.start );
        float endSign = this.DistanceToPoint( line.end );

        return ( startSign < 0 && endSign > 0 ) || ( endSign < 0 && startSign > 0 );
    }

    public boolean IntersectsBox(Box3 box){
        return box.intersectsPlane(this);
    }

    public boolean IntersectsSphere(Sphere sphere){
        return sphere.intersectsPlane(this);
    }

    public Vector3 CoplanarPoint(Vector3 target){
        return target.copy( this.normal ).multiplyScalar( - this.constant );
    }

    public Plane ApplyMatrix4(Matrix4 matrix, Matrix3 optionalNormalMatrix){
        Vector3 v1 = new Vector3();
        Matrix3 m1 = new Matrix3();
        Matrix3 normalMatrix = optionalNormalMatrix != null ? optionalNormalMatrix : m1.getNormalMatrix( matrix );
        Vector3 referencePoint = this.CoplanarPoint( v1 ).applyMatrix4( matrix );
        Vector3 normal = this.normal.applyMatrix3( normalMatrix ).normalize();
        this.constant = - referencePoint.dot( normal );

        return this;
    }

    public Plane Translate(Vector3 offset){
        this.constant -= offset.dot( this.normal );
        return this;
    }

    public boolean Equals(Plane plane){
        return plane.normal.equals( this.normal ) && ( plane.constant == this.constant );
    }
}
