package three.misc.objects;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

import three.cameras.Camera;
import three.cameras.PerspectiveCamera;
import three.core.AbstractGeometry;
import three.materials.ShaderMaterial;
import three.materials.parameters.ShaderParameters;
import three.math.Color;
import three.math.Math_;
import three.math.Matrix4;
import three.math.Plane;
import three.math.Vector3;
import three.math.Vector4;
import three.objects.Mesh;
import three.renderers.GLRenderTarget;
import three.renderers.GLRenderer;
import three.renderers.RenderTargetOptions;
import three.renderers.shaders.ShaderObject;
import three.renderers.shaders.UniformsObject;
import three.scenes.Scene;

import static three.constants.LinearFilter;
import static three.constants.RGBFormat;

public class Reflector extends Mesh {

    static ShaderObject shader = ReflectorShader();

    private float clipBias;
    private Matrix4 textureMatrix = new Matrix4();
    private GLRenderTarget renderTarget;

    public Reflector(AbstractGeometry geometry, ReflectorOptions options){
        super(geometry, null);

        Color color = options.color;
        int textureWidth = options.textureWidth;
        int textureHeight = options.textureHeight;
        clipBias = options.clipBias;

        RenderTargetOptions parameters = new RenderTargetOptions();
        parameters.minFilter = LinearFilter;
        parameters.magFilter = LinearFilter;
        parameters.format = RGBFormat;
        parameters.stencilBuffer = false;
        renderTarget = new GLRenderTarget( textureWidth, textureHeight, parameters );

        if ( ! Math_.IsPowerOfTwo( textureWidth ) || ! Math_.IsPowerOfTwo( textureHeight ) ) {
            renderTarget.texture.generateMipmaps = false;
        }

        ShaderParameters shaderParameters = new ShaderParameters();
        shaderParameters.vertexShader = shader.vertexShader;
        shaderParameters.fragmentShader = shader.fragmentShader;
        ShaderMaterial material = new ShaderMaterial(shaderParameters);
        material.uniforms.Put("tDiffuse", renderTarget.texture);
        material.uniforms.Put("color", color);
        material.uniforms.Put("textureMatrix", textureMatrix);

        this.material = material;
        this.renderOrder = Integer.MIN_VALUE; // render first

    }

    @Override
    public void OnBeforeRender(GLRenderer renderer, Scene scene, Camera camera){
        Vector3 cameraWorldPosition = new Vector3();
        Vector3 reflectorWorldPosition = new Vector3();
        Matrix4 rotationMatrix = new Matrix4();
        Plane reflectorPlane = new Plane();
        Vector3 normal = new Vector3();

        Vector3 lookAtPosition = new Vector3( 0, 0, - 1 );
        Vector4 clipPlane = new Vector4();
        Vector4 viewport = new Vector4();

        Vector3 view = new Vector3();
        Vector3 target = new Vector3();
        Vector4 q = new Vector4();
        PerspectiveCamera virtualCamera = new PerspectiveCamera();

        reflectorWorldPosition.SetFromMatrixPosition( matrixWorld );
        cameraWorldPosition.SetFromMatrixPosition( camera.matrixWorld );

        rotationMatrix.ExtractRotation( matrixWorld );

        normal.Set( 0, 0, 1 );
        normal.ApplyMatrix4( rotationMatrix );

        view.SubVectors( reflectorWorldPosition, cameraWorldPosition );

        // Avoid rendering when reflector is facing away

        if ( view.Dot( normal ) > 0 ) return;

        view.Reflect( normal ).Negate();
        view.Add( reflectorWorldPosition );

        rotationMatrix.ExtractRotation( camera.matrixWorld );

        lookAtPosition.Set( 0, 0, - 1 );
        lookAtPosition.ApplyMatrix4( rotationMatrix );
        lookAtPosition.Add( cameraWorldPosition );

        target.SubVectors( reflectorWorldPosition, lookAtPosition );
        target.Reflect( normal ).Negate();
        target.Add( reflectorWorldPosition );

        virtualCamera.position.Copy( view );
        virtualCamera.up.Set( 0, 1, 0 );
        virtualCamera.up.ApplyMatrix4( rotationMatrix );
        virtualCamera.up.Reflect( normal );
        virtualCamera.LookAt( target );

        virtualCamera.far = ((PerspectiveCamera)camera).far; // Used in WebGLBackground

        virtualCamera.UpdateMatrixWorld(true);
        virtualCamera.projectionMatrix.Copy( camera.projectionMatrix );

        // Update the texture matrix
        textureMatrix.Set(
                0.5f, 0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.0f, 0.5f,
                0.0f, 0.0f, 0.5f, 0.5f,
                0.0f, 0.0f, 0.0f, 1.0f
        );
        textureMatrix.Multiply( virtualCamera.projectionMatrix );
        textureMatrix.Multiply( virtualCamera.matrixWorldInverse );
        textureMatrix.Multiply( matrixWorld );

        // Now update projection matrix with new clip plane, implementing code from: http://www.terathon.com/code/oblique.html
        // Paper explaining this technique: http://www.terathon.com/lengyel/Lengyel-Oblique.pdf
        reflectorPlane.SetFromNormalAndCoplanarPoint( normal, reflectorWorldPosition );
        reflectorPlane.ApplyMatrix4( virtualCamera.matrixWorldInverse, null );

        clipPlane.Set( reflectorPlane.normal.x, reflectorPlane.normal.y, reflectorPlane.normal.z, reflectorPlane.constant );

        Matrix4 projectionMatrix = virtualCamera.projectionMatrix;

        int signx = clipPlane.x > 0 ? 1 : ((clipPlane.x == 0) ? 0 : -1);
        int signy = clipPlane.y > 0 ? 1 : ((clipPlane.x == 0) ? 0 : -1);
        q.x = ( signx + projectionMatrix.elements[ 8 ] ) / projectionMatrix.elements[ 0 ];
        q.y = ( signy + projectionMatrix.elements[ 9 ] ) / projectionMatrix.elements[ 5 ];
        q.z = - 1.0f;
        q.w = ( 1.0f + projectionMatrix.elements[ 10 ] ) / projectionMatrix.elements[ 14 ];

        // Calculate the scaled plane vector
        clipPlane.MultiplyScalar( 2.0f / clipPlane.Dot( q ) );

        // Replacing the third row of the projection matrix
        projectionMatrix.elements[ 2 ] = clipPlane.x;
        projectionMatrix.elements[ 6 ] = clipPlane.y;
        projectionMatrix.elements[ 10 ] = clipPlane.z + 1.0f - clipBias;
        projectionMatrix.elements[ 14 ] = clipPlane.w;

        this.visible = false;

        GLRenderTarget currentRenderTarget = renderer.GetRenderTarget();

        boolean currentShadowAutoUpdate = renderer.shadowMap.autoUpdate;

        renderer.shadowMap.autoUpdate = false; // Avoid re-computing shadows

        try {
            renderer.Render( scene, virtualCamera, renderTarget, true );
        } catch (IllegalAccessException ignored) {
        }

        renderer.shadowMap.autoUpdate = currentShadowAutoUpdate;

        renderer.SetRenderTarget( currentRenderTarget );

        //todo: Restore viewport

        this.visible = true;
    }

    private static ShaderObject ReflectorShader(){
        UniformsObject uniforms = new UniformsObject();
        uniforms.Put("color", null);
        uniforms.Put("tDiffuse", null);
        uniforms.Put("textureMatrix", null);

        ArrayList<String> listVert = new ArrayList<>();
        listVert.add("uniform mat4 textureMatrix;");
        listVert.add("varying vec4 vUv;");
        listVert.add("void main() {");
        listVert.add("\tvUv = textureMatrix * vec4( position, 1.0 );");
        listVert.add("\tgl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );");
        listVert.add("}");
        String vertexShader = TextUtils.join("\n", listVert);

        ArrayList<String> listFrag = new ArrayList<>();
        listFrag.add("uniform vec3 color;");
        listFrag.add("uniform sampler2D tDiffuse;");
        listFrag.add("varying vec4 vUv;");
        listFrag.add("float blendOverlay( float base, float blend ) {");
        listFrag.add("\treturn( base < 0.5 ? ( 2.0 * base * blend ) : ( 1.0 - 2.0 * ( 1.0 - base ) * ( 1.0 - blend ) ) );");
        listFrag.add("}");
        listFrag.add("vec3 blendOverlay( vec3 base, vec3 blend ) {");
        listFrag.add("\treturn vec3( blendOverlay( base.r, blend.r ), blendOverlay( base.g, blend.g ), blendOverlay( base.b, blend.b ) );");
        listFrag.add("}");
        listFrag.add("void main() {");
        listFrag.add("\tvec4 base = texture2DProj( tDiffuse, vUv );");
        listFrag.add("\tgl_FragColor = vec4( blendOverlay( base.rgb, color ), 1.0 );");
        listFrag.add("}");

        String fragShader = TextUtils.join("\n", listFrag);

        ShaderObject shader = new ShaderObject();

        shader.name = "ReflectorShader";
        shader.uniforms = uniforms;
        shader.vertexShader = vertexShader;
        shader.fragmentShader = fragShader;
        return shader;
    }
}
