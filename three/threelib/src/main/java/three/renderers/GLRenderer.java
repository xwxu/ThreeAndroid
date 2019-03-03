package three.renderers;

import android.opengl.GLES20;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import three.bufferAttribute.BufferAttribute;
import three.bufferAttribute.Float32BufferAttribute;
import three.bufferAttribute.Uint32BufferAttribute;
import three.cameras.Camera;
import three.core.BufferGeometry;
import three.core.Object3D;
import three.lights.Light;
import three.materials.LineMaterial;
import three.materials.Material;
import three.materials.MeshLambertMaterial;
import three.materials.MeshMaterial;
import three.materials.MeshNormalMaterial;
import three.materials.MeshPhongMaterial;
import three.materials.MeshPhysicalMaterial;
import three.materials.MeshStandardMaterial;
import three.materials.MeshToonMaterial;
import three.materials.ShaderMaterial;
import three.math.Color;
import three.math.Frustum;
import three.math.Matrix3;
import three.math.Matrix4;
import three.math.Plane;
import three.math.Vector2;
import three.math.Vector3;
import three.math.Vector4;

import three.objects.Line;
import three.objects.Mesh;
import three.objects.Points;
import three.objects.Sprite;
import three.renderers.gl.GLAttributes;
import three.renderers.gl.GLBackground;
import three.renderers.gl.GLBufferRenderer;
import three.renderers.gl.GLCapabilities;
import three.renderers.gl.GLClipping;
import three.renderers.gl.GLGeometries;
import three.renderers.gl.GLIndexedBufferRenderer;
import three.renderers.gl.GLInfo;
import three.renderers.gl.GLLights;
import three.renderers.gl.GLObjects;
import three.renderers.gl.GLProgram;
import three.renderers.gl.GLPrograms;
import three.renderers.gl.GLProperties;
import three.renderers.gl.GLRenderList;
import three.renderers.gl.GLRenderLists;
import three.renderers.gl.GLRenderState;
import three.renderers.gl.GLRenderStates;
import three.renderers.gl.GLShadowMap;
import three.renderers.gl.GLState;
import three.renderers.gl.GLTextures;
import three.renderers.gl.GLUniforms;
import three.renderers.shaders.ShaderLib;
import three.renderers.shaders.ShaderObject;
import three.renderers.shaders.UniformUtils;
import three.renderers.shaders.UniformsObject;
import three.renderers.uniforms.AbstractUniform;
import three.renderers.uniforms.SingleUniform;
import three.scenes.Fog;
import three.scenes.Scene;
import three.textures.CubeTexture;
import three.textures.Texture;
import three.util.BufferData;
import three.util.GeoMatGroup;
import three.util.LightHash;
import three.util.MaterialProperties;
import three.util.Parameters;
import three.util.RenderItem;
import three.util.RendererParameters;

import static three.constants.BackSide;
import static three.constants.LinearToneMapping;
import static three.constants.TriangleFanDrawMode;
import static three.constants.TriangleStripDrawMode;
import static three.constants.TrianglesDrawMode;

public class GLRenderer {

    private int _width;
    private int _height;
    private boolean _alpha;
    private boolean _depth;
    private boolean _stencil;
    private boolean _antialias;
    private boolean _premultipliedAlpha;
    private boolean _preserveDrawingBuffer;
    private String _powerPreference;

    private GLRenderList currentRenderList;
    private GLRenderState currentRenderState;

    public boolean autoClear = true;
    public boolean autoClearColor = true;
    public boolean autoClearDepth = true;
    public boolean autoClearStencil = true;
    public boolean sortObjects = true;
    public ArrayList<Plane> clippingPlanes = new ArrayList();
    public boolean localClippingEnabled = false;
    public float gammaFactor = 2.0f;
    public boolean gammaInput = false;
    public boolean gammaOutput = false;
    public boolean physicallyCorrectLights = false;
    public int toneMapping = LinearToneMapping;
    public float toneMappingExposure = 1.0f;
    public float toneMappingWhitePoint = 1.0f;
    public int maxMorphTargets = 8;
    public int maxMorphNormals = 4;
    private int _framebuffer;
    private GLRenderTarget _currentRenderTarget = null;
    private int _currentFrameBuffer = -1;
    private int _currentMaterialId = -1;

    // cache for render frame
    private int _currentGeometryProgram_geometry = -1;
    private int _currentGeometryProgram_program = -1;
    private boolean _currentGeometryProgram_wireframe = false;
    private Camera _currentArrayCamera;
    private Camera _currentCamera;

    private Vector4 _currentViewport = new Vector4();
    private Vector4 _currentScissor = new Vector4();
    private boolean _currentScissorTest = false;
    private int _usedTextureUnits = 0;
    private float _pixelRatio = 1;
    private Vector4 _viewport;
    private Vector4 _scissor;
    private boolean _scissorTest = false;
    private Frustum _frustum = new Frustum();
    private GLClipping _clipping = new GLClipping();
    private boolean _clippingEnabled = false;
    private boolean _localClippingEnabled = false;
    private Matrix4 _projScreenMatrix = new Matrix4();
    private Vector3 _vector3 = new Vector3();

    public GLState state;
    public GLInfo info;
    public GLCapabilities capabilities;
    public GLProperties properties;
    public GLTextures textures;
    public GLAttributes attributes;
    public GLGeometries geometries;
    public GLObjects objects;
    public GLPrograms programCache;
    public GLRenderLists renderLists;
    public GLRenderStates renderStates;
    private GLBackground background;
    private GLBufferRenderer bufferRenderer;
    private GLIndexedBufferRenderer indexedBufferRenderer;

    public GLShadowMap shadowMap;
    private ShaderLib shaderLib = new ShaderLib();

    public GLRenderer(RendererParameters parameters){
        this._width = parameters.width;
        this._height = parameters.height;
        this._alpha = parameters.alpha;
        this._depth = parameters.depth;
        this._stencil = parameters.stencil;
        this._antialias = parameters.antialias;
        this._premultipliedAlpha = parameters.premultipliedAlpha;
        this._preserveDrawingBuffer = parameters.preserveDrawingBuffer;
        this._powerPreference = parameters.powerPreference;

        InitGLContext();
    }

    public void InitGLContext(){
        state = new GLState();
        _viewport = new Vector4(0, 0, _width, _height);
        _scissor = new Vector4(0, 0, _width, _height);
        state.Scissor(_currentScissor.Copy(_scissor).MultiplyScalar(_pixelRatio));
        state.Viewport(_currentViewport.Copy(_viewport).MultiplyScalar(_pixelRatio));

        info = new GLInfo();
        properties = new GLProperties();
        capabilities = new GLCapabilities();
        textures = new GLTextures();
        attributes = new GLAttributes();
        geometries = new GLGeometries(attributes, info);
        objects = new GLObjects(geometries, info);
        programCache = new GLPrograms(this, capabilities);
        renderLists = new GLRenderLists();
        renderStates = new GLRenderStates();
        background = new GLBackground(this, state, objects, _premultipliedAlpha);
        bufferRenderer = new GLBufferRenderer(info, capabilities);
        indexedBufferRenderer = new GLIndexedBufferRenderer(info, capabilities);

        shadowMap = new GLShadowMap(this, objects, capabilities.maxTextureSize);
    }


    public void SetRenderTarget(GLRenderTarget renderTarget){
        //TODO
        //GLES20.glBindFramebuffer(1, _framebuffer);
    }

    private float GetTargetPixelRatio(){
        return _currentRenderTarget == null ? _pixelRatio : 1;
    }


    public void Render(Scene scene, Camera camera, GLRenderTarget renderTarget, boolean forceClear) throws IllegalAccessException {

        // reset caching for this frame;
        _currentGeometryProgram_geometry = 0;
        _currentGeometryProgram_program = 0;
        _currentGeometryProgram_wireframe = false;
        _currentMaterialId = - 1;
        _currentCamera = null;

        // update scene graph
        if ( scene.autoUpdate == true ) scene.UpdateMatrixWorld(true);

        if ( camera.parent == null ) camera.UpdateMatrixWorld(true);

        currentRenderState = renderStates.Get(scene, camera);
        currentRenderState.Init();

        //scene.OnBeforeRender( this, scene, camera, renderTarget );

        _projScreenMatrix.MultiplyMatrices( camera.projectionMatrix, camera.matrixWorldInverse );
        _frustum.SetFromMatrix( _projScreenMatrix );

        _localClippingEnabled = this.localClippingEnabled;
        _clippingEnabled = _clipping.Init( this.clippingPlanes, _localClippingEnabled, camera );

        currentRenderList = renderLists.Get( scene, camera );
        currentRenderList.Init();

        ProjectObject( scene, camera, this.sortObjects );

        if (sortObjects) {
            currentRenderList.Sort();
        }

        if ( _clippingEnabled ) _clipping.BeginShadows();
        ArrayList<Light> shadowsArray = currentRenderState.shadowsArray;
        shadowMap.Render( shadowsArray, scene, camera );
        currentRenderState.SetupLights( camera );
        if ( _clippingEnabled ) _clipping.EndShadows();


        if ( this.info.autoReset ) this.info.Reset();

        this.SetRenderTarget( renderTarget );

        // render background
        background.Render( currentRenderList, scene, camera, forceClear );

        // render scene
        ArrayList<RenderItem> opaqueObjects = currentRenderList.opaque;
        ArrayList<RenderItem> transparentObjects = currentRenderList.transparent;

        if ( scene.overrideMaterial != null) {

            Material overrideMaterial = scene.overrideMaterial;

            if ( opaqueObjects.size() > 0 ) RenderObjects( opaqueObjects, scene, camera, overrideMaterial );
            if ( transparentObjects.size() > 0 ) RenderObjects( transparentObjects, scene, camera, overrideMaterial );

        } else {

            // opaque pass (front-to-back order)
            if ( opaqueObjects.size() > 0 ) RenderObjects( opaqueObjects, scene, camera, null );

            // transparent pass (back-to-front order)
            if ( transparentObjects.size() > 0 ) RenderObjects( transparentObjects, scene, camera, null );

        }

        // Generate mipmap if we're using any kind of mipmap filtering
        if ( renderTarget != null ) {
            textures.UpdateRenderTargetMipmap( renderTarget );
        }

        // Ensure depth buffer writing is enabled so it can be cleared on next render

        state.depthBuffer.SetTest( true );
        state.depthBuffer.SetMask( true );
        state.colorBuffer.SetMask( true );

        state.SetPolygonOffset( false, 0, 0 );

        currentRenderList = null;
        currentRenderState = null;
    }

    private void RenderObjects(ArrayList<RenderItem> renderList, Scene scene, Camera camera, Material overrideMaterial) throws IllegalAccessException {
        for ( int i = 0, l = renderList.size(); i < l; i ++ ) {

            RenderItem renderItem = renderList.get(i);

            Object3D object = renderItem.object;
            BufferGeometry geometry = renderItem.geometry;
            Material material = overrideMaterial == null ? renderItem.material : overrideMaterial;
            GeoMatGroup group = renderItem.group;

            //TODO: array camera

            _currentArrayCamera = null;
            RenderObject( object, scene, camera, geometry, material, group );
        }
    }

    private void RenderObject(Object3D object, Scene scene, Camera camera, BufferGeometry geometry, Material material, GeoMatGroup group) throws IllegalAccessException {
        currentRenderState = renderStates.Get( scene, camera );

        object.modelViewMatrix.MultiplyMatrices( camera.matrixWorldInverse, object.matrixWorld );
        object.normalMatrix.GetNormalMatrix( object.modelViewMatrix );

        // TODO: immediateRenderObject
        if ( false ) {

        } else {
            RenderBufferDirect( camera, scene.fog, geometry, material, object, group );
        }

        //object.onAfterRender( _this, scene, camera, geometry, material, group );
        currentRenderState = renderStates.Get( scene, camera );

    }

    public void RenderBufferDirect(Camera camera, Fog fog, BufferGeometry geometry, Material material, Object3D object, GeoMatGroup group) throws IllegalAccessException {
        boolean frontFaceCW = ( object instanceof Mesh && object.normalMatrix.Determinant() < 0 );

        state.SetMaterial( material, frontFaceCW );

        GLProgram program = SetProgram( camera, fog, material, object );

        boolean updateBuffers = false;

        if ( _currentGeometryProgram_geometry != geometry.id ||
                _currentGeometryProgram_program != program.id ||
                _currentGeometryProgram_wireframe != (material.wireframe) ) {

            _currentGeometryProgram_geometry = geometry.id;
            _currentGeometryProgram_program = program.id;
            _currentGeometryProgram_wireframe = material.wireframe;
            updateBuffers = true;

        }

        // TODO: update morphTarget

        Uint32BufferAttribute index = geometry.index;
        Float32BufferAttribute position = (Float32BufferAttribute) geometry.attributes.get("position");
        int rangeFactor = 1;

        if (material.wireframe) {
            index = geometries.GetWireframeAttribute( geometry );
            rangeFactor = 2;
        }

        BufferData attribute = null;
        GLBufferRenderer renderer = bufferRenderer;

        if ( index != null ) {
            attribute = attributes.Get( index );
            indexedBufferRenderer.SetIndex( attribute );
            renderer = indexedBufferRenderer;
        }

        if ( updateBuffers ) {
            SetupVertexAttributes( material, program, geometry );
            if ( index != null ) {
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, attribute.buffer[0]);
            }
        }

        int dataCount = Integer.MAX_VALUE;

        if ( index != null ) {
            dataCount = index.count;
        } else if ( position != null ) {
            dataCount = position.count;
        }

        int rangeStart = geometry.drawRange.start * rangeFactor;
        int rangeCount = geometry.drawRange.count * rangeFactor;

        int groupStart = group != null ? group.start * rangeFactor : 0;
        int groupCount = group != null ? group.count * rangeFactor : Integer.MAX_VALUE;

        int drawStart = Math.max( rangeStart, groupStart );
        int drawEnd = Math.min( dataCount, Math.min(rangeStart + rangeCount, groupStart + groupCount )) - 1;

        int drawCount = Math.max( 0, drawEnd - drawStart + 1 );

        if ( drawCount == 0 ) return;

        if ( object instanceof Mesh ) {
            MeshMaterial meshmat = (MeshMaterial)material;
            if (material.wireframe) {

                state.SetLineWidth( meshmat.wireframeLinewidth * GetTargetPixelRatio() );
                renderer.SetMode( GLES20.GL_LINES );

            } else {
                switch ( ((Mesh)object).drawMode ) {
                    case TrianglesDrawMode:
                        renderer.SetMode( GLES20.GL_TRIANGLES );
                        break;
                    case TriangleStripDrawMode:
                        renderer.SetMode( GLES20.GL_TRIANGLE_STRIP );
                        break;
                    case TriangleFanDrawMode:
                        renderer.SetMode( GLES20.GL_TRIANGLE_FAN );
                        break;
                }
            }

        } else if ( object instanceof Line ) {

            LineMaterial linemat = (LineMaterial) material;
            float lineWidth = linemat.linewidth;

            state.SetLineWidth( lineWidth * GetTargetPixelRatio() );

            if (object.type.equals("LineSegments")) {
                renderer.SetMode( GLES20.GL_LINES );

            } else if (object.type.equals("LineLoop")) {
                renderer.SetMode( GLES20.GL_LINE_LOOP );

            } else {
                renderer.SetMode( GLES20.GL_LINE_STRIP );
            }

        } else if ( object instanceof Points ) {
            renderer.SetMode( GLES20.GL_POINTS );

        } else if ( object instanceof Sprite ) {
            renderer.SetMode(GLES20.GL_TRIANGLES ); 

        }

        // TODO: draw instances

        renderer.Render( drawStart, drawCount );

    }

    private void SetupVertexAttributes(Material material, GLProgram program, BufferGeometry geometry) {

        state.InitAttributes();

        HashMap<String, BufferAttribute> geometryAttributes = geometry.attributes;
        HashMap<String, Integer> programAttributes = program.GetAttributes();

        Field defaultAttributeField = material.GetProperty("defaultAttributeValues");

        for (Object o : programAttributes.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;

            String name = (String) pair.getKey();
            int programAttribute = (int) pair.getValue();

            if (programAttribute >= 0) {
                BufferAttribute geometryAttribute = geometryAttributes.get(name);
                if (geometryAttribute != null) {

                    boolean normalized = geometryAttribute.normalized;
                    int size = geometryAttribute.itemSize;

                    BufferData attribute = attributes.Get(geometryAttribute);

                    if (attribute == null) continue;

                    int buffer = attribute.buffer[0];
                    int type = attribute.type;
                    int bytesPerElement = attribute.bytesPerElement;

                    if (geometryAttribute.type.equals("InterleavedBufferAttribute")) {
                        // TODO: interleaved BufferAttribute
                    } else {

                        if (geometryAttribute.type.equals("InstancedBufferAttribute")) {
                            // TODO: instanced BufferAttribute

                        } else {
                            state.EnableAttribute(programAttribute);
                        }

                        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffer);
                        GLES20.glVertexAttribPointer(programAttribute, size, type, normalized, 0, 0);

                    }

                } else if (defaultAttributeField != null) {

                    HashMap<String, FloatBuffer> materialDefaultAttributeValues = null;
                    FloatBuffer value = null;
                    try {
                        materialDefaultAttributeValues = (HashMap<String, FloatBuffer>) defaultAttributeField.get(material);
                        value = materialDefaultAttributeValues.get(name);
                    } catch (IllegalAccessException ignored) {
                    }

                    if (value != null) {
                        switch (value.capacity()) {
                            case 2:
                                GLES20.glVertexAttrib2fv(programAttribute, value);
                                break;
                            case 3:
                                GLES20.glVertexAttrib3fv(programAttribute, value);
                                break;
                            case 4:
                                GLES20.glVertexAttrib4fv(programAttribute, value);
                                break;
                            default:
                                GLES20.glVertexAttrib1fv(programAttribute, value);
                        }
                    }

                }

            }
        }

        state.DisableUnusedAttributes();

    }

    private GLProgram SetProgram(Camera camera, Fog fog, Material material, Object3D object) throws IllegalAccessException {
        _usedTextureUnits = 0;

        MaterialProperties materialProperties = properties.GetMaterial(material);
        GLLights lights = currentRenderState.lights;

        LightHash lightsHash = materialProperties.lightsHash;

        if ( _clippingEnabled ) {
            if ( _localClippingEnabled || camera != _currentCamera ) {
                boolean useCache = camera == _currentCamera &&  material.id == _currentMaterialId;

                _clipping.SetState(
                        material.clippingPlanes, material.clipIntersection, material.clipShadows,
                        camera, materialProperties.clippingState, useCache );
            }
        }

        if (!material.needsUpdate) {

            if ( materialProperties.program == null ) {
                material.needsUpdate = true;

            } else if ( material.fog && materialProperties.fog != fog ) {

                material.needsUpdate = true;

            } else if ( material.lights && ( lightsHash.stateID != lights.hash_stateID ||
                    lightsHash.directionalLength != lights.hash_directionalLength ||
                    lightsHash.pointLength != lights.hash_pointLength ||
                    lightsHash.spotLength != lights.hash_spotLength ||
                    lightsHash.rectAreaLength != lights.hash_rectAreaLength ||
                    lightsHash.hemiLength != lights.hash_hemiLength ||
                    lightsHash.shadowsLength != lights.hash_shadowsLength ) ) {

                material.needsUpdate = true;

            } else if ( materialProperties.numClippingPlanes != 0 &&
                    ( materialProperties.numClippingPlanes != _clipping.numPlanes ||
                            materialProperties.numIntersection != _clipping.numIntersection ) ) {

                material.needsUpdate = true;
            }
        }

        if ( material.needsUpdate ) {
            InitMaterial( material, fog, object );
            material.needsUpdate = false;
        }

        boolean refreshProgram = false;
        boolean refreshMaterial = false;
        boolean refreshLights = false;

        GLProgram program = materialProperties.program;
        GLUniforms p_uniforms = program.GetUniforms();
        UniformsObject m_uniforms = materialProperties.shader.uniforms;

        if ( state.UseProgram( program.program ) ) {
            refreshProgram = true;
            refreshMaterial = true;
            refreshLights = true;
        }

        if ( material.id != _currentMaterialId ) {
            _currentMaterialId = material.id;
            refreshMaterial = true;
        }

        if ( refreshProgram || _currentCamera != camera ) {

            p_uniforms.SetValue4fm("projectionMatrix", camera.projectionMatrix);

//            if ( capabilities.logarithmicDepthBuffer ) {
//                p_uniforms.SetValue(  "logDepthBufFC",
//                        2.0 / ( Math.log( camera.far + 1.0 ) / Math_.LN2 ) );
//            }

            if (_currentCamera != camera) {
                _currentCamera = camera;
                refreshMaterial = true;        // set to true on material change
                refreshLights = true;        // remains set until update done
            }

            // load material specific uniforms
            // (shader material also gets them for the sake of genericity)

            if (material.type.equals("ShaderMaterial") ||
                    material.type.equals("MeshPhongMaterial") ||
                    material.type.equals("MeshStandardMaterial") ||
                    material.GetProperty("envMap") != null) {

                SingleUniform uCamPos = (SingleUniform) p_uniforms.map.get("cameraPosition");

                if (uCamPos != null) {
                    uCamPos.SetValue3fv(_vector3.SetFromMatrixPosition(camera.matrixWorld));
                }

            }

            if (material.type.equals("MeshPhongMaterial") ||
                    material.type.equals("MeshLambertMaterial") ||
                    material.type.equals("MeshBasicMaterial") ||
                    material.type.equals("MeshStandardMaterial") ||
                    material.type.equals("ShaderMaterial")) {

                p_uniforms.SetValue4fm("viewMatrix", camera.matrixWorldInverse);

            }
        }

            // TODO: skinning

        if ( refreshMaterial ) {

            p_uniforms.SetValue1f(  "toneMappingExposure", toneMappingExposure );
            p_uniforms.SetValue1f(  "toneMappingWhitePoint", toneMappingWhitePoint );

            if ( material.lights ) {
                MarkUniformsLightsNeedsUpdate( m_uniforms, refreshLights );
            }

            // refresh uniforms common to several materials
            if ( fog != null && material.fog ) {
                RefreshUniformsFog( m_uniforms, fog );
            }

            if (material.type.equals("MeshBasicMaterial")) {
                RefreshUniformsCommon( m_uniforms, material );

            } else if (material.type.equals("MeshLambertMaterial")) {

                RefreshUniformsCommon( m_uniforms, material );
                RefreshUniformsLambert( m_uniforms, material );

            } else if (material.type.equals("MeshPhongMaterial")) {
                RefreshUniformsCommon( m_uniforms, material );

                if (material.type.equals("MeshToonMaterial")) {
                    RefreshUniformsToon( m_uniforms, material );

                } else {
                    RefreshUniformsPhong( m_uniforms, material );
                }

            } else if (material.type.equals("MeshStandardMaterial")) {
                RefreshUniformsCommon( m_uniforms, material );
                if (material.type.equals("MeshPhysicalMaterial")) {
                    RefreshUniformsPhysical( m_uniforms, material );
                } else {
                    RefreshUniformsStandard( m_uniforms, material );
                }

            } else if (material.type.equals("MeshMatcapMaterial")) {
                RefreshUniformsCommon( m_uniforms, material );
                RefreshUniformsMatcap( m_uniforms, material );

            } else if (material.type.equals("MeshDepthMaterial")) {
                RefreshUniformsCommon( m_uniforms, material );
                RefreshUniformsDepth( m_uniforms, material );

            } else if (material.type.equals("MeshDistanceMaterial")) {
                RefreshUniformsCommon( m_uniforms, material );
                RefreshUniformsDistance( m_uniforms, material );

            } else if (material.type.equals("MeshNormalMaterial")) {
                RefreshUniformsCommon( m_uniforms, material );
                RefreshUniformsNormal( m_uniforms, material );

            } else if (material.type.equals("LineBasicMaterial")) {
                RefreshUniformsLine( m_uniforms, material );
                if (material.type.equals("LineDashedMaterial")) {
                    RefreshUniformsDash( m_uniforms, material );
                }

            } else if (material.type.equals("PointsMaterial")) {
                RefreshUniformsPoints( m_uniforms, material );

            } else if (material.type.equals("SpriteMaterial")) {
                RefreshUniformsSprites( m_uniforms, material );

            } else if (material.type.equals("ShadowMaterial")) {
                Field colorField = material.GetProperty("color");
                m_uniforms.Put("color", colorField.get(material));
                m_uniforms.Put("opacity", material.opacity);

            }

            GLUniforms.Upload( materialProperties.uniformsList, m_uniforms, this );
        }

        if (material.type.equals("ShaderMaterial") && ((ShaderMaterial) material).uniformsNeedUpdate) {
            GLUniforms.Upload( materialProperties.uniformsList, m_uniforms, this );
            ((ShaderMaterial)material).uniformsNeedUpdate = false;
        }

        if (material.type.equals("SpriteMaterial")) {
            p_uniforms.SetValue2fv("center", ((Sprite)object).center );
        }

        // common matrices
        p_uniforms.SetValue4fm( "modelViewMatrix", object.modelViewMatrix );
        p_uniforms.SetValue3fm( "normalMatrix", object.normalMatrix );
        p_uniforms.SetValue4fm( "modelMatrix", object.matrixWorld );

        return program;
    }

    private void RefreshUniformsCommon(UniformsObject uniforms, Material material) throws IllegalAccessException {
        uniforms.Put("opacity", material.opacity);

        Field colorField = material.GetProperty("color");
        if ( colorField != null ) {
            uniforms.Put("diffuse", colorField.get(material));
        }

        Field emissiveField = material.GetProperty("emissive");
        if ( emissiveField != null ) {
            Color emissive = (Color) emissiveField.get(material);
            Field emissiveIntensityField = material.GetProperty("emissiveIntensity");
            float emissiveIntensity = (float) emissiveIntensityField.get(material);
            ((Color)uniforms.Get("emissive")).Copy( emissive.MultiplyScalar( emissiveIntensity ));
        }

        Field mapField = material.GetProperty("map");
        if ( mapField != null ) {
            uniforms.Put("map", mapField.get(material));
        }

        Field alphaMapField = material.GetProperty("alphaMap");
        if ( alphaMapField != null ) {
            uniforms.Put("alphaMap", alphaMapField.get(material));
        }

        Field specularMapField = material.GetProperty("specularMap");
        if ( specularMapField != null ) {
            uniforms.Put("specularMap", specularMapField.get(material));
        }

        Field envMapField = material.GetProperty("envMap");
        if ( envMapField != null ) {
            Texture envMap = (Texture) envMapField.get(material);
            uniforms.Put("envMap", envMap);

            // don't flip CubeTexture envMaps, flip everything else:
            //  WebGLRenderTargetCube will be flipped for backwards compatibility
            //  WebGLRenderTargetCube.texture will be flipped because it's a Texture and NOT a CubeTexture
            // this check must be handled differently, or removed entirely, if WebGLRenderTargetCube uses a CubeTexture in the future

            uniforms.Put("flipEnvMap", envMap instanceof CubeTexture ? - 1 : 1);

            Field reflectivityField = material.GetProperty("reflectivity");
            if(reflectivityField != null){
                uniforms.Put("reflectivity", reflectivityField.get(material));
            }

            Field refractionRatioField = material.GetProperty("refractionRatio");
            if(refractionRatioField != null){
                uniforms.Put("refractionRatio", refractionRatioField.get(material));
            }

            uniforms.Put("maxMipLevel", properties.GetTexture( envMap ).__maxMipLevel);
        }

        Field lightMapField = material.GetProperty("lightMap");
        if ( lightMapField != null ) {
            Field lightMapIntensityField = material.GetProperty("lightMapIntensity");
            float lightMapIntensity = (float) lightMapIntensityField.get(material);
            uniforms.Put("lightMap", lightMapField.get(material));
            uniforms.Put("lightMapIntensity", lightMapIntensity);
        }

        Field aoMapField = material.GetProperty("aoMap");
        if ( aoMapField != null ) {
            Field aoMapIntensityField = material.GetProperty("aoMapIntensity");
            float aoMapIntensity = (float) aoMapIntensityField.get(material);
            uniforms.Put("aoMap", alphaMapField.get(material));
            uniforms.Put("aoMapIntensity", aoMapIntensity);
        }

        // uv repeat and offset setting priorities
        // 1. color map
        // 2. specular map
        // 3. normal map
        // 4. bump map
        // 5. alpha map
        // 6. emissive map

        Texture uvScaleMap = null;
        Field displacementMapField = material.GetProperty("displacementMap");
        Field normalMapField = material.GetProperty("normalMap");
        Field bumpMapField = material.GetProperty("bumpMap");
        Field roughnessMapField = material.GetProperty("roughnessMap");
        Field metalnessMapField = material.GetProperty("metalnessMap");

        if ( mapField != null ) {
            uvScaleMap = (Texture) mapField.get(material);

        } else if ( specularMapField != null ) {
            uvScaleMap = (Texture) specularMapField.get(material);

        } else if ( displacementMapField != null ) {
            uvScaleMap = (Texture) displacementMapField.get(material);

        } else if ( normalMapField != null ) {
            uvScaleMap = (Texture) normalMapField.get(material);

        } else if ( bumpMapField != null ) {
            uvScaleMap = (Texture) bumpMapField.get(material);

        } else if ( roughnessMapField != null ) {
            uvScaleMap = (Texture) roughnessMapField.get(material);

        } else if ( metalnessMapField != null ) {
            uvScaleMap = (Texture) metalnessMapField.get(material);

        } else if ( alphaMapField != null ) {
            uvScaleMap = (Texture) alphaMapField.get(material);

        } else if ( emissiveField != null ) {
            uvScaleMap = (Texture) emissiveField.get(material);

        }

        if ( uvScaleMap != null ) {
            if (uvScaleMap.matrixAutoUpdate) {
                uvScaleMap.UpdateMatrix();
            }
            ((Matrix3)uniforms.Get("unTransform")).Copy( uvScaleMap.matrix );
        }
    }

    private void RefreshUniformsPoints(UniformsObject uniforms, Material material) {

    }

    private void RefreshUniformsSprites(UniformsObject uniforms, Material material) {
    }

    private void RefreshUniformsDash(UniformsObject uniforms, Material material) {
    }

    private void RefreshUniformsLine(UniformsObject uniforms, Material material) throws IllegalAccessException {
        Field colorField = material.GetProperty("color");
        if ( colorField != null ) {
            uniforms.Put("diffuse", colorField.get(material));
        }
        uniforms.Put("opacity", material.opacity);
    }

    private void RefreshUniformsNormal(UniformsObject uniforms, Material material) {
        MeshNormalMaterial meshNormalMaterial = (MeshNormalMaterial) material;
        if ( meshNormalMaterial.bumpMap != null ) {
            uniforms.Put("bumpMap", meshNormalMaterial.bumpMap);
            if(material.side == BackSide){
                uniforms.Put("bumpScale", meshNormalMaterial.bumpScale * -1);
            }else{
                uniforms.Put("bumpScale", meshNormalMaterial.bumpScale);
            }
        }

        if ( meshNormalMaterial.normalMap != null) {
            uniforms.Put("normalMap", meshNormalMaterial.normalMap);
            if(material.side == BackSide){
                ((Vector2)uniforms.Get("normalScale")).Copy(meshNormalMaterial.normalScale).Negate();
            }else{
                ((Vector2)uniforms.Get("normalScale")).Copy(meshNormalMaterial.normalScale);
            }
        }

        if ( meshNormalMaterial.displacementMap != null) {
            uniforms.Put("displacementMap", meshNormalMaterial.displacementMap);
            uniforms.Put("displacementScale", meshNormalMaterial.displacementScale);
            uniforms.Put("displacementBias", meshNormalMaterial.displacementBias);
        }
    }

    private void RefreshUniformsDistance(UniformsObject uniforms, Material material) {
    }

    private void RefreshUniformsDepth(UniformsObject uniforms, Material material) {
    }

    private void RefreshUniformsMatcap(UniformsObject m_uniforms, Material material) {
    }

    private void RefreshUniformsStandard(UniformsObject uniforms, Material material) {
        MeshStandardMaterial meshStandardMaterial = (MeshStandardMaterial) material;
        uniforms.Put("roughness", meshStandardMaterial.roughness);
        uniforms.Put("metalness", meshStandardMaterial.metalness);

        if ( meshStandardMaterial.roughnessMap != null ) {
            uniforms.Put("roughnessMap", meshStandardMaterial.roughnessMap);
        }

        if ( meshStandardMaterial.metalnessMap != null ) {
            uniforms.Put("metalnessMap", meshStandardMaterial.metalnessMap);
        }

        if ( meshStandardMaterial.emissiveMap != null ) {
            uniforms.Put("emissiveMap", meshStandardMaterial.emissiveMap);
        }

        if ( meshStandardMaterial.bumpMap != null ) {
            uniforms.Put("bumpMap", meshStandardMaterial.bumpMap);
            if(material.side == BackSide){
                uniforms.Put("bumpScale", meshStandardMaterial.bumpScale * -1);
            }else{
                uniforms.Put("bumpScale", meshStandardMaterial.bumpScale);
            }
        }

        if ( meshStandardMaterial.normalMap != null) {
            uniforms.Put("normalMap", meshStandardMaterial.normalMap);
            if(material.side == BackSide){
                ((Vector2)uniforms.Get("normalScale")).Copy(meshStandardMaterial.normalScale).Negate();
            }else{
                ((Vector2)uniforms.Get("normalScale")).Copy(meshStandardMaterial.normalScale);
            }
        }

        if ( meshStandardMaterial.displacementMap != null) {
            uniforms.Put("displacementMap", meshStandardMaterial.displacementMap);
            uniforms.Put("displacementScale", meshStandardMaterial.displacementScale);
            uniforms.Put("displacementBias", meshStandardMaterial.displacementBias);
        }

        if ( meshStandardMaterial.envMap != null ) {
            uniforms.Put("envMapIntensity", meshStandardMaterial.envMapIntensity);
        }
    }

    private void RefreshUniformsPhysical(UniformsObject uniforms, Material material) {
        RefreshUniformsStandard( uniforms, material );

        MeshPhysicalMaterial meshPhysicalMaterial = (MeshPhysicalMaterial) material;
        uniforms.Put("clearCoat", meshPhysicalMaterial.clearCoat);
        uniforms.Put("clearCoatRoughness", meshPhysicalMaterial.clearCoatRoughness);
    }

    private void RefreshUniformsPhong(UniformsObject uniforms, Material material) {
        MeshPhongMaterial meshPhongMaterial = (MeshPhongMaterial) material;
        uniforms.Put("specular", meshPhongMaterial.specular);
        uniforms.Put("shininess", (float)Math.max( meshPhongMaterial.shininess, 1e-4 ));

        if ( meshPhongMaterial.emissiveMap != null ) {
            uniforms.Put("emissiveMap", meshPhongMaterial.emissiveMap);
        }

        if ( meshPhongMaterial.bumpMap != null ) {
            uniforms.Put("bumpMap", meshPhongMaterial.bumpMap);
            if(material.side == BackSide){
                uniforms.Put("bumpScale", meshPhongMaterial.bumpScale * -1);
            }else{
                uniforms.Put("bumpScale", meshPhongMaterial.bumpScale);
            }
        }

        if ( meshPhongMaterial.normalMap != null) {
            uniforms.Put("normalMap", meshPhongMaterial.normalMap);
            if(material.side == BackSide){
                ((Vector2)uniforms.Get("normalScale")).Copy(meshPhongMaterial.normalScale).Negate();
            }else{
                ((Vector2)uniforms.Get("normalScale")).Copy(meshPhongMaterial.normalScale);
            }
        }

        if ( meshPhongMaterial.displacementMap != null) {
            uniforms.Put("displacementMap", meshPhongMaterial.displacementMap);
            uniforms.Put("displacementScale", meshPhongMaterial.displacementScale);
            uniforms.Put("displacementBias", meshPhongMaterial.displacementBias);
        }
    }

    private void RefreshUniformsToon(UniformsObject uniforms, Material material) {
        RefreshUniformsPhong( uniforms, material );

        MeshToonMaterial meshToonMaterial = (MeshToonMaterial)material;
        if ( meshToonMaterial.gradientMap != null ) {
            uniforms.Put("gradientMap", meshToonMaterial.gradientMap);
        }
    }

    private void RefreshUniformsLambert(UniformsObject uniforms, Material material) {
        MeshLambertMaterial meshLambertMaterial = (MeshLambertMaterial) material;
        if ( meshLambertMaterial.emissiveMap != null ) {
            uniforms.Put("emissiveMap", meshLambertMaterial.emissiveMap);
        }
    }

    private void RefreshUniformsFog(UniformsObject uniforms, Fog fog) {
        uniforms.Put("fogColor", fog.color);

        uniforms.Put("fogNear", fog.near);
        uniforms.Put("fogFar", fog.far);

        // TODO: FogExp2
    }

    private void MarkUniformsLightsNeedsUpdate(UniformsObject uniformsObject, boolean value) {
        uniformsObject.uniforms.get("ambientLightColor").needsUpdate = value;
        uniformsObject.uniforms.get("directionalLights").needsUpdate = value;
        uniformsObject.uniforms.get("pointLights").needsUpdate = value;
        uniformsObject.uniforms.get("spotLights").needsUpdate = value;
        uniformsObject.uniforms.get("rectAreaLights").needsUpdate = value;
        uniformsObject.uniforms.get("hemisphereLights").needsUpdate = value;
    }

    private void InitMaterial(Material material, Fog fog, Object3D object) {
        MaterialProperties materialProperties = properties.GetMaterial(material);

        GLLights lights = currentRenderState.lights;
        ArrayList<Light> shadowsArray = currentRenderState.shadowsArray;

        LightHash lightsHash = materialProperties.lightsHash;

        Parameters parameters = programCache.GetParameters(
                material, lights, shadowsArray, fog, _clipping.numPlanes, _clipping.numIntersection, object );

        String code = programCache.GetProgramCode( material, parameters );

        GLProgram program = materialProperties.program;
        boolean programChange = true;

        if ( program == null ) {
            // new material
            //material.addEventListener( 'dispose', onMaterialDispose );

        } else if (!program.code.equals(code)) {

            // changed glsl or parameters
            ReleaseMaterialProgramReference( material );

        } else if ( lightsHash.stateID != lights.hash_stateID ||
                lightsHash.directionalLength != lights.hash_directionalLength ||
                lightsHash.pointLength != lights.hash_pointLength ||
                lightsHash.spotLength != lights.hash_spotLength ||
                lightsHash.rectAreaLength != lights.hash_rectAreaLength ||
                lightsHash.hemiLength != lights.hash_hemiLength ||
                lightsHash.shadowsLength != lights.hash_shadowsLength ) {

            lightsHash.stateID = lights.hash_stateID;
            lightsHash.directionalLength = lights.hash_directionalLength;
            lightsHash.pointLength = lights.hash_pointLength;
            lightsHash.spotLength = lights.hash_spotLength;
            lightsHash.rectAreaLength = lights.hash_rectAreaLength;
            lightsHash.hemiLength = lights.hash_hemiLength;
            lightsHash.shadowsLength = lights.hash_shadowsLength;

            programChange = false;

        } else if ( parameters.shaderID != null ) {
            // same glsl and uniform list
            return;
        } else {
            // only rebuild uniform list
            programChange = false;
        }

        if ( programChange ) {
            if ( parameters.shaderID != null ) {
                ShaderObject shader = shaderLib.Get(parameters.shaderID);

                ShaderObject shaderObject = new ShaderObject();
                shaderObject.name = material.type;
                shaderObject.uniforms = UniformUtils.CloneUniforms(shader.uniforms);
                shaderObject.vertexShader = shader.vertexShader;
                shaderObject.fragmentShader = shader.fragmentShader;
                materialProperties.shader = shaderObject;

            } else {

                ShaderObject shaderObject = new ShaderObject();
                shaderObject.name = material.type;
                shaderObject.uniforms = ((ShaderMaterial)material).uniforms;
                shaderObject.vertexShader = ((ShaderMaterial)material).vertexShader;
                shaderObject.fragmentShader = ((ShaderMaterial)material).fragmentShader;
                materialProperties.shader = shaderObject;

            }

            // Computing code again as onBeforeCompile may have changed the shaders
            code = programCache.GetProgramCode( material, parameters );
            program = programCache.AcquireProgram( material, materialProperties.shader, parameters, code );

            materialProperties.program = program;
            material.program = program;
        }

        // TODO: Morph Targets and Morph Normals

        UniformsObject uniforms = materialProperties.shader.uniforms;

        if ( ! material.type.equals("ShaderMaterial") &&
                ! material.type.equals("RawShaderMaterial") ||
                material.clipping == true ) {

            materialProperties.numClippingPlanes = _clipping.numPlanes;
            materialProperties.numIntersection = _clipping.numIntersection;
            uniforms.Put("clippingPlanes", _clipping.uniform);

        }

        materialProperties.fog = fog;

        // store the light setup it was created for
        if ( lightsHash == null ) {
            materialProperties.lightsHash = lightsHash = new LightHash();
        }

        lightsHash.stateID = lights.hash_stateID;
        lightsHash.directionalLength = lights.hash_directionalLength;
        lightsHash.pointLength = lights.hash_pointLength;
        lightsHash.spotLength = lights.hash_spotLength;
        lightsHash.rectAreaLength = lights.hash_rectAreaLength;
        lightsHash.hemiLength = lights.hash_hemiLength;
        lightsHash.shadowsLength = lights.hash_shadowsLength;

        if ( material.lights ) {

            // wire up the material to this renderer's lighting state
            uniforms.Put("ambientLightColor", lights.ambient);
            uniforms.Put("directionalLights", lights.directional);
            //uniforms..value = lights.state.directional;
            uniforms.Put("spotLights", lights.spot);
            uniforms.Put("rectAreaLights", lights.rectArea);
            uniforms.Put("pointLights", lights.point);
            uniforms.Put("hemisphereLights",lights.hemi);

            uniforms.Put("directionalShadowMap", lights.directionalShadowMap);
            uniforms.Put("directionalShadowMatrix", lights.directionalShadowMatrix);
            uniforms.Put("spotShadowMap", lights.spotShadowMap);
            uniforms.Put("spotShadowMatrix", lights.spotShadowMatrix);
            uniforms.Put("pointShadowMap", lights.pointShadowMap);
            uniforms.Put("pointShadowMatrix", lights.pointShadowMatrix);

        }

        GLUniforms progUniforms = materialProperties.program.GetUniforms();
        ArrayList<AbstractUniform> uniformsList =  GLUniforms.SeqWithValue( progUniforms.seq, uniforms );

        materialProperties.uniformsList = uniformsList;
    }

    private void ReleaseMaterialProgramReference(Material material) {
    }

    private void ProjectObject(Object3D object, Camera camera, boolean sortObjects){
        if (!object.visible) return;

        //boolean visible = object.layers.test( camera.layers );

        if ( true ) {
            if ( object instanceof Light) {
                currentRenderState.PushLight( (Light) object );
                if ( object.castShadow ) {
                    currentRenderState.PushShadow( (Light) object );
                }

            } else if ( object instanceof Sprite) {

                if ( ! object.frustumCulled || _frustum.IntersectsSprite( object ) ) {

                    if ( sortObjects ) {
                        _vector3.SetFromMatrixPosition( object.matrixWorld )
                                .ApplyMatrix4( _projScreenMatrix );
                    }

                    Sprite sprite = (Sprite) object;
                    BufferGeometry geometry = objects.Update( object );
                    Material material = sprite.material;

                    currentRenderList.Push( object, geometry, material, (int)_vector3.z, null );

                }

            } else if ( /*object.isImmediateRenderObject*/ false ) {

                // TODO: immediateRenderObject

            } else if ( object instanceof Mesh || object instanceof Line || object instanceof Points) {

                // TODO update skined mesh

                if ( ! object.frustumCulled || _frustum.IntersectsObject( object ) ) {
                    if ( sortObjects ) {
                        _vector3.SetFromMatrixPosition( object.matrixWorld )
                                .ApplyMatrix4( _projScreenMatrix );
                    }

                    BufferGeometry geometry = objects.Update( object );
                    Material material = object.material;

                    // TODO: material arrays

                    if ( material.visible ) {
                        currentRenderList.Push( object, geometry, material, (int)_vector3.z, null );
                    }
                }

            }

        }

        ArrayList<Object3D> children = object.children;

        for ( int i = 0, l = children.size(); i < l; i ++ ) {
            ProjectObject( children.get(i), camera, sortObjects );
        }
    }

    public void Compile(){}

    public int AllocTextureUnit() {
        int textureUnit = _usedTextureUnits;

        if ( textureUnit >= capabilities.maxTextures ) {
            System.out.println("exceed max textures.");
        }

        _usedTextureUnits += 1;

        return textureUnit;
    }

    public void SetTexture2D(Texture texture, int slot) {
        textures.SetTexture2D( texture, slot );
    }

    public void SetTexture3D(Texture texture, int slot) {
        textures.SetTexture3D( texture, slot );
    }

    public void SetTextureCube(CubeTexture texture, int slot) {
        textures.SetTextureCube( texture, slot );
    }
    
    public void SetSize(int width, int height){
        _width = width;
        _height = height;

        this.SetViewport( 0, 0, width, height );
    }

    private void SetViewport(int x, int y, int width, int height) {
        _viewport.Set( x, _height - y - height, width, height );
        state.Viewport( _currentViewport.Copy( _viewport ).MultiplyScalar( _pixelRatio ) );
    }

    public void Clear(boolean color, boolean depth, boolean stencil) {
        int bits = 0;

        if (  color ) bits |= GLES20.GL_COLOR_BUFFER_BIT;
        if (  depth ) bits |= GLES20.GL_DEPTH_BUFFER_BIT;
        if (  stencil ) bits |= GLES20.GL_STENCIL_BUFFER_BIT;

        GLES20.glClear( bits );
    }

    public GLRenderTarget GetRenderTarget() {
        return _currentRenderTarget;
    }
}
