package three.renderers.gl;

import android.opengl.GLES20;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import three.math.Matrix3;
import three.math.Matrix4;
import three.math.Vector2;
import three.renderers.GLRenderer;
import three.renderers.shaders.UniformState;
import three.renderers.shaders.UniformsObject;
import three.renderers.uniforms.AbstractUniform;
import three.renderers.uniforms.PureArrayUniform;
import three.renderers.uniforms.SingleUniform;
import three.renderers.uniforms.StructuredUniform;
import three.renderers.uniforms.UniformContainer;
import three.util.ActiveInfo;

public class GLUniforms extends UniformContainer {

    private GLRenderer renderer;
    public GLUniforms(int program, GLRenderer renderer){
        super("");
        this.renderer = renderer;

        final int[] n = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_ACTIVE_UNIFORMS, n, 0);

        int[] sizeBuf = new int[1];
        int[] typeBuf = new int[1];
        byte[] nameBuf = new byte[30];
        for ( int i = 0; i < n[0]; ++ i ) {
            GLES20.glGetActiveUniform( program, i, 30, null, 0,  sizeBuf, 0, typeBuf, 0, nameBuf, 0 );

            byte[] trimed = GLUtils.TrimZero(nameBuf);
            String name = new String(trimed);
            int addr = GLES20.glGetUniformLocation(program, name);
            ActiveInfo activeInfo = new ActiveInfo();
            activeInfo.name = name;
            activeInfo.type = typeBuf[0];
            activeInfo.size = sizeBuf[0];
            ParseUniform( activeInfo, addr, this );

        }
    }



    /*
    *  single: normalMatrix
    *  pureArray: ddg[1]
    *  structure: lights[0].position
    * */
    public void ParseUniform(ActiveInfo activeInfo, int addr, UniformContainer container){
        String path = activeInfo.name;

        while (true){
            int leftBracket = path.indexOf('[');
            int rightBracket = path.indexOf(']');
            int dot = path.indexOf('.');

            if(leftBracket < 0 ){ // single
                AddUniform(container, new SingleUniform(path, activeInfo, addr));
                break;

            }else if(leftBracket == 0){ // [1].xx
                if(dot > 0){
                    String id = path.substring(leftBracket+1, rightBracket);
                    UniformContainer next = new StructuredUniform(id);
                    AddUniform(container, next);
                    container = next;
                    path = path.substring(rightBracket+2);
                }

            }else{

                if(dot < 0){ // pure array
                    String id = path.substring(0, leftBracket);
                    AddUniform(container, new PureArrayUniform(id, activeInfo, addr));
                    break;

                }else{
                    String id = path.substring(0, leftBracket);
                    UniformContainer next = new StructuredUniform(id);
                    AddUniform(container, next);
                    container = next;
                    path = path.substring(leftBracket);
                }

            }
        }
    }

    private void AddUniform(UniformContainer container, AbstractUniform uniformObject){
        container.seq.add(uniformObject);
        container.map.put(uniformObject.id, uniformObject);
    }

    public static ArrayList<AbstractUniform> SeqWithValue(ArrayList<AbstractUniform> seq, UniformsObject values) {
        ArrayList<AbstractUniform> r = new ArrayList();

        for ( int i = 0, n = seq.size(); i != n; ++ i ) {

            AbstractUniform u = seq.get(i);
            if ( values.uniforms.containsKey(u.id) ){
                r.add( u );
            }

        }

        return r;
    }


    public void SetValue(){

    }

    public void SetValue1f(String name, float value){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.SetValue1f(value);
        }
    }

    public void SetValue2fv(String name, Vector2 vector){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.SetValue2fv(vector);
        }
    }

    public void SetValue3fm(String name, Matrix3 matrix){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.SetValue3fm(matrix);
        }
    }

    public void SetValue4fm(String name, Matrix4 matrix){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.SetValue4fm(matrix);
        }
    }

    public void SetOptional(){

    }

    public static void Upload(ArrayList<AbstractUniform> seq, UniformsObject values, GLRenderer renderer){
        for ( int i = 0, n = seq.size(); i != n; ++ i ) {

            AbstractUniform u = seq.get(i);
            UniformState v = values.uniforms.get(u.id);

            if ( v.needsUpdate != false ) {
                u.SetValue( v.value, renderer );
            }
        }
    }

}
