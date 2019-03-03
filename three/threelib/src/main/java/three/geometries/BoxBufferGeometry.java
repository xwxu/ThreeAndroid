package three.geometries;

import java.util.ArrayList;

import three.bufferAttribute.Float32BufferAttribute;
import three.core.BufferGeometry;
import three.math.Vector3;

public class BoxBufferGeometry extends BufferGeometry {

    ArrayList<Integer> indices = new ArrayList<>();
    ArrayList<Float> vertices = new ArrayList<>();
    ArrayList<Float> normals = new ArrayList<>();
    ArrayList<Float> uvs  = new ArrayList<>();

    int groupStart = 0;
    int numberOfVertices = 0;

    public BoxBufferGeometry(float width, float height, float depth,
                             int widthSegments, int heightSegments, int depthSegments){

        super();
        BuildPlane( 2, 1, 0, - 1, - 1, depth, height, width, depthSegments, heightSegments, 0 ); // px
        BuildPlane( 2, 1, 0, 1, - 1, depth, height, - width, depthSegments, heightSegments, 1 ); // nx
        BuildPlane( 0, 2, 1, 1, 1, width, depth, height, widthSegments, depthSegments, 2 ); // py
        BuildPlane( 0, 2, 1, 1, - 1, width, depth, - height, widthSegments, depthSegments, 3 ); // ny
        BuildPlane( 0, 1, 2, 1, - 1, width, height, depth, widthSegments, heightSegments, 4 ); // pz
        BuildPlane( 0, 1, 2, - 1, - 1, width, height, - depth, widthSegments, heightSegments, 5 ); // nz

        int[] indexArray = new int[indices.size()];
        for(int i = 0; i < indices.size(); ++i){
            indexArray[i] = indices.get(i);
        }
        this.SetIndex( indexArray );

        float[] vertexArray = new float[vertices.size()];
        for(int i = 0; i < vertices.size(); ++i){
            vertexArray[i] = vertices.get(i);
        }
        this.AddAttribute( "position", new Float32BufferAttribute( vertexArray, 3 ) );

        float[] normalArray = new float[normals.size()];
        for(int i = 0; i < normals.size(); ++i){
            normalArray[i] = normals.get(i);
        }
        this.AddAttribute( "normal", new Float32BufferAttribute( normalArray, 3 ) );

        float[] uvArray = new float[uvs.size()];
        for(int i = 0; i < uvs.size(); ++i){
            uvArray[i] = uvs.get(i);
        }
        this.AddAttribute( "uv", new Float32BufferAttribute( uvArray, 2 ) );
    }


    void BuildPlane(int u, int v, int w, int udir, int vdir, float width, float height, float depth,
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
                vertices.add( vector.x );
                vertices.add( vector.y );
                vertices.add( vector.z );

                // set values to correct vector component
                vector.SetComponent(u, 0);
                vector.SetComponent(v, 0);
                vector.SetComponent(w, depth > 0 ? 1 : - 1);

                // now apply vector to normal buffer
                normals.add( vector.x );
                normals.add( vector.y );
                normals.add( vector.z );

                // uvs
                uvs.add( (float)ix / gridX );
                uvs.add( 1 - (float)( iy / gridY ) );

                // counters
                vertexCounter += 1;
            }
        }

        for ( int iy = 0; iy < gridY; iy ++ ) {
            for ( int ix = 0; ix < gridX; ix ++ ) {

                int a = numberOfVertices + ix + gridX1 * iy;
                int b = numberOfVertices + ix + gridX1 * ( iy + 1 );
                int c = numberOfVertices + ( ix + 1 ) + gridX1 * ( iy + 1 );
                int d = numberOfVertices + ( ix + 1 ) + gridX1 * iy;

                // faces
                indices.add( a );
                indices.add( b );
                indices.add( d );
                indices.add( b );
                indices.add( c );
                indices.add( d );

                // increase counter
                groupCount += 6;
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
