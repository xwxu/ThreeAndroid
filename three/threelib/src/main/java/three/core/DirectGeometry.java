package three.core;

import java.util.ArrayList;

import three.math.Box3;
import three.math.Color;
import three.math.Sphere;
import three.math.Vector2;
import three.math.Vector3;
import three.util.GeoMatGroup;

public class DirectGeometry {
    public ArrayList<Vector3> vertices = new ArrayList<>();
    public ArrayList<Vector3> normals = new ArrayList<>();
    public ArrayList<Color> colors = new ArrayList<>();
    public ArrayList<Vector2> uvs = new ArrayList<>();
    public ArrayList<Vector2> uvs2 = new ArrayList<>();
    public ArrayList<GeoMatGroup> groups = new ArrayList<>();

    public Box3 boundingBox = null;
    public Sphere boundingSphere = null;
    public boolean verticesNeedUpdate = false;
    public boolean normalsNeedUpdate = false;
    public boolean colorsNeedUpdate = false;
    public boolean uvsNeedUpdate = false;
    public boolean groupsNeedUpdate = false;

    // TODO: inclde morphs and skins
    public DirectGeometry FromGeometry(Geometry geometry) {
        ArrayList<Face3> faces = geometry.faces;
        ArrayList<Vector3> vertices = geometry.vertices;
        ArrayList<ArrayList<ArrayList<Vector2>>> faceVertexUvs = geometry.faceVertexUvs;

        boolean hasFaceVertexUv = faceVertexUvs.get(0) != null && faceVertexUvs.get(0).size() > 0;
        boolean hasFaceVertexUv2 = faceVertexUvs.get(1) != null && faceVertexUvs.get(1).size() > 0;

        if ( vertices.size() > 0 && faces.size() == 0 ) {
            return this;
        }

        for ( int i = 0; i < faces.size(); i ++ ) {
            Face3 face = faces.get(i);
            this.vertices.add(vertices.get(face.a));
            this.vertices.add(vertices.get(face.b));
            this.vertices.add(vertices.get(face.c));

            ArrayList<Vector3> vertexNormals = face.vertexNormals;

            if ( vertexNormals.size() == 3 ) {
                this.normals.add(vertexNormals.get(0));
                this.normals.add(vertexNormals.get(1));
                this.normals.add(vertexNormals.get(2));
            } else {
                Vector3 normal = face.normal;
                this.normals.add(normal);
                this.normals.add(normal);
                this.normals.add(normal);
            }

            ArrayList<Color> vertexColors = face.vertexColors;

            if ( vertexColors.size() == 3 ) {
                this.colors.add(vertexColors.get(0));
                this.colors.add(vertexColors.get(1));
                this.colors.add(vertexColors.get(2));
            } else {
                Color color = face.color;
                this.colors.add(color);
                this.colors.add(color);
                this.colors.add(color);
            }

            if (hasFaceVertexUv) {

                ArrayList<Vector2> vertexUvs = faceVertexUvs.get(0).get(i);

                if ( vertexUvs != null ) {
                    this.uvs.add(vertexUvs.get(0));
                    this.uvs.add(vertexUvs.get(1));
                    this.uvs.add(vertexUvs.get(2));

                } else {
                    this.uvs.add(new Vector2());
                    this.uvs.add(new Vector2());
                    this.uvs.add(new Vector2());
                }

            }

            if (hasFaceVertexUv2) {
                ArrayList<Vector2> vertexUvs = faceVertexUvs.get(1).get(i);

                if ( vertexUvs != null ) {
                    this.uvs2.add(vertexUvs.get(0));
                    this.uvs2.add(vertexUvs.get(1));
                    this.uvs2.add(vertexUvs.get(2));
                } else {
                    this.uvs2.add(new Vector2());
                    this.uvs2.add(new Vector2());
                    this.uvs2.add(new Vector2());
                }
            }
        }

        this.ComputeGroups( geometry );

        this.verticesNeedUpdate = geometry.verticesNeedUpdate;
        this.normalsNeedUpdate = geometry.normalsNeedUpdate;
        this.colorsNeedUpdate = geometry.colorsNeedUpdate;
        this.uvsNeedUpdate = geometry.uvsNeedUpdate;
        this.groupsNeedUpdate = geometry.groupsNeedUpdate;
        return this;
    }

    public void ComputeGroups(Geometry geometry) {
        GeoMatGroup group = null;
        ArrayList<GeoMatGroup> groups = new ArrayList<>();
        int materialIndex = -1;

        ArrayList<Face3> faces = geometry.faces;

        for ( int i = 0; i < faces.size(); i ++ ) {
            Face3 face = faces.get(i);
            // materials
            if ( face.materialIndex != materialIndex ) {

                materialIndex = face.materialIndex;
                if ( group != null ) {
                    group.count = ( i * 3 ) - group.start;
                    groups.add( group );
                }

                group = new GeoMatGroup();
                group.start = i * 3;
                group.materialIndex = materialIndex;

            }
        }

        if ( group != null ) {
            group.count = ( faces.size() * 3 ) - group.start;
            groups.add( group );
        }

        this.groups = groups;
    }
}
