package three.geometries;

import three.core.Geometry;

public class PlaneGeometry extends Geometry {

    public PlaneGeometry(float width, float height, int widthSegments, int heightSegments){
        super();
        this.FromBufferGeometry( new PlaneBufferGeometry( width, height, widthSegments, heightSegments ) );
        this.MergeVertices();
    }
}
