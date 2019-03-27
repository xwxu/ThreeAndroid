package three.examples.buffergeometry;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import three.bufferAttribute.Float32BufferAttribute;
import three.cameras.PerspectiveCamera;
import three.core.BufferGeometry;
import three.geometries.BoxBufferGeometry;
import three.lights.AmbientLight;
import three.lights.DirectionalLight;
import three.materials.MeshBasicMaterial;
import three.materials.MeshNormalMaterial;
import three.materials.MeshPhongMaterial;
import three.materials.parameters.MeshBasicParameters;
import three.materials.parameters.MeshNormalParameters;
import three.materials.parameters.MeshPhongParameters;
import three.math.Color;
import three.math.Vector3;
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
        rendererParameters.premultipliedAlpha = false;
        renderer = new GLRenderer(rendererParameters);
        renderer.gammaInput = true;
        renderer.gammaOutput = true;

        camera = new PerspectiveCamera( 27, 0.7f, 1, 3500 );
        camera.position.z = 1500;
        Vector3 target = new Vector3();
        camera.getWorldDirection(target);

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

        int triangles = 5000;

        BufferGeometry geometry = new BufferGeometry();

        float[] positions = new float[triangles * 9];
        float[] normals = new float[triangles * 9];
        float[] colors = new float[triangles * 9];

        int pointer = 0;

        Color color = new Color();

        int n = 800, n2 = n / 2;	// triangles spread in the cube
        int d = 12, d2 = d / 2;	// individual triangle size

        Vector3 pA = new Vector3();
        Vector3 pB = new Vector3();
        Vector3 pC = new Vector3();

        Vector3 cb = new Vector3();
        Vector3 ab = new Vector3();

        for ( int i = 0; i < triangles; i ++ ) {

            // positions
            float x = (float) Math.random() * n - n2;
            float y = (float)Math.random() * n - n2;
            float z = (float)Math.random() * n - n2;

            float ax = x + (float)Math.random() * d - d2;
            float ay = y + (float)Math.random() * d - d2;
            float az = z + (float)Math.random() * d - d2;
            float bx = x + (float)Math.random() * d - d2;
            float by = y + (float)Math.random() * d - d2;
            float bz = z + (float)Math.random() * d - d2;
            float cx = x + (float)Math.random() * d - d2;
            float cy = y + (float)Math.random() * d - d2;
            float cz = z + (float)Math.random() * d - d2;

            positions[pointer] = ax;
            positions[pointer+1] = ay;
            positions[pointer+2] = az;
            positions[pointer+3] = bx;
            positions[pointer+4] = by;
            positions[pointer+5] = bz;
            positions[pointer+6] = cx;
            positions[pointer+7] = cy;
            positions[pointer+8] = cz;

            // flat face normals
            pA.set( ax, ay, az );
            pB.set( bx, by, bz );
            pC.set( cx, cy, cz );

            cb.subVectors( pC, pB );
            ab.subVectors( pA, pB );
            cb.cross( ab );
            cb.normalize();

            float nx = cb.x;
            float ny = cb.y;
            float nz = cb.z;

            normals[pointer] = nx;
            normals[pointer+1] = ny;
            normals[pointer+2] = nz;
            normals[pointer+3] = nx;
            normals[pointer+4] = ny;
            normals[pointer+5] = nz;
            normals[pointer+6] = nx;
            normals[pointer+7] = ny;
            normals[pointer+8] = nz;

            // colors
            float vx = ( x / n ) + 0.5f;
            float vy = ( y / n ) + 0.5f;
            float vz = ( z / n ) + 0.5f;

            color.setRGB( vx, vy, vz );

            colors[pointer] = color.r;
            colors[pointer+1] = color.g;
            colors[pointer+2] = color.b;
            colors[pointer+3] = color.r;
            colors[pointer+4] = color.g;
            colors[pointer+5] = color.b;
            colors[pointer+6] = color.r;
            colors[pointer+7] = color.g;
            colors[pointer+8] = color.b;

            pointer += 9;
        }

        geometry.addAttribute( "position", new Float32BufferAttribute( positions, 3 ) );
        geometry.addAttribute( "normal", new Float32BufferAttribute( normals, 3 ) );
        geometry.addAttribute( "color", new Float32BufferAttribute( colors, 3 ) );

        geometry.computeBoundingSphere();

        MeshPhongParameters parameters = new MeshPhongParameters();
        parameters.color = new Color(0xaaaaaa);
        parameters.specular = new Color(0xffffff);
        parameters.shininess = 250.0f;
        parameters.side = DoubleSide;
        parameters.vertexColors = VertexColors;
        MeshPhongMaterial material = new MeshPhongMaterial( parameters );

        //MeshNormalMaterial material = new MeshNormalMaterial(new MeshNormalParameters());
//        MeshBasicParameters parameters = new MeshBasicParameters();
//        parameters.vertexColors = VertexColors;
//        MeshBasicMaterial material = new MeshBasicMaterial(parameters);

        //BoxBufferGeometry boxGeo = new BoxBufferGeometry(200, 200, 200, 10, 10,10);
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
