package three.lights;

import three.core.Object3D;
import three.math.Color;

public class DirectionalLight extends Light {

    public Object3D target = new Object3D();
    public LightShadow shadow = new DirectionalLightShadow();

    public DirectionalLight(Color color, float intensity){
        super(color, intensity);
        this.type = "DirectionalLight";
        this.position.copy(Object3D.DefaultUp);
        this.updateMatrix();

    }

    public DirectionalLight copy(DirectionalLight source){
        super.copy(source);
        this.type = "DirectionalLight";
        this.target = source.target.clone_(false);
        this.shadow = source.shadow.clone_();

        return this;
    }
}
