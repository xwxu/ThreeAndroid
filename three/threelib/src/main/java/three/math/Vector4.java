package three.math;

public class Vector4 {

    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4() {
        x = 0;
        y = 0;
        z = 0;
        w = 1;
    }

    public boolean Equals(Vector4 v) {
        return ( ( v.x == this.x ) && ( v.y == this.y ) && ( v.z == this.z ) && ( v.w == this.w ) );
    }

    public Vector4 Set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Vector4 Copy(Vector4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    public float[] ToArray(float[] array, int offset) {

        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;
        array[ offset + 2 ] = this.z;
        array[ offset + 3 ] = this.w;

        return array;
    }

    public Vector4 Clone() {
        return new Vector4(this.x, this.y, this.z, this.w);
    }


    public Vector4 MultiplyScalar(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }
}
