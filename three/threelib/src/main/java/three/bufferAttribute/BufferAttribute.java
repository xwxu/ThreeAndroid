package three.bufferAttribute;

import java.nio.Buffer;

public class BufferAttribute {
    public String type;
    public String name;
    //public Buffer array;
    public int itemSize;
    public int count;
    public boolean normalized;
    public boolean dynamic;
    public int updateRange_offset;
    public int updateRange_count;
    public int version;

    public BufferAttribute(){
        this.type = "BufferAttribute";
        this.name = "";
        this.normalized = false;
        this.dynamic = false;
        this.updateRange_offset = 0;
        this.updateRange_count = -1;
        this.version = 0;
    }

    public void needsUpdate(boolean value){
        if(value == true){
            this.version ++;
        }
    }

    public BufferAttribute Clone(){
        return this;
    }
}
