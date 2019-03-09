package three.renderers.uniforms;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import three.math.Color;
import three.math.Matrix3;
import three.math.Matrix4;
import three.math.Vector2;
import three.math.Vector3;
import three.math.Vector4;
import three.renderers.GLRenderer;
import three.textures.CubeTexture;
import three.textures.Texture;
import three.util.ActiveUniformInfo;

public class SingleUniform extends AbstractUniform{

    public ArrayList cache = new ArrayList();
    private int addr;
    private ActiveUniformInfo activeInfo;

    public SingleUniform(String id, ActiveUniformInfo activeInfo, int addr){
        super(id);
        this.addr = addr;
        this.activeInfo = activeInfo;
    }

    @Override
    public void SetValue(Object value, GLRenderer renderer){
        switch(activeInfo.type){
            case GLES20.GL_FLOAT:
                SetValue1f((float)value);
                break;
            case GLES20.GL_FLOAT_VEC2:
                SetValue2fv((Vector2)value);
                break;
            case GLES20.GL_FLOAT_VEC3:
                SetValue3fv(value);
                break;
            case GLES20.GL_FLOAT_VEC4:
                SetValue4fv((Vector4)value);
                break;
            case GLES20.GL_FLOAT_MAT3:
                SetValue3fm((Matrix3)value);
                break;
            case GLES20.GL_FLOAT_MAT4:
                SetValue4fm((Matrix4)value);
                break;
            case GLES20.GL_SAMPLER_2D:
                SetValueT1((Texture)value, renderer);
                break;
            case GLES20.GL_SAMPLER_CUBE:
                SetValueT6((CubeTexture)value, renderer);
                break;
            case GLES20.GL_INT:
            case GLES20.GL_BOOL:
                SetValue1i(value);
                break;
            case GLES20.GL_INT_VEC2:
            case GLES20.GL_BOOL_VEC2:
                SetValue2iv((int[])value);
                break;
            case GLES20.GL_INT_VEC3:
            case GLES20.GL_BOOL_VEC3:
                SetValue3iv((int[])value);
                break;
            case GLES20.GL_INT_VEC4:
            case GLES20.GL_BOOL_VEC4:
                SetValue4iv((int[])value);
                break;

        }
    }

    // uniform samplercube
    private void SetValueT6(CubeTexture value, GLRenderer renderer) {
    }

    // uniform sampler2d
    public void SetValueT1(Texture value, GLRenderer renderer) {
        ArrayList cache = this.cache;
        int unit = renderer.AllocTextureUnit();

        if ( !cache.isEmpty() && (int)cache.get(0) != unit ) {
            GLES20.glUniform1i(this.addr, unit);
            cache.clear();
            cache.add( unit);
        }

        renderer.SetTexture2D( value != null ? value : new Texture(), unit );
    }

    // uniform float v;
    public void SetValue1f(float v){
        ArrayList cache = this.cache;

        if ( !cache.isEmpty() && (float)cache.get(0) == v ) return;

        GLES20.glUniform1f(this.addr, v);

        cache.clear();
        cache.add( v);
    }

    // uniform int v
    public void SetValue1i(Object v){
        ArrayList cache = this.cache;

        if ( !cache.isEmpty() && cache.get(0) == v ) return;

        if(v instanceof Boolean){
            int value = (boolean)v ? 1 : 0;
            GLES20.glUniform1i(this.addr, value);
        }else {
            GLES20.glUniform1i(this.addr, (int)v);
        }

        cache.clear();
        cache.add( v);
    }

    // uniform vec2 v
    public void SetValue2fv(Vector2 v){
        ArrayList cache = this.cache;

        if ( cache.isEmpty() ||
            (cache.size() == 2 && ((float)cache.get(0) != v.x || (float)cache.get(1) != v.y ))) {
            GLES20.glUniform2f(this.addr, v.x, v.y);

            cache.clear();
            cache.add( v.x);
            cache.add( v.y);
        }
    }

    // uniform vec3 v
    public void SetValue3fv(Object v){
        ArrayList cache = this.cache;

        if(v instanceof Vector3){
            Vector3 vec = (Vector3)v;
//            float[] array = new float[3];
//            vec.ToArray(array, 0);
//            GLES20.glUniform3fv(this.addr, 1, array, 0);
//            UniformContainer.CopyArray(cache, array);

            if ( cache.isEmpty() ||
                ( cache.size() == 3 && ((float)cache.get(0) != vec.x || (float)cache.get(1) != vec.y || (float)cache.get(2) != vec.z ))) {
                GLES20.glUniform3f(this.addr, vec.x, vec.y, vec.z);

                cache.clear();
                cache.add(vec.x);
                cache.add(vec.y);
                cache.add(vec.z);
            }

        }else if(v instanceof Color){
            Color color = (Color)v;
            if ( cache.isEmpty() ||
                ( cache.size() == 3 && ((float)cache.get(0) != color.r || (float)cache.get(1) != color.g || (float)cache.get(2) != color.b ))) {

                //GLES20.glUniform3f(this.addr, color.r, color.g, color.b);
                float[] floats = new float[3];
                floats[0] = color.r;
                floats[1] = color.g;
                floats[2] = color.b;
                GLES20.glUniform3fv(this.addr, 1, floats, 0);

                cache.clear();
                cache.add(color.r);
                cache.add(color.g);
                cache.add(color.b);
            }

        }else{
            float[] floats = (float[])v;
            GLES20.glUniform3fv(this.addr, 1, floats, 0);
            UniformContainer.CopyArray(cache, floats);
        }

    }

    // uniform vec4 v
    public void SetValue4fv(Vector4 v){
        ArrayList cache = this.cache;

        if ( cache.isEmpty() ||
                ( cache.size() == 4 &&
                    ((float)cache.get(0) != v.x || (float)cache.get(1) != v.y
                || (float)cache.get(2) != v.z || (float)cache.get(3) != v.w))) {
            GLES20.glUniform4f(this.addr, v.x, v.y, v.z, v.w);

            cache.clear();
            cache.add( v.x);
            cache.add( v.y);
            cache.add( v.z);
            cache.add( v.w);
        }
    }

    // uniform mat3 v
    public void SetValue3fm(Matrix3 v){
        ArrayList cache = this.cache;
        float[] elements = v.elements;

        if ( UniformContainer.ArraysEqual(cache, elements) ) return;

        ByteBuffer bb = ByteBuffer.allocateDirect(9 * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer mat3array = bb.asFloatBuffer();
        mat3array.put(elements);
        mat3array.position(0);

        GLES20.glUniformMatrix3fv(this.addr, 1, false, mat3array);

        UniformContainer.CopyArray( cache, elements );
    }

    //  uniform mat4 v
    public void SetValue4fm(Matrix4 v){
        ArrayList cache = this.cache;
        float[] elements = v.elements;

        if ( UniformContainer.ArraysEqual(cache, elements) ) return;

        ByteBuffer bb = ByteBuffer.allocateDirect(16 * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer mat4array = bb.asFloatBuffer();
        mat4array.put(elements);
        mat4array.position(0);

        GLES20.glUniformMatrix4fv(this.addr, 1, false, mat4array);

        UniformContainer.CopyArray( cache, elements );
    }

    // uniform ivec2
    public void SetValue2iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.ArraysEqual(cache, v)) return;

        GLES20.glUniform2iv(this.addr, 1, v, 0);

        UniformContainer.CopyArray(cache, v);
    }

    // uniform ivec3
    public void SetValue3iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.ArraysEqual(cache, v)) return;

        GLES20.glUniform3iv(this.addr, 1, v, 0);
    }

    // uniform ivec4
    public void SetValue4iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.ArraysEqual(cache, v)) return;

        GLES20.glUniform4iv(this.addr, 1, v, 0);
    }

}
