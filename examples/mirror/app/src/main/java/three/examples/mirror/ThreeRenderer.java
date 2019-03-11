package three.examples.mirror;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import three.bufferAttribute.Float32BufferAttribute;
import three.cameras.PerspectiveCamera;
import three.core.BufferGeometry;
import three.core.Object3D;
import three.geometries.BoxBufferGeometry;
import three.geometries.CircleBufferGeometry;
import three.geometries.CylinderBufferGeometry;
import three.geometries.PlaneBufferGeometry;
import three.geometries.TeapotBufferGeometry;
import three.lights.AmbientLight;
import three.lights.DirectionalLight;
import three.lights.Light;
import three.lights.PointLight;
import three.materials.LineBasicMaterial;
import three.materials.Material;
import three.materials.MeshBasicMaterial;
import three.materials.MeshLambertMaterial;
import three.materials.MeshNormalMaterial;
import three.materials.MeshPhongMaterial;
import three.materials.parameters.LineBasicParameters;
import three.materials.parameters.MeshBasicParameters;
import three.materials.parameters.MeshLambertParameters;
import three.materials.parameters.MeshNormalParameters;
import three.materials.parameters.MeshPhongParameters;
import three.math.Color;
import three.math.Math_;
import three.math.Vector3;
import three.misc.objects.Reflector;
import three.misc.objects.ReflectorOptions;
import three.objects.Line;
import three.objects.Mesh;
import three.renderers.GLRenderer;
import three.renderers.shaders.ShaderLibs.MeshLambertFrag;
import three.scenes.Fog;
import three.scenes.Scene;
import three.util.RendererParameters;

import static three.constants.DoubleSide;
import static three.constants.VertexColors;

public class ThreeRenderer implements GLSurfaceView.Renderer {

    private GLRenderer renderer;
    private PerspectiveCamera camera;
    private Scene scene;
    private Mesh mesh;
    private boolean firstTime = true;
    private Object3D sphereGroup;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        RendererParameters rendererParameters = new RendererParameters();
        renderer = new GLRenderer(rendererParameters);

        camera = new PerspectiveCamera( 45, 0.7f, 1, 500 );
        camera.position.Set(0, 75, 160);

        scene = new Scene();

        sphereGroup = new Object3D();
        scene.Add( sphereGroup );

        BufferGeometry geometry = new CylinderBufferGeometry( 0.1f, 15 * (float)Math.cos(Math.PI / 180 * 30),
                0.1f, 24, 1,  false, 0, (float) Math.PI * 2);
        MeshPhongParameters parameters = new MeshPhongParameters();
        parameters.color = new Color(0xffffff);
        parameters.emissive = new Color(0x444444);
        Material material = new MeshPhongMaterial( parameters );
        Mesh sphereCap = new Mesh( geometry, material );
        sphereCap.position.y = - 15 * (float)Math.sin( Math.PI / 180 * 30 ) - 0.05f;
        sphereCap.RotateX( - (float) Math.PI );

//        BufferGeometry sphereGeometry = new SphereBufferGeometry( 15, 24, 24, Math.PI / 2, Math.PI * 2, 0, Math.PI / 180 * 120 );
//        Mesh halfSphere = new Mesh( sphereGeometry, material );
//        halfSphere.Add( sphereCap );
//        halfSphere.RotateX( - (float) Math.PI / 180 * 135 );
//        halfSphere.RotateZ( - (float) Math.PI / 180 * 20 );
//        halfSphere.position.y = 7.5f + 15 * (float)Math.sin( Math.PI / 180 * 30 );
//
//        sphereGroup.Add( halfSphere );

//        var geometry = new THREE.IcosahedronBufferGeometry( 5, 0 );
//        var material = new THREE.MeshPhongMaterial( { color: 0xffffff, emissive: 0x333333, flatShading: true } );
//        smallSphere = new THREE.Mesh( geometry, material );
//        scene.add( smallSphere );

        // walls
        BufferGeometry planeGeo = new PlaneBufferGeometry( 100.1f, 100.1f, 1, 1 );

        Mesh planeTop = new Mesh( planeGeo, new MeshPhongMaterial( new MeshPhongParameters() ) );
        planeTop.position.y = 100;
        planeTop.RotateX( (float) Math.PI / 2 );
        scene.Add( planeTop );

        Mesh planeBottom = new Mesh( planeGeo, new MeshPhongMaterial( new MeshPhongParameters() ) );
        planeBottom.RotateX( - (float) Math.PI / 2 );
        scene.Add( planeBottom );

        MeshPhongParameters front = new MeshPhongParameters();
        front.color = new Color(0x7f7fff);
        Mesh planeFront = new Mesh( planeGeo, new MeshPhongMaterial( front ) );
        planeFront.position.z = 50;
        planeFront.position.y = 50;
        planeFront.RotateY( (float) Math.PI );
        scene.Add( planeFront );

        MeshPhongParameters right = new MeshPhongParameters();
        right.color = new Color(0x00ff00);
        Mesh planeRight = new Mesh( planeGeo, new MeshPhongMaterial( right ) );
        planeRight.position.x = 50;
        planeRight.position.y = 50;
        planeRight.RotateY( - (float) Math.PI / 2 );
        scene.Add( planeRight );

        MeshPhongParameters left = new MeshPhongParameters();
        left.color = new Color(0xff0000);
        Mesh planeLeft = new Mesh( planeGeo, new MeshPhongMaterial( left ) );
        planeLeft.position.x = -50;
        planeLeft.position.y = 50;
        planeLeft.RotateY( (float) Math.PI / 2 );
        scene.Add( planeLeft );

        // lights
        Light mainLight = new PointLight( new Color(0xcccccc), 1.5f, 250, 1 );
        mainLight.position.y = 60;
        scene.Add( mainLight );

        Light greenLight = new PointLight( new Color(0x00ff00), 0.25f, 1000, 1 );
        greenLight.position.Set( 550, 50, 0 );
        scene.Add( greenLight );

        Light redLight = new PointLight( new Color(0xff0000), 0.25f, 1000, 1 );
        redLight.position.Set( - 550, 50, 0 );
        scene.Add( redLight );

        Light blueLight = new PointLight( new Color(0x7f7fff), 0.25f, 1000, 1 );
        blueLight.position.Set( 0, 50, 550 );
        scene.Add( blueLight );

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(firstTime){

            BufferGeometry circleGeometry = new CircleBufferGeometry(40, 64, 0, (float) Math.PI * 2);
            ReflectorOptions options = new ReflectorOptions();
            options.clipBias = 0.003f;
            options.textureWidth = width;
            options.textureHeight = height;
            options.color = new Color(0x777777);
            options.recursion = 1;
            Mesh groundMirror = new Reflector( circleGeometry, options );
            groundMirror.position.y = 0.5f;
            groundMirror.RotateX(-(float) Math.PI / 2);
            scene.Add(groundMirror);

            BufferGeometry planeGeometry = new PlaneBufferGeometry(100, 100, 1, 1);
            ReflectorOptions options2 = new ReflectorOptions();
            options2.clipBias = 0.003f;
            options2.textureWidth = width;
            options2.textureHeight = height;
            options2.color = new Color(0x889999);
            options2.recursion = 1;
            Mesh verticalMirror = new Reflector( planeGeometry, options );
            verticalMirror.position.y = 50;
            verticalMirror.position.z = -50;
            scene.Add(verticalMirror);

            firstTime = false;
        }

        camera.aspect = (float)width / height;
        camera.UpdateProjectionMatrix();
        renderer.SetSize(width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        try {
            scene.RotateY(0.1f);
            renderer.Render(scene, camera, null, true);
        } catch (IllegalAccessException e) {
        }

    }
}
