package three.geometries;

import three.bufferAttribute.Float32BufferAttribute;
import three.core.BufferGeometry;
import three.math.Vector2;
import three.math.Vector3;

public class CircleBufferGeometry extends BufferGeometry {

    public CircleBufferGeometry(float radius, int segments, float thetaStart, float thetaLength){
        super();

        int[] indices = new int[segments * 3];
        float[] vertices = new float[ segments * 3 + 6 ];
        float[] normals = new float[ segments * 3 + 6];
        float[] uvs = new float[ segments * 2 + 4];

        Vector3 vertex = new Vector3();
        Vector2 uv = new Vector2();

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        normals[0] = 0;
        normals[1] = 0;
        normals[2] = 1;
        uvs[0] = 0.5f;
        uvs[1] = 0.5f;

        int indexCount = 0;
        int vertexCount = 3;
        int uvCount = 2;

        for ( int s = 0, i = 3; s <= segments; s ++, i += 3 ) {
            float segment = thetaStart + (float) s / segments * thetaLength;

            // vertex
            vertex.x = radius * (float) Math.cos( segment );
            vertex.y = radius * (float) Math.sin( segment );

            vertices[vertexCount] = vertex.x;
            vertices[vertexCount+1] = vertex.y;
            vertices[vertexCount+2] = vertex.z;

            // normal
            normals[vertexCount] = 0;
            normals[vertexCount+1] = 0;
            normals[vertexCount+2] = 1;

            // uvs
            uv.x = ( vertices[ i ] / radius + 1 ) / 2;
            uv.y = ( vertices[ i + 1 ] / radius + 1 ) / 2;
            uvs[uvCount] = uv.x;
            uvs[uvCount+1] = uv.y;

            vertexCount += 3;
            uvCount += 2;
        }

        // indices
        for ( int i = 1; i <= segments; i ++ ) {
            indices[indexCount] = i;
            indices[indexCount+1] = i+1;
            indices[indexCount+2] = 0;
            indexCount += 3;
        }

        this.setIndex( indices );
        this.addAttribute( "position", new Float32BufferAttribute( vertices, 3 ) );
        this.addAttribute( "normal", new Float32BufferAttribute( normals, 3 ) );
        this.addAttribute( "uv", new Float32BufferAttribute( uvs, 2 ) );
    }
}
