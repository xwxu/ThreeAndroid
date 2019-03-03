package three.objects;

import three.constants;
import three.core.AbstractGeometry;
import three.core.BufferGeometry;
import three.core.Object3D;
import three.materials.LineBasicMaterial;
import three.materials.Material;
import three.materials.parameters.LineBasicParameters;
import three.math.Color;

public class Line extends Object3D {
    public String type;
    public AbstractGeometry geometry;
    public Material material;

    public Line(){
        super();
        this.type = "Line";
        this.geometry = new BufferGeometry();
        LineBasicParameters parameters = new LineBasicParameters();
        parameters.color = new Color((int)Math.random() * 0xffffff);
        this.material = new LineBasicMaterial( parameters );
    }

    public Line(AbstractGeometry geometry, Material material){
        super();
        this.type = "Line";
        this.geometry = geometry;
        this.material = material != null ? material : new LineBasicMaterial( new LineBasicParameters() ); ;
    }

}
