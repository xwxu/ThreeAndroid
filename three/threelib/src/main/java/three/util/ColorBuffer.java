package three.util;

import android.opengl.GLES20;

import three.math.Vector4;

public class ColorBuffer {
    public boolean locked;
    public Vector4 color;
    public boolean currentColorMask;
    public Vector4 currentColorClear;

    public ColorBuffer(){
        this.locked = false;
        this.color = new Vector4();
        this.currentColorMask = false;
        this.currentColorClear = new Vector4();
    }

    public void SetMask(boolean colorMask){
        if ( this.currentColorMask != colorMask && ! locked ) {
            GLES20.glColorMask( colorMask, colorMask, colorMask, colorMask );
            this.currentColorMask = colorMask;
        }
    }

    public void SetLocked(boolean lock){
        this.locked = lock;
    }

    public void SetClear(float r, float g, float b, float a, boolean premultipliedAlpha){
        if (premultipliedAlpha) {
            r *= a; g *= a; b *= a;
        }

        this.color.set( r, g, b, a );

        if (!this.currentColorClear.equals(color)) {
            GLES20.glClearColor( r, g, b, a );
            this.currentColorClear.copy( color );
        }
    }

    public void Reset(){
        this.locked = false;
        this.currentColorMask = false;
        this.currentColorClear.set(-1, 0, 0, 0);
    }

}
