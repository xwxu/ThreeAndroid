package three.cameras;

import three.core.Object3D;
import three.math.Matrix4;
import three.math.Vector3;

public class Camera extends Object3D {
    public Matrix4 matrixWorldInverse;
    public Matrix4 projectionMatrix;
    public Matrix4 projectionMatrixInverse;

    public Camera(){
        super();
        this.type = "Camera";
        this.matrixWorldInverse = new Matrix4();
        this.projectionMatrix = new Matrix4();
        this.projectionMatrixInverse = new Matrix4();
    }

    public Camera copy(Camera source){
        super.copy(source, true);
        this.matrixWorldInverse.copy( source.matrixWorldInverse );
        this.projectionMatrix.copy( source.projectionMatrix );
        this.projectionMatrixInverse.copy( source.projectionMatrixInverse );
        return this;
    }

    public Vector3 getWorldDirection(Vector3 target){
        this.updateMatrixWorld( true );

        float[] e = this.matrixWorld.elements;

        return target.set( - e[ 8 ], - e[ 9 ], - e[ 10 ] ).normalize();
    }

    public void updateMatrixWorld(boolean force){
        super.updateMatrixWorld(force);
        this.matrixWorldInverse.getInverse(this.matrixWorld);
    }

    public Camera clone(){
        return new Camera().copy(this);
    }
}
