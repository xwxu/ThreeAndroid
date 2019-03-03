package three.renderers.gl;

import java.util.HashMap;

import three.core.AbstractGeometry;
import three.core.BufferGeometry;
import three.core.Geometry;
import three.core.Object3D;

public class GLObjects {

    private GLGeometries geometries;
    private GLInfo info;
    private HashMap<Integer, Integer> updateList = new HashMap<>();

    public GLObjects(GLGeometries geometries, GLInfo info){
        this.geometries = geometries;
        this.info = info;
    }

    public BufferGeometry Update(Object3D object){
        int frame = info.render.frame;
        AbstractGeometry geometry = object.geometry;
        BufferGeometry buffergeometry =  geometries.Get(object, geometry);

        // Update once per frame

        if ( !updateList.containsKey(buffergeometry.id) ||
                updateList.get(buffergeometry.id) != frame ) {
            if ( geometry instanceof Geometry ) {
                buffergeometry.UpdateFromObject( object );
            }

            geometries.Update( buffergeometry );
            updateList.put(buffergeometry.id, frame);
        }

        return buffergeometry;
    }

    public void Dispose(){
        updateList.clear();
    }
}
