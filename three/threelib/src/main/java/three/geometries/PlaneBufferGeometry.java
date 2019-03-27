package three.geometries;

import three.bufferAttribute.Float32BufferAttribute;
import three.core.BufferGeometry;

public class PlaneBufferGeometry extends BufferGeometry {

    public PlaneBufferGeometry(float width, float height, int widthSegments, int heightSegments){
        super();
        this.type = "PlaneBufferGeometry";

        float width_half = width / 2;
        float height_half = height / 2;

        int gridX = widthSegments;
        int gridY = heightSegments;

        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        float segment_width = width / gridX;
        float segment_height = height / gridY;

        int[] indices  = new int[gridX * gridY * 6];
        float[] vertices = new float[gridX1 * gridY1 * 3];
        float[] normals = new float[gridX1 * gridY1 * 3];
        float[] uvs = new float[gridX1 * gridY1 * 2];

        int indexCount = 0;
        int vertexCount = 0;
        int uvCount = 0;

        // generate vertices, normals and uvs
        for ( int iy = 0; iy < gridY1; iy ++ ) {
            float y = iy * segment_height - height_half;
            for ( int ix = 0; ix < gridX1; ix ++ ) {
                float x = ix * segment_width - width_half;

                vertices[vertexCount] = x;
                vertices[vertexCount + 1] = -y;
                vertices[vertexCount +2 ] = 0;

                normals[vertexCount] = 0;
                normals[vertexCount + 1] = 0;
                normals[vertexCount + 2] = 1;

                uvs[uvCount] = ix / gridX;
                uvs[uvCount+1] = 1 - ( iy / gridY );

                vertexCount += 3;
                uvCount += 2;
            }
        }

        // indices
        for ( int iy = 0; iy < gridY; iy ++ ) {
            for ( int ix = 0; ix < gridX; ix ++ ) {

                int a = ix + gridX1 * iy;
                int b = ix + gridX1 * ( iy + 1 );
                int c = ( ix + 1 ) + gridX1 * ( iy + 1 );
                int d = ( ix + 1 ) + gridX1 * iy;

                // faces
                indices[indexCount] = a;
                indices[indexCount + 1] = b;
                indices[indexCount + 2] = d;
                indices[indexCount + 3] = b;
                indices[indexCount + 4] = c;
                indices[indexCount + 5] = d;

                indexCount += 6;

            }
        }

        this.setIndex( indices );
        this.addAttribute( "position", new Float32BufferAttribute( vertices, 3 ) );
        this.addAttribute( "normal", new Float32BufferAttribute( normals, 3 ) );
        this.addAttribute( "uv", new Float32BufferAttribute( uvs, 2 ) );
    }
}
