package three.util;

import android.opengl.GLES20;

public class StencilBuffer {
    public boolean locked;
    public int currentStencilMask;
    public int currentStencilFunc;
    public int currentStencilRef;
    public int currentStencilFuncMask;
    public int currentStencilFail;
    public int currentStencilZFail;
    public int currentStencilZPass;
    public int currentStencilClear;

    public StencilBuffer(){
        this.locked = false;
    }

    public void SetTest(boolean stencilTest){
        if ( stencilTest ) {
            GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        } else {
            GLES20.glDisable(GLES20.GL_STENCIL_TEST);
        }
    }

    public void SetMask(int stencilMask){
        if ( this.currentStencilMask != stencilMask && ! locked ) {
            GLES20.glStencilMask(stencilMask);
            this.currentStencilMask = stencilMask;
        }
    }

    public void SetFunc(int stencilFunc, int stencilRef, int stencilMask){
        if ( currentStencilFunc != stencilFunc ||
                currentStencilRef != stencilRef 	||
                currentStencilFuncMask != stencilMask ) {

            GLES20.glStencilFunc(stencilFunc, stencilRef, stencilMask);

            currentStencilFunc = stencilFunc;
            currentStencilRef = stencilRef;
            currentStencilFuncMask = stencilMask;
        }
    }

    public void SetOp(int stencilFail, int stencilZFail, int stencilZPass){
        if ( currentStencilFail	 != stencilFail 	||
                currentStencilZFail != stencilZFail ||
                currentStencilZPass != stencilZPass ) {

            GLES20.glStencilOp( stencilFail, stencilZFail, stencilZPass );

            currentStencilFail = stencilFail;
            currentStencilZFail = stencilZFail;
            currentStencilZPass = stencilZPass;
        }
    }


    public void SetLocked(boolean lock){
        this.locked = lock;
    }

    public void SetClear(int stencil){
        if ( currentStencilClear != stencil ) {
            GLES20.glClearStencil( stencil );
            currentStencilClear = stencil;
        }
    }

    public void Reset(){
        this.locked = false;
        currentStencilMask = 0;
        currentStencilFunc = 0;
        currentStencilRef = 0;
        currentStencilFuncMask = 0;
        currentStencilFail = 0;
        currentStencilZFail = 0;
        currentStencilZPass = 0;
        currentStencilClear = 0;
    }
}
