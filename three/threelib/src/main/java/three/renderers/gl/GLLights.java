package three.renderers.gl;

import java.util.ArrayList;

import three.cameras.Camera;
import three.cameras.PerspectiveCamera;
import three.lights.AmbientLight;
import three.lights.DirectionalLight;
import three.lights.HemisphereLight;
import three.lights.Light;
import three.lights.LightShadow;
import three.lights.PointLight;
import three.lights.RectAreaLight;
import three.lights.SpotLight;
import three.math.Color;
import three.math.Matrix4;
import three.math.Vector3;
import three.renderers.lightUniforms.DirectionalLightUniform;
import three.renderers.lightUniforms.HemisphereLightUniform;
import three.renderers.lightUniforms.LightUniformCache;
import three.renderers.lightUniforms.PointLightUniform;
import three.renderers.lightUniforms.RectAreaLightUniform;
import three.renderers.lightUniforms.SpotLightUniform;
import three.textures.Texture;

public class GLLights {

    private LightUniformCache cache = new LightUniformCache();
    private static int count = 0;

    public int id = count++;
    public int hash_stateID = -1;
    public int hash_directionalLength = -1;
    public int hash_pointLength = -1;
    public int hash_spotLength = -1;
    public int hash_rectAreaLength = -1;
    public int hash_hemiLength = -1;
    public int hash_shadowsLength = -1;
    public float[] ambient = new float[]{0,0,0};
    public ArrayList<DirectionalLightUniform> directional = new ArrayList();
    public ArrayList<Texture> directionalShadowMap = new ArrayList<>();
    public ArrayList<Matrix4> directionalShadowMatrix = new ArrayList<>();
    public ArrayList<SpotLightUniform> spot = new ArrayList<>();
    public ArrayList<Texture> spotShadowMap = new ArrayList<>();
    public ArrayList<Matrix4> spotShadowMatrix = new ArrayList<>();
    public ArrayList<PointLightUniform> point = new ArrayList<>();
    public ArrayList<Matrix4> pointShadowMatrix = new ArrayList<>();
    public ArrayList<Texture> pointShadowMap = new ArrayList<>();
    public ArrayList<RectAreaLightUniform> rectArea = new ArrayList<>();
    public ArrayList<HemisphereLightUniform> hemi = new ArrayList<>();


    public void Setup(ArrayList<Light> lights, ArrayList<Light> shadows, Camera camera) {
        float r = 0, g = 0, b = 0;

        int directionalLength = 0;
        int pointLength = 0;
        int spotLength = 0;
        int rectAreaLength = 0;
        int hemiLength = 0;

        directional.clear();
        directionalShadowMap.clear();
        directionalShadowMatrix.clear();
        spot.clear();
        spotShadowMap.clear();
        spotShadowMatrix.clear();
        point.clear();
        pointShadowMatrix.clear();
        pointShadowMap.clear();
        rectArea.clear();
        hemi.clear();

        Vector3 vector3 = new Vector3();
        Matrix4 matrix4 = new Matrix4();
        Matrix4 matrix42 = new Matrix4();

        Matrix4 viewMatrix = camera.matrixWorldInverse;

        for ( int i = 0, l = lights.size(); i < l; i ++ ) {
            Light light = lights.get(i);

            Color color = light.color;
            float intensity = light.intensity;
            float distance = light.distance;

            Texture shadowMap = ( light.shadow != null && light.shadow.map != null ) ? light.shadow.map : null;

            if ( light instanceof AmbientLight) {

                r += color.r * intensity;
                g += color.g * intensity;
                b += color.b * intensity;

            } else if ( light instanceof DirectionalLight) {
                DirectionalLight dirlight = (DirectionalLight) light;
                DirectionalLightUniform uniforms = (DirectionalLightUniform)cache.Get( light );

                ((Color)uniforms.Get("color")).Copy( light.color ).MultiplyScalar( light.intensity );
                ((Vector3)uniforms.Get("direction")).SetFromMatrixPosition( light.matrixWorld );
                vector3.SetFromMatrixPosition( dirlight.target.matrixWorld );
                ((Vector3)uniforms.Get("direction")).Sub( vector3 );
                ((Vector3)uniforms.Get("direction")).TransformDirection( viewMatrix );

                uniforms.Put("shadow", light.castShadow);

                if ( light.castShadow ) {
                    LightShadow shadow = dirlight.shadow;
                    uniforms.Put("shadowBias", shadow.bias);
                    uniforms.Put("shadowRadius", shadow.radius);
                    uniforms.Put("shadowMapSize",shadow.mapSize);
                }

                directionalShadowMap.add(shadowMap);
                directionalShadowMatrix.add(dirlight.shadow.matrix);
                directional.add(uniforms);

                directionalLength ++;

            } else if ( light instanceof SpotLight) {
                SpotLight spotLight = (SpotLight) light;
                SpotLightUniform uniforms = (SpotLightUniform)cache.Get( light );

                ((Vector3)uniforms.Get("position")).SetFromMatrixPosition( light.matrixWorld );
                ((Vector3)uniforms.Get("position")).ApplyMatrix4( viewMatrix );
                ((Color)uniforms.Get("color")).Copy( color ).MultiplyScalar( intensity );
                uniforms.Put("distance", distance);
                ((Vector3)uniforms.Get("direction")).SetFromMatrixPosition( light.matrixWorld );
                vector3.SetFromMatrixPosition( spotLight.target.matrixWorld );
                ((Vector3)uniforms.Get("direction")).Sub( vector3 );
                ((Vector3)uniforms.Get("direction")).TransformDirection( viewMatrix );
                uniforms.Put("coneCos", (float) Math.cos( spotLight.angle ));
                uniforms.Put("penumbraCos", (float) Math.cos( spotLight.angle * ( 1 - spotLight.penumbra ) ));
                uniforms.Put("decay", spotLight.decay);
                uniforms.Put("shadow", spotLight.castShadow);

                if ( light.castShadow ) {
                    LightShadow shadow = light.shadow;
                    uniforms.Put("shadowBias", shadow.bias);
                    uniforms.Put("shadowRadius", shadow.radius);
                    uniforms.Put("shadowMapSize",shadow.mapSize);
                }

                spotShadowMap.add(shadowMap);
                spotShadowMatrix.add(light.shadow.matrix);
                spot.add(uniforms);

                spotLength ++;

            } else if ( light instanceof RectAreaLight) {

                RectAreaLightUniform uniforms = (RectAreaLightUniform)cache.Get( light );

                // (a) intensity is the total visible light emitted
                //uniforms.color.copy( color ).multiplyScalar( intensity / ( light.width * light.height * Math.PI ) );
                // (b) intensity is the brightness of the light
                ((Color)uniforms.Get("color")).Copy( color ).MultiplyScalar( intensity );

                ((Vector3)uniforms.Get("position")).SetFromMatrixPosition( light.matrixWorld );
                ((Vector3)uniforms.Get("position")).ApplyMatrix4( viewMatrix );

                // extract local rotation of light to derive width/height half vectors
                matrix42.Identity();
                matrix4.Copy( light.matrixWorld );
                matrix4.Premultiply( viewMatrix );
                matrix42.ExtractRotation( matrix4 );

                ((Vector3)uniforms.Get("halfWidth")).Set( ((RectAreaLight)light).width * 0.5f, 0.0f, 0.0f);
                ((Vector3)uniforms.Get("halfHeight")).Set( 0.0f, ((RectAreaLight)light).height * 0.5f, 0.0f );

                ((Vector3)uniforms.Get("halfWidth")).ApplyMatrix4( matrix42 );
                ((Vector3)uniforms.Get("halfHeight")).ApplyMatrix4( matrix42 );

                rectArea.add(uniforms);

                rectAreaLength ++;

            } else if ( light instanceof PointLight) {

                PointLightUniform uniforms = (PointLightUniform)cache.Get( light );

                ((Vector3)uniforms.Get("position")).SetFromMatrixPosition( light.matrixWorld );
                ((Vector3)uniforms.Get("position")).ApplyMatrix4( viewMatrix );

                ((Color)uniforms.Get("color")).Copy( light.color ).MultiplyScalar( light.intensity );
                uniforms.Put("distance", light.distance);
                uniforms.Put("decay", ((PointLight)light).decay);

                uniforms.Put("shadow", light.castShadow);

                if ( light.castShadow ) {
                    LightShadow shadow = light.shadow;
                    uniforms.Put("shadowBias", shadow.bias);
                    uniforms.Put("shadowRadius", shadow.radius);
                    uniforms.Put("shadowMapSize",shadow.mapSize);
                    uniforms.Put("shadowCameraNear", ((PerspectiveCamera)shadow.camera).near);
                    uniforms.Put("shadowCameraFar", ((PerspectiveCamera)shadow.camera).far);
                }

                pointShadowMap.add(shadowMap);
                pointShadowMatrix.add(light.shadow.matrix);
                point.add(uniforms);

                pointLength ++;

            } else if ( light instanceof HemisphereLight) {

                HemisphereLightUniform uniforms = (HemisphereLightUniform)cache.Get( light );

                ((Vector3)uniforms.Get("direction")).SetFromMatrixPosition( light.matrixWorld );
                ((Vector3)uniforms.Get("direction")).TransformDirection( viewMatrix );
                ((Vector3)uniforms.Get("direction")).Normalize();

                ;
                ((Color)uniforms.Get("skyColor")).Copy( light.color ).MultiplyScalar( intensity );
                ((Color)uniforms.Get("groundColor")).Copy( ((HemisphereLight)light).groundColor ).MultiplyScalar( intensity );

                hemi.add(uniforms);

                hemiLength ++;

            }

        }

        ambient[ 0 ] = r;
        ambient[ 1 ] = g;
        ambient[ 2 ] = b;

        hash_stateID = id;
        hash_directionalLength = directionalLength;
        hash_pointLength = pointLength;
        hash_spotLength = spotLength;
        hash_rectAreaLength = rectAreaLength;
        hash_hemiLength = hemiLength;
        hash_shadowsLength = shadows.size();
    }
}
