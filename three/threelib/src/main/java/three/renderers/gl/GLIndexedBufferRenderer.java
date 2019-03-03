package three.renderers.gl;

import android.opengl.GLES20;

import three.util.BufferData;

public class GLIndexedBufferRenderer extends GLBufferRenderer {

    private int type;
    private int bytesPerElement;

    public GLIndexedBufferRenderer(GLInfo info, GLCapabilities capabilities){
        super(info, capabilities);
    }

    public void SetIndex(BufferData value){
        type = value.type;
        bytesPerElement = value.bytesPerElement;
    }

    public void Render(int start, int count){
        GLES20.glDrawElements(mode, count, type, start * bytesPerElement);
        info.Update(count, mode, 0);
    }
}
