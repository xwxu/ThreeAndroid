package three.renderers.gl;

import android.opengl.GLES20;

import java.nio.IntBuffer;

public class GLCapabilities {

    public String maxPrecision = "highp";
    public int maxTextures;
    public int maxVertexTextures;
    public int maxTextureSize;
    public int maxCubemapSize;
    public int maxAttributes;
    public int maxVertexUniforms;
    public int maxVaryings;
    public int maxFragmentUniforms;
    public boolean vertexTextures;
    public boolean floatFragmentTextures = false; // not supported;
    public boolean floatVertexTextures = false;
    public boolean logarithmicDepthBuffer = false;

    public GLCapabilities(){

        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, buffer);
        maxTextures = buffer.get(0);

        GLES20.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, buffer);
        maxVertexTextures = buffer.get(0);

        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, buffer);
        maxTextureSize = buffer.get(0);

        GLES20.glGetIntegerv(GLES20.GL_MAX_CUBE_MAP_TEXTURE_SIZE, buffer);
        maxCubemapSize = buffer.get(0);

        GLES20.glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS, buffer);
        maxAttributes = buffer.get(0);

        GLES20.glGetIntegerv(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS, buffer);
        maxVertexUniforms = buffer.get(0);

        GLES20.glGetIntegerv(GLES20.GL_MAX_VARYING_VECTORS, buffer);
        maxVaryings = buffer.get(0);

        GLES20.glGetIntegerv(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS, buffer);
        maxFragmentUniforms = buffer.get(0);

        vertexTextures = maxVertexTextures > 0;
    }
}
