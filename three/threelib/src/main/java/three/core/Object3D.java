package three.core;

import java.util.ArrayList;
import java.util.Observable;

import three.cameras.Camera;
import three.lights.Light;
import three.materials.Material;
import three.math.Euler;
import three.math.Math_;
import three.math.Matrix4;
import three.math.Matrix3;
import three.math.Quaternion;
import three.math.Vector3;
import three.renderers.GLRenderer;
import three.scenes.Scene;
import three.util.Intersect;

public class Object3D extends Observable {
    private static int object3DId = 0;
    public static final Vector3 DefaultUp = new Vector3(0,1,0);
    public static final boolean DefaultMatrixAutoUpdate = true;

    public int id;
    public String uuid;
    public String name;
    public String type;
    public Object3D  parent;
    public ArrayList<Object3D> children;
    public Vector3 up;
    public Vector3 position;
    public Euler rotation;
    public Quaternion quaternion;
    public Vector3 scale;
    public Matrix4 modelViewMatrix;
    public Matrix3 normalMatrix;
    public Matrix4 matrix;
    public Matrix4 matrixWorld;
    public boolean matrixAutoUpdate;
    public boolean matrixWorldNeedsUpdate;
    public Layers layers;
    public boolean visible;
    public boolean castShadow;
    public boolean receiveShadow;
    public boolean frustumCulled;
    public int renderOrder;
    public AbstractGeometry geometry = null;
    public Material material = null;

    public Object3D(){
        this.id = object3DId++;
        this.uuid = Math_.generateUUID();
        this.name = "";
        this.type = "Object3D";
        this.parent = null;
        this.children = new ArrayList<Object3D>();
        this.up = DefaultUp;
        this.position = new Vector3();
        this.rotation = new Euler();
        this.quaternion = new Quaternion();
        this.rotation.setOnchangeCallback(this.quaternion);
        this.quaternion.setOnchangeCallback(this.rotation);
        this.scale = new Vector3(1, 1, 1);
        this.modelViewMatrix = new Matrix4();
        this.normalMatrix = new Matrix3();
        this.matrix = new Matrix4();
        this.matrixWorld = new Matrix4();
        this.matrixAutoUpdate = DefaultMatrixAutoUpdate;
        this.matrixWorldNeedsUpdate = false;
        this.layers = new Layers();
        this.visible = true;
        this.castShadow = false;
        this.receiveShadow = false;
        this.frustumCulled = true;
        this.renderOrder = 0;

    }

    public void applyMatrix(Matrix4 matrix){
        this.matrix.multiplyMatrices( matrix, this.matrix );
        this.matrix.decompose( this.position, this.quaternion, this.scale );
    }

    public Object3D applyQuaternion(Quaternion q){
        this.quaternion.premultiply( q );
        return this;
    }

    public void setRotationFromAxisAngle(Vector3 axis, float angle){
        this.quaternion.setFromAxisAngle( axis, angle );
    }

    public void setRotationFromEuler(Euler euler){
        this.quaternion.setFromEuler( euler, true );
    }

    public void setRotationFromMatrix(Matrix4 m){
        this.quaternion.setFromRotationMatrix( m );
    }

    public void setRotationFromQuaternion(Quaternion q){
        this.quaternion.copy( q );
    }

    public void rotateOnAxis(Vector3 axis, float angle){
        // rotate object on axis in object space
        // axis is assumed to be normalized
        Quaternion q1 = new Quaternion();
        q1.setFromAxisAngle( axis, angle );
        this.quaternion.multiply( q1 );
    }

    public void rotateOnWorldAxis(Vector3 axis, float angle){
        // rotate object on axis in world space
        // axis is assumed to be normalized
        // method assumes no rotated parent
        Quaternion q1 = new Quaternion();
        q1.setFromAxisAngle( axis, angle );
        this.quaternion.premultiply( q1 );
    }

    public void rotateX(float angle){
        Vector3 v1 = new Vector3( 1, 0, 0 );
        this.rotateOnAxis( v1, angle );
    }

    public void rotateY(float angle){
        Vector3 v1 = new Vector3( 0, 1, 0 );
        this.rotateOnAxis( v1, angle );
    }

    public void rotateZ(float angle){
        Vector3 v1 = new Vector3( 0, 0, 1 );
        this.rotateOnAxis( v1, angle );
    }

    public void translateOnAxis(Vector3 axis, float distance){
        // translate object by distance along axis in object space
        // axis is assumed to be normalized
        Vector3 v1 = new Vector3();
        v1.copy( axis ).applyQuaternion( this.quaternion );
        this.position.add( v1.multiplyScalar( distance ) );
    }

    public void translateX(float distance){
        Vector3 v1 = new Vector3( 1, 0, 0 );
        this.translateOnAxis( v1, distance );
    }

    public void translateY(float distance){
        Vector3 v1 = new Vector3( 0, 1, 0 );
        this.translateOnAxis( v1, distance );
    }

    public void translateZ(float distance){
        Vector3 v1 = new Vector3( 0, 0, 1 );
        this.translateOnAxis( v1, distance );
    }


    public Vector3 localToWorld(Vector3 vector){
        return vector.applyMatrix4( this.matrixWorld );
    }

    public Vector3 worldToLocal(Vector3 vector){
        Matrix4 m1 = new Matrix4();
        return vector.applyMatrix4( m1.getInverse( this.matrixWorld ) );
    }

    public void lookAt(Vector3 v){
        // This method does not support objects having non-uniformly-scaled parent(s)
        Quaternion q1 = new Quaternion();
        Matrix4 m1 = new Matrix4();
        Vector3 target = new Vector3();
        Vector3 position = new Vector3();

        target.copy( v );
        Object3D parent = this.parent;
        this.updateWorldMatrix( true, false );
        position.setFromMatrixPosition( this.matrixWorld );

        if ( this instanceof Camera || this instanceof Light) {
            m1.lookAt( position, target, this.up );
        } else {
            m1.lookAt( target, position, this.up );
        }

        this.quaternion.setFromRotationMatrix( m1 );

        if ( parent != null ) {
            m1.extractRotation( parent.matrixWorld );
            q1.setFromRotationMatrix( m1 );
            this.quaternion.premultiply( q1.inverse() );
        }
    }

    public void add(Object3D object){
        if(object.equals(this)){
            return;
        }

        if ( object.parent != null ) {
            object.parent.remove( object );
        }

        object.parent = this;
        this.children.add( object );
    }

    public void remove(Object3D object){
        boolean removed = this.children.remove(object);
        if ( removed ) {
            object.parent = null;
        }
    }

    public Object3D getObjectById(int id){
        if ( this.id == id ) return this;
        for ( int i = 0, l = this.children.size(); i < l; i ++ ) {
            Object3D child = this.children.get(i);
            Object3D object = child.getObjectById(id);
            if ( object != null ) {
                return object;
            }
        }

        return null;
    }

    public Object3D getObjectByName(String name){
        if ( this.name == name ) return this;
        for ( int i = 0, l = this.children.size(); i < l; i ++ ) {
            Object3D child = this.children.get(i);
            Object3D object = child.getObjectByName(name);
            if ( object != null ) {
                return object;
            }
        }

        return null;
    }

    public Vector3 getWorldPosition(Vector3 target){
        this.updateMatrixWorld(true);
        return target.setFromMatrixPosition( this.matrixWorld );
    }

    public Quaternion getWorldQuaternion(Quaternion target){
        Vector3 position = new Vector3();
        Vector3 scale = new Vector3();

        this.updateMatrixWorld( true );
        this.matrixWorld.decompose( position, target, scale );
        return target;
    }

    public Vector3 getWorldScale(Vector3 target){
        Vector3 position = new Vector3();
        Quaternion quaternion = new Quaternion();

        this.updateMatrixWorld( true );
        this.matrixWorld.decompose( position, quaternion, target );
        return target;
    }

    public Vector3 getWorldDirection(Vector3 target){
        this.updateMatrixWorld( true );
        float[] e = this.matrixWorld.elements;
        return target.set( e[ 8 ], e[ 9 ], e[ 10 ] ).normalize();
    }

    public void raycast(Raycaster raycaster, ArrayList<Intersect> intersects){}

    public void traverse(){
        // not implemented
    }

    public void updateMatrix(){
        this.matrix.compose( this.position, this.quaternion, this.scale );
        this.matrixWorldNeedsUpdate = true;
    }

    public void updateMatrixWorld(boolean force){
        if ( this.matrixAutoUpdate ) this.updateMatrix();

        if ( this.matrixWorldNeedsUpdate || force ) {
            if ( this.parent == null ) {
                this.matrixWorld.copy( this.matrix );
            } else {
                this.matrixWorld.multiplyMatrices( this.parent.matrixWorld, this.matrix );
            }

            this.matrixWorldNeedsUpdate = false;
            force = true;
        }

        // update children
        ArrayList<Object3D> children = this.children;
        for ( int i = 0, l = children.size(); i < l; i ++ ) {
            children.get(i).updateMatrixWorld( force );
        }
    }

    public void updateWorldMatrix(boolean updateParents, boolean updateChildren){
        Object3D parent = this.parent;

        if (updateParents && parent != null ) {
            parent.updateWorldMatrix( true, false );
        }

        if ( this.matrixAutoUpdate ) this.updateMatrix();

        if ( this.parent == null ) {
            this.matrixWorld.copy( this.matrix );
        } else {
            this.matrixWorld.multiplyMatrices( this.parent.matrixWorld, this.matrix );
        }

        // update children
        if (updateChildren) {
            ArrayList<Object3D> children = this.children;
            for ( int i = 0, l = children.size(); i < l; i ++ ) {
                children.get(i).updateWorldMatrix( false, true );
            }
        }
    }

    public Object3D clone_(boolean recursive){
        return new Object3D().copy( this, recursive );
    }

    public Object3D copy(Object3D source, boolean recursive){
        this.name = source.name;
        this.up.copy( source.up );

        this.position.copy( source.position );
        this.quaternion.copy( source.quaternion );
        this.scale.copy( source.scale );

        this.matrix.copy( source.matrix );
        this.matrixWorld.copy( source.matrixWorld );

        this.matrixAutoUpdate = source.matrixAutoUpdate;
        this.matrixWorldNeedsUpdate = source.matrixWorldNeedsUpdate;

        this.layers.mask = source.layers.mask;
        this.visible = source.visible;

        this.castShadow = source.castShadow;
        this.receiveShadow = source.receiveShadow;

        this.frustumCulled = source.frustumCulled;
        this.renderOrder = source.renderOrder;

        if (recursive) {
            for ( int i = 0; i < source.children.size(); i ++ ) {
                Object3D child = source.children.get(i);
                this.add( child.clone_(true) );
            }
        }

        return this;
    }

    public void onBeforeRender(GLRenderer renderer, Scene scene, Camera camera){}

}
