package three.math;

public class Euler {
    public static final String[] RotationOrders = new String[]{"XYZ", "YZX", "ZXY", "XZY", "YXZ", "ZYX"};
    public static final String DefaultOrder = "XYZ";

    public float x;
    public float y;
    public float z;
    public String order;
    public boolean  isEuler;
    private Quaternion onChangeQuaternion;

    public Euler(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.isEuler = true;
    }

    public Euler(float x, float y, float z, String order){
        this.x = x;
        this.y = y;
        this.z = z;
        this.order = order;
        this.isEuler = true;
    }

    public Euler set(float x, float y, float z, String order){
        this.x = x;
        this.y = y;
        this.z = z;
        this.order = order;
        this.onChangeCallback();
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

    public void setOrder(String order){
        this.order = order;
        onChangeCallback();
    }

    public void setOnchangeCallback(Quaternion q){
        this.onChangeQuaternion = q;
    }

    private void onChangeCallback(){
        if(this.onChangeQuaternion != null){
            this.onChangeQuaternion.setFromEuler( this, false );
        }
    }

    public Euler clone(){
        return new Euler(this.x, this.y, this.z, this.order);
    }

    public Euler copy(Euler euler){
        this.x = euler.x;
        this.y = euler.y;
        this.z = euler.z;
        this.order = euler.order;

        this.onChangeCallback();
        return this;
    }

    public Euler setFromRotationMatrix(Matrix4 m, String order, boolean update){

        // assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)
        float[] te = m.elements;
        float m11 = te[ 0 ], m12 = te[ 4 ], m13 = te[ 8 ];
        float m21 = te[ 1 ], m22 = te[ 5 ], m23 = te[ 9 ];
        float m31 = te[ 2 ], m32 = te[ 6 ], m33 = te[ 10 ];

        if (order.equals("XYZ")) {
            this.y = (float)Math.asin( Math_.clamp( m13, - 1, 1 ) );
            if ( Math.abs( m13 ) < 0.99999 ) {
                this.x = (float)Math.atan2( - m23, m33 );
                this.z = (float)Math.atan2( - m12, m11 );
            } else {
                this.x = (float)Math.atan2( m32, m22 );
                this.z = 0;
            }

        } else if (order.equals("YXZ")) {
            this.x = (float)Math.asin( - Math_.clamp( m23, - 1, 1 ) );
            if ( Math.abs( m23 ) < 0.99999 ) {
                this.y = (float)Math.atan2( m13, m33 );
                this.z = (float)Math.atan2( m21, m22 );
            } else {
                this.y = (float)Math.atan2( - m31, m11 );
                this.z = 0;
            }

        } else if (order.equals("ZXY")) {
            this.x = (float)Math.asin( Math_.clamp( m32, - 1, 1 ) );
            if ( Math.abs( m32 ) < 0.99999 ) {
                this.y = (float)Math.atan2( - m31, m33 );
                this.z = (float)Math.atan2( - m12, m22 );
            } else {
                this.y = 0;
                this.z = (float)Math.atan2( m21, m11 );
            }

        } else if (order.equals("ZYX")) {
            this.y = (float)Math.asin( - Math_.clamp( m31, - 1, 1 ) );
            if ( Math.abs( m31 ) < 0.99999 ) {
                this.x = (float)Math.atan2( m32, m33 );
                this.z = (float)Math.atan2( m21, m11 );
            } else {
                this.x = 0;
                this.z = (float)Math.atan2( - m12, m22 );
            }

        } else if (order.equals("YZX")) {
            this.z = (float)Math.asin( Math_.clamp( m21, - 1, 1 ) );
            if ( Math.abs( m21 ) < 0.99999 ) {
                this.x = (float)Math.atan2( - m23, m22 );
                this.y = (float)Math.atan2( - m31, m11 );
            } else {
                this.x = 0;
                this.y = (float)Math.atan2( m13, m33 );
            }

        } else if (order.equals("XZY")) {
            this.z = (float)Math.asin( - Math_.clamp( m12, - 1, 1 ) );
            if ( Math.abs( m12 ) < 0.99999 ) {
                this.x = (float)Math.atan2( m32, m22 );
                this.y = (float)Math.atan2( m13, m11 );
            } else {
                this.x = (float)Math.atan2( - m23, m33 );
                this.y = 0;
            }

        }

        this.order = order;

        if (update) this.onChangeCallback();
        return this;
    }

    public Euler setFromQuaternion(Quaternion q, String order, boolean update){
        Matrix4 matrix = new Matrix4();
        matrix.makeRotationFromQuaternion( q );
        return this.setFromRotationMatrix( matrix, order, update );
    }

    public Euler setFromVector3(Vector3 v, String order){
        return this.set( v.x, v.y, v.z, order );
    }

    public Euler reorder(String newOrder){
        Quaternion q = new Quaternion();
        q.setFromEuler( this, false );
        return this.setFromQuaternion( q, newOrder, false );
    }

    public boolean equals(Euler euler){
        return ( euler.x == this.x ) && ( euler.y == this.y ) && ( euler.z == this.z ) && (euler.order.equals(this.order));
    }

    public Euler fromArray(float[] array){
        this.x = array[ 0 ];
        this.y = array[ 1 ];
        this.z = array[ 2 ];
        this.onChangeCallback();
        return this;
    }

    public float[] toArray(float[] array, int offset){
        array[ offset ] = this.x;
        array[ offset + 1 ] = this.y;
        array[ offset + 2 ] = this.z;
        //array[ offset + 3 ] = this.order;
        return array;
    }

    public Vector3 toVector3(){
        return new Vector3( this.x, this.y, this.z );
    }


}
