package three.renderers.gl;

import java.util.HashMap;

import three.materials.Material;
import three.textures.Texture;
import three.util.MaterialProperties;
import three.util.TextureProperties;

public class GLProperties {

    private HashMap<Material, MaterialProperties> materialPropertiesMap;
    private HashMap<Texture, TextureProperties> texturePropertiesMap;

    public GLProperties(){
        materialPropertiesMap = new HashMap<>();
        texturePropertiesMap = new HashMap<>();
    }

    public MaterialProperties GetMaterial(Material material){
        MaterialProperties map = materialPropertiesMap.get(material);
        if(map == null){
            map = new MaterialProperties();
            materialPropertiesMap.put(material, map);
        }

        return map;
    }

    public TextureProperties GetTexture(Texture texture){
        TextureProperties map = texturePropertiesMap.get(texture);
        if(map == null){
            map = new TextureProperties();
            texturePropertiesMap.put(texture, map);
        }

        return map;
    }
}
