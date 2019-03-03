package three.objects;

import three.core.AbstractGeometry;
import three.core.Object3D;
import three.materials.Material;

public class Points extends Object3D {
    public String type;
    public AbstractGeometry geometry;
    public Material material;

    public Points(AbstractGeometry geometry, Material material){
        super();
        this.type = "Points";
        this.geometry = geometry;
        this.material = material;
    }

}
