package three.objects;

import java.util.ArrayList;


import three.bufferAttribute.Float32BufferAttribute;
import three.bufferAttribute.Uint32BufferAttribute;
import three.constants;
import three.core.AbstractGeometry;
import three.util.DrawRange;
import three.core.Face3;
import three.util.GeoMatGroup;
import three.util.Intersect;
import three.materials.Material;
import three.core.BufferGeometry;
import three.core.Geometry;
import three.core.Object3D;
import three.core.Raycaster;
import three.math.Matrix4;
import three.math.Ray;
import three.math.Sphere;
import three.math.Vector3;
import three.math.Vector2;
import three.math.Triangle;

public class Mesh extends Object3D {
    public String type;

    //public Material material;
    public ArrayList<Material> materials;
    public int drawMode;

    public Mesh(){}

    public Mesh(AbstractGeometry geometry, Material material){
        super();
        this.type = "Mesh";
        this.geometry = geometry;
        this.material = material;
        this.drawMode = constants.TrianglesDrawMode;
    }

    public void setDrawMode(int value){
        this.drawMode = value;
    }

    public void setMaterials(ArrayList<Material> materials){
        this.materials = materials;
    }

    public Mesh copy(Mesh source){
        super.copy(source, false);
        this.drawMode = source.drawMode;
        return this;
    }

    @Override
    public void raycast(Raycaster raycaster, ArrayList<Intersect> intersects){
        Matrix4 inverseMatrix = new Matrix4();
        Ray ray = new Ray();
        Sphere sphere = new Sphere();

        Vector3 vA = new Vector3();
        Vector3 vB = new Vector3();
        Vector3 vC = new Vector3();

        Vector3 tempA = new Vector3();
        Vector3 tempB = new Vector3();
        Vector3 tempC = new Vector3();

        Vector2 uvA = new Vector2();
        Vector2 uvB = new Vector2();
        Vector2 uvC = new Vector2();

        Vector3 intersectionPoint = new Vector3();

        Material material = this.material;
        Matrix4 matrixWorld = this.matrixWorld;

        if(material == null) return;

        Intersect intersection;

        inverseMatrix.getInverse( matrixWorld );
        ray.copy( raycaster.set).applyMatrix4( inverseMatrix );

        if(this.geometry instanceof BufferGeometry){
            BufferGeometry bufferGeometry = (BufferGeometry)(this.geometry);
            if(bufferGeometry.boundingSphere == null){
                bufferGeometry.computeBoundingSphere();
            }
            sphere.copy( bufferGeometry.boundingSphere );
            sphere.applyMatrix4( matrixWorld );
            if ( !raycaster.set.intersectsSphere( sphere ) ) return;

            // Check boundingBox before continuing
            if ( bufferGeometry.boundingBox != null ) {
                if ( !ray.intersectsBox( bufferGeometry.boundingBox ) ) return;
            }

            Uint32BufferAttribute index = bufferGeometry.index;
            Float32BufferAttribute position = (Float32BufferAttribute) bufferGeometry.attributes.get("position");
            Float32BufferAttribute uv = (Float32BufferAttribute) bufferGeometry.attributes.get("uv");;
            ArrayList<GeoMatGroup> groups = bufferGeometry.groups;
            DrawRange drawRange = bufferGeometry.drawRange;

            if ( index != null ) {
                // indexed buffer geometry
                if ( materials.size() > 0 ) {
                    for ( int i = 0, il = groups.size(); i < il; i ++ ) {
                        GeoMatGroup group = groups.get(i);
                        Material groupMaterial = this.materials.get(group.materialIndex);

                        int start = Math.max( group.start, drawRange.start );
                        int end = Math.min( ( group.start + group.count ), ( drawRange.start + drawRange.count ) );

                        for ( int j = start; j < end; j += 3 ) {
                            int a = index.getX( j );
                            int b = index.getX( j + 1 );
                            int c = index.getX( j + 2 );

                            intersection = checkBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
                                    this, groupMaterial, raycaster, ray, position, uv, a, b, c );

                            if ( intersection != null ) {
                                intersection.faceIndex = (int)Math.floor( j / 3 ); // triangle number in indexed buffer semantics
                                intersects.add( intersection );
                            }
                        }
                    }
                } else {
                    int start = Math.max( 0, drawRange.start );
                    int end = Math.min( index.count, ( drawRange.start + drawRange.count ) );

                    for ( int i = start; i < end; i += 3 ) {
                        int a = (int)index.getX( i );
                        int b = (int)index.getX( i + 1 );
                        int c = (int)index.getX( i + 2 );

                        intersection = checkBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
                                this, material, raycaster, ray, position, uv, a, b, c );

                        if ( intersection != null) {
                            intersection.faceIndex = (int)Math.floor( i / 3 ); // triangle number in indexed buffer semantics
                            intersects.add( intersection );
                        }
                    }
                }

            } else if ( position != null ) {

                // non-indexed buffer geometry
                if ( materials.size() > 0 ) {
                    for ( int i = 0, il = groups.size(); i < il; i ++ ) {
                        GeoMatGroup group = groups.get(i);
                        Material groupMaterial = materials.get(group.materialIndex);

                        int start = Math.max( group.start, drawRange.start );
                        int end = Math.min( ( group.start + group.count ), ( drawRange.start + drawRange.count ) );

                        for ( int j = start; j < end; j += 3 ) {
                            int a = j;
                            int b = j + 1;
                            int c = j + 2;
                            intersection = checkBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
                                    this, groupMaterial, raycaster, ray, position, uv, a, b, c );

                            if ( intersection != null) {
                                intersection.faceIndex = (int)Math.floor( j / 3 ); // triangle number in non-indexed buffer semantics
                                intersects.add( intersection );
                            }
                        }
                    }
                } else {
                    int start = Math.max( 0, drawRange.start );
                    int end = Math.min( position.count, ( drawRange.start + drawRange.count ) );

                    for ( int i = start; i < end; i += 3 ) {
                        int a = i;
                        int b = i + 1;
                        int c = i + 2;
                        intersection = checkBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
                                this, material, raycaster, ray, position, uv, a, b, c );

                        if ( intersection != null) {
                            intersection.faceIndex = (int)Math.floor( i / 3 ); // triangle number in non-indexed buffer semantics
                            intersects.add( intersection );
                        }
                    }
                }
            }

        }else{
            Geometry geometry = (Geometry)this.geometry;
            if(geometry.boundingSphere == null){
                geometry.computeBoundingSphere();
            }
            sphere.copy( geometry.boundingSphere );
            sphere.applyMatrix4( matrixWorld );
            if ( !raycaster.set.intersectsSphere( sphere ) ) return;

            // Check boundingBox before continuing
            if ( geometry.boundingBox != null ) {
                if ( !ray.intersectsBox( geometry.boundingBox ) ) return;
            }

            Vector3 fvA, fvB, fvC;
            boolean isMultiMaterial = this.materials.size() > 0;

            ArrayList<Vector3> vertices = geometry.vertices;
            ArrayList<Face3> faces = geometry.faces;

            ArrayList<ArrayList<Vector2>> faceVertexUvs = geometry.faceVertexUvs.get(0);

            for ( int f = 0, fl = faces.size(); f < fl; f ++ ) {
                Face3 face = faces.get(f);
                Material faceMaterial = isMultiMaterial ? this.materials.get(face.materialIndex) : material;

                if ( faceMaterial == null ) continue;

                fvA = vertices.get(face.a);
                fvB = vertices.get(face.b);
                fvC = vertices.get(face.c);

                intersection = checkIntersection( this, faceMaterial, raycaster, ray, fvA, fvB, fvC, intersectionPoint );

                if ( intersection != null ) {
                    if ( faceVertexUvs.size() > 0 ) {
                        ArrayList<Vector2> uvs_f = faceVertexUvs.get(f);
                        uvA.copy( uvs_f.get(0) );
                        uvB.copy( uvs_f.get(1) );
                        uvC.copy( uvs_f.get(2) );
                        intersection.uv = Triangle.getUV( intersectionPoint, fvA, fvB, fvC, uvA, uvB, uvC, new Vector2() );
                    }

                    intersection.face = face;
                    intersection.faceIndex = f;
                    intersects.add( intersection );
                }
            }
        }

    }

    private Intersect checkIntersection(Object3D object, Material material, Raycaster raycaster,
                                        Ray ray, Vector3 pA, Vector3 pB, Vector3 pC, Vector3 point){
        Vector3 intersect;

        if ( material.side == constants.BackSide ) {
            intersect = ray.intersectTriangle( pC, pB, pA, true, point );
        } else {
            intersect = ray.intersectTriangle( pA, pB, pC, material.side != constants.DoubleSide, point );
        }

        if ( intersect == null ) return null;

        Vector3 intersectionPointWorld = new Vector3();
        intersectionPointWorld.copy( point );
        intersectionPointWorld.applyMatrix4( object.matrixWorld );

        double distance = raycaster.set.origin.distanceTo( intersectionPointWorld );

        if ( distance < raycaster.near || distance > raycaster.far ) return null;

        return new Intersect(distance, intersectionPointWorld.Clone(), object);
    }

    private Intersect checkBufferGeometryIntersection(Vector3 vA, Vector3 vB, Vector3 vC, Vector2 uvA, Vector2 uvB, Vector2 uvC,
                                                      Vector3 intersectionPoint, Object3D object, Material material, Raycaster raycaster,
                                                      Ray ray, Float32BufferAttribute position, Float32BufferAttribute uv, int a, int b, int c){
        vA.fromBufferAttribute( position, a, 0 );
        vB.fromBufferAttribute( position, b, 0 );
        vC.fromBufferAttribute( position, c, 0 );

        Intersect intersection = checkIntersection( object, material, raycaster, ray, vA, vB, vC, intersectionPoint );

        if ( intersection != null ) {
            if ( uv != null ) {
                uvA.fromBufferAttribute( uv, a, 0 );
                uvB.fromBufferAttribute( uv, b, 0 );
                uvC.fromBufferAttribute( uv, c, 0 );
                intersection.uv = Triangle.getUV( intersectionPoint, vA, vB, vC, uvA, uvB, uvC, new Vector2() );
            }

            Face3 face = new Face3( a, b, c );
            Triangle.getNormal( vA, vB, vC, face.normal );
            intersection.face = face;
        }

        return intersection;
    }

    public Mesh Clone(){
        Mesh mesh = new Mesh();
        mesh.geometry = this.geometry;
        mesh.material = this.material;
        mesh.materials = this.materials;
        return mesh.copy(this);
    }
}
