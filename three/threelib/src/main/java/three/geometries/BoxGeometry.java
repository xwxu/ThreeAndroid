package three.geometries;

import three.core.Geometry;

public class BoxGeometry extends Geometry {

    public BoxGeometry(float width, float height, float depth,
        int widthSegments, int heightSegments, int depthSegments){

        super();
        this.type = "BoxGeometry";
        this.FromBufferGeometry(
                new BoxBufferGeometry(width, height, depth, widthSegments, heightSegments, depthSegments));
        this.MergeVertices();
    }
}
