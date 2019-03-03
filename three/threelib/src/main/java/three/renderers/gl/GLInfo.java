package three.renderers.gl;

import android.opengl.GLES20;

import three.util.MemoryInfo;
import three.util.RenderInfo;

public class GLInfo {
    public MemoryInfo memory;
    public RenderInfo render;
    public boolean autoReset = true;

    public GLInfo(){
        this.memory = new MemoryInfo();
        this.render = new RenderInfo();
    }

    public void Update(int count, int mode, int instanceCount){
        this.render.calls ++;
        switch (mode){
            case GLES20.GL_TRIANGLES:
                this.render.triangles += instanceCount * (count / 3);
                break;

            case GLES20.GL_TRIANGLE_STRIP:
            case GLES20.GL_TRIANGLE_FAN:
                render.triangles += instanceCount * ( count - 2 );
                break;

            case GLES20.GL_LINES:
                render.lines += instanceCount * ( count / 2 );
                break;

            case GLES20.GL_LINE_STRIP:
                render.lines += instanceCount * ( count - 1 );
                break;

            case GLES20.GL_LINE_LOOP:
                render.lines += instanceCount * count;
                break;

            case GLES20.GL_POINTS:
                render.points += instanceCount * count;
                break;
        }
    }

    public void Reset(){
        render.frame ++;
        render.calls = 0;
        render.triangles = 0;
        render.points = 0;
        render.lines = 0;
    }
}
