package three.bufferAttribute;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Uint32BufferAttribute extends BufferAttribute {

    public IntBuffer array;
    public Uint32BufferAttribute(int[] array, int itemSize){
        super();
        this.type = "Uint32BufferAttribute";
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.array = bb.asIntBuffer();
        this.array.put(array);
        this.array.position(0);
        this.itemSize = itemSize;
        this.count = array.length / itemSize;
    }

    public Uint32BufferAttribute clone_(){
        return this;
    }

    public int getX(int index){
        return this.array.get(index * this.itemSize);
    }

    public int getY(int index){
        return this.array.get(index * this.itemSize + 1);
    }

    public int getZ(int index){
        return this.array.get(index * this.itemSize + 2);
    }

}
