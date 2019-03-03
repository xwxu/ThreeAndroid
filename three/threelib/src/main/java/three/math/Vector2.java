package three.math;

import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;

public class Vector2 {
    public float x;
    public float y;

    public Vector2(){
        this.x = 0;
        this.y = 0;
    }

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2 Set(float x, float y){
        this.x = x;
        this.y = y;
        return  this;
    }

    public Vector2 SetScalar(float scalar){
        this.x = scalar;
        this.y = scalar;
        return this;
    }

    public Vector2 SetX(float x){
        this.x = x;
        return this;
    }

    public Vector2 SetY(float y){
        this.y = y;
        return this;
    }

    public float width(){
        return this.x;
    }

    public void width(float value){
        this.x = value;
    }

    public float height(){
        return this.y;
    }

    public void height(float value){
        this.y = value;
    }

    public Vector2 Clone(){
        return new Vector2(this.x, this.y);
    }

    public Vector2 Copy(Vector2 v){
        this.x = v.x;
        this.y = v.y;

        return this;
    }

    public Vector2 Add(Vector2 v){
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2 AddScalar(float s){
        this.x += s;
        this.y += s;
        return this;
    }

    public Vector2 AddVectors(Vector2 a, Vector2 b){
        this.x = a.x + b.x;
        this.y = a.y + b.y;
        return this;
    }

    public Vector2 AddScaledVector(Vector2 v, float s){
        this.x += v.x * s;
        this.y += v.y * s;
        return this;
    }

    public Vector2 Sub(Vector2 v){
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2 SubScalar(float s){
        this.x -= s;
        this.y -= s;
        return this;
    }

    public Vector2 SubVectors(Vector2 a, Vector2 b){
        this.x = a.x - b.x;
        this.y = a.y - b.y;
        return this;
    }

    public Vector2 Multiply(Vector2 v){
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vector2 MultiplyScalar(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2 Divide(Vector2 v){
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    public  Vector2 DivideScalar(float scalar){
        return this.MultiplyScalar( 1 / scalar );
    }

    public Vector2 ApplyMatrix3(Matrix3 m){
        float x = this.x, y = this.y;
        float[] e = m.elements;

        this.x = e[ 0 ] * x + e[ 3 ] * y + e[ 6 ];
        this.y = e[ 1 ] * x + e[ 4 ] * y + e[ 7 ];
        return this;
    }

    public Vector2 Min(Vector2 v){
        this.x = Math.min( this.x, v.x );
        this.y = Math.min( this.y, v.y );

        return this;
    }

    public Vector2 Max(Vector2 v){
        this.x = Math.max( this.x, v.x );
        this.y = Math.max( this.y, v.y );
        return this;
    }

    public Vector2 Clamp(Vector2 min, Vector2 max){
        this.x = Math.max( min.x, Math.min( max.x, this.x ) );
        this.y = Math.max( min.y, Math.min( max.y, this.y ) );
        return this;
    }

    public Vector2 ClampScalar(float minVal, float maxVal){
        Vector2 min = new Vector2();
        Vector2 max = new Vector2();
        min.Set( minVal, minVal );
        max.Set( maxVal, maxVal );

        return this.Clamp( min, max );
    }

    public Vector2 ClampLength(float min, float max){
        float length = this.Length() != 0 ? this.Length() : 1;
        return this.DivideScalar( length ).MultiplyScalar( Math.max( min, Math.min( max, length ) ) );
    }

    public Vector2 Floor(){
        this.x = (float)Math.floor( this.x );
        this.y = (float)Math.floor( this.y );

        return this;
    }

    public Vector2 Ceil(){
        this.x = (float)Math.ceil( this.x );
        this.y = (float)Math.ceil( this.y );

        return this;
    }

    public Vector2 Round(){
        this.x = Math.round( this.x );
        this.y = Math.round( this.y );

        return this;
    }

    public Vector2 RoundToZero(){
        this.x = ( this.x < 0 ) ? (float)Math.ceil( this.x ) : (float)Math.floor( this.x );
        this.y = ( this.y < 0 ) ? (float)Math.ceil( this.y ) : (float)Math.floor( this.y );
        return this;
    }

    public Vector2 Negate(){
        this.x = - this.x;
        this.y = - this.y;
        return this;
    }

    public float Dot(Vector2 v){
        return this.x * v.x + this.y * v.y;
    }

    public float LengthSq(){
        return this.x * this.x + this.y * this.y;
    }

    public float Length(){
        return (float)Math.sqrt( this.x * this.x + this.y * this.y );
    }

    public float ManhattanLength(){
        return Math.abs( this.x ) + Math.abs( this.y );
    }

    public Vector2 Normalize(){
        float length = this.Length() != 0 ? this.Length() : 1;
        return this.DivideScalar( length );
    }

    public float Cross(Vector2 v){
        return this.x * v.y - this.y * v.x;
    }

    public float Angle(){
        float angle = (float)Math.atan2( this.y, this.x );
        if ( angle < 0 ) angle += 2 * Math.PI;
        return angle;
    }

    public float DistanceTo(Vector2 v){
        return (float)Math.sqrt( this.DistanceToSquared( v ) );
    }

    public float DistanceToSquared(Vector2 v){
        float dx = this.x - v.x, dy = this.y - v.y;
        return dx * dx + dy * dy;
    }

    public float ManhattanDistanceTo(Vector3 v){
        return Math.abs( this.x - v.x ) + Math.abs( this.y - v.y );
    }

    public Vector2 SetLength(float length){
        return this.Normalize().MultiplyScalar( length );
    }

    public Vector2 Lerp(Vector3 v, float alpha){
        this.x += ( v.x - this.x ) * alpha;
        this.y += ( v.y - this.y ) * alpha;
        return this;
    }

    public Vector2 LerpVectors(Vector2 v1, Vector2 v2, float alpha){
        return this.SubVectors( v2, v1 ).MultiplyScalar( alpha ).Add( v1 );
    }

    public boolean Equals(Vector2 v){
        return ( ( v.x == this.x ) && ( v.y == this.y ) );
    }

    public Vector2 FromArray(float[] array, int offset){
        this.x = array[ offset ];
        this.y = array[ offset + 1 ];
        return this;
    }

    public float[] ToArray(float[] array, int offset){
        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;

        return array;
    }

    public Vector2 FromBufferAttribute(Float32BufferAttribute attribute, int index, int offset){
        this.x = attribute.GetX( index );
        this.y = attribute.GetY( index );

        return this;
    }

    public Vector2 RotateAround(Vector2 center, float angle){
        float c = (float)Math.cos( angle ), s = (float)Math.sin( angle );

        float x = this.x - center.x;
        float y = this.y - center.y;

        this.x = x * c - y * s + center.x;
        this.y = x * s + y * c + center.y;

        return this;
    }
}
