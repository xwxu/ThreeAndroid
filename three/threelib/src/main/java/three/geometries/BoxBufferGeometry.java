package three.geometries;

import three.bufferAttribute.Float32BufferAttribute;
import three.core.BufferGeometry;
import three.math.Vector3;

public class BoxBufferGeometry extends BufferGeometry {

    private int[] indices;
    private float[] vertices;
    private float[] normals;
    private float[] uvs;

    private int groupStart = 0;
    private int numberOfVertices = 0;
    private int vertexIndex = 0;
    private int uvIndex = 0;
    private int indexCounter = 0;

    public BoxBufferGeometry(float width, float height, float depth,
                             int widthSegments, int heightSegments, int depthSegments){

        super();

        int indexCount = (widthSegments * heightSegments + widthSegments * depthSegments + heightSegments * depthSegments) * 2 * 6;
        this.indices = new int[ indexCount ];

        int vertexCount = (widthSegments + 1) * (heightSegments + 1) * 6 +
                (widthSegments + 1) * (depthSegments + 1) * 6 +
                (heightSegments + 1) * (depthSegments + 1) * 6;
        this.vertices = new float[vertexCount];
        this.normals = new float[vertexCount];

        int uvCount = vertexCount / 3 * 2;
        this.uvs = new float[uvCount];


        BuildPlane( 2, 1, 0, - 1, - 1, depth, height, width, depthSegments, heightSegments, 0 ); // px
        BuildPlane( 2, 1, 0, 1, - 1, depth, height, - width, depthSegments, heightSegments, 1 ); // nx
        BuildPlane( 0, 2, 1, 1, 1, width, depth, height, widthSegments, depthSegments, 2 ); // py
        BuildPlane( 0, 2, 1, 1, - 1, width, depth, - height, widthSegments, depthSegments, 3 ); // ny
        BuildPlane( 0, 1, 2, 1, - 1, width, height, depth, widthSegments, heightSegments, 4 ); // pz
        BuildPlane( 0, 1, 2, - 1, - 1, width, height, - depth, widthSegments, heightSegments, 5 ); // nz


        this.SetIndex( indices );
        this.AddAttribute( "position", new Float32BufferAttribute( this.vertices, 3 ) );
        this.AddAttribute( "normal", new Float32BufferAttribute( this.normals, 3 ) );
        this.AddAttribute( "uv", new Float32BufferAttribute( this.uvs, 2 ) );
    }


    private void BuildPlane(int u, int v, int w, int udir, int vdir, float width, float height, float depth,
                            int gridX, int gridY, int materialIndex){

        float segmentWidth = width / gridX;
        float segmentHeight = height / gridY;

        float widthHalf = width / 2;
        float heightHalf = height / 2;
        float depthHalf = depth / 2;

        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        int vertexCounter = 0;
        int groupCount = 0;

        Vector3 vector = new Vector3();

        // generate vertices, normals and uvs
        for ( int iy = 0; iy < gridY1; iy ++ ) {
            float y = iy * segmentHeight - heightHalf;

            for ( int ix = 0; ix < gridX1; ix ++ ) {
                float x = ix * segmentWidth - widthHalf;

                // set values to correct vector component
                vector.SetComponent(u, x * udir);
                vector.SetComponent(v,y * vdir );
                vector.SetComponent(w, depthHalf);

                // now apply vector to vertex buffer
                vertices[vertexIndex] = vector.x;
                vertices[vertexIndex+1] = vector.y;
                vertices[vertexIndex+2] = vector.z;

                // set values to correct vector component
                vector.SetComponent(u, 0);
                vector.SetComponent(v, 0);
                vector.SetComponent(w, depth > 0 ? 1 : - 1);

                // now apply vector to normal buffer
                normals[vertexIndex] = vector.x;
                normals[vertexIndex+1] = vector.y;
                normals[vertexIndex+2] = vector.z;

                // uvs
                uvs[uvIndex] =(float)ix / gridX;
                uvs[uvIndex + 1] = 1 - (float)( iy / gridY );

                // counters
                vertexCounter += 1;
                vertexIndex += 3;
                uvIndex += 2;

            }
        }

        for ( int iy = 0; iy < gridY; iy ++ ) {
            for ( int ix = 0; ix < gridX; ix ++ ) {

                int a = numberOfVertices + ix + gridX1 * iy;
                int b = numberOfVertices + ix + gridX1 * ( iy + 1 );
                int c = numberOfVertices + ( ix + 1 ) + gridX1 * ( iy + 1 );
                int d = numberOfVertices + ( ix + 1 ) + gridX1 * iy;

                // faces
                indices[indexCounter] = a;
                indices[indexCounter + 1] = b;
                indices[indexCounter + 2] = d;
                indices[indexCounter + 3] = b;
                indices[indexCounter + 4] = c;
                indices[indexCounter + 5] = d;

                // increase counter
                groupCount += 6;
                indexCounter += 6;
            }
        }

        // add a group to the geometry. this will ensure multi material support
        this.AddGroup( groupStart, groupCount, materialIndex );

        // calculate new start value for groups
        groupStart += groupCount;

        // update total number of vertices
        numberOfVertices += vertexCounter;
    }
}
