package three.lights;

import three.core.Object3D;
import three.math.Color;

public class SpotLight extends Light {

    public String type = "SpotLight";
    public Object3D target = new Object3D();
    public float distance;
    public float angle;
    public float penumbra;
    public float decay;
    public LightShadow shadow = new SpotLightShadow();

    public SpotLight(Color color, float intensity){
        super(color, intensity);
        this.type = "SpotLight";
        this.position.copy(Object3D.DefaultUp);
        this.updateMatrix();
        this.distance = 0;
        this.angle = (float) Math.PI / 3;
        this.penumbra = 0;
        this.decay = 0;
    }

    public SpotLight(Color color, float intensity, float distance, float angle, float penumbra, float decay){
        super(color, intensity);
        this.position.copy(Object3D.DefaultUp);
        this.updateMatrix();
        this.distance = distance;
        this.angle = angle;
        this.penumbra = penumbra;
        this.decay = decay;
    }

    public float power(){
        return (float)(this.intensity * Math.PI);
    }

    public void power(float power){
        this.intensity = power / (float) Math.PI;
    }

    public SpotLight copy(SpotLight source){
        super.copy(source);
        this.distance = source.distance;
        this.angle = source.angle;
        this.penumbra = source.penumbra;
        this.decay = source.decay;
        this.target = source.target.clone_(false);
        this.shadow = source.shadow.clone_();

        return this;
    }
}
