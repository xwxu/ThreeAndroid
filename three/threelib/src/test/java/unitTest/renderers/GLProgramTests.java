package unitTest.renderers;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import three.materials.Material;
import three.materials.MeshPhongMaterial;
import three.materials.parameters.MeshPhongParameters;
import three.renderers.gl.GLProgram;
import three.renderers.shaders.ShaderChunk;
import three.renderers.shaders.ShaderChunks.ShadowMapVertex;
import three.renderers.shaders.ShaderLib;
import three.renderers.shaders.ShaderLibs.MeshPhongFrag;
import three.textures.Texture;

public class GLProgramTests {
    @Test
    public void ParseIncludes(){
        String parsed = GLProgram.ParseIncludes(MeshPhongFrag.code);
        System.out.println(parsed);

    }

    @Test
    public void UnrollLoops(){
        String regex = "#pragma unroll_loop[\\s]+?for \\( int i \\= (\\d+)\\; i < (\\d+)\\; i \\+\\+ \\) \\{\\}";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(ShadowMapVertex.code);
        System.out.println(m.find());
    }

    @Test
    public void Material(){
        MeshPhongMaterial mat = new MeshPhongMaterial(new MeshPhongParameters());

        Material a = mat;
        Class cls = a.getClass();
        Field f = null;
        try {
            f = cls.getField("map");
            Texture map = (Texture) f.get(a);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {

        }

        if(f != null){
            System.out.println(f.getType());
        }
    }

}
