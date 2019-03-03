package three.materials;

import three.materials.parameters.SpriteParameters;
import three.math.Color;
import three.textures.Texture;

public class SpriteMaterial extends Material{

    public Color color;
    public Texture map;
    public float rotation;
    public boolean sizeAttenuation;

    public SpriteMaterial(SpriteParameters parameters){
        super(parameters);
        type = "SpriteMaterial";
        color = parameters.color;
        map = parameters.map;
        rotation = parameters.rotation;
        sizeAttenuation = parameters.sizeAttenuation;
    }

    public SpriteMaterial Copy(SpriteMaterial source){
        super.Copy(source);
        this.color.Copy( source.color );
        this.map = source.map;

        this.rotation = source.rotation;

        this.sizeAttenuation = source.sizeAttenuation;

        return this;
    }
}
