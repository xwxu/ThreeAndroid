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
    public void setValue(Object value, GLRenderer renderer){
        switch(activeInfo.type){
            case GLES20.GL_FLOAT:
                setValue1F((float)value);
                break;
            case GLES20.GL_FLOAT_VEC2:
                setValue2Fv((Vector2)value);
                break;
            case GLES20.GL_FLOAT_VEC3:
                setValue3Fv(value);
                break;
            case GLES20.GL_FLOAT_VEC4:
                setValue4Fv((Vector4)value);
                break;
            case GLES20.GL_FLOAT_MAT3:
                setValue3fm((Matrix3)value);
                break;
            case GLES20.GL_FLOAT_MAT4:
                setValue4fm((Matrix4)value);
                break;
            case GLES20.GL_SAMPLER_2D:
                setValueT1((Texture)value, renderer);
                break;
            case GLES20.GL_SAMPLER_CUBE:
                setValueT6((CubeTexture)value, renderer);
                break;
            case GLES20.GL_INT:
            case GLES20.GL_BOOL:
                setValue1I(value);
                break;
            case GLES20.GL_INT_VEC2:
            case GLES20.GL_BOOL_VEC2:
                setValue2iv((int[])value);
                break;
            case GLES20.GL_INT_VEC3:
            case GLES20.GL_BOOL_VEC3:
                setValue3iv((int[])value);
                break;
            case GLES20.GL_INT_VEC4:
            case GLES20.GL_BOOL_VEC4:
                setValue4iv((int[])value);
                break;

        }
    }

    // uniform samplercube
    private void setValueT6(CubeTexture value, GLRenderer renderer) {
    }

    // uniform sampler2d
    public void setValueT1(Texture value, GLRenderer renderer) {
        ArrayList cache = this.cache;
        int unit = renderer.allocTextureUnit();

        if ( !cache.isEmpty() && (int)cache.get(0) != unit ) {
            GLES20.glUniform1i(this.addr, unit);
            cache.clear();
            cache.add( unit);
        }

        renderer.setTexture2D( value != null ? value : new Texture(), unit );
    }

    // uniform float v;
    public void setValue1F(float v){
        ArrayList cache = this.cache;

        if ( !cache.isEmpty() && (float)cache.get(0) == v ) return;

        GLES20.glUniform1f(this.addr, v);

        cache.clear();
        cache.add( v);
    }

    // uniform int v
    public void setValue1I(Object v){
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
    public void setValue2Fv(Vector2 v){
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
    public void setValue3Fv(Object v){
        ArrayList cache = this.cache;

        if(v instanceof Vector3){
            Vector3 vec = (Vector3)v;

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

                GLES20.glUniform3f(this.addr, color.r, color.g, color.b);

                cache.clear();
                cache.add(color.r);
                cache.add(color.g);
                cache.add(color.b);
            }

        }else{
            float[] floats = (float[])v;
            GLES20.glUniform3fv(this.addr, 1, floats, 0);
            UniformContainer.copyArray(cache, floats);
        }

    }

    // uniform vec4 v
    public void setValue4Fv(Vector4 v){
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
    public void setValue3fm(Matrix3 v){
        ArrayList cache = this.cache;
        float[] elements = v.elements;

        if ( UniformContainer.arraysEqual(cache, elements) ) return;

        ByteBuffer bb = ByteBuffer.allocateDirect(9 * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer mat3array = bb.asFloatBuffer();
        mat3array.put(elements);
        mat3array.position(0);

        GLES20.glUniformMatrix3fv(this.addr, 1, false, mat3array);

        UniformContainer.copyArray( cache, elements );
    }

    //  uniform mat4 v
    public void setValue4fm(Matrix4 v){
        ArrayList cache = this.cache;
        float[] elements = v.elements;

        if ( UniformContainer.arraysEqual(cache, elements) ) return;

        ByteBuffer bb = ByteBuffer.allocateDirect(16 * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer mat4array = bb.asFloatBuffer();
        mat4array.put(elements);
        mat4array.position(0);

        GLES20.glUniformMatrix4fv(this.addr, 1, false, mat4array);

        UniformContainer.copyArray( cache, elements );
    }

    // uniform ivec2
    public void setValue2iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform2iv(this.addr, 1, v, 0);

        UniformContainer.copyArray(cache, v);
    }

    // uniform ivec3
    public void setValue3iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform3iv(this.addr, 1, v, 0);
    }

    // uniform ivec4
    public void setValue4iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform4iv(this.addr, 1, v, 0);
    }

}
