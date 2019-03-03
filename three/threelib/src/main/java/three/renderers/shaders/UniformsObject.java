package three.renderers.shaders;

import java.util.HashMap;

import three.math.Color;
import three.textures.Texture;

public class UniformsObject {
    public HashMap<String, UniformState> uniforms = new HashMap<>();

    public Object Get(String name){
        UniformState u = uniforms.get(name);
        if(u != null){
            return  u.value;
        }
        return null;
    }

    public void Put(String name, Object object){
        UniformState u = new UniformState();
        u.value = object;
        if(uniforms.containsKey(name)){
            uniforms.remove(name);
        }
        uniforms.put(name, u);
    }
}
