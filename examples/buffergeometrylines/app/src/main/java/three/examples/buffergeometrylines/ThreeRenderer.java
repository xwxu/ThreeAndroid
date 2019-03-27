package three.examples.buffergeometrylines;

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
    private Line line;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        RendererParameters rendererParameters = new RendererParameters();
        rendererParameters.premultipliedAlpha = false;
        renderer = new GLRenderer(rendererParameters);
        renderer.gammaInput = true;
        renderer.gammaOutput = true;

        camera = new PerspectiveCamera( 27, 0.7f, 1, 3500 );
        camera.position.z = 2500;

        scene = new Scene();
        //scene.background = new Color( 0x050505 );
        //scene.fog = new Fog(  new Color( 0x050505 ), 2000, 3500 );

        scene.add( new AmbientLight( new Color (0x444444 ), 1 ));

        DirectionalLight light1 = new DirectionalLight( new Color(0xffffff), 0.5f );
        light1.position.set( 1, 1, 1 );
        scene.add( light1 );

        DirectionalLight light2 = new DirectionalLight( new Color(0xffffff), 1.5f );
        light2.position.set( 0, - 1, 0 );
        scene.add( light2 );

        int segments = 5000;

        BufferGeometry geometry = new BufferGeometry();

        float[] positions = new float[segments * 3];
        float[] colors = new float[segments * 3];

        int pointer = 0;

        Color color = new Color();

        int r = 800;

        for ( int i = 0; i < segments; i ++ ) {

            float x = (float) Math.random() * r - r / 2;
            float y = (float) Math.random() * r - r / 2;
            float z = (float) Math.random() * r - r / 2;

            // positions
            positions[pointer] = x;
            positions[pointer+1] = y;
            positions[pointer+2] = z;

            // colors
            colors[pointer] = ( x / r ) + 0.5f;
            colors[pointer+1] = ( y / r ) + 0.5f;
            colors[pointer+2] = ( z / r ) + 0.5f;

            pointer += 3;
        }

        geometry.addAttribute( "position", new Float32BufferAttribute( positions, 3 ) );
        geometry.addAttribute( "color", new Float32BufferAttribute( colors, 3 ) );

        geometry.computeBoundingSphere();

        LineBasicParameters parameters = new LineBasicParameters();
        parameters.vertexColors = VertexColors;
        LineBasicMaterial material = new LineBasicMaterial(parameters);

        line = new Line( geometry, material );
        scene.add( line );
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
            line.rotateOnAxis(new Vector3(0,1,0), 0.1f);
            renderer.render(scene, camera, null, true);
        } catch (IllegalAccessException e) {
        }

    }
}
