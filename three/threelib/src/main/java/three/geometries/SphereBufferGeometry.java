package three.geometries;

import three.bufferAttribute.Float32BufferAttribute;
import three.core.BufferGeometry;
import three.math.Vector3;

public class SphereBufferGeometry extends BufferGeometry {
    public SphereBufferGeometry(float radius, int widthSegments, int heightSegments, float phiStart,
                                float phiLength, float thetaStart, float thetaLength){

        super();
        float thetaEnd = thetaStart + thetaLength;
        Vector3 vertex = new Vector3();
        Vector3 normal = new Vector3();

        int index = 0;
        int grid[][] = new int[heightSegments+1][widthSegments+1];

        int[] indices  = new int[widthSegments * heightSegments * 6 - heightSegments * 3];
        float[] vertices = new float[(widthSegments + 1)* (heightSegments + 1) * 3];
        float[] normals = new float[(widthSegments + 1)* (heightSegments + 1) * 3];
        float[] uvs = new float[(widthSegments + 1)* (heightSegments + 1) * 2];

        int indexCount = 0;
        int vertexCount = 0;
        int uvCount = 0;

        for ( int iy = 0; iy <= heightSegments; iy ++ ) {
            int[] verticesRow = new int[widthSegments+1];
            float v = (float) iy / heightSegments;

            for ( int ix = 0; ix <= widthSegments; ix ++ ) {
                float u = (float) ix / widthSegments;
                // vertex
                vertex.x = - radius * (float) Math.cos( phiStart + u * phiLength ) * (float) Math.sin( thetaStart + v * thetaLength );
                vertex.y = radius * (float) Math.cos( thetaStart + v * thetaLength );
                vertex.z = radius * (float) Math.sin( phiStart + u * phiLength ) * (float) Math.sin( thetaStart + v * thetaLength );
                vertices[vertexCount] = vertex.x;
                vertices[vertexCount+1] = vertex.y;
                vertices[vertexCount+2] = vertex.z;
                // normal
                normal.set( vertex.x, vertex.y, vertex.z ).normalize();
                normals[vertexCount] = normal.x;
                normals[vertexCount+1] = normal.y;
                normals[vertexCount+2] = normal.z;
                // uv
                uvs[uvCount] = u;
                uvs[uvCount+1] = 1-v;

                verticesRow[ix] = index++;
                vertexCount += 3;
                uvCount += 2;
            }
            grid[iy] = verticesRow;
        }

        // indices
        for ( int iy = 0; iy < heightSegments; iy ++ ) {
            for ( int ix = 0; ix < widthSegments; ix ++ ) {
                int a = grid[ iy ][ ix + 1 ];
                int b = grid[ iy ][ ix ];
                int c = grid[ iy + 1 ][ ix ];
                int d = grid[ iy + 1 ][ ix + 1 ];

                if ( iy != 0 || thetaStart > 0 ) {
                    indices[indexCount] = a;
                    indices[indexCount + 1] = b;
                    indices[indexCount + 2] = d;
                    indexCount += 3;
                }

                if ( iy != heightSegments - 1 || thetaEnd < Math.PI ){
                    indices[indexCount] = b;
                    indices[indexCount + 1] = c;
                    indices[indexCount + 2] = d;
                    indexCount += 3;
                }
            }
        }

        this.setIndex( indices );
        this.addAttribute( "position", new Float32BufferAttribute( vertices, 3 ) );
        this.addAttribute( "normal", new Float32BufferAttribute( normals, 3 ) );
        this.addAttribute( "uv", new Float32BufferAttribute( uvs, 2 ) );
    }
}
