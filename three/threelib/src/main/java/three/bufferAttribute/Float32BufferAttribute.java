package three.bufferAttribute;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import three.math.Color;
import three.math.Vector2;
import three.math.Vector3;

public class Float32BufferAttribute extends BufferAttribute {
    public FloatBuffer array;

    public Float32BufferAttribute(int arrayLength, int itemSize){
        super();
        this.type = "Float32BufferAttribute";
        ByteBuffer bb = ByteBuffer.allocateDirect(arrayLength * 4);
        bb.order(ByteOrder.nativeOrder());
        this.array = bb.asFloatBuffer();
        this.itemSize = itemSize;
        this.count = arrayLength / itemSize;
    }
    
    public Float32BufferAttribute(float[] array, int itemSize){
        super();
        this.type = "Float32BufferAttribute";
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.array = bb.asFloatBuffer();
        this.array.put(array);
        this.array.position(0);
        this.itemSize = itemSize;
        this.count = array.length / itemSize;
    }

    public float getX(int index){
        return this.array.get(index * this.itemSize);
    }

    public float getY(int index){
        return this.array.get(index * this.itemSize + 1);
    }

    public float getZ(int index){
        return this.array.get(index * this.itemSize + 2);
    }

    public BufferAttribute setXYZ(int index, float x, float y, float z){
        index *= this.itemSize;

        this.array.put(index, x);
        this.array.put(index + 1, y );
        this.array.put(index + 2, z);

        return this;
    }

    public Float32BufferAttribute copyColorsArray(ArrayList<Color> colors){
        FloatBuffer array = this.array;
        int offset = 0;

        for ( int i = 0, l = colors.size(); i < l; i ++ ) {
            Color color = colors.get(i);
            if ( color == null ) {
                color = new Color();
            }
            array.put(offset ++, color.r);
            array.put(offset ++, color.g);
            array.put(offset ++, color.b);
        }

        return this;
    }

    public Float32BufferAttribute copyVector2SArray(ArrayList<Vector2> vectors){
        FloatBuffer array = this.array;
        int offset = 0;

        for ( int i = 0, l = vectors.size(); i < l; i ++ ) {
            Vector2 vector = vectors.get(i);
            if ( vector == null ) {
                vector = new Vector2();
            }
            array.put(offset ++, vector.x);
            array.put(offset ++, vector.y);
        }

        return this;
    }

    public Float32BufferAttribute copyVector3SArray(ArrayList<Vector3> vectors){
        FloatBuffer array = this.array;
        int offset = 0;

        for ( int i = 0, l = vectors.size(); i < l; i ++ ) {
            Vector3 vector = vectors.get(i);
            if ( vector == null ) {
                vector = new Vector3();
            }
            array.put(offset ++, vector.x);
            array.put(offset ++, vector.y);
            array.put(offset ++, vector.z);
        }

        return this;
    }

    public Float32BufferAttribute set(float value, int offset){
        this.array.put( offset, value);
        return this;
    }

    public Float32BufferAttribute copyArray(ArrayList lineDistances) {
        return this;
    }
}
