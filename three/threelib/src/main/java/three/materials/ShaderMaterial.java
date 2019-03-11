package three.materials;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import three.materials.parameters.ShaderParameters;
import three.renderers.shaders.UniformsObject;

public class ShaderMaterial extends Material{


    public UniformsObject uniforms = new UniformsObject();
    public String vertexShader = "void main() {\n\tgl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n}";
    public String fragmentShader = "void main() {\n\tgl_FragColor = vec4( 1.0, 0.0, 0.0, 1.0 );\n}";
    public boolean uniformsNeedUpdate;
    public HashMap<String, FloatBuffer> defaultAttributeValues;

    public ShaderMaterial(ShaderParameters parameters){
        super(parameters);
        this.vertexShader = parameters.vertexShader;
        this.fragmentShader = parameters.fragmentShader;
        uniformsNeedUpdate = parameters.uniformsNeedUpdate;

        defaultAttributeValues = new HashMap<>();

        ByteBuffer bb1 = ByteBuffer.allocateDirect(3 * 4);
        bb1.order(ByteOrder.nativeOrder());
        FloatBuffer colorBuffer = bb1.asFloatBuffer();
        colorBuffer.put(new float[]{1,1,1});
        colorBuffer.position(0);
        defaultAttributeValues.put("color", colorBuffer );

        ByteBuffer bb2 = ByteBuffer.allocateDirect(2 * 4);
        bb2.order(ByteOrder.nativeOrder());
        FloatBuffer uvBuffer = bb2.asFloatBuffer();
        uvBuffer.put(new float[]{0,0});
        uvBuffer.position(0);
        defaultAttributeValues.put("uv", uvBuffer );

        ByteBuffer bb3 = ByteBuffer.allocateDirect(2 * 4);
        bb3.order(ByteOrder.nativeOrder());
        FloatBuffer uv2Buffer = bb3.asFloatBuffer();
        uv2Buffer.put(new float[]{0,0});
        uv2Buffer.position(0);
        defaultAttributeValues.put("uv2", uv2Buffer );

    }
}
