package three.renderers.uniforms;

import android.opengl.GLES20;

import java.util.ArrayList;

import three.math.Matrix3;
import three.math.Matrix4;
import three.math.Vector2;
import three.math.Vector3;
import three.math.Vector4;
import three.renderers.GLRenderer;
import three.textures.CubeTexture;
import three.textures.Texture;
import three.util.ActiveUniformInfo;

public class PureArrayUniform extends AbstractUniform{

    public int addr;
    public ArrayList cache = new ArrayList();
    public int size;
    private ActiveUniformInfo activeInfo;

    public PureArrayUniform(String id, ActiveUniformInfo activeInfo, int addr){
        super(id);
        this.addr = addr;
        this.activeInfo = activeInfo;
    }

    public void updateCache(float[] data){
    }

    @Override
    public void setValue(Object object, GLRenderer renderer){
        switch (activeInfo.type){
            case GLES20.GL_FLOAT:
                setValue1fv((float[]) object);
                break;
            case GLES20.GL_FLOAT_VEC2:
                setValueV2A((ArrayList<Vector2>) object);
                break;
            case GLES20.GL_FLOAT_VEC3:
                setValueV3A((ArrayList<Vector3>) object);
                break;
            case GLES20.GL_FLOAT_VEC4:
                setValueV4A((ArrayList<Vector4>) object);
                break;
            case GLES20.GL_FLOAT_MAT3:
                setValueM3A((ArrayList<Matrix3>) object);
                break;
            case GLES20.GL_FLOAT_MAT4:
                setValueM4A((ArrayList<Matrix4>) object);
                break;
            case GLES20.GL_SAMPLER_2D:
                setValueT1A((ArrayList<Texture>) object, renderer);
                break;
            case GLES20.GL_SAMPLER_CUBE:
                setValueT6A((ArrayList<CubeTexture>) object, renderer);
                break;
            case GLES20.GL_INT:
            case GLES20.GL_BOOL:
                setValue1iv((int[]) object);
                break;
            case GLES20.GL_INT_VEC2:
            case GLES20.GL_BOOL_VEC2:
                setValue2iv((int[])object);
                break;
            case GLES20.GL_INT_VEC3:
            case GLES20.GL_BOOL_VEC3:
                setValue3iv((int[]) object);
                break;
            case GLES20.GL_INT_VEC4:
            case GLES20.GL_BOOL_VEC4:
                setValue4iv((int[]) object);
                break;

        }

    }

    private void setValueT6A(ArrayList<CubeTexture> object, GLRenderer renderer) {
    }

    private void setValueT1A(ArrayList<Texture> object, GLRenderer renderer) {
    }

    // uniform float v[10]
    public void setValue1fv(float[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform1fv(this.addr, v.length, v, 0);

        UniformContainer.copyArray(cache, v);
    }

    // uniform int v[10]
    public void setValue1iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform1iv(this.addr, v.length, v, 0);

        UniformContainer.copyArray(cache, v);
    }

    // uniform vec2 v[10]
    public void setValueV2A(ArrayList<Vector2> v){
        float[] data = UniformContainer.flattenV2(v, this.size, 2);

        if(UniformContainer.arraysEqual(cache, data)) return;

        GLES20.glUniform2fv(this.addr, v.size(), data, 0);

        this.updateCache(data);
    }

    // uniform vec3 v[10]
    public void setValueV3A(ArrayList<Vector3> v){
        float[] data = UniformContainer.flattenV3(v, this.size, 3);

        if(UniformContainer.arraysEqual(cache, data)) return;

        GLES20.glUniform3fv(this.addr, v.size(), data, 0);

        this.updateCache(data);
    }

    // uniform vec4 v[10]
    public void setValueV4A(ArrayList<Vector4> v){
        float[] data = UniformContainer.flattenV4(v, this.size, 4);

        if(UniformContainer.arraysEqual(cache, data)) return;

        GLES20.glUniform4fv(this.addr, v.size(), data, 0);

        this.updateCache(data);
    }

    // uniform mat3 v[10]
    public void setValueM3A(ArrayList<Matrix3> v){
        float[] data = UniformContainer.flattenM3(v, this.size, 9);

        if(UniformContainer.arraysEqual(cache, data)) return;

        GLES20.glUniformMatrix3fv(this.addr, v.size(), false, data, 0);

        this.updateCache(data);
    }

    // uniform mat4 v[10]
    public void setValueM4A(ArrayList<Matrix4> v){
        float[] data = UniformContainer.flattenM4(v, this.size, 16);

        if(UniformContainer.arraysEqual(cache, data)) return;

        GLES20.glUniformMatrix4fv(this.addr, v.size(), false, data, 0);

        this.updateCache(data);
    }

    // uniform ivec2 v[3]
    public void setValue2iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform2iv(this.addr, v.length/2, v, 0);

        UniformContainer.copyArray(cache, v);
    }

    // uniform ivec3 v[4]
    public void setValue3iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform3iv(this.addr, v.length/3, v, 0);
    }

    // uniform ivec4 v[5]
    public void setValue4iv(int[] v){
        ArrayList cache = this.cache;

        if(UniformContainer.arraysEqual(cache, v)) return;

        GLES20.glUniform4iv(this.addr, v.length/4, v, 0);
    }
}
