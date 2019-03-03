package three.lights;

import three.cameras.Camera;
import three.math.Matrix4;
import three.math.Vector2;
import three.textures.Texture;

public class LightShadow {

    public Camera camera;
    public float bias = 0;
    public float radius = 1;
    public Vector2 mapSize = new Vector2(512, 512);
    public Texture map = null;
    public Matrix4 matrix = new Matrix4();

    public LightShadow(Camera camera){
        this.camera = camera;
    }

    public LightShadow Copy(LightShadow source){
        this.camera = source.camera.Clone();
        this.bias = source.bias;
        this.radius = source.radius;

        this.mapSize.Copy( source.mapSize );

        return this;
    }

    public LightShadow Clone(){
        return new LightShadow(this.camera).Copy(this);
    }
}
