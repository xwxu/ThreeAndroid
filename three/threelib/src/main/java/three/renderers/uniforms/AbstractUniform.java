package three.renderers.uniforms;

import three.renderers.GLRenderer;

public abstract class AbstractUniform {
    public String id;

    public AbstractUniform(String id){
        this.id = id;
    }

    public void SetValue(Object value, GLRenderer renderer){}
}
