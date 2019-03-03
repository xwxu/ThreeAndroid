package three.materials;

import three.materials.parameters.MeshPhysicalParameters;

public class MeshPhysicalMaterial extends MeshStandardMaterial{

    public float reflectivity;
    public float clearCoat;
    public float clearCoatRoughness;

    public MeshPhysicalMaterial(MeshPhysicalParameters parameters){
        super(parameters);
        type = "MeshPhysicalMaterial";
        reflectivity = parameters.reflectivity;
        clearCoat = parameters.clearCoat;
        clearCoatRoughness = parameters.clearCoatRoughness;
    }
}
