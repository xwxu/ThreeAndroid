package three.core;

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

    public BufferAttribute getIndex(){
        return this.index;
    }

    public void setIndex(BufferAttribute index){
        if(index instanceof Uint16BufferAttribute){
            //this.index = (Uint16BufferAttribute) index;
        }else if(index instanceof Uint32BufferAttribute){
            this.index = (Uint32BufferAttribute) index;
        }
    }

//    public void setIndex(short[] index){
//        this.index = new Uint16BufferAttribute(index, 1);
//    }

    public void setIndex(int[] index){
        this.index = new Uint32BufferAttribute(index, 1);
    }

    public BufferGeometry addAttribute(String name, BufferAttribute attribute){
        if (name.equals("index")) {
            this.setIndex( attribute );
            return this;
        }

        this.attributes.put(name, attribute);
        return this;
    }

    public BufferAttribute getAttribute(String name){
        return this.attributes.get(name);
    }

    public BufferAttribute removeAttribute(String name){
        return this.attributes.remove(name);
    }

    public void addGroup(int start, int count, int materialIndex){
        GeoMatGroup group = new GeoMatGroup();
        group.start = start;
        group.count = count;
        group.materialIndex = materialIndex;
        this.groups.add(group);
    }

    public void clearGroups(){
        this.groups.clear();
    }

    public void setDrawRange(int start, int count){
        this.drawRange.start = start;
        this.drawRange.count = count;
    }

    public void applyMatrix(Matrix4 matrix){
        Float32BufferAttribute position = (Float32BufferAttribute) this.attributes.get("position");

        if ( position != null ) {
            matrix.applyToBufferAttribute( position );
            position.needsUpdate(true);
        }

        Float32BufferAttribute normal = (Float32BufferAttribute) this.attributes.get("normal");

        if ( normal != null ) {
            Matrix3 normalMatrix = new Matrix3().getNormalMatrix( matrix );
            normalMatrix.applyToBufferAttribute( normal );
            normal.needsUpdate(true);

        }

        if ( this.boundingBox != null ) {
            this.computeBoundingBox();
        }

        if ( this.boundingSphere != null ) {
            this.computeBoundingSphere();
        }
    }

    public void rotateX(float angle){
        // rotate geometry around world x-axis
        Matrix4 m1 = new Matrix4();
        m1.makeRotationX( angle );
        this.applyMatrix( m1 );
    }

    public void rotateY(float angle){
        // rotate geometry around world Y-axis
        Matrix4 m1 = new Matrix4();
        m1.makeRotationY( angle );
        this.applyMatrix( m1 );
    }

    public void rotateZ(float angle){
        // rotate geometry around world Z-axis
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

    public void center(){
        Vector3 offset = new Vector3();
        this.computeBoundingBox();
        this.boundingBox.getCenter( offset ).negate();
        this.translate( offset.x, offset.y, offset.z );
    }

    public BufferGeometry setFromObject(Object3D object){
        if(!(object.geometry instanceof Geometry)){
            return null;
        }

        Geometry geometry = (Geometry) (object.geometry);;
        if ( object instanceof Points || object instanceof Line) {

            Float32BufferAttribute positions = new Float32BufferAttribute( geometry.vertices.size() * 3, 3 );
            Float32BufferAttribute colors = new Float32BufferAttribute( geometry.colors.size() * 3, 3 );

            this.addAttribute( "position", positions.copyVector3SArray( geometry.vertices ) );
            this.addAttribute( "color", colors.copyColorsArray( geometry.colors ) );

            if ( geometry.lineDistances != null && geometry.lineDistances.size() == geometry.vertices.size() ) {
                Float32BufferAttribute lineDistances = new Float32BufferAttribute( geometry.lineDistances.size(), 1 );
                this.addAttribute( "lineDistance", lineDistances.copyArray( geometry.lineDistances ) );
            }

            if ( geometry.boundingSphere != null ) {
                this.boundingSphere = geometry.boundingSphere.Clone();
            }

            if ( geometry.boundingBox != null ) {
                this.boundingBox = geometry.boundingBox.clone();
            }

        } else if ( object instanceof Mesh) {
            if ( geometry != null ) {
                this.fromGeometry( geometry );
            }
        }

        return this;
    }

    public BufferGeometry setFromPoints(ArrayList<Vector3> points){
        float[] position = new float[points.size() * 3];

        for ( int i = 0, l = points.size(); i < l; i ++ ) {
            Vector3 point = points.get(i);
            position[i*3] = point.x;
            position[i*3+1] = point.y;
            position[i*3+2] = point.z;
        }

        this.addAttribute( "position", new Float32BufferAttribute( position, 3 ) );

        return this;
    }

    // mesh has normal, uv, groups attribute
    // other objects
    public BufferGeometry updateFromObject(Object3D object){
        Geometry geometry = (Geometry) object.geometry;

        if ( object instanceof Mesh ) {
            DirectGeometry direct = geometry._directGeometry;

            if (geometry.elementsNeedUpdate) {
                direct = null;
                geometry.elementsNeedUpdate = false;
            }

            if ( direct == null ) {
                return this.fromGeometry( geometry );
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
                    attribute.copyVector3SArray( direct.vertices );
                    attribute.needsUpdate(true);
                }
                direct.verticesNeedUpdate = false;
            }

            if (direct.normalsNeedUpdate) {
                attribute = (Float32BufferAttribute) this.attributes.get("normal");

                if ( attribute != null ) {
                    attribute.copyVector3SArray( direct.normals );
                    attribute.needsUpdate(true);
                }

                direct.normalsNeedUpdate = false;
            }

            if (direct.colorsNeedUpdate) {
                attribute = (Float32BufferAttribute) this.attributes.get("color");

                if ( attribute != null ) {
                    attribute.copyColorsArray( direct.colors );
                    attribute.needsUpdate(true);
                }

                direct.colorsNeedUpdate = false;
            }

            if ( direct.uvsNeedUpdate ) {
                attribute = (Float32BufferAttribute) this.attributes.get("uv");

                if ( attribute != null ) {
                    attribute.copyVector2SArray( direct.uvs );
                    attribute.needsUpdate(true);
                }

                direct.uvsNeedUpdate = false;
            }


            if ( direct.groupsNeedUpdate ) {
                direct.computeGroups( geometry );
                this.groups = direct.groups;

                direct.groupsNeedUpdate = false;
            }

        }

        Float32BufferAttribute attribute;

        if (geometry.verticesNeedUpdate) {
            attribute = (Float32BufferAttribute) this.attributes.get("position");

            if ( attribute != null ) {
                attribute.copyVector3SArray( geometry.vertices );
                attribute.needsUpdate(true);
            }

            geometry.verticesNeedUpdate = false;

        }

        if (geometry.colorsNeedUpdate) {
            attribute = (Float32BufferAttribute) this.attributes.get("color");

            if ( attribute != null ) {
                attribute.copyColorsArray( geometry.colors );
                attribute.needsUpdate(true);
            }

            geometry.colorsNeedUpdate = false;
        }


        if ( geometry.lineDistancesNeedUpdate ) {
            attribute = (Float32BufferAttribute) this.attributes.get("lineDistance");

            if ( attribute != null ) {
                attribute.copyArray( geometry.lineDistances );
                attribute.needsUpdate(true);
            }

            geometry.lineDistancesNeedUpdate = false;
        }

        return this;
    }

    public BufferGeometry fromGeometry(Geometry geometry) {
        geometry._directGeometry = new DirectGeometry().fromGeometry( geometry );
        return this.fromDirectGeometry( geometry._directGeometry );
    }

    public BufferGeometry fromDirectGeometry(DirectGeometry geometry) {

        this.addAttribute( "position", new Float32BufferAttribute( geometry.vertices.size() * 3, 3 ).copyVector3SArray( geometry.vertices ) );

        if ( geometry.normals.size() > 0 ) {
            this.addAttribute( "normal", new Float32BufferAttribute( geometry.normals.size() * 3, 3 ).copyVector3SArray( geometry.normals ) );
        }

        if ( geometry.colors.size() > 0 ) {
            this.addAttribute( "color", new Float32BufferAttribute( geometry.colors.size() * 3, 3 ).copyColorsArray( geometry.colors ) );
        }

        if ( geometry.uvs.size() > 0 ) {
            this.addAttribute( "uv", new Float32BufferAttribute( geometry.uvs.size() * 2, 2 ).copyVector2SArray( geometry.uvs ) );
        }

        if ( geometry.uvs2.size() > 0 ) {
            this.addAttribute( "uv2", new Float32BufferAttribute( geometry.uvs2.size() * 2, 2 ).copyVector2SArray( geometry.uvs2 ) );
        }

        this.groups = geometry.groups;

        if ( geometry.boundingSphere != null ) {
            this.boundingSphere = geometry.boundingSphere.Clone();
        }

        if ( geometry.boundingBox != null ) {
            this.boundingBox = geometry.boundingBox.clone();
        }

        return this;
    }

    public void computeBoundingBox(){
        if(this.boundingBox == null){
            this.boundingBox = new Box3();
        }
        Float32BufferAttribute position = (Float32BufferAttribute)this.attributes.get("position");

        if ( position != null ) {
            this.boundingBox.setFromBufferAttribute( position );
        } else {
            this.boundingBox.makeEmpty();
        }
    }

    public void computeBoundingSphere(){
        if(this.boundingSphere == null){
            this.boundingSphere = new Sphere();
        }
        Box3 box = new Box3();
        Vector3 vector = new Vector3();

        Float32BufferAttribute position = (Float32BufferAttribute) this.attributes.get("position");
        if(position != null){
            Vector3 center = this.boundingSphere.center;

            box.setFromBufferAttribute( position );
            box.getCenter( center );

            // hoping to find a boundingSphere with a radius smaller than the
            // boundingSphere of the boundingBox: sqrt(3) smaller in the best case
            float maxRadiusSq = 0;

            for ( int i = 0, il = position.count; i < il; i ++ ) {
                vector.x = position.getX( i );
                vector.y = position.getY( i );
                vector.z = position.getZ( i );
                maxRadiusSq = Math.max( maxRadiusSq, center.distanceToSquared( vector ) );
            }

            this.boundingSphere.radius = (float) Math.sqrt( maxRadiusSq );
        }
    }

    public BufferGeometry clone(){
        return new BufferGeometry().copy(this);
    }

    public BufferGeometry copy(BufferGeometry source){
        this.index = null;
        this.attributes.clear();

        this.boundingBox = null;
        this.boundingSphere = null;

        // name
        this.name = source.name;

        // index
        BufferAttribute index = source.index;
        if ( index != null ) {
            this.setIndex( index.clone() );
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
            this.boundingBox = boundingBox.clone();
        }

        // bounding sphere
        Sphere boundingSphere = source.boundingSphere;

        if ( boundingSphere != null ) {
            this.boundingSphere = boundingSphere.Clone();
        }

        return this;
    }
}
