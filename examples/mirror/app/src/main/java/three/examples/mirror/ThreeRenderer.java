package three.examples.mirror;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import three.cameras.PerspectiveCamera;
import three.core.BufferGeometry;
import three.core.Object3D;
import three.geometries.CircleBufferGeometry;
import three.geometries.CylinderBufferGeometry;
import three.geometries.PlaneBufferGeometry;
import three.geometries.SphereBufferGeometry;
import three.geometries.TeapotBufferGeometry;
import three.lights.Light;
import three.lights.PointLight;
import three.materials.Material;
import three.materials.MeshPhongMaterial;
import three.materials.parameters.MeshPhongParameters;
import three.math.Color;
import three.misc.controls.TrackballControls;
import three.misc.objects.Reflector;
import three.misc.objects.ReflectorOptions;
import three.objects.Mesh;
import three.renderers.GLRenderer;
import three.scenes.Scene;
import three.util.RendererParameters;

public class ThreeRenderer implements GLSurfaceView.Renderer {

    private GLRenderer renderer;
    private PerspectiveCamera camera;
    private Scene scene;
    private boolean firstTime = true;
    private Object3D sphereGroup;
    private Mesh smallSphere;
    private int delta;
    public TrackballControls controls;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        RendererParameters rendererParameters = new RendererParameters();
        renderer = new GLRenderer(rendererParameters);

        camera = new PerspectiveCamera( 45, 0.7f, 1, 500 );
        camera.position.set(0, 50, 160);

        scene = new Scene();

        controls = new TrackballControls(camera);
        sphereGroup = new Object3D();
        scene.add( sphereGroup );

        BufferGeometry geometry = new CylinderBufferGeometry( 0.1f, 15 * (float)Math.cos(Math.PI / 180 * 30),
                0.1f, 24, 1,  false, 0, (float) Math.PI * 2);
        MeshPhongParameters parameters = new MeshPhongParameters();
        parameters.color = new Color(0xffffff);
        parameters.emissive = new Color(0x444444);
        Material material = new MeshPhongMaterial( parameters );
        Mesh sphereCap = new Mesh( geometry, material );
        sphereCap.position.y = - 15 * (float)Math.sin( Math.PI / 180 * 30 ) - 0.05f;
        sphereCap.rotateX( - (float) Math.PI );

        BufferGeometry sphereGeometry = new SphereBufferGeometry( 15, 24, 24,
                (float) Math.PI / 2, (float) Math.PI * 2, 0, (float) Math.PI / 180 * 120 );
        Mesh halfSphere = new Mesh( sphereGeometry, material );
        halfSphere.add( sphereCap );
        halfSphere.rotateX( - (float) Math.PI / 180 * 135 );
        halfSphere.rotateZ( - (float) Math.PI / 180 * 20 );
        halfSphere.position.y = 7.5f + 15 * (float)Math.sin( Math.PI / 180 * 30 );

        //scene.add( halfSphere );

        // teapot
        BufferGeometry teapotGeo = new TeapotBufferGeometry( 10, 15, true, true, true, false, true );
        Mesh teapot = new Mesh( teapotGeo, material );
        teapot.position.set(0,15,0);
        scene.add( teapot );

//        BufferGeometry icosahedronGeometry = new IcosahedronBufferGeometry( 5, 0 );
//        MeshPhongParameters parameters2 = new MeshPhongParameters();
//        parameters2.color = new Color(0xffffff);
//        parameters2.emissive = new Color(0x333333);
//        parameters2.flatShading = true;
//        Material material2 = new MeshPhongMaterial( parameters2 );
//        smallSphere = new Mesh( icosahedronGeometry, material2 );
//        scene.add( smallSphere );

        // walls
        BufferGeometry planeGeo = new PlaneBufferGeometry( 100.1f, 100.1f, 1, 1 );

        Mesh planeTop = new Mesh( planeGeo, new MeshPhongMaterial( new MeshPhongParameters() ) );
        planeTop.position.y = 100;
        planeTop.rotateX( (float) Math.PI / 2 );
        scene.add( planeTop );

        Mesh planeBottom = new Mesh( planeGeo, new MeshPhongMaterial( new MeshPhongParameters() ) );
        planeBottom.rotateX( - (float) Math.PI / 2 );
        scene.add( planeBottom );

        MeshPhongParameters front = new MeshPhongParameters();
        front.color = new Color(0x7f7fff);
        Mesh planeFront = new Mesh( planeGeo, new MeshPhongMaterial( front ) );
        planeFront.position.z = 50;
        planeFront.position.y = 50;
        planeFront.rotateY( (float) Math.PI );
        scene.add( planeFront );

        MeshPhongParameters right = new MeshPhongParameters();
        right.color = new Color(0x00ff00);
        Mesh planeRight = new Mesh( planeGeo, new MeshPhongMaterial( right ) );
        planeRight.position.x = 50;
        planeRight.position.y = 50;
        planeRight.rotateY( - (float) Math.PI / 2 );
        scene.add( planeRight );

        MeshPhongParameters left = new MeshPhongParameters();
        left.color = new Color(0xff0000);
        Mesh planeLeft = new Mesh( planeGeo, new MeshPhongMaterial( left ) );
        planeLeft.position.x = -50;
        planeLeft.position.y = 50;
        planeLeft.rotateY( (float) Math.PI / 2 );
        scene.add( planeLeft );

        // lights
        Light mainLight = new PointLight( new Color(0xcccccc), 1.5f, 250, 1 );
        mainLight.position.y = 60;
        scene.add( mainLight );

        Light greenLight = new PointLight( new Color(0x00ff00), 0.25f, 1000, 1 );
        greenLight.position.set( 550, 50, 0 );
        scene.add( greenLight );

        Light redLight = new PointLight( new Color(0xff0000), 0.25f, 1000, 1 );
        redLight.position.set( - 550, 50, 0 );
        scene.add( redLight );

        Light blueLight = new PointLight( new Color(0x7f7fff), 0.25f, 1000, 1 );
        blueLight.position.set( 0, 50, 550 );
        scene.add( blueLight );

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
            groundMirror.rotateX(-(float) Math.PI / 2);
            scene.add(groundMirror);

            BufferGeometry planeGeometry = new PlaneBufferGeometry(100, 100, 1, 1);
            ReflectorOptions options2 = new ReflectorOptions();
            options2.clipBias = 0.003f;
            options2.textureWidth = width;
            options2.textureHeight = height;
            options2.color = new Color(0x889999);
            options2.recursion = 1;
            Mesh verticalMirror = new Reflector( planeGeometry, options2 );
            verticalMirror.position.y = 50;
            verticalMirror.position.z = -50;
            scene.add(verticalMirror);

            firstTime = false;
        }

        camera.aspect = (float)width / height;
        camera.updateProjectionMatrix();
        controls.handleResize(width, height);
        renderer.setSize(width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        try {
//            smallSphere.position.set(
//                    (float) Math.cos( delta * 0.01f ) * 30,
//                    (float) Math.abs( Math.cos( delta * 0.02f ) ) * 20 + 5,
//                    (float) Math.sin( delta * 0.01f ) * 30
//            );
//            smallSphere.rotation.setY((float) Math.PI / 2  - delta * 0.01f);
//            smallSphere.rotation.setZ(delta * 0.08f);

            scene.rotateY(0.1f);
            //controls.update();
            renderer.render(scene, camera, null, true);

        } catch (IllegalAccessException e) {
        }

    }
}
