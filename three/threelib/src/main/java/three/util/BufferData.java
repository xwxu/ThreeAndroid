package three.util;

import java.nio.IntBuffer;

public class BufferData {
    public int[] buffer;
    public int type;
    public int bytesPerElement;
    public int version;

    public BufferData(int[] buffer, int type, int bytesPerElement, int version){
        this.buffer = buffer;
        this.type = type;
        this.bytesPerElement = bytesPerElement;
        this.version = version;
    }
}
