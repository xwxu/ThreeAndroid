package three.materials;

import three.materials.parameters.MeshMatcapParameters;

public class MeshMatcapMaterial extends MeshMaterial{

    public MeshMatcapMaterial(MeshMatcapParameters parameters){
        super(parameters);
        type = "MeshMatcapMaterial";
    }
}
