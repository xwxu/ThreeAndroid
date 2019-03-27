package three.renderers.shaders;

import java.util.ArrayList;
import java.util.HashMap;

import three.math.Color;
import three.math.Matrix3;
import three.renderers.shaders.ShaderLibs.BackgroundFrag;
import three.renderers.shaders.ShaderLibs.BackgroundVert;
import three.renderers.shaders.ShaderLibs.LineDashedFrag;
import three.renderers.shaders.ShaderLibs.LineDashedVert;
import three.renderers.shaders.ShaderLibs.MeshBasicFrag;
import three.renderers.shaders.ShaderLibs.MeshBasicVert;
import three.renderers.shaders.ShaderLibs.MeshLambertFrag;
import three.renderers.shaders.ShaderLibs.MeshLambertVert;
import three.renderers.shaders.ShaderLibs.MeshMatcapFrag;
import three.renderers.shaders.ShaderLibs.MeshMatcapVert;
import three.renderers.shaders.ShaderLibs.MeshPhongFrag;
import three.renderers.shaders.ShaderLibs.MeshPhongVert;
import three.renderers.shaders.ShaderLibs.MeshPhysicalFrag;
import three.renderers.shaders.ShaderLibs.MeshPhysicalVert;
import three.renderers.shaders.ShaderLibs.NormalFrag;
import three.renderers.shaders.ShaderLibs.NormalVert;
import three.renderers.shaders.ShaderLibs.PointsFrag;
import three.renderers.shaders.ShaderLibs.PointsVert;
import three.renderers.shaders.ShaderLibs.ShadowFrag;
import three.renderers.shaders.ShaderLibs.ShadowVert;
import three.renderers.shaders.ShaderLibs.SpriteFrag;
import three.renderers.shaders.ShaderLibs.SpriteVert;

public class ShaderLib {

    public HashMap<String, ShaderObject> libs = new HashMap<>();
    private UniformsLib uniformsLib = new UniformsLib();

    public ShaderLib(){

        // Basic
        ShaderObject shaderBasic = new ShaderObject();
        ArrayList<UniformsObject> listBasic = new ArrayList<>();
        listBasic.add(uniformsLib.common);
        listBasic.add(uniformsLib.specularmap);
        listBasic.add(uniformsLib.envmap);
        listBasic.add(uniformsLib.aomap);
        listBasic.add(uniformsLib.lightmap);
        listBasic.add(uniformsLib.fog);
        shaderBasic.uniforms = UniformUtils.mergeUniforms(listBasic);
        shaderBasic.vertexShader = MeshBasicVert.code;
        shaderBasic.fragmentShader = MeshBasicFrag.code;
        libs.put("basic", shaderBasic);

        // Lambert
        ShaderObject shaderLambert = new ShaderObject();
        ArrayList<UniformsObject> listLambert = new ArrayList<>();
        listLambert.add(uniformsLib.common);
        listLambert.add(uniformsLib.specularmap);
        listLambert.add(uniformsLib.envmap);
        listLambert.add(uniformsLib.aomap);
        listLambert.add(uniformsLib.lightmap);
        listLambert.add(uniformsLib.emissivemap);
        listLambert.add(uniformsLib.fog);
        listLambert.add(uniformsLib.lights);
        shaderLambert.uniforms = UniformUtils.mergeUniforms(listLambert);
        shaderLambert.uniforms.put("emissive", new Color(0x000000));
        shaderLambert.vertexShader = MeshLambertVert.code;
        shaderLambert.fragmentShader = MeshLambertFrag.code;
        libs.put("lambert", shaderLambert);

        // Phong
        ShaderObject shaderPhong = new ShaderObject();
        ArrayList<UniformsObject> listPhong = new ArrayList<>();
        listPhong.add(uniformsLib.common);
        listPhong.add(uniformsLib.specularmap);
        listPhong.add(uniformsLib.envmap);
        listPhong.add(uniformsLib.aomap);
        listPhong.add(uniformsLib.lightmap);
        listPhong.add(uniformsLib.emissivemap);
        listPhong.add(uniformsLib.bumpmap);
        listPhong.add(uniformsLib.normalmap);
        listPhong.add(uniformsLib.displacementmap);
        listPhong.add(uniformsLib.gradientmap);
        listPhong.add(uniformsLib.fog);
        listPhong.add(uniformsLib.lights);
        shaderPhong.uniforms = UniformUtils.mergeUniforms(listPhong);
        shaderPhong.uniforms.put("emissive", new Color(0x000000));
        shaderPhong.uniforms.put("specular", new Color(0x111111));
        shaderPhong.uniforms.put("shininess", 30.0f);
        shaderPhong.vertexShader = MeshPhongVert.code;
        shaderPhong.fragmentShader = MeshPhongFrag.code;
        libs.put("phong", shaderPhong);

        // Standard
        ShaderObject shaderStandard = new ShaderObject();
        ArrayList<UniformsObject> listStandard = new ArrayList<>();
        listStandard.add(uniformsLib.common);
        listStandard.add(uniformsLib.specularmap);
        listStandard.add(uniformsLib.envmap);
        listStandard.add(uniformsLib.aomap);
        listStandard.add(uniformsLib.lightmap);
        listStandard.add(uniformsLib.emissivemap);
        listStandard.add(uniformsLib.bumpmap);
        listStandard.add(uniformsLib.normalmap);
        listStandard.add(uniformsLib.displacementmap);
        listStandard.add(uniformsLib.gradientmap);
        listStandard.add(uniformsLib.fog);
        listStandard.add(uniformsLib.lights);
        shaderStandard.uniforms = UniformUtils.mergeUniforms(listStandard);
        shaderStandard.uniforms.put("emissive", new Color(0x000000));
        shaderStandard.uniforms.put("roughness", 0.5f);
        shaderStandard.uniforms.put("metalness", 0.5f);
        shaderStandard.uniforms.put("envMapIntensity", 1.0f);
        shaderStandard.vertexShader = MeshPhysicalVert.code;
        shaderStandard.fragmentShader = MeshPhysicalFrag.code;
        libs.put("standard", shaderStandard);

        // Physical
        ShaderObject shaderPhysical = new ShaderObject();
        shaderPhysical.uniforms = UniformUtils.cloneUniforms(shaderStandard.uniforms);
        shaderPhysical.uniforms.put("clearCoat", 0.0f);
        shaderPhysical.uniforms.put("clearCoatRoughness", 0.0f);
        shaderPhysical.vertexShader = MeshPhysicalVert.code;
        shaderPhysical.fragmentShader = MeshPhysicalFrag.code;
        libs.put("physical", shaderPhysical);

        // Matcap
        ShaderObject shaderMatcap = new ShaderObject();
        ArrayList<UniformsObject> listMatcap = new ArrayList<>();
        listMatcap.add(uniformsLib.common);
        listMatcap.add(uniformsLib.bumpmap);
        listMatcap.add(uniformsLib.normalmap);
        listMatcap.add(uniformsLib.displacementmap);
        listMatcap.add(uniformsLib.fog);
        shaderMatcap.uniforms = UniformUtils.mergeUniforms(listMatcap);
        shaderMatcap.uniforms.put("matcap", null);
        shaderMatcap.vertexShader = MeshMatcapVert.code;
        shaderMatcap.fragmentShader = MeshMatcapFrag.code;
        libs.put("matcap", shaderMatcap);

        // Points
        ShaderObject shaderPoints = new ShaderObject();
        ArrayList<UniformsObject> listPoints = new ArrayList<>();
        listPoints.add(uniformsLib.points);
        listPoints.add(uniformsLib.fog);
        shaderPoints.uniforms = UniformUtils.mergeUniforms(listPoints);
        shaderPoints.vertexShader = PointsVert.code;
        shaderPoints.fragmentShader = PointsFrag.code;
        libs.put("points", shaderPoints);

        // Dashed
        ShaderObject shaderDashed = new ShaderObject();
        ArrayList<UniformsObject> listDashed = new ArrayList<>();
        listDashed.add(uniformsLib.common);
        listDashed.add(uniformsLib.fog);
        shaderDashed.uniforms = UniformUtils.mergeUniforms(listDashed);
        shaderDashed.uniforms.put("scale", 1.0f);
        shaderDashed.uniforms.put("dashSize", 1.0f);
        shaderDashed.uniforms.put("totalSize", 2.0f);
        shaderDashed.vertexShader = LineDashedVert.code;
        shaderDashed.fragmentShader = LineDashedFrag.code;
        libs.put("dashed", shaderDashed);

        // Depth
        ShaderObject shaderDepth = new ShaderObject();
        libs.put("depth", shaderDepth);

        // Normal
        ShaderObject shaderNormal = new ShaderObject();
        ArrayList<UniformsObject> listNormal = new ArrayList<>();
        listNormal.add(uniformsLib.common);
        listNormal.add(uniformsLib.bumpmap);
        listNormal.add(uniformsLib.normalmap);
        listNormal.add(uniformsLib.displacementmap);
        shaderNormal.uniforms = UniformUtils.mergeUniforms(listNormal);
        shaderNormal.uniforms.put("opacity", 1.0f);
        shaderNormal.vertexShader = NormalVert.code;
        shaderNormal.fragmentShader = NormalFrag.code;
        libs.put("normal", shaderNormal);

        // Sprite
        ShaderObject shaderSprite = new ShaderObject();
        ArrayList<UniformsObject> listSprite = new ArrayList<>();
        listSprite.add(uniformsLib.sprite);
        listSprite.add(uniformsLib.fog);
        shaderSprite.uniforms = UniformUtils.mergeUniforms(listSprite);
        shaderSprite.vertexShader = SpriteVert.code;
        shaderSprite.fragmentShader = SpriteFrag.code;
        libs.put("sprite", shaderSprite);

        // Background
        ShaderObject shaderBackground = new ShaderObject();
        shaderBackground.uniforms = new UniformsObject();
        shaderBackground.uniforms.put("uvTransform", new Matrix3());
        shaderBackground.uniforms.put("t2D", null);
        shaderBackground.vertexShader = BackgroundVert.code;
        shaderBackground.fragmentShader = BackgroundFrag.code;
        libs.put("background", shaderBackground);

        // Cube
        ShaderObject shaderCube = new ShaderObject();
        libs.put("cube", shaderCube);

        ShaderObject shaderEquirect = new ShaderObject();
        libs.put("equirect", shaderEquirect);

        ShaderObject shaderDistanceRGBA = new ShaderObject();
        libs.put("distanceRGBA", shaderDistanceRGBA);

        ShaderObject shaderShadow = new ShaderObject();
        ArrayList<UniformsObject> listShadow = new ArrayList<>();
        listShadow.add(uniformsLib.lights);
        listShadow.add(uniformsLib.fog);
        shaderShadow.uniforms = UniformUtils.mergeUniforms(listShadow);
        shaderShadow.vertexShader = ShadowVert.code;
        shaderShadow.fragmentShader = ShadowFrag.code;
        libs.put("shadow", shaderShadow);
    }

    public ShaderObject get(String name){
        return libs.get(name);
    }

}
