package three.lights;

import three.core.Object3D;
import three.math.Color;
import three.math.Math_;

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
        this.position.Copy(Object3D.DefaultUp);
        this.UpdateMatrix();
        this.distance = 0;
        this.angle = (float) Math.PI / 3;
        this.penumbra = 0;
        this.decay = 0;
    }

    public SpotLight(Color color, float intensity, float distance, float angle, float penumbra, float decay){
        super(color, intensity);
        this.position.Copy(Object3D.DefaultUp);
        this.UpdateMatrix();
        this.distance = distance;
        this.angle = angle;
        this.penumbra = penumbra;
        this.decay = decay;
    }

    public float Power(){
        return (float)(this.intensity * Math.PI);
    }

    public void Power(float power){
        this.intensity = power / (float) Math.PI;
    }

    public SpotLight Copy(SpotLight source){
        super.Copy(source);
        this.distance = source.distance;
        this.angle = source.angle;
        this.penumbra = source.penumbra;
        this.decay = source.decay;

        this.target = source.target.Clone(false);

        this.shadow = source.shadow.Clone();

        return this;
    }
}
