package three.materials;

import three.materials.parameters.LineParameters;

public class LineMaterial extends Material{

    public float linewidth;

    public LineMaterial(LineParameters lineParameters){
        super(lineParameters);
        this.linewidth = lineParameters.linewidth;
    }
}
