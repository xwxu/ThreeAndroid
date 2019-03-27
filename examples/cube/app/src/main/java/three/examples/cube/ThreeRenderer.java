package three.examples.cube;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import three.bufferAttribute.Float32BufferAttribute;
import three.cameras.PerspectiveCamera;
import three.core.BufferGeometry;
import three.geometries.BoxBufferGeometry;
import three.lights.AmbientLight;
import three.lights.DirectionalLight;
import three.materials.LineBasicMaterial;
import three.materials.MeshBasicMaterial;
import three.materials.MeshNormalMaterial;
import three.materials.MeshPhongMaterial;
import three.materials.parameters.LineBasicParameters;
import three.materials.parameters.MeshBasicParameters;
import three.materials.parameters.MeshNormalParameters;
import three.materials.parameters.MeshPhongParameters;
import three.math.Color;
import three.math.Vector3;
import three.objects.Line;
import three.objects.Mesh;
import three.renderers.GLRenderer;
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

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        RendererParameters rendererParameters = new RendererParameters();
        renderer = new GLRenderer(rendererParameters);

        camera = new PerspectiveCamera( 70, 0.7f, 1, 1000 );
        camera.position.z = 400;

        scene = new Scene();
        //scene.background = new Color( 0x050505 );
        scene.fog = new Fog(  new Color( 0x050505 ), 2000, 3500 );

        scene.add( new AmbientLight( new Color (0x444444 ), 1 ));

        DirectionalLight light1 = new DirectionalLight( new Color(0xffffff), 0.5f );
        light1.position.set( 1, 1, 1 );
        scene.add( light1 );

        DirectionalLight light2 = new DirectionalLight( new Color(0xffffff), 1.5f );
        light2.position.set( 0, - 1, 0 );
        scene.add( light2 );


        BufferGeometry geometry = new BoxBufferGeometry( 200, 200, 200, 1, 1, 1 );
        MeshBasicParameters parameters = new MeshBasicParameters();
        parameters.color = new Color(0x11dd11);
        MeshBasicMaterial material = new MeshBasicMaterial(parameters);

        mesh = new Mesh( geometry, material );
        scene.add( mesh );
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        camera.aspect = (float)width / height;
        camera.updateProjectionMatrix();
        renderer.setSize(width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        try {
            //mesh.rotation.x += 0.25;
            //mesh.rotation.y += 0.5;
            mesh.rotateOnAxis(new Vector3(0,1,0), 0.1f);
            renderer.render(scene, camera, null, true);
        } catch (IllegalAccessException e) {
        }

    }
}
