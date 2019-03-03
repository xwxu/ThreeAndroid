package three.renderers.gl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import java.util.Observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;
import three.bufferAttribute.Uint32BufferAttribute;
import three.core.AbstractGeometry;
import three.core.BufferGeometry;
import three.core.Geometry;
import three.core.Object3D;

public class GLGeometries {
    public GLAttributes attributes;
    public GLInfo info;
    public HashMap<Integer, BufferGeometry> geometries;
    public HashMap<Integer, BufferAttribute> wireframeAttributes;
    public Observer<AbstractGeometry> observer;

    public GLGeometries(GLAttributes attributes, GLInfo info){
        this.attributes = attributes;
        this.info = info;
        geometries = new HashMap<>();
        wireframeAttributes = new HashMap<>();

        observer = new Observer<AbstractGeometry>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(AbstractGeometry geometry) {
                OnGeometryDispose(geometry);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

    }

    private void OnGeometryDispose(AbstractGeometry geometry){

        BufferGeometry buffergeometry = geometries.get(geometry.id);

        if ( buffergeometry.index != null ) {
            attributes.Remove( buffergeometry.index );
        }

        for (Object o : buffergeometry.attributes.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            String name = (String) pair.getKey();
            attributes.Remove(buffergeometry.attributes.get(name));
        }

        geometry.subject.onComplete();

        geometries.remove(geometry.id);

        BufferAttribute attribute = wireframeAttributes.get(buffergeometry.id);

        if ( attribute != null) {
            attributes.Remove( attribute );
            wireframeAttributes.remove(buffergeometry.id);
        }

        info.memory.geometries --;
    }


    public BufferGeometry Get(Object3D object, AbstractGeometry geometry) {
        BufferGeometry bufferGeometry = geometries.get(geometry.id);
        if(bufferGeometry != null){
            return bufferGeometry;
        }

        geometry.subject.subscribe(observer);

        if(geometry instanceof BufferGeometry){
            bufferGeometry = (BufferGeometry) geometry;
        }else{
            Geometry geometry1 = (Geometry)geometry;
            if ( geometry1._bufferGeometry == null ) {
                geometry1._bufferGeometry = new BufferGeometry().SetFromObject( object );
            }

            bufferGeometry = geometry1._bufferGeometry;
        }

        geometries.put(geometry.id, bufferGeometry);

        info.memory.geometries ++;
        return bufferGeometry;
    }


    public void Update(BufferGeometry geometry){
        Uint32BufferAttribute index = geometry.index;
        HashMap<String, BufferAttribute> geometryAttributes = geometry.attributes;

        if ( index != null ) {
            attributes.Update( index, GLES20.GL_ELEMENT_ARRAY_BUFFER );
        }

        for (Object o : geometryAttributes.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            String name = (String) pair.getKey();
            BufferAttribute attribute = (BufferAttribute) pair.getValue();

            attributes.Update(attribute, GLES20.GL_ARRAY_BUFFER);
        }

    }

    public Uint32BufferAttribute GetWireframeAttribute(BufferGeometry geometry){
        Uint32BufferAttribute attribute = (Uint32BufferAttribute)wireframeAttributes.get(geometry.id);

        if ( attribute != null ) return attribute;

        int[] indices;

        Uint32BufferAttribute geometryIndex = geometry.index;
        HashMap<String, BufferAttribute> geometryAttributes = geometry.attributes;

        if ( geometryIndex != null ) {
            IntBuffer array = geometryIndex.array;
            indices = new int[array.capacity() * 2];

            for ( int i = 0, l = array.capacity(), j = 0; i < l; i += 3, j += 6 ) {
                int a = array.get(i);
                int b = array.get(i+1);
                int c = array.get(i+2);
                indices[j] = a;
                indices[j + 1] = b;
                indices[j + 2] = b;
                indices[j + 3] = c;
                indices[j + 4] = c;
                indices[j + 5] = a;
            }

        } else {
            FloatBuffer array = ((Float32BufferAttribute) geometryAttributes.get("position")).array;
            indices = new int[(array.capacity() / 3 - 1) * 2];

            for ( int i = 0, l = ( array.capacity() / 3 ) - 1, j = 0; i < l; i += 3, j += 6 ) {
                int a = i;
                int b = i + 1;
                int c = i + 2;
                indices[j] = a;
                indices[j + 1] = b;
                indices[j + 2] = b;
                indices[j + 3] = c;
                indices[j + 4] = c;
                indices[j + 5] = a;
            }

        }
        attribute = new Uint32BufferAttribute(indices, 1);

        attributes.Update( attribute, GLES20.GL_ELEMENT_ARRAY_BUFFER );
        wireframeAttributes.put(geometry.id, attribute);

        return attribute;
    }

}
