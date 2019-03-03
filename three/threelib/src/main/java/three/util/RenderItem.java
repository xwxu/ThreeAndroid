package three.util;

import three.core.BufferGeometry;
import three.core.Object3D;
import three.materials.Material;
import three.renderers.gl.GLProgram;

public class RenderItem {
    public int id;
    public Object3D object;
    public BufferGeometry geometry;
    public Material material;
    public GLProgram program;
    public int renderOrder;
    public int z;
    public GeoMatGroup group;

    public RenderItem(){

    }
}
