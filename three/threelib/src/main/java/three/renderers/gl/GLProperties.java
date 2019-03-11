package three.renderers.gl;

import java.util.HashMap;

import three.materials.Material;
import three.renderers.GLRenderTarget;
import three.textures.Texture;
import three.util.MaterialProperties;
import three.util.RenderTargetProperties;
import three.util.TextureProperties;

public class GLProperties {

    private HashMap<Material, MaterialProperties> materialPropertiesMap;
    private HashMap<Texture, TextureProperties> texturePropertiesMap;
    private HashMap<GLRenderTarget, RenderTargetProperties> renderTargetPropertiesHashMap;

    public GLProperties(){
        materialPropertiesMap = new HashMap<>();
        texturePropertiesMap = new HashMap<>();
        renderTargetPropertiesHashMap = new HashMap<>();
    }

    public MaterialProperties GetMaterial(Material material){
        MaterialProperties property = materialPropertiesMap.get(material);
        if(property == null){
            property = new MaterialProperties();
            materialPropertiesMap.put(material, property);
        }

        return property;
    }

    public TextureProperties GetTexture(Texture texture){
        TextureProperties property = texturePropertiesMap.get(texture);
        if(property == null){
            property = new TextureProperties();
            texturePropertiesMap.put(texture, property);
        }

        return property;
    }

    public RenderTargetProperties GetRenderTarget(GLRenderTarget renderTarget){
        RenderTargetProperties property = renderTargetPropertiesHashMap.get(renderTarget);
        if(property == null){
            property = new RenderTargetProperties();
            renderTargetPropertiesHashMap.put(renderTarget, property);
        }

        return property;
    }
}
