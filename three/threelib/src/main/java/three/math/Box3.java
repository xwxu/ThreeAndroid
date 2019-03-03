package three.math;

import java.util.ArrayList;

import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;
import three.core.AbstractGeometry;
import three.core.BufferGeometry;
import three.core.Geometry;
import three.core.Object3D;

public class Box3 {
    public Vector3 min;
    public Vector3 max;
    public boolean isBox3;

    public Box3(){
        this.min = new Vector3(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        this.max = new Vector3(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        this.isBox3 = true;
    }

    public Box3(Vector3 min, Vector3 max){
        this.min = min;
        this.max = max;
        this.isBox3 = true;
    }

    public Box3 Set(Vector3 min, Vector3 max){
        this.min.Copy(min);
        this.max.Copy(max);
        return this;
    }

    public Box3 SetFromArray(float[] array){
        float minX = Float.POSITIVE_INFINITY;
        float minY =  Float.POSITIVE_INFINITY;
        float minZ =  Float.POSITIVE_INFINITY;

        float maxX =  Float.NEGATIVE_INFINITY;
        float maxY =  Float.NEGATIVE_INFINITY;
        float maxZ =  Float.NEGATIVE_INFINITY;

        for ( int i = 0, l = array.length; i < l; i += 3 ) {
            float x = array[ i ];
            float y = array[ i + 1 ];
            float z = array[ i + 2 ];

            if ( x < minX ) minX = x;
            if ( y < minY ) minY = y;
            if ( z < minZ ) minZ = z;

            if ( x > maxX ) maxX = x;
            if ( y > maxY ) maxY = y;
            if ( z > maxZ ) maxZ = z;
        }

        this.min.Set( minX, minY, minZ );
        this.max.Set( maxX, maxY, maxZ );
        return this;
    }

    public Box3 SetFromBufferAttribute(Float32BufferAttribute attribute){
        float minX = Float.POSITIVE_INFINITY;
        float minY =  Float.POSITIVE_INFINITY;
        float minZ =  Float.POSITIVE_INFINITY;

        float maxX =  Float.NEGATIVE_INFINITY;
        float maxY =  Float.NEGATIVE_INFINITY;
        float maxZ =  Float.NEGATIVE_INFINITY;

        for ( int i = 0, l = attribute.count; i < l; i ++ ) {
            float x = attribute.GetX( i );
            float y = attribute.GetY( i );
            float z = attribute.GetZ( i );

            if ( x < minX ) minX = x;
            if ( y < minY ) minY = y;
            if ( z < minZ ) minZ = z;

            if ( x > maxX ) maxX = x;
            if ( y > maxY ) maxY = y;
            if ( z > maxZ ) maxZ = z;
        }

        this.min.Set( minX, minY, minZ );
        this.max.Set( maxX, maxY, maxZ );

        return this;
    }

    public Box3 SetFromPoints(ArrayList<Vector3> points){
        this.MakeEmpty();

        for ( int i = 0, il = points.size(); i < il; i ++ ) {
            this.ExpandByPoint( points.get(i) );
        }
        return this;
    }

    public Box3 SetFromCenterAndSize(Vector3 center, Vector3 size){
        Vector3 v1 = new Vector3();
        Vector3 halfSize = v1.Copy( size ).MultiplyScalar( 0.5f );
        this.min.Copy( center ).Sub( halfSize );
        this.max.Copy( center ).Add( halfSize );

        return this;
    }

    public Box3 SetFromObject(Object3D object){
        this.MakeEmpty();
        return this.ExpandByObject( object );
    }

    public Box3 Clone(){
        return new Box3().Copy(this);
    }

    public Box3 Copy(Box3 box){
        this.min.Copy( box.min );
        this.max.Copy( box.max );
        return this;
    }

    public Box3 MakeEmpty(){
        this.min.x = this.min.y = this.min.z = Float.POSITIVE_INFINITY;
        this.max.x = this.max.y = this.max.z = Float.NEGATIVE_INFINITY;
        return this;
    }

    public boolean IsEmpty(){
        return ( this.max.x < this.min.x ) || ( this.max.y < this.min.y ) || ( this.max.z < this.min.z );
    }

    public Vector3 GetCenter(Vector3 target){
        return this.IsEmpty() ? target.Set( 0, 0, 0 ) : target.AddVectors( this.min, this.max ).MultiplyScalar( 0.5f );
    }

    public Vector3 GetSize(Vector3 target){
        return this.IsEmpty() ? target.Set( 0, 0, 0 ) : target.SubVectors( this.max, this.min );
    }

    public Box3 ExpandByPoint(Vector3 point){
        this.min.Min( point );
        this.max.Max( point );
        return this;
    }

    public Box3 ExpandByVector(Vector3 vector){
        this.min.Sub( vector );
        this.max.Add( vector );
        return this;
    }

    public Box3 ExpandByScalar(float scalar){
        this.min.AddScalar( -scalar );
        this.max.AddScalar( scalar );
        return this;
    }

    public Box3 ExpandByObject(Object3D object){
        object.UpdateMatrixWorld(true);
        Expand(object, this);
        return this;
    }

    private void Expand(Object3D node, Box3 box){
        AbstractGeometry geometry = node.geometry;
        Vector3 v1 = new Vector3();
        if ( geometry != null ) {
            if ( geometry instanceof Geometry) {
                Geometry geometry1 = (Geometry)geometry;
                ArrayList<Vector3> vertices = geometry1.vertices;
                for ( int i = 0, l = vertices.size(); i < l; i ++ ) {
                    v1.Copy( vertices.get(i) );
                    v1.ApplyMatrix4( node.matrixWorld );

                    box.ExpandByPoint( v1 );
                }

            } else if ( geometry instanceof BufferGeometry) {
                BufferGeometry bufferGeometry = (BufferGeometry) geometry;
                Float32BufferAttribute attribute = (Float32BufferAttribute) bufferGeometry.attributes.get("position");

                if ( attribute != null ) {
                    for ( int i = 0, l = attribute.count; i < l; i ++ ) {
                        v1.FromBufferAttribute( attribute, i, 0 ).ApplyMatrix4( node.matrixWorld );
                        box.ExpandByPoint( v1 );
                    }
                }
            }
        }

        for(int i = 0; i < node.children.size(); ++i){
            Object3D child = node.children.get(i);
            Expand(child, box);
        }
    }

    public boolean ContainsPoint(Vector3 point){
        return point.x < this.min.x || point.x > this.max.x ||
                point.y < this.min.y || point.y > this.max.y ||
                point.z < this.min.z || point.z > this.max.z ? false : true;
    }

    public boolean ContainsBox(Box3 box){
        return this.min.x <= box.min.x && box.max.x <= this.max.x &&
                this.min.y <= box.min.y && box.max.y <= this.max.y &&
                this.min.z <= box.min.z && box.max.z <= this.max.z;
    }

    public Vector3 GetParameter(Vector3 point, Vector3 target){
        return target.Set(
                ( point.x - this.min.x ) / ( this.max.x - this.min.x ),
                ( point.y - this.min.y ) / ( this.max.y - this.min.y ),
                ( point.z - this.min.z ) / ( this.max.z - this.min.z )
        );
    }

    public boolean IntersectsBox(Box3 box){
        // using 6 splitting planes to rule out intersections.
        return box.max.x < this.min.x || box.min.x > this.max.x ||
                box.max.y < this.min.y || box.min.y > this.max.y ||
                box.max.z < this.min.z || box.min.z > this.max.z ? false : true;
    }

    public boolean IntersectsSphere(Sphere sphere){
        Vector3 closestPoint = new Vector3();
        this.ClampPoint( sphere.center, closestPoint );
        return closestPoint.DistanceToSquared( sphere.center ) <= ( sphere.radius * sphere.radius );
    }

    public boolean IntersectsPlane(Plane plane){
        float min, max;

        if ( plane.normal.x > 0 ) {
            min = plane.normal.x * this.min.x;
            max = plane.normal.x * this.max.x;
        } else {
            min = plane.normal.x * this.max.x;
            max = plane.normal.x * this.min.x;
        }

        if ( plane.normal.y > 0 ) {
            min += plane.normal.y * this.min.y;
            max += plane.normal.y * this.max.y;
        } else {
            min += plane.normal.y * this.max.y;
            max += plane.normal.y * this.min.y;
        }

        if ( plane.normal.z > 0 ) {
            min += plane.normal.z * this.min.z;
            max += plane.normal.z * this.max.z;
        } else {
            min += plane.normal.z * this.max.z;
            max += plane.normal.z * this.min.z;
        }

        return ( min <= - plane.constant && max >= - plane.constant );
    }

    public boolean IntersectsTriangle(Triangle triangle){
        // triangle centered vertices
        Vector3 v0 = new Vector3();
        Vector3 v1 = new Vector3();
        Vector3 v2 = new Vector3();
        // triangle edge vectors
        Vector3 f0 = new Vector3();
        Vector3 f1 = new Vector3();
        Vector3 f2 = new Vector3();
        Vector3 center = new Vector3();
        Vector3 extents = new Vector3();
        Vector3 triangleNormal = new Vector3();

        if ( this.IsEmpty() ) {
            return false;
        }

        // compute box center and extents
        this.GetCenter( center );
        extents.SubVectors( this.max, center );

        // translate triangle to aabb origin
        v0.SubVectors( triangle.a, center );
        v1.SubVectors( triangle.b, center );
        v2.SubVectors( triangle.c, center );

        // compute edge vectors for triangle
        f0.SubVectors( v1, v0 );
        f1.SubVectors( v2, v1 );
        f2.SubVectors( v0, v2 );

        // test against axes that are given by cross product combinations of the edges of the triangle and the edges of the aabb
        // make an axis testing of each of the 3 sides of the aabb against each of the 3 sides of the triangle = 9 axis of separation
        // axis_ij = u_i x f_j (u0, u1, u2 = face normals of aabb = x,y,z axes vectors since aabb is axis aligned)
        float[] axes = new float[]{0, - f0.z, f0.y, 0, - f1.z, f1.y, 0, - f2.z, f2.y,
                f0.z, 0, - f0.x, f1.z, 0, - f1.x, f2.z, 0, - f2.x,
                - f0.y, f0.x, 0, - f1.y, f1.x, 0, - f2.y, f2.x, 0};

        if ( ! SatForAxes( axes, v0, v1, v2, extents ) ) {
            return false;
        }

        // test 3 face normals from the aabb
        axes = new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1};
        if ( ! SatForAxes( axes, v0, v1, v2, extents ) ) {
            return false;
        }

        // finally testing the face normal of the triangle
        // use already existing triangle edge vectors here
        triangleNormal.CrossVectors( f0, f1 );
        axes = new float[]{triangleNormal.x, triangleNormal.y, triangleNormal.z};
        return SatForAxes( axes, v0, v1, v2, extents );

    }

    private boolean SatForAxes(float[] axes, Vector3 v0, Vector3 v1, Vector3 v2, Vector3 extents){
        Vector3 testAxis = new Vector3();
        for ( int i = 0, j = axes.length - 3; i <= j; i += 3 ) {

            testAxis.FromArray( axes, i );
            // project the aabb onto the seperating axis
            float r = extents.x * Math.abs( testAxis.x ) + extents.y * Math.abs( testAxis.y ) + extents.z * Math.abs( testAxis.z );
            // project all 3 vertices of the triangle onto the seperating axis
            float p0 = v0.Dot( testAxis );
            float p1 = v1.Dot( testAxis );
            float p2 = v2.Dot( testAxis );
            // actual test, basically see if either of the most extreme of the triangle points intersects r
            if ( Math.max( - Math.max( p0, Math.max(p1, p2) ), Math.min( p0, Math.min(p1, p2) ) ) > r ) {
                // points of the projected triangle are outside the projected half-length of the aabb
                // the axis is seperating and we can exit
                return false;
            }

        }

        return true;
    }

    public Vector3 ClampPoint(Vector3 point, Vector3 target){
        return target.Copy( point ).Clamp( this.min, this.max );
    }

    public float DistanceToPoint(Vector3 point){
        Vector3 v1 = new Vector3();
        Vector3 clampedPoint = v1.Copy( point ).Clamp( this.min, this.max );
        return clampedPoint.Sub( point ).Length();
    }

    public Sphere GetBoundingSphere(Sphere target){
        Vector3 v1 = new Vector3();
        this.GetCenter( target.center );

        target.radius = this.GetSize( v1 ).Length() * 0.5f;
        return target;
    }

    public Box3 Intersect(Box3 box){
        this.min.Max( box.min );
        this.max.Min( box.max );

        // ensure that if there is no overlap, the result is fully empty, not slightly empty with non-inf/+inf values that will cause subsequence intersects to erroneously return valid values.
        if ( this.IsEmpty() ) this.MakeEmpty();

        return this;
    }

    public Box3 Union(Box3 box){
        this.min.Min( box.min );
        this.max.Max( box.max );
        return this;
    }

    public Box3 ApplyMatrix4(Matrix4 matrix){
        ArrayList<Vector3> points = new ArrayList<>();
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());

        if ( this.IsEmpty() ) return this;

        // NOTE: I am using a binary pattern to specify all 2^3 combinations below
        points.get(0).Set( this.min.x, this.min.y, this.min.z ).ApplyMatrix4( matrix ); // 000
        points.get(1).Set( this.min.x, this.min.y, this.max.z ).ApplyMatrix4( matrix ); // 001
        points.get(2).Set( this.min.x, this.max.y, this.min.z ).ApplyMatrix4( matrix ); // 010
        points.get(3).Set( this.min.x, this.max.y, this.max.z ).ApplyMatrix4( matrix ); // 011
        points.get(4).Set( this.max.x, this.min.y, this.min.z ).ApplyMatrix4( matrix ); // 100
        points.get(5).Set( this.max.x, this.min.y, this.max.z ).ApplyMatrix4( matrix ); // 101
        points.get(6).Set( this.max.x, this.max.y, this.min.z ).ApplyMatrix4( matrix ); // 110
        points.get(7).Set( this.max.x, this.max.y, this.max.z ).ApplyMatrix4( matrix ); // 111

        this.SetFromPoints( points );

        return this;
    }

    public Box3 Translate(Vector3 offset){
        this.min.Add( offset );
        this.max.Add( offset );
        return this;
    }

    public boolean Equals(Box3 box){
        return box.min.Equals( this.min ) && box.max.Equals( this.max );
    }
}
