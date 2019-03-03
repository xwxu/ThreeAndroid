package three.materials;

import three.materials.parameters.MeshParameters;

public class MeshMaterial extends Material {

    public boolean wireframe;
    public float wireframeLinewidth;

    public MeshMaterial(MeshParameters parameters){
        super(parameters);
        wireframe = parameters.wireframe;
        wireframeLinewidth =  parameters.wireframeLinewidth;
    }
}
