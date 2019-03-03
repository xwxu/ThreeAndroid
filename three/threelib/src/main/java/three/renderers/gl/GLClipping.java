package three.renderers.gl;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import three.cameras.Camera;
import three.math.Matrix3;
import three.math.Matrix4;
import three.math.Plane;
import three.renderers.shaders.UniformState;

public class GLClipping {
    FloatBuffer globalState;
    int numGlobalPlanes = 0;
    boolean localClippingEnabled = false;
    boolean renderingShadows = false;
    Plane plane = new Plane();
    Matrix3 viewNormalMatrix = new Matrix3();

    public UniformState uniform = new UniformState();
    public int numPlanes = 0;
    public int numIntersection = 0;

    public GLClipping(){
        uniform.value = null;
        uniform.needsUpdate = false;
    }

    public boolean Init(ArrayList<Plane> planes, boolean enableLocalClipping, Camera camera){
        boolean enabled = planes.size() != 0 ||
                        enableLocalClipping ||
                        numGlobalPlanes != 0 ||
                        localClippingEnabled;

        localClippingEnabled = enableLocalClipping;

        globalState = ProjectPlanes( planes, camera, 0, false );
        numGlobalPlanes = planes.size();

        return enabled;
    }

    public void BeginShadows(){
        renderingShadows = true;
        ProjectPlanes(null, null, 0, false);
    }

    public void EndShadows(){
        renderingShadows = false;
        ResetGlobalState();
    }

    // cache..
    public void SetState(ArrayList<Plane> planes, boolean clipIntersection, boolean clipShadows,
                         Camera camera, FloatBuffer cacheClippingState, boolean fromCache){
        if ( ! localClippingEnabled || planes == null || planes.size() == 0 || renderingShadows && ! clipShadows ) {

            // there's no local clipping
            if ( renderingShadows ) {
                // there's no global clipping
                ProjectPlanes( null, null, 0, false );
            } else {
                ResetGlobalState();
            }

        } else {
            int nGlobal = renderingShadows ? 0 : numGlobalPlanes,
                    lGlobal = nGlobal * 4;
            FloatBuffer dstArray = cacheClippingState;

            uniform.value = dstArray; // ensure unique state
            dstArray = ProjectPlanes( planes, camera, lGlobal, fromCache );

            for ( int i = 0; i != lGlobal; ++ i ) {
                dstArray.put(i, globalState.get(i));
            }

            cacheClippingState = dstArray;
            this.numIntersection = clipIntersection ? this.numPlanes : 0;
            this.numPlanes += nGlobal;

        }
    }

    private void ResetGlobalState() {
    }

    private FloatBuffer ProjectPlanes(ArrayList<Plane> planes, Camera camera, int dstOffset, boolean skipTransform) {
        int nPlanes = planes != null ? planes.size() : 0;
        FloatBuffer dstArray = null;

        if ( nPlanes != 0 ) {
            dstArray = (FloatBuffer) uniform.value;
            if ( skipTransform != true || dstArray == null ) {
                int flatSize = dstOffset + nPlanes * 4;
                Matrix4 viewMatrix = camera.matrixWorldInverse;

                viewNormalMatrix.GetNormalMatrix( viewMatrix );

                if ( dstArray == null || dstArray.capacity() < flatSize ) {
                    dstArray = FloatBuffer.allocate(flatSize);
                }

                for ( int i = 0, i4 = dstOffset; i != nPlanes; ++ i, i4 += 4 ) {
                    plane.Copy( planes.get(i) ).ApplyMatrix4( viewMatrix, viewNormalMatrix );
                    plane.normal.ToArray( dstArray.array(), i4 );
                    dstArray.put(i4 + 3, plane.constant);
                }
            }

            uniform.value = dstArray;
            uniform.needsUpdate = true;
        }

        numPlanes = nPlanes;
        return dstArray;
    }

}
