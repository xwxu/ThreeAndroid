package three.bufferAttribute;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class Uint16BufferAttribute extends BufferAttribute{

    public ShortBuffer array;

    public Uint16BufferAttribute(short[] array, int itemSize){
        super();
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 2);
        bb.order(ByteOrder.nativeOrder());
        this.array = bb.asShortBuffer();
        this.array.put(array);
        this.array.position(0);
        this.count = array.length / itemSize;
    }

    public Uint16BufferAttribute Clone(){
        return this;
    }

    public short GetX(int index){
        return this.array.get(index * this.itemSize);
    }

    public short GetY(int index){
        return this.array.get(index * this.itemSize + 1);
    }

    public short GetZ(int index){
        return this.array.get(index * this.itemSize + 2);
    }
}
