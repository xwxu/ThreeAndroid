package three.materials;

import three.materials.parameters.PointsParameters;
import three.math.Color;
import three.textures.Texture;

public class PointsMaterial extends Material {

    public Color color;
    public Texture map;
    public float size;
    public boolean sizeAttenuation;
    public boolean morphTargets;

    public PointsMaterial(PointsParameters parameters){
        super(parameters);
        type = "PointsMaterial";
        color = parameters.color;
        map = parameters.map;
        sizeAttenuation = parameters.sizeAttenuation;
        morphTargets = parameters.morphTargets;
    }

    public PointsMaterial Copy(PointsMaterial source){
        super.Copy(source);
        this.color.Copy( source.color );

        this.map = source.map;

        this.size = source.size;
        this.sizeAttenuation = source.sizeAttenuation;

        this.morphTargets = source.morphTargets;

        return this;
    }
}
