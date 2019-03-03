package three.math;

import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;

public class Matrix3 {
    public float[] elements;
    public Matrix3(){
        this.elements = new float[]{
                1,0,0,
                0,1,0,
                0,0,1};
    }

    public Matrix3 Set(float n11,float n12,float n13,
                       float n21,float n22,float n23,
                       float n31,float n32,float n33){
        float[] te = this.elements;
        te[0] = n11; te[3] = n12; te[6] = n13;
        te[1] = n21; te[4] = n22; te[7] = n23;
        te[2] = n31; te[5] = n32; te[8] = n33;
        return this;
    }

    public Matrix3 Identity(){
        this.Set(
                1,0,0,
                0,1,0,
                0,0,1
        );
        return this;
    }

    public Matrix3 Clone(){
        return new Matrix3().FromArray(this.elements, 0);
    }

    public Matrix3 Copy(Matrix3 m){
        float[] te = this.elements;
        float[] me = m.elements;
        te[ 0 ] = me[ 0 ]; te[ 1 ] = me[ 1 ]; te[ 2 ] = me[ 2 ];
        te[ 3 ] = me[ 3 ]; te[ 4 ] = me[ 4 ]; te[ 5 ] = me[ 5 ];
        te[ 6 ] = me[ 6 ]; te[ 7 ] = me[ 7 ]; te[ 8 ] = me[ 8 ];

        return this;
    }

    public Matrix3 SetFromMatrix4(Matrix4 m){
        float[] me = m.elements;

        this.Set(
                me[ 0 ], me[ 4 ], me[ 8 ],
                me[ 1 ], me[ 5 ], me[ 9 ],
                me[ 2 ], me[ 6 ], me[ 10 ]
        );

        return this;
    }

    public Matrix3 Multiply(Matrix3 m){
        return this.MultiplyMatrices(this, m);
    }

    public Matrix3 Premultiply(Matrix3 m){
        return this.MultiplyMatrices(m, this);
    }

    public Matrix3 MultiplyMatrices(Matrix3 a, Matrix3 b){
        float[] ae = a.elements;
        float[] be = b.elements;
        float[] te = this.elements;

        float a11 = ae[ 0 ], a12 = ae[ 3 ], a13 = ae[ 6 ];
        float a21 = ae[ 1 ], a22 = ae[ 4 ], a23 = ae[ 7 ];
        float a31 = ae[ 2 ], a32 = ae[ 5 ], a33 = ae[ 8 ];

        float b11 = be[ 0 ], b12 = be[ 3 ], b13 = be[ 6 ];
        float b21 = be[ 1 ], b22 = be[ 4 ], b23 = be[ 7 ];
        float b31 = be[ 2 ], b32 = be[ 5 ], b33 = be[ 8 ];

        te[ 0 ] = a11 * b11 + a12 * b21 + a13 * b31;
        te[ 3 ] = a11 * b12 + a12 * b22 + a13 * b32;
        te[ 6 ] = a11 * b13 + a12 * b23 + a13 * b33;

        te[ 1 ] = a21 * b11 + a22 * b21 + a23 * b31;
        te[ 4 ] = a21 * b12 + a22 * b22 + a23 * b32;
        te[ 7 ] = a21 * b13 + a22 * b23 + a23 * b33;

        te[ 2 ] = a31 * b11 + a32 * b21 + a33 * b31;
        te[ 5 ] = a31 * b12 + a32 * b22 + a33 * b32;
        te[ 8 ] = a31 * b13 + a32 * b23 + a33 * b33;
        return this;
    }

    public Matrix3 MultiplyScalar(float s){
        float[] te = this.elements;

        te[ 0 ] *= s; te[ 3 ] *= s; te[ 6 ] *= s;
        te[ 1 ] *= s; te[ 4 ] *= s; te[ 7 ] *= s;
        te[ 2 ] *= s; te[ 5 ] *= s; te[ 8 ] *= s;
        return this;
    }

    public Matrix3 ApplyToBufferAttribute(Float32BufferAttribute attribute){
        Vector3 v1 = new Vector3();
        for ( int i = 0, l = attribute.count; i < l; i ++ ) {

            v1.x = attribute.GetX( i );
            v1.y = attribute.GetY( i );
            v1.z = attribute.GetZ( i );

            v1.ApplyMatrix3( this );

            attribute.SetXYZ( i, v1.x, v1.y, v1.z );

        }
        return this;
    }

    public float Determinant(){
        float[] te = this.elements;

        float a = te[ 0 ], b = te[ 1 ], c = te[ 2 ],
                d = te[ 3 ], e = te[ 4 ], f = te[ 5 ],
                g = te[ 6 ], h = te[ 7 ], i = te[ 8 ];

        return a * e * i - a * f * h - b * d * i + b * f * g + c * d * h - c * e * g;
    }

    public Matrix3 Transpose(){
        float[] m = this.elements;
        float tmp;

        tmp = m[ 1 ]; m[ 1 ] = m[ 3 ]; m[ 3 ] = tmp;
        tmp = m[ 2 ]; m[ 2 ] = m[ 6 ]; m[ 6 ] = tmp;
        tmp = m[ 5 ]; m[ 5 ] = m[ 7 ]; m[ 7 ] = tmp;
        return this;
    }

    public Matrix3 GetInverse(Matrix3 m){
        float[] te = this.elements;
        float[] me = m.elements;

        float n11 = me[ 0 ], n21 = me[ 1 ], n31 = me[ 2 ];
        float n12 = me[ 3 ], n22 = me[ 4 ], n32 = me[ 5 ];
        float n13 = me[ 6 ], n23 = me[ 7 ], n33 = me[ 8 ];

        float t11 = n33 * n22 - n32 * n23;
        float t12 = n32 * n13 - n33 * n12;
        float t13 = n23 * n12 - n22 * n13;

        float det = n11 * t11 + n21 * t12 + n31 * t13;

        if ( det == 0 ) {
            System.out.println("det is 0");
            return this.Identity();
        }

        float detInv = 1 / det;

        te[ 0 ] = t11 * detInv;
        te[ 1 ] = ( n31 * n23 - n33 * n21 ) * detInv;
        te[ 2 ] = ( n32 * n21 - n31 * n22 ) * detInv;

        te[ 3 ] = t12 * detInv;
        te[ 4 ] = ( n33 * n11 - n31 * n13 ) * detInv;
        te[ 5 ] = ( n31 * n12 - n32 * n11 ) * detInv;

        te[ 6 ] = t13 * detInv;
        te[ 7 ] = ( n21 * n13 - n23 * n11 ) * detInv;
        te[ 8 ] = ( n22 * n11 - n21 * n12 ) * detInv;
        return this;
    }

    public Matrix3 GetNormalMatrix(Matrix4 matrix4){
        return this.SetFromMatrix4( matrix4 ).GetInverse( this ).Transpose();
    }

    public Matrix3 Scale(float sx, float sy){
        float[] te = this.elements;
        te[ 0 ] *= sx; te[ 3 ] *= sx; te[ 6 ] *= sx;
        te[ 1 ] *= sy; te[ 4 ] *= sy; te[ 7 ] *= sy;
        return this;
    }

    public boolean Equals(Matrix3 matrix){
        float[] te = this.elements;
        float[] me = matrix.elements;

        for ( int i = 0; i < 9; i ++ ) {
            if ( te[ i ] != me[ i ] ) return false;
        }
        return true;
    }

    public Matrix3 FromArray(float[] array, int offset){
        for ( int i = 0; i < 9; i ++ ) {
            this.elements[ i ] = array[ i + offset ];
        }
        return this;
    }

    public float[] ToArray(float[] array, int offset) {
        float[] te = this.elements;

        array[ offset ] = te[ 0 ];
        array[ offset + 1 ] = te[ 1 ];
        array[ offset + 2 ] = te[ 2 ];

        array[ offset + 3 ] = te[ 3 ];
        array[ offset + 4 ] = te[ 4 ];
        array[ offset + 5 ] = te[ 5 ];

        array[ offset + 6 ] = te[ 6 ];
        array[ offset + 7 ] = te[ 7 ];
        array[ offset + 8 ] = te[ 8 ];

        return array;
    }

}
