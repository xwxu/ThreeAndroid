package three.lights;

import three.cameras.PerspectiveCamera;

public class SpotLightShadow extends LightShadow {

    public SpotLightShadow(){
        super(new PerspectiveCamera( 50, 1, 0.5f, 500 ));
    }
}
