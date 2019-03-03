package three.renderers.gl;

import android.opengl.GLES20;

import java.nio.IntBuffer;

public class GLShader {

    public static int CreateShader(int type, String shaderStr){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderStr);
        GLES20.glCompileShader(shader);

        final int[] buffer = new int[1];
        //IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, buffer, 0);

        if(buffer[0] == 0){
            System.err.print("shader couldnt compile\n");
        }

        String log = GLES20.glGetShaderInfoLog(shader);
        System.out.println(log);

        return shader;
    }

}
