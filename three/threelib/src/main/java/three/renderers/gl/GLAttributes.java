package three.renderers.gl;

import android.opengl.GLES20;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;

import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;
import three.bufferAttribute.Uint16BufferAttribute;
import three.bufferAttribute.Uint32BufferAttribute;
import three.util.BufferData;

public class GLAttributes {
    private HashMap<BufferAttribute, BufferData> buffers;


    public GLAttributes(){
        this.buffers = new HashMap<BufferAttribute, BufferData>();

    }

    private BufferData CreateBuffer(BufferAttribute attribute, int bufferType){
        int usage = attribute.dynamic ? GLES20.GL_DYNAMIC_DRAW : GLES20.GL_STATIC_DRAW;

        final int[] bufferNames = new int[1];
        GLES20.glGenBuffers(1, bufferNames, 0);
        GLES20.glBindBuffer(bufferType, bufferNames[0]);

        int type = GLES20.GL_FLOAT;
        int bytesPerElement = 4;

        if(attribute instanceof Float32BufferAttribute){
            Float32BufferAttribute floatAttrib = (Float32BufferAttribute) attribute;
            GLES20.glBufferData(bufferType, floatAttrib.array.capacity() * 4, floatAttrib.array, usage);
            type = GLES20.GL_FLOAT;
            bytesPerElement = 4;

        }else if (attribute instanceof Uint16BufferAttribute){
            Uint16BufferAttribute uint16Attrib = (Uint16BufferAttribute) attribute;
            GLES20.glBufferData(bufferType, uint16Attrib.array.capacity() * 2, uint16Attrib.array, usage);
            type = GLES20.GL_UNSIGNED_SHORT;
            bytesPerElement = 2;

        }else if (attribute instanceof Uint32BufferAttribute){
            Uint32BufferAttribute uint32Attrib = (Uint32BufferAttribute) attribute;
            GLES20.glBufferData(bufferType, uint32Attrib.array.capacity() * 4, uint32Attrib.array, usage);
            type = GLES20.GL_UNSIGNED_INT;
            bytesPerElement = 4;
        }

        return new BufferData(bufferNames, type, bytesPerElement, attribute.version);
    }

    private void UpdateBuffer(int[] buffer, BufferAttribute attribute, int bufferType){
        Buffer array = null;
        if(attribute instanceof Float32BufferAttribute){
            array = ((Float32BufferAttribute) attribute).array;
        }else if(attribute instanceof Uint32BufferAttribute){
            array = ((Uint32BufferAttribute) attribute).array;
        }else {
            return;
        }

        GLES20.glBindBuffer( bufferType, buffer[0] );

        if ( attribute.dynamic == false ) {

            GLES20.glBufferData( bufferType, array.capacity() * attribute.itemSize, array, GLES20.GL_STATIC_DRAW );

        } else if ( attribute.updateRange_count == - 1 ) {

            // Not using update ranges
            GLES20.glBufferSubData( bufferType, 0, array.capacity() * 4, array );

        } else if ( attribute.updateRange_count == 0 ) {


        } else {

            GLES20.glBufferSubData( bufferType, attribute.updateRange_offset * 4,
                    attribute.updateRange_count * 4, array);

            attribute.updateRange_count = -1;// reset range
        }
    }

    public BufferData Get(BufferAttribute attribute){
        return this.buffers.get(attribute);
    }

    public void Update(BufferAttribute attribute, int bufferType){
        //if ( attribute.isInterleavedBufferAttribute ) attribute = attribute.data;

        BufferData data = buffers.get( attribute );

        if ( data == null ) {

            buffers.put( attribute, CreateBuffer( attribute, bufferType ) );

        } else if ( data.version < attribute.version ) {

            UpdateBuffer( data.buffer, attribute, bufferType );
            data.version = attribute.version;

        }
    }

    public void Remove(BufferAttribute attribute){

        BufferData data = this.buffers.get(attribute);
        if(data != null){
            GLES20.glDeleteBuffers(1, data.buffer, 0);
            this.buffers.remove(attribute);
        }
    }
}
