package three.scenes;

import three.core.Object3D;
import three.materials.Material;
import three.textures.Texture;

public class Scene extends Object3D {

    public Material overrideMaterial = null;
    public Fog fog = null;
    public boolean autoUpdate = true;
    public Texture background = null;

    public Scene(){
        super();
        this.type = "Scene";
    }
}
