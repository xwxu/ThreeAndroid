package three.geometries;

import three.core.Geometry;

public class PlaneGeometry extends Geometry {

    public PlaneGeometry(float width, float height, int widthSegments, int heightSegments){
        super();
        this.fromBufferGeometry( new PlaneBufferGeometry( width, height, widthSegments, heightSegments ) );
        this.mergeVertices();
    }
}
