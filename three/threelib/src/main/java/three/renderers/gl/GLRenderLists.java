package three.renderers.gl;

import java.util.HashMap;

import three.cameras.Camera;
import three.scenes.Scene;

public class GLRenderLists {
    public HashMap<Integer, HashMap<Integer, GLRenderList>> lists;

    public GLRenderLists(){
        lists = new HashMap<>();
    }

    public GLRenderList Get(Scene scene, Camera camera){
        GLRenderList renderList;
        HashMap<Integer, GLRenderList> cameras = lists.get(scene.id);

        if ( cameras == null ) {
            renderList = new GLRenderList();
            lists.put(scene.id, new HashMap<Integer, GLRenderList>());
            lists.get(scene.id).put(camera.id, renderList);

        } else {
            renderList = cameras.get(camera.id);
            if ( renderList == null ) {
                renderList = new GLRenderList();
                cameras.put(camera.id, renderList);
            }
        }

        return renderList;
    }

    public void Dispose(){
        this.lists.clear();
    }
}
