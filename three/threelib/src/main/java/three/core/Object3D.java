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
        this.uuid = Math_.GenerateUUID();
        this.name = "";
        this.type = "Object3D";
        this.parent = null;
        this.children = new ArrayList<Object3D>();
        this.up = DefaultUp;
        this.position = new Vector3();
        this.rotation = new Euler();
        this.quaternion = new Quaternion();
        this.rotation.SetOnchangeCallback(this.quaternion);
        this.quaternion.SetOnchangeCallback(this.rotation);
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

    public void ApplyMatrix(Matrix4 matrix){
        this.matrix.MultiplyMatrices( matrix, this.matrix );
        this.matrix.Decompose( this.position, this.quaternion, this.scale );
    }

    public Object3D ApplyQuaternion(Quaternion q){
        this.quaternion.Premultiply( q );
        return this;
    }

    public void SetRotationFromAxisAngle(Vector3 axis, float angle){
        this.quaternion.SetFromAxisAngle( axis, angle );
    }

    public void SetRotationFromEuler(Euler euler){
        this.quaternion.SetFromEuler( euler, true );
    }

    public void SetRotationFromMatrix(Matrix4 m){
        this.quaternion.SetFromRotationMatrix( m );
    }

    public void SetRotationFromQuaternion(Quaternion q){
        this.quaternion.Copy( q );
    }

    public void RotateOnAxis(Vector3 axis, float angle){
        // rotate object on axis in object space
        // axis is assumed to be normalized
        Quaternion q1 = new Quaternion();
        q1.SetFromAxisAngle( axis, angle );
        this.quaternion.Multiply( q1 );
    }

    public void RotateOnWorldAxis(Vector3 axis, float angle){
        // rotate object on axis in world space
        // axis is assumed to be normalized
        // method assumes no rotated parent
        Quaternion q1 = new Quaternion();
        q1.SetFromAxisAngle( axis, angle );
        this.quaternion.Premultiply( q1 );
    }

    public void RotateX(float angle){
        Vector3 v1 = new Vector3( 1, 0, 0 );
        this.RotateOnAxis( v1, angle );
    }

    public void RotateY(float angle){
        Vector3 v1 = new Vector3( 0, 1, 0 );
        this.RotateOnAxis( v1, angle );
    }

    public void RotateZ(float angle){
        Vector3 v1 = new Vector3( 0, 0, 1 );
        this.RotateOnAxis( v1, angle );
    }

    public void TranslateOnAxis(Vector3 axis, float distance){
        // translate object by distance along axis in object space
        // axis is assumed to be normalized
        Vector3 v1 = new Vector3();
        v1.Copy( axis ).ApplyQuaternion( this.quaternion );
        this.position.Add( v1.MultiplyScalar( distance ) );
    }

    public void TranslateX(float distance){
        Vector3 v1 = new Vector3( 1, 0, 0 );
        this.TranslateOnAxis( v1, distance );
    }

    public void TranslateY(float distance){
        Vector3 v1 = new Vector3( 0, 1, 0 );
        this.TranslateOnAxis( v1, distance );
    }

    public void TranslateZ(float distance){
        Vector3 v1 = new Vector3( 0, 0, 1 );
        this.TranslateOnAxis( v1, distance );
    }


    public Vector3 LocalToWorld(Vector3 vector){
        return vector.ApplyMatrix4( this.matrixWorld );
    }

    public Vector3 WorldToLocal(Vector3 vector){
        Matrix4 m1 = new Matrix4();
        return vector.ApplyMatrix4( m1.GetInverse( this.matrixWorld ) );
    }

    public void LookAt(Vector3 v){
        // This method does not support objects having non-uniformly-scaled parent(s)
        Quaternion q1 = new Quaternion();
        Matrix4 m1 = new Matrix4();
        Vector3 target = new Vector3();
        Vector3 position = new Vector3();

        target.Copy( v );
        Object3D parent = this.parent;
        this.UpdateWorldMatrix( true, false );
        position.SetFromMatrixPosition( this.matrixWorld );

        if ( this instanceof Camera || this instanceof Light) {
            m1.LookAt( position, target, this.up );
        } else {
            m1.LookAt( target, position, this.up );
        }

        this.quaternion.SetFromRotationMatrix( m1 );

        if ( parent != null ) {
            m1.ExtractRotation( parent.matrixWorld );
            q1.SetFromRotationMatrix( m1 );
            this.quaternion.Premultiply( q1.Inverse() );
        }
    }

    public void Add(Object3D object){
        if(object.equals(this)){
            return;
        }

        if ( object.parent != null ) {
            object.parent.Remove( object );
        }

        object.parent = this;
        this.children.add( object );
    }

    public void Remove(Object3D object){
        boolean removed = this.children.remove(object);
        if ( removed ) {
            object.parent = null;
        }
    }

    public Object3D GetObjectById(int id){
        if ( this.id == id ) return this;
        for ( int i = 0, l = this.children.size(); i < l; i ++ ) {
            Object3D child = this.children.get(i);
            Object3D object = child.GetObjectById(id);
            if ( object != null ) {
                return object;
            }
        }

        return null;
    }

    public Object3D GetObjectByName(String name){
        if ( this.name == name ) return this;
        for ( int i = 0, l = this.children.size(); i < l; i ++ ) {
            Object3D child = this.children.get(i);
            Object3D object = child.GetObjectByName(name);
            if ( object != null ) {
                return object;
            }
        }

        return null;
    }

    public Vector3 GetWorldPosition(Vector3 target){
        this.UpdateMatrixWorld(true);
        return target.SetFromMatrixPosition( this.matrixWorld );
    }

    public Quaternion GetWorldQuaternion(Quaternion target){
        Vector3 position = new Vector3();
        Vector3 scale = new Vector3();

        this.UpdateMatrixWorld( true );
        this.matrixWorld.Decompose( position, target, scale );
        return target;
    }

    public Vector3 GetWorldScale(Vector3 target){
        Vector3 position = new Vector3();
        Quaternion quaternion = new Quaternion();

        this.UpdateMatrixWorld( true );
        this.matrixWorld.Decompose( position, quaternion, target );
        return target;
    }

    public Vector3 GetWorldDirection(Vector3 target){
        this.UpdateMatrixWorld( true );
        float[] e = this.matrixWorld.elements;
        return target.Set( e[ 8 ], e[ 9 ], e[ 10 ] ).Normalize();
    }

    public void Raycast(Raycaster raycaster, ArrayList<Intersect> intersects){}

    public void Traverse(){
        // not implemented
    }

    public void UpdateMatrix(){
        this.matrix.Compose( this.position, this.quaternion, this.scale );
        this.matrixWorldNeedsUpdate = true;
    }

    public void UpdateMatrixWorld(boolean force){
        if ( this.matrixAutoUpdate ) this.UpdateMatrix();

        if ( this.matrixWorldNeedsUpdate || force ) {
            if ( this.parent == null ) {
                this.matrixWorld.Copy( this.matrix );
            } else {
                this.matrixWorld.MultiplyMatrices( this.parent.matrixWorld, this.matrix );
            }

            this.matrixWorldNeedsUpdate = false;
            force = true;
        }

        // update children
        ArrayList<Object3D> children = this.children;
        for ( int i = 0, l = children.size(); i < l; i ++ ) {
            children.get(i).UpdateMatrixWorld( force );
        }
    }

    public void UpdateWorldMatrix(boolean updateParents, boolean updateChildren){
        Object3D parent = this.parent;

        if ( updateParents == true && parent != null ) {
            parent.UpdateWorldMatrix( true, false );
        }

        if ( this.matrixAutoUpdate ) this.UpdateMatrix();

        if ( this.parent == null ) {
            this.matrixWorld.Copy( this.matrix );
        } else {
            this.matrixWorld.MultiplyMatrices( this.parent.matrixWorld, this.matrix );
        }

        // update children
        if ( updateChildren == true ) {
            ArrayList<Object3D> children = this.children;
            for ( int i = 0, l = children.size(); i < l; i ++ ) {
                children.get(i).UpdateWorldMatrix( false, true );
            }
        }
    }

    public Object3D Clone(boolean recursive){
        return new Object3D().Copy( this, recursive );
    }

    public Object3D Copy(Object3D source, boolean recursive){
        this.name = source.name;
        this.up.Copy( source.up );

        this.position.Copy( source.position );
        this.quaternion.Copy( source.quaternion );
        this.scale.Copy( source.scale );

        this.matrix.Copy( source.matrix );
        this.matrixWorld.Copy( source.matrixWorld );

        this.matrixAutoUpdate = source.matrixAutoUpdate;
        this.matrixWorldNeedsUpdate = source.matrixWorldNeedsUpdate;

        this.layers.mask = source.layers.mask;
        this.visible = source.visible;

        this.castShadow = source.castShadow;
        this.receiveShadow = source.receiveShadow;

        this.frustumCulled = source.frustumCulled;
        this.renderOrder = source.renderOrder;

        if ( recursive == true ) {
            for ( int i = 0; i < source.children.size(); i ++ ) {
                Object3D child = source.children.get(i);
                this.Add( child.Clone(true) );
            }
        }

        return this;
    }

}
