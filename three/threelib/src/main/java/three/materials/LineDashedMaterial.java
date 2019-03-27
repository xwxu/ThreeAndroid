package three.materials;

import three.materials.parameters.LineDashedParameters;

public class LineDashedMaterial extends LineMaterial{

    public float scale;
    public float dashSize;
    public float gapSize;

    public LineDashedMaterial(LineDashedParameters parameters){
        super(parameters);
        this.type = "LineDashedMaterial";
        scale = parameters.scale;
        dashSize = parameters.dashSize;
        gapSize = parameters.gapSize;
    }

    public LineDashedMaterial copy(LineDashedMaterial source){
        super.copy(source);
        this.scale = source.scale;
        this.dashSize = source.dashSize;
        this.gapSize = source.gapSize;

        return this;
    }
}
