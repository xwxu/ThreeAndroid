package three.materials;

import three.materials.parameters.LineBasicParameters;
import three.math.Color;

public class LineBasicMaterial extends LineMaterial{

    public Color color;
    public String linecap;
    public String linejoin;

    public LineBasicMaterial(LineBasicParameters parameters){
        super(parameters);
        this.type = "LineBasicMaterial";
        this.color = parameters.color;
        this.linecap = parameters.linecap;
        this.linejoin = parameters.linejoin;
    }

    public LineBasicMaterial copy(LineBasicMaterial source){
        super.copy(source);
        this.color.copy( source.color );
        this.linecap = source.linecap;
        this.linejoin = source.linejoin;

        return this;
    }

}
