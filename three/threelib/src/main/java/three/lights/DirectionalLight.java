package three.lights;

import three.core.Object3D;
import three.math.Color;
import three.math.Vector3;

public class DirectionalLight extends Light {

    public Object3D target = new Object3D();
    public LightShadow shadow = new DirectionalLightShadow();

    public DirectionalLight(Color color, float intensity){
        super(color, intensity);
        this.type = "DirectionalLight";
        this.position.Copy(Object3D.DefaultUp);
        this.UpdateMatrix();

    }

    public DirectionalLight Copy(DirectionalLight source){
        super.Copy(source);
        this.type = "DirectionalLight";
        this.target = source.target.Clone(false);
        this.shadow = source.shadow.Clone();

        return this;
    }
}
