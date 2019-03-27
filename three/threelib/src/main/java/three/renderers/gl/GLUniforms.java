package three.renderers.gl;

import android.opengl.GLES20;

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
import three.util.ActiveUniformInfo;

public class GLUniforms extends UniformContainer {

    private GLRenderer renderer;
    public GLUniforms(int program, GLRenderer renderer){
        super("");
        this.renderer = renderer;

        final int[] n = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_ACTIVE_UNIFORMS, n, 0);

        int[] sizeBuf = new int[1];
        int[] typeBuf = new int[1];
        // NOTE: allocate enough space for name
        byte[] nameBuf = new byte[40];
        for ( int i = 0; i < n[0]; ++ i ) {
            GLES20.glGetActiveUniform( program, i, 40, null, 0,  sizeBuf, 0, typeBuf, 0, nameBuf, 0 );

            byte[] trimed = GLUtils.trimZero(nameBuf);
            String name = new String(trimed);
            int addr = GLES20.glGetUniformLocation(program, name);
            ActiveUniformInfo activeInfo = new ActiveUniformInfo();
            activeInfo.name = name;
            activeInfo.type = typeBuf[0];
            activeInfo.size = sizeBuf[0];
            parseUniform( activeInfo, addr, this );

        }
    }


    /*
    *  single: normalMatrix
    *  pureArray: ddg[1]
    *  structure: lights[0].position
    * */
    public void parseUniform(ActiveUniformInfo activeInfo, int addr, UniformContainer container){
        String path = activeInfo.name;

        while (true){
            int leftBracket = path.indexOf('[');
            int rightBracket = path.indexOf(']');
            int dot = path.indexOf('.');

            if(leftBracket < 0 ){ // single
                addUniform(container, new SingleUniform(path, activeInfo, addr));
                break;

            }else if(leftBracket == 0){ // [1].xx
                if(dot > 0){
                    String id = path.substring(leftBracket+1, rightBracket);
                    UniformContainer next;
                    if(container.map.containsKey(id)){
                        next = (UniformContainer) container.map.get(id);
                    }else{
                        next = new StructuredUniform(id);
                        addUniform(container, next);
                    }

                    container = next;
                    path = path.substring(rightBracket+2);
                }

            }else{

                if(dot < 0){ // pure array
                    String id = path.substring(0, leftBracket);
                    addUniform(container, new PureArrayUniform(id, activeInfo, addr));
                    break;

                }else{
                    String id = path.substring(0, leftBracket);

                    UniformContainer next;
                    if(container.map.containsKey(id)){
                        next = (UniformContainer) container.map.get(id);
                    }else{
                        next = new StructuredUniform(id);
                        addUniform(container, next);
                    }

                    container = next;
                    path = path.substring(leftBracket);
                }

            }
        }
    }

    private void addUniform(UniformContainer container, AbstractUniform uniformObject){
        container.seq.add(uniformObject);
        container.map.put(uniformObject.id, uniformObject);
    }

    public static ArrayList<AbstractUniform> seqWithValue(ArrayList<AbstractUniform> seq, UniformsObject values) {
        ArrayList<AbstractUniform> r = new ArrayList();

        for ( int i = 0, n = seq.size(); i != n; ++ i ) {

            AbstractUniform u = seq.get(i);
            if ( values.uniforms.containsKey(u.id) ){
                r.add( u );
            }

        }

        return r;
    }


    public void setValue(){

    }

    public void setValue1F(String name, float value){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.setValue1F(value);
        }
    }

    public void setValue2Fv(String name, Vector2 vector){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.setValue2Fv(vector);
        }
    }

    public void setValue3Fm(String name, Matrix3 matrix){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.setValue3fm(matrix);
        }
    }

    public void setValue4Fm(String name, Matrix4 matrix){
        SingleUniform u = (SingleUniform) this.map.get(name);
        if(u != null){
            u.setValue4fm(matrix);
        }
    }

    public void setOptional(){

    }

    public static void upload(ArrayList<AbstractUniform> seq, UniformsObject values, GLRenderer renderer){
        for ( int i = 0, n = seq.size(); i != n; ++ i ) {

            AbstractUniform u = seq.get(i);
            UniformState v = values.uniforms.get(u.id);

            if ( v.needsUpdate != false ) {
                u.setValue( v.value, renderer );
            }
        }
    }

}
