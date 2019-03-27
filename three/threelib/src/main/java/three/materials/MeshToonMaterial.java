package three.materials;

import java.util.HashMap;

import three.materials.parameters.MeshToonParameters;
import three.textures.Texture;

public class MeshToonMaterial extends MeshPhongMaterial{

    public HashMap<String, String> defines;
    public Texture gradientMap;

    public MeshToonMaterial(MeshToonParameters parameters){
        super(parameters);
        type = "MeshToonMaterial";
        defines = parameters.defines;
        gradientMap = parameters.gradientMap;
    }

    public MeshToonMaterial copy(MeshToonMaterial source){
        super.copy(source);
        this.gradientMap = source.gradientMap;
        return this;
    }
}
