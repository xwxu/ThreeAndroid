package three.renderers.gl;

import java.util.ArrayList;

import three.cameras.Camera;
import three.lights.Light;

public class GLRenderState {
    public GLLights lights;
    public ArrayList<Light> lightsArray;
    public ArrayList<Light> shadowsArray;

    public GLRenderState(){
        this.lights = new GLLights();
        this.lightsArray = new ArrayList<>();
        this.shadowsArray = new ArrayList();
    }

    public void init(){
        lightsArray.clear();
        shadowsArray.clear();
    }

    public void pushLight(Light light){
        lightsArray.add(light);
    }

    public void pushShadow(Light shadowLight){
        shadowsArray.add(shadowLight);
    }

    public void setupLights(Camera camera){
        lights.setup( lightsArray, shadowsArray, camera );
    }
}
