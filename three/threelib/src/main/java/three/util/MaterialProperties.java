package three.util;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import three.renderers.gl.GLProgram;
import three.renderers.shaders.ShaderObject;
import three.renderers.uniforms.AbstractUniform;
import three.scenes.Fog;

public class MaterialProperties {
    public Fog fog;
    public LightHash lightsHash;
    public int numClippingPlanes;
    public int numIntersection;
    public GLProgram program;
    public ShaderObject shader;
    public ArrayList<AbstractUniform> uniformsList;
    public FloatBuffer clippingState;
}
