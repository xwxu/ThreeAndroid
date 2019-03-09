package three.renderers.uniforms;

import java.util.ArrayList;

import three.renderers.GLRenderer;
import three.renderers.shaders.UniformsObject;

public class StructuredUniform extends UniformContainer {

    public StructuredUniform(String id){
        super(id);
    }

    @Override
    public void SetValue(Object value, GLRenderer renderer){

        ArrayList<AbstractUniform> seq = this.seq;

        for ( int i = 0, n = seq.size(); i != n; ++ i ) {
            AbstractUniform u = seq.get(i);

            if(value instanceof UniformsObject){
                UniformsObject uniforms = (UniformsObject) value;
                Object uniformObj = uniforms.Get(u.id);
                if(uniformObj != null){
                    u.SetValue(uniformObj, renderer);
                }

            }else{
                ArrayList<UniformsObject> uniformList = (ArrayList<UniformsObject>) value;
                int index = Integer.parseInt(u.id);
                u.SetValue( uniformList.get(index), renderer );
            }

        }
    }
}
