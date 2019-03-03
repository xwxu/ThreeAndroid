package three.math;

import three.bufferAttribute.Float32BufferAttribute;
import three.cameras.Camera;
import three.bufferAttribute.BufferAttribute;

public class Vector3 {
    public float x;
    public float y;
    public float z;
    public boolean isVector3;

    public Vector3(){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.isVector3 = true;
    }

    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.isVector3 = true;
    }

    public Vector3 Set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 SetScalar(float scalar){
        this.x = scalar;
        this.y = scalar;
        this.z = scalar;
        return this;
    }

    public Vector3 SetX(float x){
        this.x = x;
        return this;
    }

    public Vector3 SetY(float y){
        this.y = y;
        return this;
    }

    public Vector3 SetZ(float z){
        this.z = z;
        return this;
    }

    public Vector3 SetComponent(int index, float value){
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

    public float GetComponent(int index){
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

    public Vector3 Copy(Vector3 v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3 Add(Vector3 v){
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3 AddScalar(float s){
        this.x += s;
        this.y += s;
        this.z += s;
        return this;
    }

    public Vector3 AddVectors(Vector3 a, Vector3 b){
        this.x = a.x + b.x;
        this.y = a.y + b.y;
        this.z = a.z + b.z;
        return this;
    }

    public Vector3 AddScaledVector(Vector3 v, float s){
        this.x += v.x * s;
        this.y += v.y * s;
        this.z =  v.z * s;
        return this;
    }

    public Vector3 Sub(Vector3 v){
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3 SubScalar(float s){
        this.x -= s;
        this.y -= s;
        this.z -= s;
        return this;
    }

    public Vector3 SubVectors(Vector3 a, Vector3 b){
        this.x = a.x - b.x;
        this.y = a.y - b.y;
        this.z = a.z - b.z;
        return this;
    }

    public Vector3 Multiply(Vector3 v){
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector3 MultiplyScalar(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public Vector3 MultiplyVectors(Vector3 a, Vector3 b){
        this.x = a.x * b.x;
        this.y = a.y * b.y;
        this.z = a.z * b.z;
        return this;
    }

    public Vector3 Divide(Vector3 v){
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public  Vector3 DivideScalar(float scalar){
        return this.MultiplyScalar( 1 / scalar );
    }

    public Vector3 ApplyEuler(Euler euler){
        Quaternion quaternion = new Quaternion();
        return this.ApplyQuaternion( quaternion.SetFromEuler( euler, true ) );
    }

    public Vector3 ApplyAxisAngle(Vector3 axis, float angle){
        Quaternion quaternion = new Quaternion();
        return this.ApplyQuaternion( quaternion.SetFromAxisAngle( axis, angle ) );
    }

    public Vector3 ApplyMatrix3(Matrix3 m){
        float x = this.x, y = this.y, z = this.z;
        float[] e = m.elements;

        this.x = e[ 0 ] * x + e[ 3 ] * y + e[ 6 ] * z;
        this.y = e[ 1 ] * x + e[ 4 ] * y + e[ 7 ] * z;
        this.z = e[ 2 ] * x + e[ 5 ] * y + e[ 8 ] * z;
        return this;
    }

    public Vector3 ApplyMatrix4(Matrix4 m){
        float x = this.x, y = this.y, z = this.z;
        float[] e = m.elements;

        float w = 1 / ( e[ 3 ] * x + e[ 7 ] * y + e[ 11 ] * z + e[ 15 ] );

        this.x = ( e[ 0 ] * x + e[ 4 ] * y + e[ 8 ] * z + e[ 12 ] ) * w;
        this.y = ( e[ 1 ] * x + e[ 5 ] * y + e[ 9 ] * z + e[ 13 ] ) * w;
        this.z = ( e[ 2 ] * x + e[ 6 ] * y + e[ 10 ] * z + e[ 14 ] ) * w;
        return this;
    }

    public Vector3 ApplyQuaternion(Quaternion q){
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

    public Vector3 Project(Camera camera){
        return this.ApplyMatrix4(camera.matrixWorldInverse).ApplyMatrix4(camera.projectionMatrix);
    }

    public Vector3 Unproject(Camera camera){
        Matrix4 matrix = new Matrix4();
        return this.ApplyMatrix4(matrix.GetInverse(camera.projectionMatrix)).ApplyMatrix4(camera.matrixWorld);
    }

    public Vector3 TransformDirection(Matrix4 m){

        // input: THREE.Matrix4 affine matrix
        // vector interpreted as a direction
        float x = this.x, y = this.y, z = this.z;
        float[] e = m.elements;

        this.x = e[ 0 ] * x + e[ 4 ] * y + e[ 8 ] * z;
        this.y = e[ 1 ] * x + e[ 5 ] * y + e[ 9 ] * z;
        this.z = e[ 2 ] * x + e[ 6 ] * y + e[ 10 ] * z;

        return this.Normalize();
    }

    public Vector3 Min(Vector3 v){
        this.x = Math.min( this.x, v.x );
        this.y = Math.min( this.y, v.y );
        this.z = Math.min( this.z, v.z );
        return this;
    }

    public Vector3 Max(Vector3 v){
        this.x = Math.max( this.x, v.x );
        this.y = Math.max( this.y, v.y );
        this.z = Math.max( this.z, v.z );
        return this;
    }

    public Vector3 Clamp(Vector3 min, Vector3 max){
        this.x = Math.max( min.x, Math.min( max.x, this.x ) );
        this.y = Math.max( min.y, Math.min( max.y, this.y ) );
        this.z = Math.max( min.z, Math.min( max.z, this.z ) );
        return this;
    }

    public Vector3 ClampScalar(float minVal, float maxVal){
        Vector3 min = new Vector3();
        Vector3 max = new Vector3();
        min.Set( minVal, minVal, minVal );
        max.Set( maxVal, maxVal, maxVal );

        return this.Clamp( min, max );
    }

    public Vector3 ClampLength(float min, float max){
        float length = this.Length() != 0 ? this.Length() : 1;
        return this.DivideScalar( length ).MultiplyScalar( Math.max( min, Math.min( max, length ) ) );
    }

    public Vector3 Floor(){
        this.x = (float)Math.floor( this.x );
        this.y = (float)Math.floor( this.y );
        this.z = (float)Math.floor( this.z );
        return this;
    }

    public Vector3 Ceil(){
        this.x = (float)Math.ceil( this.x );
        this.y = (float)Math.ceil( this.y );
        this.z = (float)Math.ceil( this.z );
        return this;
    }

    public Vector3 Round(){
        this.x = (float)Math.round( this.x );
        this.y = (float)Math.round( this.y );
        this.z = (float)Math.round( this.z );
        return this;
    }

    public Vector3 RoundToZero(){
        this.x = ( this.x < 0 ) ? (float)Math.ceil( this.x ) : (float)Math.floor( this.x );
        this.y = ( this.y < 0 ) ? (float)Math.ceil( this.y ) : (float)Math.floor( this.y );
        this.z = ( this.z < 0 ) ? (float)Math.ceil( this.z ) : (float)Math.floor( this.z );
        return this;
    }

    public Vector3 Negate(){
        this.x = - this.x;
        this.y = - this.y;
        this.z = - this.z;
        return this;
    }

    public float Dot(Vector3 v){
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public float LengthSq(){
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float Length(){
        return (float)Math.sqrt( this.x * this.x + this.y * this.y + this.z * this.z );
    }

    public float ManhattanLength(){
        return Math.abs( this.x ) + Math.abs( this.y ) + Math.abs( this.z );
    }

    public Vector3 Normalize(){
        float length = this.Length() != 0 ? this.Length() : 1;
        return this.DivideScalar( length );
    }

    public Vector3 SetLength(float length){
        return this.Normalize().MultiplyScalar( length );
    }

    public Vector3 Lerp(Vector3 v, float alpha){
        this.x += ( v.x - this.x ) * alpha;
        this.y += ( v.y - this.y ) * alpha;
        this.z += ( v.z - this.z ) * alpha;
        return this;
    }

    public Vector3 LerpVectors(Vector3 v1, Vector3 v2, float alpha){
        return this.SubVectors( v2, v1 ).MultiplyScalar( alpha ).Add( v1 );
    }

    public Vector3 Cross(Vector3 v){
        return this.CrossVectors( this, v );
    }

    public Vector3 CrossVectors(Vector3 a, Vector3 b){
        float ax = a.x, ay = a.y, az = a.z;
        float bx = b.x, by = b.y, bz = b.z;

        this.x = ay * bz - az * by;
        this.y = az * bx - ax * bz;
        this.z = ax * by - ay * bx;
        return this;
    }

    public Vector3 ProjectOnVector(Vector3 vector){
        float scalar = vector.Dot( this ) / vector.LengthSq();
        return this.Copy( vector ).MultiplyScalar( scalar );
    }

    public Vector3 ProjectOnPlane(Vector3 planeNormal){
        Vector3 v1 = new Vector3();
        v1.Copy( this ).ProjectOnVector( planeNormal );
        return this.Sub( v1 );
    }

    public Vector3 Reflect(Vector3 normal){
        Vector3 v1 = new Vector3();
        return this.Sub( v1.Copy( normal ).MultiplyScalar( 2 * this.Dot( normal ) ) );
    }

    public float AngleTo(Vector3 v){
        float theta = (float)(this.Dot( v ) / ( Math.sqrt( this.LengthSq() * v.LengthSq() ) ));
        return (float)Math.acos( Math_.Clamp( theta, - 1, 1 ) );
    }

    public float DistanceTo(Vector3 v){
        return (float)Math.sqrt( this.DistanceToSquared( v ) );
    }

    public float DistanceToSquared(Vector3 v){
        float dx = this.x - v.x, dy = this.y - v.y, dz = this.z - v.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public float ManhattanDistanceTo(Vector3 v){
        return Math.abs( this.x - v.x ) + Math.abs( this.y - v.y ) + Math.abs( this.z - v.z );
    }

    public Vector3 SetFromSphericacl(Spherical s){
        return this.SetFromSphericalCoords( s.radius, s.phi, s.theta );
    }

    public Vector3 SetFromSphericalCoords(float radius, float phi, float theta){
        float sinPhiRadius = (float) Math.sin( phi ) * radius;
        this.x = sinPhiRadius * (float)Math.sin( theta );
        this.y = (float)Math.cos( phi ) * radius;
        this.z = sinPhiRadius * (float)Math.cos( theta );
        return this;
    }

    public Vector3 SetFromCylindrical(Cylindrical c){
        return this.SetFromCylindricalCoords( c.radius, c.theta, c.y );
    }

    public Vector3 SetFromCylindricalCoords(float radius, float theta, float y){
        this.x = radius * (float)Math.sin( theta );
        this.y = y;
        this.z = radius * (float)Math.cos( theta );
        return this;
    }

    public Vector3 SetFromMatrixPosition(Matrix4 m){
        float[] e = m.elements;
        this.x = e[ 12 ];
        this.y = e[ 13 ];
        this.z = e[ 14 ];
        return this;
    }

    public Vector3 SetFromMatrixScale(Matrix4 m){
        float sx = this.SetFromMatrixColumn( m, 0 ).Length();
        float sy = this.SetFromMatrixColumn( m, 1 ).Length();
        float sz = this.SetFromMatrixColumn( m, 2 ).Length();

        this.x = sx;
        this.y = sy;
        this.z = sz;
        return this;
    }

    public Vector3 SetFromMatrixColumn(Matrix4 m, int index){
        return this.FromArray( m.elements, index * 4 );
    }

    public boolean Equals(Vector3 v){
        return ( ( v.x == this.x ) && ( v.y == this.y ) && ( v.z == this.z ) );
    }

    public Vector3 FromArray(float[] array, int offset){
        this.x = array[ offset ];
        this.y = array[ offset + 1 ];
        this.z = array[ offset + 2 ];
        return this;
    }

    public float[] ToArray(float[] array, int offset){
        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;
        array[ offset + 2 ] = this.z;

        return array;
    }

    public Vector3 FromBufferAttribute(Float32BufferAttribute attribute, int index, int offset){
        this.x = (float)attribute.GetX( index );
        this.y = (float)attribute.GetY( index );
        this.z = (float)attribute.GetZ( index );

        return this;
    }

}
