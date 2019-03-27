package three.renderers.gl;

import android.opengl.GLES20;

public class GLBufferRenderer {

    protected int mode;
    protected GLInfo info;
    protected GLCapabilities capabilities;

    public GLBufferRenderer(GLInfo info, GLCapabilities capabilities){
        this.info = info;
        this.capabilities = capabilities;
    }

    public void setMode(int value){
        mode = value;
    }

    public void render(int start, int count){
        GLES20.glDrawArrays(mode, start, count);
        info.update(count, mode, 0);
    }
}
