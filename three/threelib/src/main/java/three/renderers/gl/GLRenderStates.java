package three.renderers.gl;

import java.util.HashMap;

import three.cameras.Camera;
import three.scenes.Scene;

public class GLRenderStates {
    public HashMap<Integer, HashMap<Integer, GLRenderState>> renderStates;

    public GLRenderStates(){
        renderStates = new HashMap<Integer, HashMap<Integer, GLRenderState>>();
    }

    public GLRenderState get(Scene scene, Camera camera){
        GLRenderState renderState;
        HashMap<Integer, GLRenderState> cameras = renderStates.get(scene.id);

        if ( cameras == null ) {
            renderState = new GLRenderState();
            renderStates.put(scene.id, new HashMap<Integer, GLRenderState>());
            renderStates.get(scene.id).put(camera.id, renderState);

        } else {
            renderState = cameras.get(camera.id);
            if ( renderState == null ) {
                renderState = new GLRenderState();
                cameras.put(camera.id, renderState);
            }
        }

        return renderState;
    }

    public void dispose(){
        this.renderStates.clear();
    }
}
