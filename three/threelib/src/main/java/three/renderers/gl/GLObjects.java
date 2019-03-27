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

    public BufferGeometry update(Object3D object){
        int frame = info.render.frame;
        AbstractGeometry geometry = object.geometry;
        BufferGeometry buffergeometry =  geometries.get(object, geometry);

        // update once per frame
        if ( !updateList.containsKey(buffergeometry.id) ||
                updateList.get(buffergeometry.id) != frame ) {
            if ( geometry instanceof Geometry ) {
                buffergeometry.updateFromObject( object );
            }

            geometries.update( buffergeometry );
            updateList.put(buffergeometry.id, frame);
        }

        return buffergeometry;
    }

    public void dispose(){
        updateList.clear();
    }
}
