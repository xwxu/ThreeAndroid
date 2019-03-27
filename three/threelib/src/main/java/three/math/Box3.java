package three.math;

import java.util.ArrayList;

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

    public Box3 set(Vector3 min, Vector3 max){
        this.min.copy(min);
        this.max.copy(max);
        return this;
    }

    public Box3 setFromArray(float[] array){
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

        this.min.set( minX, minY, minZ );
        this.max.set( maxX, maxY, maxZ );
        return this;
    }

    public Box3 setFromBufferAttribute(Float32BufferAttribute attribute){
        float minX = Float.POSITIVE_INFINITY;
        float minY =  Float.POSITIVE_INFINITY;
        float minZ =  Float.POSITIVE_INFINITY;

        float maxX =  Float.NEGATIVE_INFINITY;
        float maxY =  Float.NEGATIVE_INFINITY;
        float maxZ =  Float.NEGATIVE_INFINITY;

        for ( int i = 0, l = attribute.count; i < l; i ++ ) {
            float x = attribute.getX( i );
            float y = attribute.getY( i );
            float z = attribute.getZ( i );

            if ( x < minX ) minX = x;
            if ( y < minY ) minY = y;
            if ( z < minZ ) minZ = z;

            if ( x > maxX ) maxX = x;
            if ( y > maxY ) maxY = y;
            if ( z > maxZ ) maxZ = z;
        }

        this.min.set( minX, minY, minZ );
        this.max.set( maxX, maxY, maxZ );

        return this;
    }

    public Box3 setFromPoints(ArrayList<Vector3> points){
        this.makeEmpty();

        for ( int i = 0, il = points.size(); i < il; i ++ ) {
            this.expandByPoint( points.get(i) );
        }
        return this;
    }

    public Box3 setFromCenterAndSize(Vector3 center, Vector3 size){
        Vector3 v1 = new Vector3();
        Vector3 halfSize = v1.copy( size ).multiplyScalar( 0.5f );
        this.min.copy( center ).sub( halfSize );
        this.max.copy( center ).add( halfSize );

        return this;
    }

    public Box3 setFromObject(Object3D object){
        this.makeEmpty();
        return this.expandByObject( object );
    }

    public Box3 clone(){
        return new Box3().copy(this);
    }

    public Box3 copy(Box3 box){
        this.min.copy( box.min );
        this.max.copy( box.max );
        return this;
    }

    public Box3 makeEmpty(){
        this.min.x = this.min.y = this.min.z = Float.POSITIVE_INFINITY;
        this.max.x = this.max.y = this.max.z = Float.NEGATIVE_INFINITY;
        return this;
    }

    public boolean isEmpty(){
        return ( this.max.x < this.min.x ) || ( this.max.y < this.min.y ) || ( this.max.z < this.min.z );
    }

    public Vector3 getCenter(Vector3 target){
        return this.isEmpty() ? target.set( 0, 0, 0 ) : target.addVectors( this.min, this.max ).multiplyScalar( 0.5f );
    }

    public Vector3 getSize(Vector3 target){
        return this.isEmpty() ? target.set( 0, 0, 0 ) : target.subVectors( this.max, this.min );
    }

    public Box3 expandByPoint(Vector3 point){
        this.min.min( point );
        this.max.max( point );
        return this;
    }

    public Box3 expandByVector(Vector3 vector){
        this.min.sub( vector );
        this.max.add( vector );
        return this;
    }

    public Box3 expandByScalar(float scalar){
        this.min.addScalar( -scalar );
        this.max.addScalar( scalar );
        return this;
    }

    public Box3 expandByObject(Object3D object){
        object.updateMatrixWorld(true);
        expand(object, this);
        return this;
    }

    private void expand(Object3D node, Box3 box){
        AbstractGeometry geometry = node.geometry;
        Vector3 v1 = new Vector3();
        if ( geometry != null ) {
            if ( geometry instanceof Geometry) {
                Geometry geometry1 = (Geometry)geometry;
                ArrayList<Vector3> vertices = geometry1.vertices;
                for ( int i = 0, l = vertices.size(); i < l; i ++ ) {
                    v1.copy( vertices.get(i) );
                    v1.applyMatrix4( node.matrixWorld );

                    box.expandByPoint( v1 );
                }

            } else if ( geometry instanceof BufferGeometry) {
                BufferGeometry bufferGeometry = (BufferGeometry) geometry;
                Float32BufferAttribute attribute = (Float32BufferAttribute) bufferGeometry.attributes.get("position");

                if ( attribute != null ) {
                    for ( int i = 0, l = attribute.count; i < l; i ++ ) {
                        v1.fromBufferAttribute( attribute, i, 0 ).applyMatrix4( node.matrixWorld );
                        box.expandByPoint( v1 );
                    }
                }
            }
        }

        for(int i = 0; i < node.children.size(); ++i){
            Object3D child = node.children.get(i);
            expand(child, box);
        }
    }

    public boolean containsPoint(Vector3 point){
        return point.x < this.min.x || point.x > this.max.x ||
                point.y < this.min.y || point.y > this.max.y ||
                point.z < this.min.z || point.z > this.max.z ? false : true;
    }

    public boolean containsBox(Box3 box){
        return this.min.x <= box.min.x && box.max.x <= this.max.x &&
                this.min.y <= box.min.y && box.max.y <= this.max.y &&
                this.min.z <= box.min.z && box.max.z <= this.max.z;
    }

    public Vector3 getParameter(Vector3 point, Vector3 target){
        return target.set(
                ( point.x - this.min.x ) / ( this.max.x - this.min.x ),
                ( point.y - this.min.y ) / ( this.max.y - this.min.y ),
                ( point.z - this.min.z ) / ( this.max.z - this.min.z )
        );
    }

    public boolean intersectsBox(Box3 box){
        // using 6 splitting planes to rule out intersections.
        return box.max.x < this.min.x || box.min.x > this.max.x ||
                box.max.y < this.min.y || box.min.y > this.max.y ||
                box.max.z < this.min.z || box.min.z > this.max.z ? false : true;
    }

    public boolean intersectsSphere(Sphere sphere){
        Vector3 closestPoint = new Vector3();
        this.clampPoint( sphere.center, closestPoint );
        return closestPoint.distanceToSquared( sphere.center ) <= ( sphere.radius * sphere.radius );
    }

    public boolean intersectsPlane(Plane plane){
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

    public boolean intersectsTriangle(Triangle triangle){
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

        if ( this.isEmpty() ) {
            return false;
        }

        // compute box center and extents
        this.getCenter( center );
        extents.subVectors( this.max, center );

        // translate triangle to aabb origin
        v0.subVectors( triangle.a, center );
        v1.subVectors( triangle.b, center );
        v2.subVectors( triangle.c, center );

        // compute edge vectors for triangle
        f0.subVectors( v1, v0 );
        f1.subVectors( v2, v1 );
        f2.subVectors( v0, v2 );

        // test against axes that are given by cross product combinations of the edges of the triangle and the edges of the aabb
        // make an axis testing of each of the 3 sides of the aabb against each of the 3 sides of the triangle = 9 axis of separation
        // axis_ij = u_i x f_j (u0, u1, u2 = face normals of aabb = x,y,z axes vectors since aabb is axis aligned)
        float[] axes = new float[]{0, - f0.z, f0.y, 0, - f1.z, f1.y, 0, - f2.z, f2.y,
                f0.z, 0, - f0.x, f1.z, 0, - f1.x, f2.z, 0, - f2.x,
                - f0.y, f0.x, 0, - f1.y, f1.x, 0, - f2.y, f2.x, 0};

        if ( ! satForAxes( axes, v0, v1, v2, extents ) ) {
            return false;
        }

        // test 3 face normals from the aabb
        axes = new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1};
        if ( ! satForAxes( axes, v0, v1, v2, extents ) ) {
            return false;
        }

        // finally testing the face normal of the triangle
        // use already existing triangle edge vectors here
        triangleNormal.crossVectors( f0, f1 );
        axes = new float[]{triangleNormal.x, triangleNormal.y, triangleNormal.z};
        return satForAxes( axes, v0, v1, v2, extents );

    }

    private boolean satForAxes(float[] axes, Vector3 v0, Vector3 v1, Vector3 v2, Vector3 extents){
        Vector3 testAxis = new Vector3();
        for ( int i = 0, j = axes.length - 3; i <= j; i += 3 ) {

            testAxis.fromArray( axes, i );
            // project the aabb onto the seperating axis
            float r = extents.x * Math.abs( testAxis.x ) + extents.y * Math.abs( testAxis.y ) + extents.z * Math.abs( testAxis.z );
            // project all 3 vertices of the triangle onto the seperating axis
            float p0 = v0.dot( testAxis );
            float p1 = v1.dot( testAxis );
            float p2 = v2.dot( testAxis );
            // actual test, basically see if either of the most extreme of the triangle points intersects r
            if ( Math.max( - Math.max( p0, Math.max(p1, p2) ), Math.min( p0, Math.min(p1, p2) ) ) > r ) {
                // points of the projected triangle are outside the projected half-length of the aabb
                // the axis is seperating and we can exit
                return false;
            }

        }

        return true;
    }

    public Vector3 clampPoint(Vector3 point, Vector3 target){
        return target.copy( point ).clamp( this.min, this.max );
    }

    public float distanceToPoint(Vector3 point){
        Vector3 v1 = new Vector3();
        Vector3 clampedPoint = v1.copy( point ).clamp( this.min, this.max );
        return clampedPoint.sub( point ).length();
    }

    public Sphere getBoundingSphere(Sphere target){
        Vector3 v1 = new Vector3();
        this.getCenter( target.center );

        target.radius = this.getSize( v1 ).length() * 0.5f;
        return target;
    }

    public Box3 intersect(Box3 box){
        this.min.max( box.min );
        this.max.min( box.max );

        // ensure that if there is no overlap, the result is fully empty, not slightly empty with non-inf/+inf values that will cause subsequence intersects to erroneously return valid values.
        if ( this.isEmpty() ) this.makeEmpty();

        return this;
    }

    public Box3 union(Box3 box){
        this.min.min( box.min );
        this.max.max( box.max );
        return this;
    }

    public Box3 applyMatrix4(Matrix4 matrix){
        ArrayList<Vector3> points = new ArrayList<>();
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());
        points.add(new Vector3());

        if ( this.isEmpty() ) return this;

        // NOTE: I am using a binary pattern to specify all 2^3 combinations below
        points.get(0).set( this.min.x, this.min.y, this.min.z ).applyMatrix4( matrix ); // 000
        points.get(1).set( this.min.x, this.min.y, this.max.z ).applyMatrix4( matrix ); // 001
        points.get(2).set( this.min.x, this.max.y, this.min.z ).applyMatrix4( matrix ); // 010
        points.get(3).set( this.min.x, this.max.y, this.max.z ).applyMatrix4( matrix ); // 011
        points.get(4).set( this.max.x, this.min.y, this.min.z ).applyMatrix4( matrix ); // 100
        points.get(5).set( this.max.x, this.min.y, this.max.z ).applyMatrix4( matrix ); // 101
        points.get(6).set( this.max.x, this.max.y, this.min.z ).applyMatrix4( matrix ); // 110
        points.get(7).set( this.max.x, this.max.y, this.max.z ).applyMatrix4( matrix ); // 111

        this.setFromPoints( points );

        return this;
    }

    public Box3 translate(Vector3 offset){
        this.min.add( offset );
        this.max.add( offset );
        return this;
    }

    public boolean equals(Box3 box){
        return box.min.equals( this.min ) && box.max.equals( this.max );
    }
}
