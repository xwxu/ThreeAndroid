package three.math;

public class Quaternion {
    public float x;
    public float y;
    public float z;
    public float w;
    public boolean isQuaternion;
    private Euler onChangeEuler;

    public Quaternion(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 1;
        this.isQuaternion = true;
    }

    public Quaternion(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.isQuaternion = true;
    }

    public Quaternion slerp(Quaternion qa, Quaternion qb, Quaternion qm, float t){
        return this;
    }

    public Quaternion slerpFlat(){
        return this;
    }

    public void setX(float x){
        this.x = x;
        onChangeCallback();
    }

    public void setY(float y){
        this.y = y;
        onChangeCallback();
    }

    public void setZ(float z){
        this.z = z;
        onChangeCallback();
    }

    public void setW(float w){
        this.w = w;
        onChangeCallback();
    }

    public void setOnchangeCallback(Euler euler){
        this.onChangeEuler = euler;
    }

    private void onChangeCallback(){
        if(this.onChangeEuler != null){
            this.onChangeEuler.setFromQuaternion( this, "", false );
        }
    }

    public Quaternion set(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternion copy(Quaternion quaternion){
        this.x = quaternion.x;
        this.y = quaternion.y;
        this.z = quaternion.z;
        this.w = quaternion.w;

        this.onChangeCallback();
        return this;
    }

    public Quaternion clone_(){
        return new Quaternion(this.x, this.y, this.z, this.w);
    }

    public Quaternion setFromEuler(Euler euler, boolean update){
        float x = euler.x, y = euler.y, z = euler.z;
        String order = euler.order;

        float c1 = (float)Math.cos( x / 2 );
        float c2 = (float)Math.cos( y / 2 );
        float c3 = (float)Math.cos( z / 2 );

        float s1 = (float)Math.sin( x / 2 );
        float s2 = (float)Math.sin( y / 2 );
        float s3 = (float)Math.sin( z / 2 );

        if (order.equals("XYZ")) {
            this.x = s1 * c2 * c3 + c1 * s2 * s3;
            this.y = c1 * s2 * c3 - s1 * c2 * s3;
            this.z = c1 * c2 * s3 + s1 * s2 * c3;
            this.w = c1 * c2 * c3 - s1 * s2 * s3;
        } else if (order.equals("YXZ")) {
            this.x = s1 * c2 * c3 + c1 * s2 * s3;
            this.y = c1 * s2 * c3 - s1 * c2 * s3;
            this.z = c1 * c2 * s3 - s1 * s2 * c3;
            this.w = c1 * c2 * c3 + s1 * s2 * s3;
        } else if (order.equals("ZXY")) {
            this.x = s1 * c2 * c3 - c1 * s2 * s3;
            this.y = c1 * s2 * c3 + s1 * c2 * s3;
            this.z = c1 * c2 * s3 + s1 * s2 * c3;
            this.w = c1 * c2 * c3 - s1 * s2 * s3;
        } else if (order.equals("ZYX")) {
            this.x = s1 * c2 * c3 - c1 * s2 * s3;
            this.y = c1 * s2 * c3 + s1 * c2 * s3;
            this.z = c1 * c2 * s3 - s1 * s2 * c3;
            this.w = c1 * c2 * c3 + s1 * s2 * s3;
        } else if (order.equals("YZX")) {
            this.x = s1 * c2 * c3 + c1 * s2 * s3;
            this.y = c1 * s2 * c3 + s1 * c2 * s3;
            this.z = c1 * c2 * s3 - s1 * s2 * c3;
            this.w = c1 * c2 * c3 - s1 * s2 * s3;
        } else if (order.equals("XZY")) {
            this.x = s1 * c2 * c3 - c1 * s2 * s3;
            this.y = c1 * s2 * c3 - s1 * c2 * s3;
            this.z = c1 * c2 * s3 + s1 * s2 * c3;
            this.w = c1 * c2 * c3 + s1 * s2 * s3;
        }

        if (update) this.onChangeCallback();

        return this;
    }

    public Quaternion setFromAxisAngle(Vector3 axis, float angle){
        float halfAngle = angle / 2, s = (float)Math.sin( halfAngle );

        this.x = axis.x * s;
        this.y = axis.y * s;
        this.z = axis.z * s;
        this.w = (float)Math.cos( halfAngle );

        this.onChangeCallback();
        return this;
    }

    public Quaternion setFromRotationMatrix(Matrix4 m){
        float[] te = m.elements;

        float m11 = te[ 0 ], m12 = te[ 4 ], m13 = te[ 8 ],
        m21 = te[ 1 ], m22 = te[ 5 ], m23 = te[ 9 ],
        m31 = te[ 2 ], m32 = te[ 6 ], m33 = te[ 10 ],
        trace = m11 + m22 + m33,
        s;

        if ( trace > 0 ) {
            s = 0.5f / (float)Math.sqrt( trace + 1.0 );
            this.w = 0.25f / s;
            this.x = ( m32 - m23 ) * s;
            this.y = ( m13 - m31 ) * s;
            this.z = ( m21 - m12 ) * s;

        } else if ( m11 > m22 && m11 > m33 ) {
            s = 2.0f * (float)Math.sqrt( 1.0f + m11 - m22 - m33 );
            this.w = ( m32 - m23 ) / s;
            this.x = 0.25f * s;
            this.y = ( m12 + m21 ) / s;
            this.z = ( m13 + m31 ) / s;

        } else if ( m22 > m33 ) {
            s = 2.0f * (float)Math.sqrt( 1.0 + m22 - m11 - m33 );
            this.w = ( m13 - m31 ) / s;
            this.x = ( m12 + m21 ) / s;
            this.y = 0.25f * s;
            this.z = ( m23 + m32 ) / s;

        } else {
            s = 2.0f * (float)Math.sqrt( 1.0f + m33 - m11 - m22 );
            this.w = ( m21 - m12 ) / s;
            this.x = ( m13 + m31 ) / s;
            this.y = ( m23 + m32 ) / s;
            this.z = 0.25f * s;
        }

        this.onChangeCallback();
        return this;
    }

    public Quaternion setFromUnitVectors(Vector3 vFrom, Vector3 vTo){
        Vector3 v1 = new Vector3();
        float r;
        float EPS = 0.000001f;

        r = vFrom.dot( vTo ) + 1;

        if ( r < EPS ) {
            r = 0;
            if ( Math.abs( vFrom.x ) > Math.abs( vFrom.z ) ) {
                v1.set( - vFrom.y, vFrom.x, 0 );
            } else {
                v1.set( 0, - vFrom.z, vFrom.y );
            }

        } else {
            v1.crossVectors( vFrom, vTo );
        }

        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;
        this.w = r;

        return this.normalize();

    }

    public float angleTo(Quaternion q){
        return 2 * (float)Math.acos( Math.abs( Math_.clamp( this.dot( q ), - 1, 1 ) ) );
    }

    public Quaternion rotateTowards(Quaternion q, float step){
        float angle = this.angleTo(q);

        if ( angle == 0 )
            return this;

        float t = Math.min( 1, step / angle );
        this.slerp( q, t );

        return this;
    }

    public Quaternion inverse(){
        return this.conjugate();
    }

    public Quaternion conjugate(){
        this.x *= - 1;
        this.y *= - 1;
        this.z *= - 1;

        this.onChangeCallback();
        return this;
    }

    public float dot(Quaternion v){
        return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
    }

    public float lengthSq(){
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }

    public float length(){
        return (float)Math.sqrt( this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w );
    }

    public Quaternion normalize(){
        float l = this.length();

        if ( l == 0 ) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.w = 1;
        } else {
            l = 1 / l;
            this.x = this.x * l;
            this.y = this.y * l;
            this.z = this.z * l;
            this.w = this.w * l;
        }

        this.onChangeCallback();
        return this;
    }

    public Quaternion multiply(Quaternion q){
        return this.multiplyQuaternions( this, q );
    }

    public Quaternion premultiply(Quaternion q){
        return this.multiplyQuaternions( q, this );
    }

    public Quaternion multiplyQuaternions(Quaternion a, Quaternion b){
        float qax = a.x, qay = a.y, qaz = a.z, qaw = a.w;
        float qbx = b.x, qby = b.y, qbz = b.z, qbw = b.w;

        this.x = qax * qbw + qaw * qbx + qay * qbz - qaz * qby;
        this.y = qay * qbw + qaw * qby + qaz * qbx - qax * qbz;
        this.z = qaz * qbw + qaw * qbz + qax * qby - qay * qbx;
        this.w = qaw * qbw - qax * qbx - qay * qby - qaz * qbz;

        this.onChangeCallback();
        return this;
    }

    public Quaternion slerp(Quaternion qb, float t){
        if ( t == 0 ) return this;
        if ( t == 1 ) return this.copy( qb );

        float x = this.x, y = this.y, z = this.z, w = this.w;

        float cosHalfTheta = w * qb.w + x * qb.x + y * qb.y + z * qb.z;

        if ( cosHalfTheta < 0 ) {
            this.w = - qb.w;
            this.x = - qb.x;
            this.y = - qb.y;
            this.z = - qb.z;
            cosHalfTheta = - cosHalfTheta;
        } else {
            this.copy( qb );
        }

        if ( cosHalfTheta >= 1.0 ) {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        float sqrSinHalfTheta = 1.0f - cosHalfTheta * cosHalfTheta;

        if ( sqrSinHalfTheta <= 0.000000001 ) {
            float s = 1 - t;
            this.w = s * w + t * this.w;
            this.x = s * x + t * this.x;
            this.y = s * y + t * this.y;
            this.z = s * z + t * this.z;
            return this.normalize();
        }

        float sinHalfTheta = (float)Math.sqrt( sqrSinHalfTheta );
        float halfTheta = (float)Math.atan2( sinHalfTheta, cosHalfTheta );
        float ratioA = (float)Math.sin( ( 1 - t ) * halfTheta ) / sinHalfTheta,
                ratioB = (float)Math.sin( t * halfTheta ) / sinHalfTheta;

        this.w = ( w * ratioA + this.w * ratioB );
        this.x = ( x * ratioA + this.x * ratioB );
        this.y = ( y * ratioA + this.y * ratioB );
        this.z = ( z * ratioA + this.z * ratioB );

        this.onChangeCallback();
        return this;
    }

    public boolean equals(Quaternion quaternion){
        return ( quaternion.x == this.x ) && ( quaternion.y == this.y ) && ( quaternion.z == this.z ) && ( quaternion.w == this.w );
    }

    public Quaternion fromArray(float[] array, int offset){
        this.x = array[ offset ];
        this.y = array[ offset + 1 ];
        this.z = array[ offset + 2 ];
        this.w = array[ offset + 3 ];

        this.onChangeCallback();
        return this;
    }

    public float[] toArray(float[] array, int offset){
        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;
        array[ offset + 2 ] = this.z;
        array[ offset + 3 ] = this.w;
        return array;
    }

}
