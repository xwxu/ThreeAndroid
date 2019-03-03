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

    public Camera Copy(Camera source){
        super.Copy(source, true);
        this.matrixWorldInverse.Copy( source.matrixWorldInverse );
        this.projectionMatrix.Copy( source.projectionMatrix );
        this.projectionMatrixInverse.Copy( source.projectionMatrixInverse );
        return this;
    }

    public Vector3 GetWorldDirection(Vector3 target){
        this.UpdateMatrixWorld( true );

        float[] e = this.matrixWorld.elements;

        return target.Set( - e[ 8 ], - e[ 9 ], - e[ 10 ] ).Normalize();
    }

    public void UpdateMatrixWorld(boolean force){
        super.UpdateMatrixWorld(force);
        this.matrixWorldInverse.GetInverse(this.matrixWorld);
    }

    public Camera Clone(){
        return new Camera().Copy(this);
    }
}
