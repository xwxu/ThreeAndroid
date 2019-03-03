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

    public void SetDrawMode(int value){
        this.drawMode = value;
    }

    public void SetMaterials(ArrayList<Material> materials){
        this.materials = materials;
    }

    public Mesh Copy(Mesh source){
        super.Copy(source, false);
        this.drawMode = source.drawMode;
        return this;
    }

    @Override
    public void Raycast(Raycaster raycaster, ArrayList<Intersect> intersects){
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

        inverseMatrix.GetInverse( matrixWorld );
        ray.Copy( raycaster.ray ).ApplyMatrix4( inverseMatrix );

        if(this.geometry instanceof BufferGeometry){
            BufferGeometry bufferGeometry = (BufferGeometry)(this.geometry);
            if(bufferGeometry.boundingSphere == null){
                bufferGeometry.ComputeBoundingSphere();
            }
            sphere.Copy( bufferGeometry.boundingSphere );
            sphere.ApplyMatrix4( matrixWorld );
            if ( !raycaster.ray.IntersectsSphere( sphere ) ) return;

            // Check boundingBox before continuing
            if ( bufferGeometry.boundingBox != null ) {
                if ( !ray.IntersectsBox( bufferGeometry.boundingBox ) ) return;
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
                            int a = index.GetX( j );
                            int b = index.GetX( j + 1 );
                            int c = index.GetX( j + 2 );

                            intersection = CheckBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
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
                        int a = (int)index.GetX( i );
                        int b = (int)index.GetX( i + 1 );
                        int c = (int)index.GetX( i + 2 );

                        intersection = CheckBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
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
                            intersection = CheckBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
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
                        intersection = CheckBufferGeometryIntersection( vA, vB, vC, uvA, uvB, uvC, intersectionPoint,
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
                geometry.ComputeBoundingSphere();
            }
            sphere.Copy( geometry.boundingSphere );
            sphere.ApplyMatrix4( matrixWorld );
            if ( !raycaster.ray.IntersectsSphere( sphere ) ) return;

            // Check boundingBox before continuing
            if ( geometry.boundingBox != null ) {
                if ( !ray.IntersectsBox( geometry.boundingBox ) ) return;
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

                intersection = CheckIntersection( this, faceMaterial, raycaster, ray, fvA, fvB, fvC, intersectionPoint );

                if ( intersection != null ) {
                    if ( faceVertexUvs.size() > 0 ) {
                        ArrayList<Vector2> uvs_f = faceVertexUvs.get(f);
                        uvA.Copy( uvs_f.get(0) );
                        uvB.Copy( uvs_f.get(1) );
                        uvC.Copy( uvs_f.get(2) );
                        intersection.uv = Triangle.GetUV( intersectionPoint, fvA, fvB, fvC, uvA, uvB, uvC, new Vector2() );
                    }

                    intersection.face = face;
                    intersection.faceIndex = f;
                    intersects.add( intersection );
                }
            }
        }

    }

    private Intersect CheckIntersection(Object3D object, Material material, Raycaster raycaster,
                                        Ray ray, Vector3 pA, Vector3 pB, Vector3 pC, Vector3 point){
        Vector3 intersect;

        if ( material.side == constants.BackSide ) {
            intersect = ray.IntersectTriangle( pC, pB, pA, true, point );
        } else {
            intersect = ray.IntersectTriangle( pA, pB, pC, material.side != constants.DoubleSide, point );
        }

        if ( intersect == null ) return null;

        Vector3 intersectionPointWorld = new Vector3();
        intersectionPointWorld.Copy( point );
        intersectionPointWorld.ApplyMatrix4( object.matrixWorld );

        double distance = raycaster.ray.origin.DistanceTo( intersectionPointWorld );

        if ( distance < raycaster.near || distance > raycaster.far ) return null;

        return new Intersect(distance, intersectionPointWorld.Clone(), object);
    }

    private Intersect CheckBufferGeometryIntersection(Vector3 vA, Vector3 vB, Vector3 vC, Vector2 uvA, Vector2 uvB, Vector2 uvC,
                                                      Vector3 intersectionPoint, Object3D object, Material material, Raycaster raycaster,
                                                      Ray ray, Float32BufferAttribute position, Float32BufferAttribute uv, int a, int b, int c){
        vA.FromBufferAttribute( position, a, 0 );
        vB.FromBufferAttribute( position, b, 0 );
        vC.FromBufferAttribute( position, c, 0 );

        Intersect intersection = CheckIntersection( object, material, raycaster, ray, vA, vB, vC, intersectionPoint );

        if ( intersection != null ) {
            if ( uv != null ) {
                uvA.FromBufferAttribute( uv, a, 0 );
                uvB.FromBufferAttribute( uv, b, 0 );
                uvC.FromBufferAttribute( uv, c, 0 );
                intersection.uv = Triangle.GetUV( intersectionPoint, vA, vB, vC, uvA, uvB, uvC, new Vector2() );
            }

            Face3 face = new Face3( a, b, c );
            Triangle.GetNormal( vA, vB, vC, face.normal );
            intersection.face = face;
        }

        return intersection;
    }

    public Mesh Clone(){
        Mesh mesh = new Mesh();
        mesh.geometry = this.geometry;
        mesh.material = this.material;
        mesh.materials = this.materials;
        return mesh.Copy(this);
    }
}
