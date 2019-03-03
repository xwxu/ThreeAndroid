package three.core;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;
import three.bufferAttribute.Uint16BufferAttribute;
import three.bufferAttribute.Uint32BufferAttribute;
import three.math.Box3;
import three.math.Matrix3;
import three.math.Matrix4;
import three.math.Sphere;
import three.math.Vector3;
import three.objects.Line;
import three.objects.Mesh;
import three.objects.Points;
import three.util.DrawRange;
import three.util.GeoMatGroup;

public class BufferGeometry extends AbstractGeometry{

    private static int bufferGeometryId = 1;

    //public Uint16BufferAttribute index; // this is used when index number < 65535
    public Uint32BufferAttribute index;
    public HashMap<String, BufferAttribute> attributes;
    public ArrayList<GeoMatGroup> groups;
    public Box3 boundingBox;
    public Sphere boundingSphere;
    public DrawRange drawRange;

    public BufferGeometry(){
        id = bufferGeometryId += 2;
        type = "BufferGeometry";
        this.index = null;
        this.attributes = new HashMap<String, BufferAttribute>();
        this.groups = new ArrayList<GeoMatGroup>();
        this.boundingBox = null;
        this.boundingSphere = null;
        this.drawRange = new DrawRange();

    }

    public BufferAttribute GetIndex(){
        return this.index;
    }

    public void SetIndex(BufferAttribute index){
        if(index instanceof Uint16BufferAttribute){
            //this.index = (Uint16BufferAttribute) index;
        }else if(index instanceof Uint32BufferAttribute){
            this.index = (Uint32BufferAttribute) index;
        }
    }

//    public void SetIndex(short[] index){
//        this.index = new Uint16BufferAttribute(index, 1);
//    }

    public void SetIndex(int[] index){
        this.index = new Uint32BufferAttribute(index, 1);
    }

    public BufferGeometry AddAttribute(String name, BufferAttribute attribute){
        if ( name == "index" ) {
            this.SetIndex( attribute );
            return this;
        }

        this.attributes.put(name, attribute);
        return this;
    }

    public BufferAttribute GetAttribute(String name){
        return this.attributes.get(name);
    }

    public BufferAttribute RemoveAttribute(String name){
        return this.attributes.remove(name);
    }

    public void AddGroup(int start, int count, int materialIndex){
        GeoMatGroup group = new GeoMatGroup();
        group.start = start;
        group.count = count;
        group.materialIndex = materialIndex;
        this.groups.add(group);
    }

    public void ClearGroups(){
        this.groups.clear();
    }

    public void SetDrawRange(int start, int count){
        this.drawRange.start = start;
        this.drawRange.count = count;
    }

    public void ApplyMatrix(Matrix4 matrix){
        Float32BufferAttribute position = (Float32BufferAttribute) this.attributes.get("position");

        if ( position != null ) {
            matrix.ApplyToBufferAttribute( position );
            position.needsUpdate(true);
        }

        Float32BufferAttribute normal = (Float32BufferAttribute) this.attributes.get("normal");

        if ( normal != null ) {
            Matrix3 normalMatrix = new Matrix3().GetNormalMatrix( matrix );
            normalMatrix.ApplyToBufferAttribute( normal );
            normal.needsUpdate(true);

        }

        if ( this.boundingBox != null ) {
            this.ComputeBoundingBox();
        }

        if ( this.boundingSphere != null ) {
            this.ComputeBoundingSphere();
        }
    }

    public void RotateX(float angle){
        // rotate geometry around world x-axis
        Matrix4 m1 = new Matrix4();
        m1.MakeRotationX( angle );
        this.ApplyMatrix( m1 );
    }

    public void RotateY(float angle){
        // rotate geometry around world Y-axis
        Matrix4 m1 = new Matrix4();
        m1.MakeRotationY( angle );
        this.ApplyMatrix( m1 );
    }

    public void RotateZ(float angle){
        // rotate geometry around world Z-axis
        Matrix4 m1 = new Matrix4();
        m1.MakeRotationZ( angle );
        this.ApplyMatrix( m1 );
    }

    public void Translate(float x, float y, float z){
        Matrix4 m1 = new Matrix4();
        m1.MakeTranslation( x, y, z );
        this.ApplyMatrix( m1 );
    }

    public void Scale(float x, float y, float z){
        Matrix4 m1 = new Matrix4();
        m1.MakeScale( x, y, z );
        this.ApplyMatrix( m1 );
    }

    public void LookAt(Vector3 vector){
        Object3D obj = new Object3D();
        obj.LookAt( vector );
        obj.UpdateMatrix();
        this.ApplyMatrix( obj.matrix );
    }

    public void Center(){
        Vector3 offset = new Vector3();
        this.ComputeBoundingBox();
        this.boundingBox.GetCenter( offset ).Negate();
        this.Translate( offset.x, offset.y, offset.z );
    }

    public BufferGeometry SetFromObject(Object3D object){
        if(!(object.geometry instanceof Geometry)){
            return null;
        }

        Geometry geometry = (Geometry) (object.geometry);;
        if ( object instanceof Points || object instanceof Line) {

            Float32BufferAttribute positions = new Float32BufferAttribute( geometry.vertices.size() * 3, 3 );
            Float32BufferAttribute colors = new Float32BufferAttribute( geometry.colors.size() * 3, 3 );

            this.AddAttribute( "position", positions.CopyVector3sArray( geometry.vertices ) );
            this.AddAttribute( "color", colors.CopyColorsArray( geometry.colors ) );

            if ( geometry.lineDistances != null && geometry.lineDistances.size() == geometry.vertices.size() ) {
                Float32BufferAttribute lineDistances = new Float32BufferAttribute( geometry.lineDistances.size(), 1 );
                this.AddAttribute( "lineDistance", lineDistances.CopyArray( geometry.lineDistances ) );
            }

            if ( geometry.boundingSphere != null ) {
                this.boundingSphere = geometry.boundingSphere.Clone();
            }

            if ( geometry.boundingBox != null ) {
                this.boundingBox = geometry.boundingBox.Clone();
            }

        } else if ( object instanceof Mesh) {
            if ( geometry != null ) {
                this.FromGeometry( geometry );
            }
        }

        return this;
    }

    public BufferGeometry SetFromPoints(ArrayList<Vector3> points){
        float[] position = new float[points.size() * 3];

        for ( int i = 0, l = points.size(); i < l; i ++ ) {
            Vector3 point = points.get(i);
            position[i*3] = point.x;
            position[i*3+1] = point.y;
            position[i*3+2] = point.z;
        }

        this.AddAttribute( "position", new Float32BufferAttribute( position, 3 ) );

        return this;
    }

    // mesh has normal, uv, groups attribute
    // other objects
    public BufferGeometry UpdateFromObject(Object3D object){
        Geometry geometry = (Geometry) object.geometry;

        if ( object instanceof Mesh ) {
            DirectGeometry direct = geometry._directGeometry;

            if (geometry.elementsNeedUpdate) {
                direct = null;
                geometry.elementsNeedUpdate = false;
            }

            if ( direct == null ) {
                return this.FromGeometry( geometry );
            }

            direct.verticesNeedUpdate = geometry.verticesNeedUpdate;
            direct.normalsNeedUpdate = geometry.normalsNeedUpdate;
            direct.colorsNeedUpdate = geometry.colorsNeedUpdate;
            direct.uvsNeedUpdate = geometry.uvsNeedUpdate;
            direct.groupsNeedUpdate = geometry.groupsNeedUpdate;

            geometry.verticesNeedUpdate = false;
            geometry.normalsNeedUpdate = false;
            geometry.colorsNeedUpdate = false;
            geometry.uvsNeedUpdate = false;
            geometry.groupsNeedUpdate = false;

            Float32BufferAttribute attribute;

            if (direct.verticesNeedUpdate) {
                attribute = (Float32BufferAttribute) this.attributes.get("position");

                if ( attribute != null ) {
                    attribute.CopyVector3sArray( direct.vertices );
                    attribute.needsUpdate(true);
                }
                direct.verticesNeedUpdate = false;
            }

            if (direct.normalsNeedUpdate) {
                attribute = (Float32BufferAttribute) this.attributes.get("normal");

                if ( attribute != null ) {
                    attribute.CopyVector3sArray( direct.normals );
                    attribute.needsUpdate(true);
                }

                direct.normalsNeedUpdate = false;
            }

            if (direct.colorsNeedUpdate) {
                attribute = (Float32BufferAttribute) this.attributes.get("color");

                if ( attribute != null ) {
                    attribute.CopyColorsArray( direct.colors );
                    attribute.needsUpdate(true);
                }

                direct.colorsNeedUpdate = false;
            }

            if ( direct.uvsNeedUpdate ) {
                attribute = (Float32BufferAttribute) this.attributes.get("uv");

                if ( attribute != null ) {
                    attribute.CopyVector2sArray( direct.uvs );
                    attribute.needsUpdate(true);
                }

                direct.uvsNeedUpdate = false;
            }


            if ( direct.groupsNeedUpdate ) {
                direct.ComputeGroups( geometry );
                this.groups = direct.groups;

                direct.groupsNeedUpdate = false;
            }

        }

        Float32BufferAttribute attribute;

        if (geometry.verticesNeedUpdate) {
            attribute = (Float32BufferAttribute) this.attributes.get("position");

            if ( attribute != null ) {
                attribute.CopyVector3sArray( geometry.vertices );
                attribute.needsUpdate(true);
            }

            geometry.verticesNeedUpdate = false;

        }

        if (geometry.colorsNeedUpdate) {
            attribute = (Float32BufferAttribute) this.attributes.get("color");

            if ( attribute != null ) {
                attribute.CopyColorsArray( geometry.colors );
                attribute.needsUpdate(true);
            }

            geometry.colorsNeedUpdate = false;
        }


        if ( geometry.lineDistancesNeedUpdate ) {
            attribute = (Float32BufferAttribute) this.attributes.get("lineDistance");

            if ( attribute != null ) {
                attribute.CopyArray( geometry.lineDistances );
                attribute.needsUpdate(true);
            }

            geometry.lineDistancesNeedUpdate = false;
        }

        return this;
    }

    public BufferGeometry FromGeometry(Geometry geometry) {
        geometry._directGeometry = new DirectGeometry().FromGeometry( geometry );
        return this.FromDirectGeometry( geometry._directGeometry );
    }

    public BufferGeometry FromDirectGeometry(DirectGeometry geometry) {

        this.AddAttribute( "position", new Float32BufferAttribute( geometry.vertices.size() * 3, 3 ).CopyVector3sArray( geometry.vertices ) );

        if ( geometry.normals.size() > 0 ) {
            this.AddAttribute( "normal", new Float32BufferAttribute( geometry.normals.size() * 3, 3 ).CopyVector3sArray( geometry.normals ) );
        }

        if ( geometry.colors.size() > 0 ) {
            this.AddAttribute( "color", new Float32BufferAttribute( geometry.colors.size() * 3, 3 ).CopyColorsArray( geometry.colors ) );
        }

        if ( geometry.uvs.size() > 0 ) {
            this.AddAttribute( "uv", new Float32BufferAttribute( geometry.uvs.size() * 2, 2 ).CopyVector2sArray( geometry.uvs ) );
        }

        if ( geometry.uvs2.size() > 0 ) {
            this.AddAttribute( "uv2", new Float32BufferAttribute( geometry.uvs2.size() * 2, 2 ).CopyVector2sArray( geometry.uvs2 ) );
        }

        this.groups = geometry.groups;

        if ( geometry.boundingSphere != null ) {
            this.boundingSphere = geometry.boundingSphere.Clone();
        }

        if ( geometry.boundingBox != null ) {
            this.boundingBox = geometry.boundingBox.Clone();
        }

        return this;
    }

    public void ComputeBoundingBox(){
        if(this.boundingBox == null){
            this.boundingBox = new Box3();
        }
        Float32BufferAttribute position = (Float32BufferAttribute)this.attributes.get("position");

        if ( position != null ) {
            this.boundingBox.SetFromBufferAttribute( position );
        } else {
            this.boundingBox.MakeEmpty();
        }
    }

    public void ComputeBoundingSphere(){
        if(this.boundingSphere == null){
            this.boundingSphere = new Sphere();
        }
        Box3 box = new Box3();
        Vector3 vector = new Vector3();

        Float32BufferAttribute position = (Float32BufferAttribute) this.attributes.get("position");
        if(position != null){
            Vector3 center = this.boundingSphere.center;

            box.SetFromBufferAttribute( position );
            box.GetCenter( center );

            // hoping to find a boundingSphere with a radius smaller than the
            // boundingSphere of the boundingBox: sqrt(3) smaller in the best case
            float maxRadiusSq = 0;

            for ( int i = 0, il = position.count; i < il; i ++ ) {
                vector.x = position.GetX( i );
                vector.y = position.GetY( i );
                vector.z = position.GetZ( i );
                maxRadiusSq = Math.max( maxRadiusSq, center.DistanceToSquared( vector ) );
            }

            this.boundingSphere.radius = (float) Math.sqrt( maxRadiusSq );
        }
    }

    public BufferGeometry Clone(){
        return new BufferGeometry().Copy(this);
    }

    public BufferGeometry Copy(BufferGeometry source){
        this.index = null;
        this.attributes.clear();

        this.boundingBox = null;
        this.boundingSphere = null;

        // name
        this.name = source.name;

        // index
        BufferAttribute index = source.index;
        if ( index != null ) {
            this.SetIndex( index.Clone() );
        }

        Iterator iterator = source.attributes.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)iterator.next();
            String name = (String) pair.getKey();
            BufferAttribute attribute = (BufferAttribute) pair.getValue();
            this.attributes.put(name, attribute);
            iterator.remove(); // avoids a ConcurrentModificationException
        }

        // bounding box
        Box3 boundingBox = source.boundingBox;

        if ( boundingBox != null ) {
            this.boundingBox = boundingBox.Clone();
        }

        // bounding sphere
        Sphere boundingSphere = source.boundingSphere;

        if ( boundingSphere != null ) {
            this.boundingSphere = boundingSphere.Clone();
        }

        return this;
    }
}
