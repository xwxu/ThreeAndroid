package three.renderers.gl;

import java.util.ArrayList;
import java.util.HashMap;

import three.cameras.Camera;
import three.lights.Light;
import three.lights.LightShadow;
import three.lights.PointLight;
import three.lights.SpotLightShadow;
import three.materials.Material;
import three.materials.MeshDepthMaterial;
import three.materials.MeshDistanceMaterial;
import three.materials.parameters.MeshDepthParameters;
import three.materials.parameters.MeshDistanceParameters;
import three.math.Frustum;
import three.math.Matrix4;
import three.math.Vector2;
import three.math.Vector3;
import three.math.Vector4;
import three.renderers.GLRenderer;
import three.scenes.Scene;

import static three.constants.BackSide;
import static three.constants.DoubleSide;
import static three.constants.FrontSide;
import static three.constants.PCFShadowMap;
import static three.constants.RGBADepthPacking;

public class GLShadowMap {
    private GLRenderer _renderer;
    private GLObjects _objects;
    private Frustum _frustum = new Frustum();
    private Matrix4 _projScreenMatrix = new Matrix4();
    private Vector2 _shadowMapSize = new Vector2();
    private Vector2 _maxShadowMapSize;
    private Vector3 _lookTarget = new Vector3();
    private Vector3 _lightPositionWorld = new Vector3();
    private int _MorphingFlag = 1;
    private int _SkinningFlag = 2;
    private int _NumberOfMaterialVariants = (_MorphingFlag | _SkinningFlag) + 1;
    private ArrayList<Material> _depthMaterials = new ArrayList<>(_NumberOfMaterialVariants);
    private ArrayList<Material> _distanceMaterials = new ArrayList<>(_NumberOfMaterialVariants);
    private HashMap<String, Material> _materialCache = new HashMap<>();
    private HashMap<Integer, Integer> shadowSide = new HashMap<>();
    private ArrayList<Vector3> cubeDirections = new ArrayList<>();
    private ArrayList<Vector3> cubeUps = new ArrayList<>();
    private ArrayList<Vector4> cube2DViewPorts = new ArrayList<>();

    public boolean enabled = false;
    public boolean autoUpdate = true;
    public boolean needsUpdate = false;
    public int type = PCFShadowMap;

    public GLShadowMap(GLRenderer renderer, GLObjects objects, int maxTextureSize){
        this._renderer = renderer;
        this._objects = objects;
        _maxShadowMapSize = new Vector2(maxTextureSize, maxTextureSize);

        shadowSide.put(0, BackSide);
        shadowSide.put(1, FrontSide);
        shadowSide.put(2, DoubleSide);

        cubeDirections.add(new Vector3(1, 0, 0));
        cubeDirections.add(new Vector3(-1, 0, 0));
        cubeDirections.add(new Vector3(0, 0, 1));
        cubeDirections.add(new Vector3(0, 0, -1));
        cubeDirections.add(new Vector3(0, 1, 0));
        cubeDirections.add(new Vector3(0, -1, 0));

        cubeUps.add(new Vector3(0, 1, 0));
        cubeUps.add(new Vector3(0, 1, 0));
        cubeUps.add(new Vector3(0, 1, 0));
        cubeUps.add(new Vector3(0, 1, 0));
        cubeUps.add(new Vector3(0, 0, 1));
        cubeUps.add(new Vector3(0, 0, -1));

        cube2DViewPorts.add(new Vector4());
        cube2DViewPorts.add(new Vector4());
        cube2DViewPorts.add(new Vector4());
        cube2DViewPorts.add(new Vector4());
        cube2DViewPorts.add(new Vector4());
        cube2DViewPorts.add(new Vector4());

        for ( int i = 0; i != _NumberOfMaterialVariants; ++ i ) {

            boolean useMorphing = ( i & _MorphingFlag ) != 0;
            boolean useSkinning = ( i & _SkinningFlag ) != 0;

            MeshDepthParameters depthParameters = new MeshDepthParameters();
            depthParameters.depthPacking = RGBADepthPacking;
            depthParameters.morphTargets = useMorphing;
            depthParameters.skinning = useSkinning;
            Material depthMaterial = new MeshDepthMaterial( depthParameters);

            _depthMaterials.add(i, depthMaterial);

            MeshDistanceParameters distanceParameters = new MeshDistanceParameters();
            distanceParameters.morphTargets = useMorphing;
            distanceParameters.skinning = useSkinning;
            Material distanceMaterial = new MeshDistanceMaterial(distanceParameters);

            _distanceMaterials.add(i, distanceMaterial);

        }
    }

    public void Render(ArrayList<Light> lights, Scene scene, Camera camera){
//        if (!enabled) return;
//        if (!autoUpdate && !needsUpdate) return;
//
//        if ( lights.size() == 0 ) return;
//
//        // TODO Clean up (needed in case of contextlost)
//        GLState _state = _renderer.state;
//
//        // Set GL state for depth map.
//        _state.disable( _gl.BLEND );
//        _state.buffers.color.setClear( 1, 1, 1, 1 );
//        _state.buffers.depth.setTest( true );
//        _state.setScissorTest( false );
//
//        // render depth map
//
//        var faceCount;
//
//        for ( int i = 0, il = lights.size(); i < il; i ++ ) {
//
//            Light light = lights.get(i);
//            LightShadow shadow = light.shadow;
//            boolean isPointLight = light != null && light instanceof PointLight;
//
//            if ( shadow == null ) {
//                continue;
//            }
//
//            var shadowCamera = shadow.camera;
//
//            _shadowMapSize.copy( shadow.mapSize );
//            _shadowMapSize.min( _maxShadowMapSize );
//
//            if ( isPointLight ) {
//
//                var vpWidth = _shadowMapSize.x;
//                var vpHeight = _shadowMapSize.y;
//
//                // These viewports map a cube-map onto a 2D texture with the
//                // following orientation:
//                //
//                //  xzXZ
//                //   y Y
//                //
//                // X - Positive x direction
//                // x - Negative x direction
//                // Y - Positive y direction
//                // y - Negative y direction
//                // Z - Positive z direction
//                // z - Negative z direction
//
//                // positive X
//                cube2DViewPorts[ 0 ].set( vpWidth * 2, vpHeight, vpWidth, vpHeight );
//                // negative X
//                cube2DViewPorts[ 1 ].set( 0, vpHeight, vpWidth, vpHeight );
//                // positive Z
//                cube2DViewPorts[ 2 ].set( vpWidth * 3, vpHeight, vpWidth, vpHeight );
//                // negative Z
//                cube2DViewPorts[ 3 ].set( vpWidth, vpHeight, vpWidth, vpHeight );
//                // positive Y
//                cube2DViewPorts[ 4 ].set( vpWidth * 3, 0, vpWidth, vpHeight );
//                // negative Y
//                cube2DViewPorts[ 5 ].set( vpWidth, 0, vpWidth, vpHeight );
//
//                _shadowMapSize.x *= 4.0;
//                _shadowMapSize.y *= 2.0;
//
//            }
//
//            if ( shadow.map == null ) {
//
//                var pars = { minFilter: NearestFilter, magFilter: NearestFilter, format: RGBAFormat };
//
//                shadow.map = new WebGLRenderTarget( _shadowMapSize.x, _shadowMapSize.y, pars );
//                shadow.map.texture.name = light.name + ".shadowMap";
//
//                shadowCamera.updateProjectionMatrix();
//
//            }
//
//            if ( shadow instanceof SpotLightShadow) {
//
//                shadow.update( light );
//
//            }
//
//            var shadowMap = shadow.map;
//            var shadowMatrix = shadow.matrix;
//
//            _lightPositionWorld.SetFromMatrixPosition( light.matrixWorld );
//            shadowCamera.position.copy( _lightPositionWorld );
//
//            if ( isPointLight ) {
//
//                faceCount = 6;
//
//                // for point lights we set the shadow matrix to be a translation-only matrix
//                // equal to inverse of the light's position
//
//                shadowMatrix.MakeTranslation( - _lightPositionWorld.x, - _lightPositionWorld.y, - _lightPositionWorld.z );
//
//            } else {
//
//                faceCount = 1;
//
//                _lookTarget.setFromMatrixPosition( light.target.matrixWorld );
//                shadowCamera.lookAt( _lookTarget );
//                shadowCamera.updateMatrixWorld();
//
//                // compute shadow matrix
//
//                shadowMatrix.set(
//                        0.5, 0.0, 0.0, 0.5,
//                        0.0, 0.5, 0.0, 0.5,
//                        0.0, 0.0, 0.5, 0.5,
//                        0.0, 0.0, 0.0, 1.0
//                );
//
//                shadowMatrix.multiply( shadowCamera.projectionMatrix );
//                shadowMatrix.multiply( shadowCamera.matrixWorldInverse );
//
//            }
//
//            _renderer.setRenderTarget( shadowMap );
//            _renderer.clear();
//
//            // render shadow map for each cube face (if omni-directional) or
//            // run a single pass if not
//
//            for ( var face = 0; face < faceCount; face ++ ) {
//
//                if ( isPointLight ) {
//
//                    _lookTarget.copy( shadowCamera.position );
//                    _lookTarget.add( cubeDirections[ face ] );
//                    shadowCamera.up.copy( cubeUps[ face ] );
//                    shadowCamera.lookAt( _lookTarget );
//                    shadowCamera.updateMatrixWorld();
//
//                    var vpDimensions = cube2DViewPorts[ face ];
//                    _state.viewport( vpDimensions );
//
//                }
//
//                // update camera matrices and frustum
//
//                _projScreenMatrix.multiplyMatrices( shadowCamera.projectionMatrix, shadowCamera.matrixWorldInverse );
//                _frustum.setFromMatrix( _projScreenMatrix );
//
//                // set object matrices & frustum culling
//
//                renderObject( scene, camera, shadowCamera, isPointLight );
//
//            }
//
//        }
//
//        needsUpdate = false;
    }
}
