package three.renderers.gl;


import three.cameras.Camera;
import three.math.Color;
import three.renderers.GLRenderer;
import three.scenes.Scene;
import three.textures.Texture;

public class GLBackground {
    public Color clearColor = new Color(0.0f,0.0f,0);
    public int clearAlpha = 0;

    Texture currentBackground = null;
    int currentBackgroundVersion = 0;

    GLRenderer renderer;
    GLState state;
    GLObjects objects;
    boolean premultipliedAlpha;

    public GLBackground(GLRenderer renderer, GLState state, GLObjects objects, boolean _premultipliedAlpha){
        this.renderer = renderer;
        this.state = state;
        this.objects = objects;
        this.premultipliedAlpha = _premultipliedAlpha;
    }

    public Color getClearColor(){
        return this.clearColor;
    }

    private void setClear(Color color, int alpha){
        state.colorBuffer.SetClear( color.r, color.g, color.b, alpha, premultipliedAlpha );
    }

    public void setClearColor(int color, int alpha){
        this.clearColor.set(color);
        this.clearAlpha = alpha;
    }

    public void render(GLRenderList renderList, Scene scene, Camera camera, boolean forceClear){
        Object background = scene.background;

        if ( background == null ) {
            setClear( clearColor, clearAlpha );
            currentBackground = null;
            currentBackgroundVersion = 0;

        } else if ( background!= null && background instanceof Color ) {
            //setClear( background, 1 );
            forceClear = true;
            currentBackground = null;
            currentBackgroundVersion = 0;
        }

        if ( renderer.autoClear || forceClear ) {
            renderer.clear( renderer.autoClearColor, renderer.autoClearDepth, renderer.autoClearStencil );
        }
    }
}
