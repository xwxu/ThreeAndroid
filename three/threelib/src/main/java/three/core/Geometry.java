package three.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Observable;

import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;
import three.math.Box3;
import three.math.Matrix4;
import three.math.Matrix3;
import three.math.Sphere;
import three.math.Vector2;
import three.math.Vector3;
import three.math.Color;
import three.util.GeoMatGroup;

public class Geometry extends AbstractGeometry{

    private static int geometryId = 0;

    public ArrayList<Vector3> vertices;
    public ArrayList<Color> colors;
    public ArrayList<Face3> faces;
    public ArrayList<ArrayList<ArrayList<Vector2>>> faceVertexUvs;
    public ArrayList lineDistances;

    public Box3 boundingBox;
    public Sphere boundingSphere;

    public BufferGeometry _bufferGeometry;
    public DirectGeometry _directGeometry;

    public boolean elementsNeedUpdate;
    public boolean verticesNeedUpdate;
    public boolean uvsNeedUpdate;
    public boolean normalsNeedUpdate;
    public boolean colorsNeedUpdate;
    public boolean lineDistancesNeedUpdate;
    public boolean groupsNeedUpdate;


    public Geometry(){
        id = geometryId +=2;
        this.type = "Geometry";

        this.vertices = new ArrayList<Vector3>();
        this.colors = new ArrayList<Color>();
        this.faces = new ArrayList<Face3>();
        this.faceVertexUvs = new ArrayList<ArrayList<ArrayList<Vector2>>>();
        this.lineDistances = new ArrayList();

        this.elementsNeedUpdate = false;
        this.verticesNeedUpdate = false;
        this.uvsNeedUpdate = false;
        this.normalsNeedUpdate = false;
        this.colorsNeedUpdate = false;
        this.lineDistancesNeedUpdate = false;
        this.groupsNeedUpdate = false;

    }


    public Geometry applyMatrix(Matrix4 matrix){
        Matrix3 normalMatrix = new Matrix3().getNormalMatrix( matrix );

        for ( int i = 0, il = this.vertices.size(); i < il; i ++ ) {
            Vector3 vertex = this.vertices.get(i);
            vertex.applyMatrix4( matrix );
        }

        for ( int i = 0, il = this.faces.size(); i < il; i ++ ) {
            Face3 face = this.faces.get(i);
            face.normal.applyMatrix3( normalMatrix ).normalize();

            for ( int j = 0, jl = face.vertexNormals.size(); j < jl; j ++ ) {
                face.vertexNormals.get(j).applyMatrix3( normalMatrix ).normalize();
            }

        }

        if ( this.boundingBox != null ) {
            this.computeBoundingBox();
        }

        if ( this.boundingSphere != null ) {
            this.computeBoundingSphere();
        }

        this.verticesNeedUpdate = true;
        this.normalsNeedUpdate = true;
        return this;
    }

    public void rotateX(float angle){
        Matrix4 m1 = new Matrix4();
        m1.makeRotationX( angle );
        this.applyMatrix( m1 );
    }

    public void rotateY(float angle){
        Matrix4 m1 = new Matrix4();
        m1.makeRotationY( angle );
        this.applyMatrix( m1 );
    }

    public void rotateZ(float angle){
        Matrix4 m1 = new Matrix4();
        m1.makeRotationZ( angle );
        this.applyMatrix( m1 );
    }

    public void translate(float x, float y, float z){
        Matrix4 m1 = new Matrix4();
        m1.makeTranslation( x, y, z );
        this.applyMatrix( m1 );
    }

    public void scale(float x, float y, float z){
        Matrix4 m1 = new Matrix4();
        m1.makeScale( x, y, z );
        this.applyMatrix( m1 );
    }

    public void lookAt(Vector3 vector){
        Object3D obj = new Object3D();
        obj.lookAt( vector );
        obj.updateMatrix();
        this.applyMatrix( obj.matrix );
    }

    private void addFace(FloatBuffer colors, FloatBuffer normals, FloatBuffer uvs, FloatBuffer uvs2,
                         int a, int b, int c, int materialIndex){
        ArrayList<Color> vertexColors = new ArrayList<Color>();
        if(colors != null){
            vertexColors.add(this.colors.get(a).clone_());
            vertexColors.add(this.colors.get(b).clone_());
            vertexColors.add(this.colors.get(c).clone_());
        }

        ArrayList<Vector3> vertexNormals = new ArrayList<Vector3>();
        if(normals != null){
            vertexNormals.add(new Vector3().fromArray( normals.array(), a * 3 ));
            vertexNormals.add(new Vector3().fromArray( normals.array(), b * 3 ));
            vertexNormals.add(new Vector3().fromArray( normals.array(), c * 3 ));
        }

        Face3 face = new Face3( a, b, c, vertexNormals, vertexColors, materialIndex );
        this.faces.add( face );

        if ( uvs != null ) {
            ArrayList<Vector2> uv = new ArrayList<Vector2>();
            uv.add(new Vector2().fromArray( uvs.array(), a * 2 ));
            uv.add(new Vector2().fromArray( uvs.array(), b * 2 ));
            uv.add(new Vector2().fromArray( uvs.array(), c * 2 ));
            this.faceVertexUvs.get(0).add( uv );
        }

        if ( uvs2 != null ) {
            ArrayList<Vector2> uv = new ArrayList<Vector2>();
            uv.add(new Vector2().fromArray( uvs2.array(), a * 2 ));
            uv.add(new Vector2().fromArray( uvs2.array(), b * 2 ));
            uv.add(new Vector2().fromArray( uvs2.array(), c * 2 ));
            this.faceVertexUvs.get(1).add( uv );
        }
    }

    public Geometry fromBufferGeometry(BufferGeometry geometry){
        IntBuffer indices = geometry.index.array;
        HashMap<String, BufferAttribute> attributes = geometry.attributes;

        Float32BufferAttribute position = (Float32BufferAttribute)attributes.get("position");
        FloatBuffer positions = position != null ? position.array : null;
        Float32BufferAttribute normal = (Float32BufferAttribute)attributes.get("normal");
        FloatBuffer normals = normal != null ? normal.array : null;
        Float32BufferAttribute color = (Float32BufferAttribute)attributes.get("color");
        FloatBuffer colors = color != null ? color.array : null;
        Float32BufferAttribute uv = (Float32BufferAttribute)attributes.get("uv");
        FloatBuffer uvs = uv != null ? uv.array : null;
        Float32BufferAttribute uv2 = (Float32BufferAttribute)attributes.get("uv2");
        FloatBuffer uvs2 = uv2 != null ? uv2.array : null;

        if(uvs2 != null){
            this.faceVertexUvs.set(1, new ArrayList<ArrayList<Vector2>>());
        }

        for ( int i = 0, j = 0; i < positions.capacity(); i += 3, j += 2 ) {
            this.vertices.add( new Vector3().fromArray( positions.array(), i ) );
            if ( colors != null ) {
                this.colors.add( new Color().fromArray( colors.array(), i ) );
            }
        }

        ArrayList<GeoMatGroup> groups = geometry.groups;

        if ( groups.size() > 0 ) {
            for ( int i = 0; i < groups.size(); i ++ ) {
                GeoMatGroup group = groups.get(i);
                int start = group.start;
                int count = group.count;

                for ( int j = start, jl = start + count; j < jl; j += 3 ) {
                    if ( indices != null ) {
                        addFace( colors, normals, uvs, uvs2,
                                indices.get(j), indices.get(j+1), indices.get(j+2), group.materialIndex );
                    } else {
                        addFace( colors, normals, uvs, uvs2, j,j + 1, j + 2, group.materialIndex );
                    }
                }
            }

        } else {
            if ( indices != null ) {
                for ( int i = 0; i < indices.capacity(); i += 3 ) {
                    addFace( colors, normals, uvs, uvs2,
                            indices.get(i), indices.get(i+1), indices.get(i+2), 0 );
                }
            } else {
                for ( int i = 0; i < positions.capacity() / 3; i += 3 ) {
                    addFace( colors, normals, uvs, uvs2, i, i+1, i+2, 0 );
                }
            }

        }

        this.computeFaceNormals();

        if ( geometry.boundingBox != null ) {
            this.boundingBox = geometry.boundingBox.clone_();
        }

        if ( geometry.boundingSphere != null ) {
            this.boundingSphere = geometry.boundingSphere.clone_();
        }

        return this;
    }

    public void center(){
        Vector3 offset = new Vector3();
        this.computeBoundingBox();
        this.boundingBox.getCenter( offset ).negate();
        this.translate( offset.x, offset.y, offset.z );
    }

    public Geometry normalize(){
        this.computeBoundingSphere();

        Vector3 center = this.boundingSphere.center;
        float radius = this.boundingSphere.radius;

        float s = radius == 0 ? 1 : 1.0f / radius;

        Matrix4 matrix = new Matrix4();
        matrix.set(
                s, 0, 0, - s * center.x,
                0, s, 0, - s * center.y,
                0, 0, s, - s * center.z,
                0, 0, 0, 1
        );

        this.applyMatrix( matrix );

        return this;
    }

    public void computeFaceNormals(){
        Vector3 cb = new Vector3(), ab = new Vector3();

        for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {
            Face3 face = this.faces.get(f);

            Vector3 vA = this.vertices.get(face.a);
            Vector3 vB = this.vertices.get(face.b);
            Vector3 vC = this.vertices.get(face.c);

            cb.subVectors( vC, vB );
            ab.subVectors( vA, vB );
            cb.cross( ab );

            cb.normalize();

            face.normal.copy( cb );
        }
    }

    public void computeVertexNormals(boolean areaWeighted){
        ArrayList<Vector3> vertices = new ArrayList<>();

        for ( int v = 0, vl = this.vertices.size(); v < vl; v ++ ) {
            vertices.set(v, new Vector3());
        }

        if ( areaWeighted ) {
            Vector3 cb = new Vector3(), ab = new Vector3();

            for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {
                Face3 face = this.faces.get(f);

                Vector3 vA = this.vertices.get(face.a);
                Vector3 vB = this.vertices.get(face.b);
                Vector3 vC = this.vertices.get(face.c);

                cb.subVectors( vC, vB );
                ab.subVectors( vA, vB );
                cb.cross( ab );

                vertices.get(face.a).add( cb );
                vertices.get(face.b).add( cb );
                vertices.get(face.c).add( cb );
            }

        } else {
            this.computeFaceNormals();

            for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {
                Face3 face = this.faces.get(f);
                vertices.get(face.a).add( face.normal );
                vertices.get(face.b).add( face.normal );
                vertices.get(face.c).add( face.normal );
            }
        }

        for ( int v = 0, vl = this.vertices.size(); v < vl; v ++ ) {
            vertices.get(v).normalize();
        }

        for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {
            Face3 face = this.faces.get(f);

            if ( face.vertexNormals.size() == 3 ) {

                face.vertexNormals.get(0).copy( vertices.get(face.a) );
                face.vertexNormals.get(1).copy( vertices.get(face.b) );
                face.vertexNormals.get(2).copy( vertices.get(face.c) );

            } else {

                face.vertexNormals.set(0, vertices.get(face.a).Clone());
                face.vertexNormals.set(1, vertices.get(face.b).Clone());
                face.vertexNormals.set(2, vertices.get(face.c).Clone());

            }

        }

        if ( this.faces.size() > 0 ) {
            this.normalsNeedUpdate = true;
        }
    }

    public void computeFlatVertexNormals(){
        this.computeFaceNormals();

        for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

            Face3 face = this.faces.get(f);
            ArrayList<Vector3> vertexNormals = face.vertexNormals;

            if ( vertexNormals.size() == 3 ) {

                vertexNormals.get(0).copy( face.normal );
                vertexNormals.get(1).copy( face.normal );
                vertexNormals.get(2).copy( face.normal );

            } else {

                vertexNormals.set(0, face.normal.Clone());
                vertexNormals.set(1, face.normal.Clone());
                vertexNormals.set(2, face.normal.Clone());

            }

        }

        if ( this.faces.size() > 0 ) {
            this.normalsNeedUpdate = true;
        }
    }

    public void computeBoundingBox(){
        if(this.boundingBox == null){
            this.boundingBox = new Box3();
        }
        this.boundingBox.setFromPoints(this.vertices);
    }

    public void computeBoundingSphere(){
        if(this.boundingSphere == null){
            this.boundingSphere = new Sphere();
        }
        this.boundingSphere.setFromPoints(this.vertices);
    }

    public Geometry setFromPoints(ArrayList<Vector3> points){
        this.vertices = new ArrayList<Vector3>();

        for ( int i = 0, l = points.size(); i < l; i ++ ) {
            Vector3 point = points.get(i);
            this.vertices.add( new Vector3( point.x, point.y, point.z ) );
        }
        return this;
    }

    public Geometry clone_(){
        return new Geometry().copy(this);
    }

    public Geometry copy(Geometry source){
        // reset
        this.vertices.clear();
        this.colors.clear();
        this.faces.clear();
        this.faceVertexUvs.clear();
        this.lineDistances.clear();
        this.boundingBox = null;
        this.boundingSphere = null;

        // name
        this.name = source.name;

        // vertices
        ArrayList<Vector3> vertices = source.vertices;
        for ( int i = 0, il = vertices.size(); i < il; i ++ ) {
            this.vertices.add( vertices.get(i).Clone() );
        }

        // colors
        ArrayList<Color> colors = source.colors;
        for ( int i = 0, il = colors.size(); i < il; i ++ ) {
            this.colors.add( colors.get(i).clone_() );
        }

        // faces
        ArrayList<Face3> faces = source.faces;
        for ( int i = 0, il = faces.size(); i < il; i ++ ) {
            this.faces.add( faces.get(i).clone_() );
        }

        // face vertex uvs
        for ( int i = 0, il = source.faceVertexUvs.size(); i < il; i ++ ) {
            ArrayList<ArrayList<Vector2>> faceVertexUvs = source.faceVertexUvs.get(i);
            if ( this.faceVertexUvs.get(i) == null ) {
                this.faceVertexUvs.set(i, new ArrayList());
            }

            for ( int j = 0, jl = faceVertexUvs.size(); j < jl; j ++ ) {
                ArrayList<Vector2> uvs = faceVertexUvs.get(j);
                ArrayList<Vector2> uvsCopy = new ArrayList<Vector2>();
                for ( int k = 0, kl = uvs.size(); k < kl; k ++ ) {
                    Vector2 uv = uvs.get(k);
                    uvsCopy.add( uv.clone_() );
                }
                this.faceVertexUvs.get(i).add( uvsCopy );
            }
        }
        return this;
    }

    public void dispose(){
        this.subject.onNext(this);
    }

    protected int mergeVertices() {
        // Hashmap for looking up vertices by position coordinates (and making sure they are unique)
        HashMap<Float, Integer> verticesMap = new HashMap();

        ArrayList<Vector3> unique = new ArrayList();
        ArrayList<Integer> changes = new ArrayList();

        int precisionPoints = 4; // number of decimal points, e.g. 4 for epsilon of 0.0001
        float precision = (float) Math.pow( 10, precisionPoints );
        ArrayList indices = new ArrayList();

        for ( int i = 0, il = this.vertices.size(); i < il; i ++ ) {

            Vector3 v = this.vertices.get(i);
            float key = Math.round( v.x * precision ) + '_' + Math.round( v.y * precision ) + '_' + Math.round( v.z * precision );

            if ( verticesMap.get(key) == null ) {
                verticesMap.put(key, i);
                unique.add( this.vertices.get(i) );
                changes.set(i, unique.size() - 1);
            } else {
                changes.set(i, changes.get(verticesMap.get(key)));
            }

        }

        // if faces are completely degenerate after merging vertices, we
        // have to remove them from the geometry.
        ArrayList<Integer> faceIndicesToRemove = new ArrayList();

        for ( int i = 0, il = this.faces.size(); i < il; i ++ ) {

            Face3 face = this.faces.get(i);

            face.a = changes.get(face.a);
            face.b = changes.get(face.b);
            face.c = changes.get(face.c);

            indices.clear();
            indices.add(face.a);
            indices.add(face.b);
            indices.add(face.c);

            // if any duplicate vertices are found in a Face3
            // we have to remove the face as nothing can be saved
            for ( int n = 0; n < 3; n ++ ) {
                if ( indices.get(n) == indices.get(( n + 1 ) % 3)) {
                    faceIndicesToRemove.add( i );
                    break;
                }
            }

        }

        for ( int i = faceIndicesToRemove.size() - 1; i >= 0; i -- ) {

            int idx = faceIndicesToRemove.get(i);

            this.faces.remove(idx);

            for ( int j = 0, jl = this.faceVertexUvs.size(); j < jl; j ++ ) {
                this.faceVertexUvs.get(j).remove( idx );
            }

        }

        // Use unique set of vertices

        int diff = this.vertices.size() - unique.size();
        this.vertices = unique;
        return diff;
    }
}
