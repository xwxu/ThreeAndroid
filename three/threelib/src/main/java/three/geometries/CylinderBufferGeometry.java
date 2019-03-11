package three.geometries;

import three.core.BufferGeometry;

public class CylinderBufferGeometry extends BufferGeometry {

    public CylinderBufferGeometry(float radiusTop, float radiusBottom, float height, int radialSegments,
                                  int heightSegments, boolean openEnded, float thetaStart, float thetaLength){
        super();
        this.type = "CylinderBufferGeometry";

//        int[] indices = new int[segments * 3];
//        float[] vertices = new float[ segments * 3 + 3 ];
//        float[] normals = new float[ segments * 3 ];
//        float[] uvs = new float[ segments * 2 ];
    }
}
