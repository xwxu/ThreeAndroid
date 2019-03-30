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

    public boolean equals(Vector4 v) {
        return ( ( v.x == this.x ) && ( v.y == this.y ) && ( v.z == this.z ) && ( v.w == this.w ) );
    }

    public Vector4 set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Vector4 copy(Vector4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    public float[] toArray(float[] array, int offset) {

        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;
        array[ offset + 2 ] = this.z;
        array[ offset + 3 ] = this.w;

        return array;
    }

    public Vector4 clone_() {
        return new Vector4(this.x, this.y, this.z, this.w);
    }


    public Vector4 multiplyScalar(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }

    public Vector4 fromArray(float[] array, int offset) {
        this.x = array[ offset ];
        this.y = array[ offset + 1 ];
        this.z = array[ offset + 2 ];
        this.w = array[ offset + 3 ];

        return this;
    }

    public float dot(Vector4 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
    }

    public Vector4 applyMatrix4(Matrix4 m) {
        float x = this.x, y = this.y, z = this.z, w = this.w;
        float[] e = m.elements;

        this.x = e[ 0 ] * x + e[ 4 ] * y + e[ 8 ] * z + e[ 12 ] * w;
        this.y = e[ 1 ] * x + e[ 5 ] * y + e[ 9 ] * z + e[ 13 ] * w;
        this.z = e[ 2 ] * x + e[ 6 ] * y + e[ 10 ] * z + e[ 14 ] * w;
        this.w = e[ 3 ] * x + e[ 7 ] * y + e[ 11 ] * z + e[ 15 ] * w;

        return this;
    }
}
