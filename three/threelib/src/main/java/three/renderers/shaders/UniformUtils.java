package three.renderers.shaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import three.math.Color;
import three.math.Matrix3;
import three.math.Matrix4;
import three.math.Vector2;
import three.math.Vector3;
import three.math.Vector4;
import three.textures.Texture;

public class UniformUtils {

    public static UniformsObject MergeUniforms(ArrayList<UniformsObject> uniforms){
        UniformsObject merged = new UniformsObject();

        for ( int u = 0; u < uniforms.size(); u ++ ) {
            UniformsObject tmp = CloneUniforms( uniforms.get(u) );

            Iterator iterator = tmp.uniforms.entrySet().iterator();
            while (iterator.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) iterator.next();

                String name = (String) pair.getKey();
                UniformState uniformState = (UniformState) pair.getValue();

                merged.uniforms.put(name, uniformState);
            }

        }

        return merged;
    }

    public static UniformsObject CloneUniforms(UniformsObject src){
        UniformsObject dst = new UniformsObject();

        Iterator iterator = src.uniforms.entrySet().iterator();
        while (iterator.hasNext()){
            HashMap.Entry pair = (HashMap.Entry)iterator.next();
            String name = (String) pair.getKey();
            UniformState uniformState = (UniformState) pair.getValue();

            UniformState newUniform = new UniformState();
            if(uniformState.value instanceof Color){
                newUniform.value = ((Color)uniformState.value).Clone();

            }else if(uniformState.value instanceof Matrix3 ){
                newUniform.value = ((Matrix3)uniformState.value).Clone();

            }else if(uniformState.value instanceof Matrix4 ){
                newUniform.value = ((Matrix4)uniformState.value).Clone();

            }else if(uniformState.value instanceof Vector2 ){
                newUniform.value = ((Vector2)uniformState.value).Clone();

            }else if(uniformState.value instanceof Vector3 ){
                newUniform.value = ((Vector3)uniformState.value).Clone();

            }else if(uniformState.value instanceof Vector4 ){
                newUniform.value = ((Vector4)uniformState.value).Clone();

            }else if(uniformState.value instanceof Texture ){
                newUniform.value = ((Texture)uniformState.value).Clone();
            }else{
                newUniform.value = uniformState.value;
            }

            newUniform.needsUpdate = uniformState.needsUpdate;

            dst.uniforms.put(name, newUniform);
        }

        return dst;
    }
}
