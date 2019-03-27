package three.renderers.shaders;

import java.util.HashMap;

import three.math.Color;
import three.textures.Texture;

public class UniformsObject {
    public HashMap<String, UniformState> uniforms = new HashMap<>();

    public Object get(String name){
        if(uniforms.containsKey(name)){
            UniformState u = uniforms.get(name);
            return u.value;
        }else{
            return null;
        }
    }

    public void put(String name, Object object){
        UniformState u = new UniformState();
        u.value = object;
        if(uniforms.containsKey(name)){
            uniforms.remove(name);
        }
        uniforms.put(name, u);
    }
}
