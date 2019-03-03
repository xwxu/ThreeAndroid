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

    public void Init(){
        lightsArray.clear();
        shadowsArray.clear();
    }

    public void PushLight(Light light){
        lightsArray.add(light);
    }

    public void PushShadow(Light shadowLight){
        shadowsArray.add(shadowLight);
    }

    public void SetupLights(Camera camera){
        lights.Setup( lightsArray, shadowsArray, camera );
    }
}
