package three.util;

import android.opengl.GLES20;

import three.math.Vector4;
import static three.constants.*;

public class DepthBuffer {
    public boolean locked;
    public boolean currentDepthMask;
    public int currentDepthFunc;
    public float currentDepthClear;

    public DepthBuffer(){
        this.locked = false;
        this.currentDepthMask = false;
        this.currentDepthClear = 0;
    }

    public void SetTest(boolean depthTest){
        if ( depthTest ) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        } else {
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        }
    }

    public void SetMask(boolean depthMask){
        if ( this.currentDepthMask != depthMask && ! locked ) {
            GLES20.glDepthMask(depthMask);
            this.currentDepthMask = depthMask;
        }
    }

    public void SetFunc(int depthFunc){
        if ( this.currentDepthFunc != depthFunc ) {
            if (depthFunc != 0) {
                switch (depthFunc) {
                    case NeverDepth:
                        GLES20.glDepthFunc(GLES20.GL_NEVER);
                        break;

                    case AlwaysDepth:
                        GLES20.glDepthFunc(GLES20.GL_ALWAYS);
                        break;

                    case LessDepth:
                        GLES20.glDepthFunc(GLES20.GL_LESS);
                        break;

                    case LessEqualDepth:
                        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
                        break;

                    case EqualDepth:
                        GLES20.glDepthFunc(GLES20.GL_EQUAL);
                        break;

                    case GreaterEqualDepth:
                        GLES20.glDepthFunc(GLES20.GL_GEQUAL);
                        break;

                    case GreaterDepth:
                        GLES20.glDepthFunc(GLES20.GL_GREATER);
                        break;

                    case NotEqualDepth:
                        GLES20.glDepthFunc(GLES20.GL_NOTEQUAL);
                        break;

                    default:
                        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
                }
            } else {
                GLES20.glDepthFunc(GLES20.GL_LEQUAL);
            }
            this.currentDepthFunc = depthFunc;
        }
    }

    public void SetLocked(boolean lock){
        this.locked = lock;
    }

    public void SetClear(float depth){
        if ( currentDepthClear != depth ) {
            GLES20.glClearDepthf( depth );
            currentDepthClear = depth;
        }
    }

    public void Reset(){
        this.locked = false;
        this.currentDepthMask = false;
        this.currentDepthFunc = 0;
        this.currentDepthClear = 0;
    }
}
