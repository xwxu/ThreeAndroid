package unitTest.bufferAttribute;

import org.junit.Test;

import three.bufferAttribute.Float32BufferAttribute;

import static org.junit.Assert.*;

public class BufferAttributeTests {
    @Test
    public void Instancing() {

        float[] array = new float[16];
        Float32BufferAttribute a = new Float32BufferAttribute(array, 4);
    }
}
