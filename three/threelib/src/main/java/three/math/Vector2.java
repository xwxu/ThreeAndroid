package three.math;

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

    public Vector2 set(float x, float y){
        this.x = x;
        this.y = y;
        return  this;
    }

    public Vector2 setScalar(float scalar){
        this.x = scalar;
        this.y = scalar;
        return this;
    }

    public Vector2 setX(float x){
        this.x = x;
        return this;
    }

    public Vector2 setY(float y){
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

    public Vector2 copy(Vector2 v){
        this.x = v.x;
        this.y = v.y;

        return this;
    }

    public Vector2 add(Vector2 v){
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2 addScalar(float s){
        this.x += s;
        this.y += s;
        return this;
    }

    public Vector2 addVectors(Vector2 a, Vector2 b){
        this.x = a.x + b.x;
        this.y = a.y + b.y;
        return this;
    }

    public Vector2 addScaledVector(Vector2 v, float s){
        this.x += v.x * s;
        this.y += v.y * s;
        return this;
    }

    public Vector2 sub(Vector2 v){
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2 subScalar(float s){
        this.x -= s;
        this.y -= s;
        return this;
    }

    public Vector2 subVectors(Vector2 a, Vector2 b){
        this.x = a.x - b.x;
        this.y = a.y - b.y;
        return this;
    }

    public Vector2 multiply(Vector2 v){
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vector2 multiplyScalar(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2 divide(Vector2 v){
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    public  Vector2 divideScalar(float scalar){
        return this.multiplyScalar( 1 / scalar );
    }

    public Vector2 applyMatrix3(Matrix3 m){
        float x = this.x, y = this.y;
        float[] e = m.elements;

        this.x = e[ 0 ] * x + e[ 3 ] * y + e[ 6 ];
        this.y = e[ 1 ] * x + e[ 4 ] * y + e[ 7 ];
        return this;
    }

    public Vector2 min(Vector2 v){
        this.x = Math.min( this.x, v.x );
        this.y = Math.min( this.y, v.y );

        return this;
    }

    public Vector2 max(Vector2 v){
        this.x = Math.max( this.x, v.x );
        this.y = Math.max( this.y, v.y );
        return this;
    }

    public Vector2 clamp(Vector2 min, Vector2 max){
        this.x = Math.max( min.x, Math.min( max.x, this.x ) );
        this.y = Math.max( min.y, Math.min( max.y, this.y ) );
        return this;
    }

    public Vector2 clampScalar(float minVal, float maxVal){
        Vector2 min = new Vector2();
        Vector2 max = new Vector2();
        min.set( minVal, minVal );
        max.set( maxVal, maxVal );

        return this.clamp( min, max );
    }

    public Vector2 clampLength(float min, float max){
        float length = this.length() != 0 ? this.length() : 1;
        return this.divideScalar( length ).multiplyScalar( Math.max( min, Math.min( max, length ) ) );
    }

    public Vector2 floor(){
        this.x = (float)Math.floor( this.x );
        this.y = (float)Math.floor( this.y );

        return this;
    }

    public Vector2 ceil(){
        this.x = (float)Math.ceil( this.x );
        this.y = (float)Math.ceil( this.y );

        return this;
    }

    public Vector2 round(){
        this.x = Math.round( this.x );
        this.y = Math.round( this.y );

        return this;
    }

    public Vector2 roundToZero(){
        this.x = ( this.x < 0 ) ? (float)Math.ceil( this.x ) : (float)Math.floor( this.x );
        this.y = ( this.y < 0 ) ? (float)Math.ceil( this.y ) : (float)Math.floor( this.y );
        return this;
    }

    public Vector2 negate(){
        this.x = - this.x;
        this.y = - this.y;
        return this;
    }

    public float dot(Vector2 v){
        return this.x * v.x + this.y * v.y;
    }

    public float lengthSq(){
        return this.x * this.x + this.y * this.y;
    }

    public float length(){
        return (float)Math.sqrt( this.x * this.x + this.y * this.y );
    }

    public float manhattanLength(){
        return Math.abs( this.x ) + Math.abs( this.y );
    }

    public Vector2 normalize(){
        float length = this.length() != 0 ? this.length() : 1;
        return this.divideScalar( length );
    }

    public float cross(Vector2 v){
        return this.x * v.y - this.y * v.x;
    }

    public float angle(){
        float angle = (float)Math.atan2( this.y, this.x );
        if ( angle < 0 ) angle += 2 * Math.PI;
        return angle;
    }

    public float distanceTo(Vector2 v){
        return (float)Math.sqrt( this.distanceToSquared( v ) );
    }

    public float distanceToSquared(Vector2 v){
        float dx = this.x - v.x, dy = this.y - v.y;
        return dx * dx + dy * dy;
    }

    public float manhattanDistanceTo(Vector3 v){
        return Math.abs( this.x - v.x ) + Math.abs( this.y - v.y );
    }

    public Vector2 setLength(float length){
        return this.normalize().multiplyScalar( length );
    }

    public Vector2 lerp(Vector3 v, float alpha){
        this.x += ( v.x - this.x ) * alpha;
        this.y += ( v.y - this.y ) * alpha;
        return this;
    }

    public Vector2 lerpVectors(Vector2 v1, Vector2 v2, float alpha){
        return this.subVectors( v2, v1 ).multiplyScalar( alpha ).add( v1 );
    }

    public boolean equals(Vector2 v){
        return ( ( v.x == this.x ) && ( v.y == this.y ) );
    }

    public Vector2 fromArray(float[] array, int offset){
        this.x = array[ offset ];
        this.y = array[ offset + 1 ];
        return this;
    }

    public float[] toArray(float[] array, int offset){
        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;

        return array;
    }

    public Vector2 fromBufferAttribute(Float32BufferAttribute attribute, int index, int offset){
        this.x = attribute.getX( index );
        this.y = attribute.getY( index );

        return this;
    }

    public Vector2 rotateAround(Vector2 center, float angle){
        float c = (float)Math.cos( angle ), s = (float)Math.sin( angle );

        float x = this.x - center.x;
        float y = this.y - center.y;

        this.x = x * c - y * s + center.x;
        this.y = x * s + y * c + center.y;

        return this;
    }
}
