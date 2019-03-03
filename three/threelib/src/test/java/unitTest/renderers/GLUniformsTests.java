package unitTest.renderers;

import org.junit.Test;

import three.renderers.gl.GLUniforms;
import three.util.ActiveInfo;

public class GLUniformsTests {

    @Test
    public void TestParse(){
        GLUniforms a = new GLUniforms(10, null);
        ActiveInfo activeInfo = new ActiveInfo();
        activeInfo.name = "matrix[2].position";
        a.ParseUniform(activeInfo, 0, a);
    }
}
