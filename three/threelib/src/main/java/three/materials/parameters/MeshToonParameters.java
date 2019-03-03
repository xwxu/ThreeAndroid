package three.materials.parameters;

import java.util.HashMap;

import three.textures.Texture;

public class MeshToonParameters extends MeshPhongParameters{

    public HashMap<String, String> defines = new HashMap<>();
    public Texture gradientMap = null;

    public MeshToonParameters(){
        super();
        defines.put("TOON", "");
    }
}
