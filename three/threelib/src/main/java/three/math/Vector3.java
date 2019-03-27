package three.math;

import three.bufferAttribute.Float32BufferAttribute;
import three.cameras.Camera;

public class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3(){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 setScalar(float scalar){
        this.x = scalar;
        this.y = scalar;
        this.z = scalar;
        return this;
    }

    public Vector3 setX(float x){
        this.x = x;
        return this;
    }

    public Vector3 setY(float y){
        this.y = y;
        return this;
    }

    public Vector3 setZ(float z){
        this.z = z;
        return this;
    }

    public Vector3 setComponent(int index, float value){
        switch (index){
            case 0 :
                this.x = value;
                break;
            case 1:
                this.y = value;
                break;
            case 2:
                this.z = value;
                break;
        }
        return this;
    }

    public float getComponent(int index){
        switch (index){
            case 0 :
                return this.x;
            case 1:
                return this.y;
            case 2:
                return this.z;
        }
        return 0;
    }

    public Vector3 Clone(){
        Vector3 clone = new Vector3(this.x, this.y, this.z);
        return clone;
    }

    public Vector3 copy(Vector3 v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3 add(Vector3 v){
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3 addScalar(float s){
        this.x += s;
        this.y += s;
        this.z += s;
        return this;
    }

    public Vector3 addVectors(Vector3 a, Vector3 b){
        this.x = a.x + b.x;
        this.y = a.y + b.y;
        this.z = a.z + b.z;
        return this;
    }

    public Vector3 addScaledVector(Vector3 v, float s){
        this.x += v.x * s;
        this.y += v.y * s;
        this.z =  v.z * s;
        return this;
    }

    public Vector3 sub(Vector3 v){
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3 subScalar(float s){
        this.x -= s;
        this.y -= s;
        this.z -= s;
        return this;
    }

    public Vector3 subVectors(Vector3 a, Vector3 b){
        this.x = a.x - b.x;
        this.y = a.y - b.y;
        this.z = a.z - b.z;
        return this;
    }

    public Vector3 multiply(Vector3 v){
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector3 multiplyScalar(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public Vector3 multiplyVectors(Vector3 a, Vector3 b){
        this.x = a.x * b.x;
        this.y = a.y * b.y;
        this.z = a.z * b.z;
        return this;
    }

    public Vector3 divide(Vector3 v){
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public  Vector3 divideScalar(float scalar){
        return this.multiplyScalar( 1 / scalar );
    }

    public Vector3 applyEuler(Euler euler){
        Quaternion quaternion = new Quaternion();
        return this.applyQuaternion( quaternion.setFromEuler( euler, true ) );
    }

    public Vector3 applyAxisAngle(Vector3 axis, float angle){
        Quaternion quaternion = new Quaternion();
        return this.applyQuaternion( quaternion.setFromAxisAngle( axis, angle ) );
    }

    public Vector3 applyMatrix3(Matrix3 m){
        float x = this.x, y = this.y, z = this.z;
        float[] e = m.elements;

        this.x = e[ 0 ] * x + e[ 3 ] * y + e[ 6 ] * z;
        this.y = e[ 1 ] * x + e[ 4 ] * y + e[ 7 ] * z;
        this.z = e[ 2 ] * x + e[ 5 ] * y + e[ 8 ] * z;
        return this;
    }

    public Vector3 applyMatrix4(Matrix4 m){
        float x = this.x, y = this.y, z = this.z;
        float[] e = m.elements;

        float w = 1 / ( e[ 3 ] * x + e[ 7 ] * y + e[ 11 ] * z + e[ 15 ] );

        this.x = ( e[ 0 ] * x + e[ 4 ] * y + e[ 8 ] * z + e[ 12 ] ) * w;
        this.y = ( e[ 1 ] * x + e[ 5 ] * y + e[ 9 ] * z + e[ 13 ] ) * w;
        this.z = ( e[ 2 ] * x + e[ 6 ] * y + e[ 10 ] * z + e[ 14 ] ) * w;
        return this;
    }

    public Vector3 applyQuaternion(Quaternion q){
        float x = this.x, y = this.y, z = this.z;
        float qx = q.x, qy = q.y, qz = q.z, qw = q.w;

        // calculate quat * vector
        float ix = qw * x + qy * z - qz * y;
        float iy = qw * y + qz * x - qx * z;
        float iz = qw * z + qx * y - qy * x;
        float iw = - qx * x - qy * y - qz * z;

        // calculate result * inverse quat
        this.x = ix * qw + iw * - qx + iy * - qz - iz * - qy;
        this.y = iy * qw + iw * - qy + iz * - qx - ix * - qz;
        this.z = iz * qw + iw * - qz + ix * - qy - iy * - qx;
        return this;
    }

    public Vector3 project(Camera camera){
        return this.applyMatrix4(camera.matrixWorldInverse).applyMatrix4(camera.projectionMatrix);
    }

    public Vector3 unproject(Camera camera){
        Matrix4 matrix = new Matrix4();
        return this.applyMatrix4(matrix.getInverse(camera.projectionMatrix)).applyMatrix4(camera.matrixWorld);
    }

    public Vector3 transformDirection(Matrix4 m){
        // input: THREE.Matrix4 affine matrix
        // vector interpreted as a direction
        float x = this.x, y = this.y, z = this.z;
        float[] e = m.elements;

        this.x = e[ 0 ] * x + e[ 4 ] * y + e[ 8 ] * z;
        this.y = e[ 1 ] * x + e[ 5 ] * y + e[ 9 ] * z;
        this.z = e[ 2 ] * x + e[ 6 ] * y + e[ 10 ] * z;

        return this.normalize();
    }

    public Vector3 min(Vector3 v){
        this.x = Math.min( this.x, v.x );
        this.y = Math.min( this.y, v.y );
        this.z = Math.min( this.z, v.z );
        return this;
    }

    public Vector3 max(Vector3 v){
        this.x = Math.max( this.x, v.x );
        this.y = Math.max( this.y, v.y );
        this.z = Math.max( this.z, v.z );
        return this;
    }

    public Vector3 clamp(Vector3 min, Vector3 max){
        this.x = Math.max( min.x, Math.min( max.x, this.x ) );
        this.y = Math.max( min.y, Math.min( max.y, this.y ) );
        this.z = Math.max( min.z, Math.min( max.z, this.z ) );
        return this;
    }

    public Vector3 clampScalar(float minVal, float maxVal){
        Vector3 min = new Vector3();
        Vector3 max = new Vector3();
        min.set( minVal, minVal, minVal );
        max.set( maxVal, maxVal, maxVal );

        return this.clamp( min, max );
    }

    public Vector3 clampLength(float min, float max){
        float length = this.length() != 0 ? this.length() : 1;
        return this.divideScalar( length ).multiplyScalar( Math.max( min, Math.min( max, length ) ) );
    }

    public Vector3 floor(){
        this.x = (float)Math.floor( this.x );
        this.y = (float)Math.floor( this.y );
        this.z = (float)Math.floor( this.z );
        return this;
    }

    public Vector3 ceil(){
        this.x = (float)Math.ceil( this.x );
        this.y = (float)Math.ceil( this.y );
        this.z = (float)Math.ceil( this.z );
        return this;
    }

    public Vector3 round(){
        this.x = (float)Math.round( this.x );
        this.y = (float)Math.round( this.y );
        this.z = (float)Math.round( this.z );
        return this;
    }

    public Vector3 roundToZero(){
        this.x = ( this.x < 0 ) ? (float)Math.ceil( this.x ) : (float)Math.floor( this.x );
        this.y = ( this.y < 0 ) ? (float)Math.ceil( this.y ) : (float)Math.floor( this.y );
        this.z = ( this.z < 0 ) ? (float)Math.ceil( this.z ) : (float)Math.floor( this.z );
        return this;
    }

    public Vector3 negate(){
        this.x = - this.x;
        this.y = - this.y;
        this.z = - this.z;
        return this;
    }

    public float dot(Vector3 v){
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public float lengthSq(){
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float length(){
        return (float)Math.sqrt( this.x * this.x + this.y * this.y + this.z * this.z );
    }

    public float manhattanLength(){
        return Math.abs( this.x ) + Math.abs( this.y ) + Math.abs( this.z );
    }

    public Vector3 normalize(){
        float length = this.length() != 0 ? this.length() : 1;
        return this.divideScalar( length );
    }

    public Vector3 setLength(float length){
        return this.normalize().multiplyScalar( length );
    }

    public Vector3 lerp(Vector3 v, float alpha){
        this.x += ( v.x - this.x ) * alpha;
        this.y += ( v.y - this.y ) * alpha;
        this.z += ( v.z - this.z ) * alpha;
        return this;
    }

    public Vector3 lerpVectors(Vector3 v1, Vector3 v2, float alpha){
        return this.subVectors( v2, v1 ).multiplyScalar( alpha ).add( v1 );
    }

    public Vector3 cross(Vector3 v){
        return this.crossVectors( this, v );
    }

    public Vector3 crossVectors(Vector3 a, Vector3 b){
        float ax = a.x, ay = a.y, az = a.z;
        float bx = b.x, by = b.y, bz = b.z;

        this.x = ay * bz - az * by;
        this.y = az * bx - ax * bz;
        this.z = ax * by - ay * bx;
        return this;
    }

    public Vector3 projectOnVector(Vector3 vector){
        float scalar = vector.dot( this ) / vector.lengthSq();
        return this.copy( vector ).multiplyScalar( scalar );
    }

    public Vector3 projectOnPlane(Vector3 planeNormal){
        Vector3 v1 = new Vector3();
        v1.copy( this ).projectOnVector( planeNormal );
        return this.sub( v1 );
    }

    public Vector3 reflect(Vector3 normal){
        Vector3 v1 = new Vector3();
        return this.sub( v1.copy( normal ).multiplyScalar( 2 * this.dot( normal ) ) );
    }

    public float angleTo(Vector3 v){
        float theta = (float)(this.dot( v ) / ( Math.sqrt( this.lengthSq() * v.lengthSq() ) ));
        return (float)Math.acos( Math_.clamp( theta, - 1, 1 ) );
    }

    public float distanceTo(Vector3 v){
        return (float)Math.sqrt( this.distanceToSquared( v ) );
    }

    public float distanceToSquared(Vector3 v){
        float dx = this.x - v.x, dy = this.y - v.y, dz = this.z - v.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public float manhattanDistanceTo(Vector3 v){
        return Math.abs( this.x - v.x ) + Math.abs( this.y - v.y ) + Math.abs( this.z - v.z );
    }

    public Vector3 setFromSphericacl(Spherical s){
        return this.setFromSphericalCoords( s.radius, s.phi, s.theta );
    }

    public Vector3 setFromSphericalCoords(float radius, float phi, float theta){
        float sinPhiRadius = (float) Math.sin( phi ) * radius;
        this.x = sinPhiRadius * (float)Math.sin( theta );
        this.y = (float)Math.cos( phi ) * radius;
        this.z = sinPhiRadius * (float)Math.cos( theta );
        return this;
    }

    public Vector3 setFromCylindrical(Cylindrical c){
        return this.setFromCylindricalCoords( c.radius, c.theta, c.y );
    }

    public Vector3 setFromCylindricalCoords(float radius, float theta, float y){
        this.x = radius * (float)Math.sin( theta );
        this.y = y;
        this.z = radius * (float)Math.cos( theta );
        return this;
    }

    public Vector3 setFromMatrixPosition(Matrix4 m){
        float[] e = m.elements;
        this.x = e[ 12 ];
        this.y = e[ 13 ];
        this.z = e[ 14 ];
        return this;
    }

    public Vector3 setFromMatrixScale(Matrix4 m){
        float sx = this.setFromMatrixColumn( m, 0 ).length();
        float sy = this.setFromMatrixColumn( m, 1 ).length();
        float sz = this.setFromMatrixColumn( m, 2 ).length();

        this.x = sx;
        this.y = sy;
        this.z = sz;
        return this;
    }

    public Vector3 setFromMatrixColumn(Matrix4 m, int index){
        return this.fromArray( m.elements, index * 4 );
    }

    public boolean equals(Vector3 v){
        return ( ( v.x == this.x ) && ( v.y == this.y ) && ( v.z == this.z ) );
    }

    public Vector3 fromArray(float[] array, int offset){
        this.x = array[ offset ];
        this.y = array[ offset + 1 ];
        this.z = array[ offset + 2 ];
        return this;
    }

    public float[] toArray(float[] array, int offset){
        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;
        array[ offset + 2 ] = this.z;

        return array;
    }

    public Vector3 fromBufferAttribute(Float32BufferAttribute attribute, int index, int offset){
        this.x = (float)attribute.getX( index );
        this.y = (float)attribute.getY( index );
        this.z = (float)attribute.getZ( index );

        return this;
    }

}
