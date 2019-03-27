package three.examples.teapot;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import three.bufferAttribute.Float32BufferAttribute;
import three.cameras.PerspectiveCamera;
import three.core.BufferGeometry;
import three.geometries.BoxBufferGeometry;
import three.geometries.TeapotBufferGeometry;
import three.lights.AmbientLight;
import three.lights.DirectionalLight;
import three.materials.LineBasicMaterial;
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
import three.math.Vector3;
import three.misc.controls.TrackballControls;
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
    public TrackballControls controls;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        RendererParameters rendererParameters = new RendererParameters();
        renderer = new GLRenderer(rendererParameters);

        camera = new PerspectiveCamera( 45, 0.7f, 1, 80000 );
        camera.position.set(-0, 200, 3300);

        scene = new Scene();
        controls = new TrackballControls(camera);
        //scene.background = new Color( 0x050505 );
        //scene.fog = new Fog(  new Color( 0x050505 ), 2000, 3500 );

        scene.add( new AmbientLight( new Color (0.2f, 0.2f, 0.2f ), 1 ));

        DirectionalLight light1 = new DirectionalLight( new Color(0xffffff), 1.0f );
        light1.position.set( 0.32f, 0.39f, 0.7f );
        scene.add( light1 );

//        DirectionalLight light2 = new DirectionalLight( new Color(0xffffff), 1.5f );
//        light2.position.Set( 0, - 1, 0 );
//        scene.Add( light2 );


        BufferGeometry geometry = new TeapotBufferGeometry( 300, 15, true, true, true, false, true );

//        MeshBasicParameters parameters = new MeshBasicParameters();
//        parameters.color = new Color(0x11dd11);
//        MeshBasicMaterial material = new MeshBasicMaterial(parameters);

        MeshPhongParameters parameters = new MeshPhongParameters();
        parameters.color = new Color(0xcccccc);
        parameters.emissive = new Color(0xcc0000);
        parameters.side = DoubleSide;
        MeshPhongMaterial material = new MeshPhongMaterial(parameters);

//        MeshLambertParameters parameters =  new MeshLambertParameters();
//        parameters.color = new Color (0xdddddd);
//        MeshLambertMaterial material = new MeshLambertMaterial(parameters);


        mesh = new Mesh( geometry, material );
        scene.add( mesh );
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        camera.aspect = (float)width / height;
        camera.updateProjectionMatrix();
        controls.handleResize(width, height);
        renderer.setSize(width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        try {
            //mesh.rotation.x += 0.25;
            //mesh.rotation.y += 0.5;
            //mesh.RotateOnAxis(new Vector3(0,1,0), 0.1f);
            controls.update();
            renderer.render(scene, camera, null, true);
        } catch (IllegalAccessException e) {
        }

    }
}
