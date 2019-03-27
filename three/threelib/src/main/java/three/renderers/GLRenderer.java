package three.renderers;

import android.opengl.GLES20;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

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
import three.util.TextureProperties;

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
    private int _currentFramebuffer = -1;
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

        initGLContext();
    }

    public void initGLContext(){
        state = new GLState();
        _viewport = new Vector4(0, 0, _width, _height);
        _scissor = new Vector4(0, 0, _width, _height);
        state.scissor(_currentScissor.copy(_scissor).multiplyScalar(_pixelRatio));
        state.viewport(_currentViewport.copy(_viewport).multiplyScalar(_pixelRatio));

        info = new GLInfo();
        properties = new GLProperties();
        capabilities = new GLCapabilities();
        textures = new GLTextures(state, properties, info);
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


    public void setRenderTarget(GLRenderTarget renderTarget){
        _currentRenderTarget = renderTarget;

        if ( renderTarget != null && properties.getRenderTarget( renderTarget ).__glFramebuffer < 0 ) {
            textures.setupRenderTarget( renderTarget );
        }

        int framebuffer = _framebuffer;
        boolean isCube = false;

        if ( renderTarget != null ) {
            int __glFramebuffer = properties.getRenderTarget( renderTarget ).__glFramebuffer;
            if ( renderTarget instanceof GLRenderTargetCube ) {
                // todo
            } else {
                framebuffer = __glFramebuffer;
            }
            _currentViewport.copy( renderTarget.viewport );
            _currentScissor.copy( renderTarget.scissor );
            _currentScissorTest = renderTarget.scissorTest;

        } else {
            _currentViewport.copy( _viewport ).multiplyScalar( _pixelRatio );
            _currentScissor.copy( _scissor ).multiplyScalar( _pixelRatio );
            _currentScissorTest = _scissorTest;
        }

        if ( _currentFramebuffer != framebuffer ) {
            GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, framebuffer );
            _currentFramebuffer = framebuffer;
        }

        state.viewport( _currentViewport );
        state.scissor( _currentScissor );
        state.setScissorTest( _currentScissorTest );

        if ( isCube ) {
            TextureProperties textureProperties = properties.getTexture( renderTarget.texture );
//            GLES20.glFramebufferTexture2D( GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
//                    GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + renderTarget.activeCubeFace, textureProperties.__glTexture, renderTarget.activeMipMapLevel );
        }

    }

    private float getTargetPixelRatio(){
        return _currentRenderTarget == null ? _pixelRatio : 1;
    }


    public void render(Scene scene, Camera camera, GLRenderTarget renderTarget, boolean forceClear) throws IllegalAccessException {

        // reset caching for this frame;
        _currentGeometryProgram_geometry = 0;
        _currentGeometryProgram_program = 0;
        _currentGeometryProgram_wireframe = false;
        _currentMaterialId = - 1;
        _currentCamera = null;

        // update scene graph
        if ( scene.autoUpdate == true ) scene.updateMatrixWorld(true);

        if ( camera.parent == null ) camera.updateMatrixWorld(true);

        currentRenderState = renderStates.get(scene, camera);
        currentRenderState.init();

        //scene.onBeforeRender( this, scene, camera, renderTarget );

        _projScreenMatrix.multiplyMatrices( camera.projectionMatrix, camera.matrixWorldInverse );
        _frustum.setFromMatrix( _projScreenMatrix );

        _localClippingEnabled = this.localClippingEnabled;
        _clippingEnabled = _clipping.init( this.clippingPlanes, _localClippingEnabled, camera );

        currentRenderList = renderLists.get( scene, camera );
        currentRenderList.init();

        projectObject( scene, camera, this.sortObjects );

        if (sortObjects) {
            currentRenderList.sort();
        }

        if ( _clippingEnabled ) _clipping.beginShadows();
        ArrayList<Light> shadowsArray = currentRenderState.shadowsArray;
        shadowMap.render( shadowsArray, scene, camera );
        currentRenderState.setupLights( camera );
        if ( _clippingEnabled ) _clipping.endShadows();


        if ( this.info.autoReset ) this.info.reset();

        this.setRenderTarget( renderTarget );

        // render background
        background.render( currentRenderList, scene, camera, forceClear );

        // render scene
        ArrayList<RenderItem> opaqueObjects = currentRenderList.opaque;
        ArrayList<RenderItem> transparentObjects = currentRenderList.transparent;

        if ( scene.overrideMaterial != null) {

            Material overrideMaterial = scene.overrideMaterial;

            if ( opaqueObjects.size() > 0 ) renderObjects( opaqueObjects, scene, camera, overrideMaterial );
            if ( transparentObjects.size() > 0 ) renderObjects( transparentObjects, scene, camera, overrideMaterial );

        } else {

            // opaque pass (front-to-back order)
            if ( opaqueObjects.size() > 0 ) renderObjects( opaqueObjects, scene, camera, null );

            // transparent pass (back-to-front order)
            if ( transparentObjects.size() > 0 ) renderObjects( transparentObjects, scene, camera, null );

        }

        // Generate mipmap if we're using any kind of mipmap filtering
        if ( renderTarget != null ) {
            textures.updateRenderTargetMipmap( renderTarget );
        }

        // Ensure depth buffer writing is enabled so it can be cleared on next render

        state.depthBuffer.SetTest( true );
        state.depthBuffer.SetMask( true );
        state.colorBuffer.SetMask( true );

        state.setPolygonOffset( false, 0, 0 );

        currentRenderList = null;
        currentRenderState = null;
    }

    private void renderObjects(ArrayList<RenderItem> renderList, Scene scene, Camera camera, Material overrideMaterial) throws IllegalAccessException {
        for ( int i = 0, l = renderList.size(); i < l; i ++ ) {

            RenderItem renderItem = renderList.get(i);

            Object3D object = renderItem.object;
            BufferGeometry geometry = renderItem.geometry;
            Material material = overrideMaterial == null ? renderItem.material : overrideMaterial;
            GeoMatGroup group = renderItem.group;

            //TODO: array camera

            _currentArrayCamera = null;
            renderObject( object, scene, camera, geometry, material, group );
        }
    }

    private void renderObject(Object3D object, Scene scene, Camera camera, BufferGeometry geometry, Material material, GeoMatGroup group) throws IllegalAccessException {
        object.onBeforeRender(this, scene, camera);
        currentRenderState = renderStates.get( scene, camera );

        object.modelViewMatrix.multiplyMatrices( camera.matrixWorldInverse, object.matrixWorld );
        object.normalMatrix.getNormalMatrix( object.modelViewMatrix );

        // TODO: immediateRenderObject
        if ( false ) {

        } else {
            renderBufferDirect( camera, scene.fog, geometry, material, object, group );
        }

        //object.onAfterRender( _this, scene, camera, geometry, material, group );
        currentRenderState = renderStates.get( scene, camera );

    }

    public void renderBufferDirect(Camera camera, Fog fog, BufferGeometry geometry, Material material, Object3D object, GeoMatGroup group) throws IllegalAccessException {
        boolean frontFaceCW = ( object instanceof Mesh && object.normalMatrix.determinant() < 0 );

        state.setMaterial( material, frontFaceCW );

        GLProgram program = setProgram( camera, fog, material, object );

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
            index = geometries.getWireframeAttribute( geometry );
            rangeFactor = 2;
        }

        BufferData attribute = null;
        GLBufferRenderer renderer = bufferRenderer;

        if ( index != null ) {
            attribute = attributes.get( index );
            indexedBufferRenderer.setIndex( attribute );
            renderer = indexedBufferRenderer;
        }

        if ( updateBuffers ) {
            setupVertexAttributes( material, program, geometry );
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

            if (material.wireframe) {
                if(material instanceof MeshMaterial){
                    MeshMaterial meshmat = (MeshMaterial)material;
                    state.setLineWidth( meshmat.wireframeLinewidth * getTargetPixelRatio() );
                    renderer.setMode( GLES20.GL_LINES );
                }

            } else {
                switch ( ((Mesh)object).drawMode ) {
                    case TrianglesDrawMode:
                        renderer.setMode( GLES20.GL_TRIANGLES );
                        break;
                    case TriangleStripDrawMode:
                        renderer.setMode( GLES20.GL_TRIANGLE_STRIP );
                        break;
                    case TriangleFanDrawMode:
                        renderer.setMode( GLES20.GL_TRIANGLE_FAN );
                        break;
                }
            }

        } else if ( object instanceof Line ) {

            LineMaterial linemat = (LineMaterial) material;
            float lineWidth = linemat.linewidth;

            state.setLineWidth( lineWidth * getTargetPixelRatio() );

            if (object.type.equals("LineSegments")) {
                renderer.setMode( GLES20.GL_LINES );

            } else if (object.type.equals("LineLoop")) {
                renderer.setMode( GLES20.GL_LINE_LOOP );

            } else {
                renderer.setMode( GLES20.GL_LINE_STRIP );
            }

        } else if ( object instanceof Points ) {
            renderer.setMode( GLES20.GL_POINTS );

        } else if ( object instanceof Sprite ) {
            renderer.setMode(GLES20.GL_TRIANGLES );

        }

        // TODO: draw instances

        renderer.render( drawStart, drawCount );

    }

    private void setupVertexAttributes(Material material, GLProgram program, BufferGeometry geometry) {

        state.initAttributes();

        HashMap<String, BufferAttribute> geometryAttributes = geometry.attributes;
        HashMap<String, Integer> programAttributes = program.getAttributes();

        Field defaultAttributeField = material.getProperty("defaultAttributeValues");

        for (Object o : programAttributes.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;

            String name = (String) pair.getKey();
            int programAttribute = (int) pair.getValue();

            if (programAttribute >= 0) {
                BufferAttribute geometryAttribute = geometryAttributes.get(name);
                if (geometryAttribute != null) {

                    boolean normalized = geometryAttribute.normalized;
                    int size = geometryAttribute.itemSize;

                    BufferData attribute = attributes.get(geometryAttribute);

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
                            state.enableAttribute(programAttribute);
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

        state.disableUnusedAttributes();

    }

    private GLProgram setProgram(Camera camera, Fog fog, Material material, Object3D object) throws IllegalAccessException {
        _usedTextureUnits = 0;

        MaterialProperties materialProperties = properties.getMaterial(material);
        GLLights lights = currentRenderState.lights;

        LightHash lightsHash = materialProperties.lightsHash;

        if ( _clippingEnabled ) {
            if ( _localClippingEnabled || camera != _currentCamera ) {
                boolean useCache = camera == _currentCamera &&  material.id == _currentMaterialId;

                _clipping.setState(
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
            initMaterial( material, fog, object );
            material.needsUpdate = false;
        }

        boolean refreshProgram = false;
        boolean refreshMaterial = false;
        boolean refreshLights = false;

        GLProgram program = materialProperties.program;
        GLUniforms p_uniforms = program.getUniforms();
        UniformsObject m_uniforms = materialProperties.shader.uniforms;

        if ( state.useProgram( program.program ) ) {
            refreshProgram = true;
            refreshMaterial = true;
            refreshLights = true;
        }

        if ( material.id != _currentMaterialId ) {
            _currentMaterialId = material.id;
            refreshMaterial = true;
        }

        if ( refreshProgram || _currentCamera != camera ) {

            p_uniforms.setValue4Fm("projectionMatrix", camera.projectionMatrix);

//            if ( capabilities.logarithmicDepthBuffer ) {
//                p_uniforms.setValue(  "logDepthBufFC",
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
                    material.getProperty("envMap") != null) {

                SingleUniform uCamPos = (SingleUniform) p_uniforms.map.get("cameraPosition");

                if (uCamPos != null) {
                    uCamPos.setValue3Fv(_vector3.setFromMatrixPosition(camera.matrixWorld));
                }

            }

            if (material.type.equals("MeshPhongMaterial") ||
                    material.type.equals("MeshLambertMaterial") ||
                    material.type.equals("MeshBasicMaterial") ||
                    material.type.equals("MeshStandardMaterial") ||
                    material.type.equals("ShaderMaterial")) {

                p_uniforms.setValue4Fm("viewMatrix", camera.matrixWorldInverse);

            }
        }

            // TODO: skinning

        if ( refreshMaterial ) {

            p_uniforms.setValue1F(  "toneMappingExposure", toneMappingExposure );
            p_uniforms.setValue1F(  "toneMappingWhitePoint", toneMappingWhitePoint );

            if ( material.lights ) {
                markUniformsLightsNeedsUpdate( m_uniforms, refreshLights );
            }

            // refresh uniforms common to several materials
            if ( fog != null && material.fog ) {
                refreshUniformsFog( m_uniforms, fog );
            }

            if (material.type.equals("MeshBasicMaterial")) {
                refreshUniformsCommon( m_uniforms, material );

            } else if (material.type.equals("MeshLambertMaterial")) {

                refreshUniformsCommon( m_uniforms, material );
                refreshUniformsLambert( m_uniforms, material );

            } else if (material.type.equals("MeshPhongMaterial")) {
                refreshUniformsCommon( m_uniforms, material );

                if (material.type.equals("MeshToonMaterial")) {
                    refreshUniformsToon( m_uniforms, material );

                } else {
                    refreshUniformsPhong( m_uniforms, material );
                }

            } else if (material.type.equals("MeshStandardMaterial")) {
                refreshUniformsCommon( m_uniforms, material );
                if (material.type.equals("MeshPhysicalMaterial")) {
                    refreshUniformsPhysical( m_uniforms, material );
                } else {
                    refreshUniformsStandard( m_uniforms, material );
                }

            } else if (material.type.equals("MeshMatcapMaterial")) {
                refreshUniformsCommon( m_uniforms, material );
                refreshUniformsMatcap( m_uniforms, material );

            } else if (material.type.equals("MeshDepthMaterial")) {
                refreshUniformsCommon( m_uniforms, material );
                refreshUniformsDepth( m_uniforms, material );

            } else if (material.type.equals("MeshDistanceMaterial")) {
                refreshUniformsCommon( m_uniforms, material );
                refreshUniformsDistance( m_uniforms, material );

            } else if (material.type.equals("MeshNormalMaterial")) {
                refreshUniformsCommon( m_uniforms, material );
                refreshUniformsNormal( m_uniforms, material );

            } else if (material.type.equals("LineBasicMaterial")) {
                refreshUniformsLine( m_uniforms, material );
                if (material.type.equals("LineDashedMaterial")) {
                    refreshUniformsDash( m_uniforms, material );
                }

            } else if (material.type.equals("PointsMaterial")) {
                refreshUniformsPoints( m_uniforms, material );

            } else if (material.type.equals("SpriteMaterial")) {
                refreshUniformsSprites( m_uniforms, material );

            } else if (material.type.equals("ShadowMaterial")) {
                Field colorField = material.getProperty("color");
                m_uniforms.put("color", colorField.get(material));
                m_uniforms.put("opacity", material.opacity);

            }

            GLUniforms.upload( materialProperties.uniformsList, m_uniforms, this );
        }

        if (material.type.equals("ShaderMaterial") && ((ShaderMaterial) material).uniformsNeedUpdate) {
            GLUniforms.upload( materialProperties.uniformsList, m_uniforms, this );
            ((ShaderMaterial)material).uniformsNeedUpdate = false;
        }

        if (material.type.equals("SpriteMaterial")) {
            p_uniforms.setValue2Fv("center", ((Sprite)object).center );
        }

        // common matrices
        p_uniforms.setValue4Fm( "modelViewMatrix", object.modelViewMatrix );
        p_uniforms.setValue3Fm( "normalMatrix", object.normalMatrix );
        p_uniforms.setValue4Fm( "modelMatrix", object.matrixWorld );

        return program;
    }

    private void refreshUniformsCommon(UniformsObject uniforms, Material material) throws IllegalAccessException {
        uniforms.put("opacity", material.opacity);

        Field colorField = material.getProperty("color");
        if ( colorField != null ) {
            uniforms.put("diffuse", colorField.get(material));
        }

        Field emissiveField = material.getProperty("emissive");
        if ( emissiveField != null ) {
            Color emissive = (Color) emissiveField.get(material);
            Field emissiveIntensityField = material.getProperty("emissiveIntensity");
            float emissiveIntensity = (float) emissiveIntensityField.get(material);
            ((Color)uniforms.get("emissive")).copy( emissive.multiplyScalar( emissiveIntensity ));
        }

        Field mapField = material.getProperty("map");
        if ( material.checkFieldValid("map") ) {
            uniforms.put("map", mapField.get(material));
        }

        Field alphaMapField = material.getProperty("alphaMap");
        if ( material.checkFieldValid("alphaMap") ) {
            uniforms.put("alphaMap", alphaMapField.get(material));
        }

        Field specularMapField = material.getProperty("specularMap");
        if ( material.checkFieldValid("specularMap") ) {
            uniforms.put("specularMap", specularMapField.get(material));
        }

        Field envMapField = material.getProperty("envMap");
        if ( material.checkFieldValid("envMap") ) {
            Texture envMap = (Texture) envMapField.get(material);
            uniforms.put("envMap", envMap);

            // don't flip CubeTexture envMaps, flip everything else:
            //  WebGLRenderTargetCube will be flipped for backwards compatibility
            //  WebGLRenderTargetCube.texture will be flipped because it's a Texture and NOT a CubeTexture
            // this check must be handled differently, or removed entirely, if WebGLRenderTargetCube uses a CubeTexture in the future

            uniforms.put("flipEnvMap", envMap instanceof CubeTexture ? - 1 : 1);

            Field reflectivityField = material.getProperty("reflectivity");
            if(reflectivityField != null){
                uniforms.put("reflectivity", reflectivityField.get(material));
            }

            Field refractionRatioField = material.getProperty("refractionRatio");
            if(refractionRatioField != null){
                uniforms.put("refractionRatio", refractionRatioField.get(material));
            }

            uniforms.put("maxMipLevel", properties.getTexture( envMap ).__maxMipLevel);
        }

        Field lightMapField = material.getProperty("lightMap");
        if ( material.checkFieldValid("lightMap") ) {
            Field lightMapIntensityField = material.getProperty("lightMapIntensity");
            float lightMapIntensity = (float) lightMapIntensityField.get(material);
            uniforms.put("lightMap", lightMapField.get(material));
            uniforms.put("lightMapIntensity", lightMapIntensity);
        }

        Field aoMapField = material.getProperty("aoMap");
        if ( material.checkFieldValid("aoMap") ) {
            Field aoMapIntensityField = material.getProperty("aoMapIntensity");
            float aoMapIntensity = (float) aoMapIntensityField.get(material);
            uniforms.put("aoMap", alphaMapField.get(material));
            uniforms.put("aoMapIntensity", aoMapIntensity);
        }

        // uv repeat and offset setting priorities
        // 1. color map
        // 2. specular map
        // 3. normal map
        // 4. bump map
        // 5. alpha map
        // 6. emissive map

        Texture uvScaleMap = null;
        Field displacementMapField = material.getProperty("displacementMap");
        Field normalMapField = material.getProperty("normalMap");
        Field bumpMapField = material.getProperty("bumpMap");
        Field roughnessMapField = material.getProperty("roughnessMap");
        Field metalnessMapField = material.getProperty("metalnessMap");

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
                uvScaleMap.updateMatrix();
            }
            ((Matrix3)uniforms.get("unTransform")).copy( uvScaleMap.matrix );
        }
    }

    private void refreshUniformsPoints(UniformsObject uniforms, Material material) {

    }

    private void refreshUniformsSprites(UniformsObject uniforms, Material material) {
    }

    private void refreshUniformsDash(UniformsObject uniforms, Material material) {
    }

    private void refreshUniformsLine(UniformsObject uniforms, Material material) throws IllegalAccessException {
        Field colorField = material.getProperty("color");
        if ( colorField != null ) {
            uniforms.put("diffuse", colorField.get(material));
        }
        uniforms.put("opacity", material.opacity);
    }

    private void refreshUniformsNormal(UniformsObject uniforms, Material material) {
        MeshNormalMaterial meshNormalMaterial = (MeshNormalMaterial) material;
        if ( meshNormalMaterial.bumpMap != null ) {
            uniforms.put("bumpMap", meshNormalMaterial.bumpMap);
            if(material.side == BackSide){
                uniforms.put("bumpScale", meshNormalMaterial.bumpScale * -1);
            }else{
                uniforms.put("bumpScale", meshNormalMaterial.bumpScale);
            }
        }

        if ( meshNormalMaterial.normalMap != null) {
            uniforms.put("normalMap", meshNormalMaterial.normalMap);
            if(material.side == BackSide){
                ((Vector2)uniforms.get("normalScale")).copy(meshNormalMaterial.normalScale).negate();
            }else{
                ((Vector2)uniforms.get("normalScale")).copy(meshNormalMaterial.normalScale);
            }
        }

        if ( meshNormalMaterial.displacementMap != null) {
            uniforms.put("displacementMap", meshNormalMaterial.displacementMap);
            uniforms.put("displacementScale", meshNormalMaterial.displacementScale);
            uniforms.put("displacementBias", meshNormalMaterial.displacementBias);
        }
    }

    private void refreshUniformsDistance(UniformsObject uniforms, Material material) {
    }

    private void refreshUniformsDepth(UniformsObject uniforms, Material material) {
    }

    private void refreshUniformsMatcap(UniformsObject m_uniforms, Material material) {
    }

    private void refreshUniformsStandard(UniformsObject uniforms, Material material) {
        MeshStandardMaterial meshStandardMaterial = (MeshStandardMaterial) material;
        uniforms.put("roughness", meshStandardMaterial.roughness);
        uniforms.put("metalness", meshStandardMaterial.metalness);

        if ( meshStandardMaterial.roughnessMap != null ) {
            uniforms.put("roughnessMap", meshStandardMaterial.roughnessMap);
        }

        if ( meshStandardMaterial.metalnessMap != null ) {
            uniforms.put("metalnessMap", meshStandardMaterial.metalnessMap);
        }

        if ( meshStandardMaterial.emissiveMap != null ) {
            uniforms.put("emissiveMap", meshStandardMaterial.emissiveMap);
        }

        if ( meshStandardMaterial.bumpMap != null ) {
            uniforms.put("bumpMap", meshStandardMaterial.bumpMap);
            if(material.side == BackSide){
                uniforms.put("bumpScale", meshStandardMaterial.bumpScale * -1);
            }else{
                uniforms.put("bumpScale", meshStandardMaterial.bumpScale);
            }
        }

        if ( meshStandardMaterial.normalMap != null) {
            uniforms.put("normalMap", meshStandardMaterial.normalMap);
            if(material.side == BackSide){
                ((Vector2)uniforms.get("normalScale")).copy(meshStandardMaterial.normalScale).negate();
            }else{
                ((Vector2)uniforms.get("normalScale")).copy(meshStandardMaterial.normalScale);
            }
        }

        if ( meshStandardMaterial.displacementMap != null) {
            uniforms.put("displacementMap", meshStandardMaterial.displacementMap);
            uniforms.put("displacementScale", meshStandardMaterial.displacementScale);
            uniforms.put("displacementBias", meshStandardMaterial.displacementBias);
        }

        if ( meshStandardMaterial.envMap != null ) {
            uniforms.put("envMapIntensity", meshStandardMaterial.envMapIntensity);
        }
    }

    private void refreshUniformsPhysical(UniformsObject uniforms, Material material) {
        refreshUniformsStandard( uniforms, material );

        MeshPhysicalMaterial meshPhysicalMaterial = (MeshPhysicalMaterial) material;
        uniforms.put("clearCoat", meshPhysicalMaterial.clearCoat);
        uniforms.put("clearCoatRoughness", meshPhysicalMaterial.clearCoatRoughness);
    }

    private void refreshUniformsPhong(UniformsObject uniforms, Material material) {
        MeshPhongMaterial meshPhongMaterial = (MeshPhongMaterial) material;
        uniforms.put("specular", meshPhongMaterial.specular);
        uniforms.put("shininess", (float)Math.max( meshPhongMaterial.shininess, 1e-4 ));

        if ( meshPhongMaterial.emissiveMap != null ) {
            uniforms.put("emissiveMap", meshPhongMaterial.emissiveMap);
        }

        if ( meshPhongMaterial.bumpMap != null ) {
            uniforms.put("bumpMap", meshPhongMaterial.bumpMap);
            if(material.side == BackSide){
                uniforms.put("bumpScale", meshPhongMaterial.bumpScale * -1);
            }else{
                uniforms.put("bumpScale", meshPhongMaterial.bumpScale);
            }
        }

        if ( meshPhongMaterial.normalMap != null) {
            uniforms.put("normalMap", meshPhongMaterial.normalMap);
            if(material.side == BackSide){
                ((Vector2)uniforms.get("normalScale")).copy(meshPhongMaterial.normalScale).negate();
            }else{
                ((Vector2)uniforms.get("normalScale")).copy(meshPhongMaterial.normalScale);
            }
        }

        if ( meshPhongMaterial.displacementMap != null) {
            uniforms.put("displacementMap", meshPhongMaterial.displacementMap);
            uniforms.put("displacementScale", meshPhongMaterial.displacementScale);
            uniforms.put("displacementBias", meshPhongMaterial.displacementBias);
        }
    }

    private void refreshUniformsToon(UniformsObject uniforms, Material material) {
        refreshUniformsPhong( uniforms, material );

        MeshToonMaterial meshToonMaterial = (MeshToonMaterial)material;
        if ( meshToonMaterial.gradientMap != null ) {
            uniforms.put("gradientMap", meshToonMaterial.gradientMap);
        }
    }

    private void refreshUniformsLambert(UniformsObject uniforms, Material material) {
        MeshLambertMaterial meshLambertMaterial = (MeshLambertMaterial) material;
        if ( meshLambertMaterial.emissiveMap != null ) {
            uniforms.put("emissiveMap", meshLambertMaterial.emissiveMap);
        }
    }

    private void refreshUniformsFog(UniformsObject uniforms, Fog fog) {
        uniforms.put("fogColor", fog.color);

        uniforms.put("fogNear", fog.near);
        uniforms.put("fogFar", fog.far);

        // TODO: FogExp2
    }

    private void markUniformsLightsNeedsUpdate(UniformsObject uniformsObject, boolean value) {
        uniformsObject.uniforms.get("ambientLightColor").needsUpdate = value;
        uniformsObject.uniforms.get("directionalLights").needsUpdate = value;
        uniformsObject.uniforms.get("pointLights").needsUpdate = value;
        uniformsObject.uniforms.get("spotLights").needsUpdate = value;
        uniformsObject.uniforms.get("rectAreaLights").needsUpdate = value;
        uniformsObject.uniforms.get("hemisphereLights").needsUpdate = value;
    }

    private void initMaterial(Material material, Fog fog, Object3D object) {
        MaterialProperties materialProperties = properties.getMaterial(material);

        GLLights lights = currentRenderState.lights;
        ArrayList<Light> shadowsArray = currentRenderState.shadowsArray;

        LightHash lightsHash = materialProperties.lightsHash;

        Parameters parameters = programCache.getParameters(
                material, lights, shadowsArray, fog, _clipping.numPlanes, _clipping.numIntersection, object );

        String code = programCache.getProgramCode( material, parameters );

        GLProgram program = materialProperties.program;
        boolean programChange = true;

        if ( program == null ) {
            // new material
            //material.addEventListener( 'dispose', onMaterialDispose );

        } else if (!program.code.equals(code)) {

            // changed glsl or parameters
            releaseMaterialProgramReference( material );

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
                ShaderObject shader = shaderLib.get(parameters.shaderID);

                ShaderObject shaderObject = new ShaderObject();
                shaderObject.name = material.type;
                shaderObject.uniforms = UniformUtils.cloneUniforms(shader.uniforms);
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
            code = programCache.getProgramCode( material, parameters );
            program = programCache.acquireProgram( material, materialProperties.shader, parameters, code );

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
            uniforms.put("clippingPlanes", _clipping.uniform);

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
            uniforms.put("ambientLightColor", lights.ambient);
            uniforms.put("directionalLights", lights.directional);
            //uniforms..value = lights.state.directional;
            uniforms.put("spotLights", lights.spot);
            uniforms.put("rectAreaLights", lights.rectArea);
            uniforms.put("pointLights", lights.point);
            uniforms.put("hemisphereLights",lights.hemi);

            uniforms.put("directionalShadowMap", lights.directionalShadowMap);
            uniforms.put("directionalShadowMatrix", lights.directionalShadowMatrix);
            uniforms.put("spotShadowMap", lights.spotShadowMap);
            uniforms.put("spotShadowMatrix", lights.spotShadowMatrix);
            uniforms.put("pointShadowMap", lights.pointShadowMap);
            uniforms.put("pointShadowMatrix", lights.pointShadowMatrix);

        }

        GLUniforms progUniforms = materialProperties.program.getUniforms();
        ArrayList<AbstractUniform> uniformsList =  GLUniforms.seqWithValue( progUniforms.seq, uniforms );

        materialProperties.uniformsList = uniformsList;
    }

    private void releaseMaterialProgramReference(Material material) {
    }

    private void projectObject(Object3D object, Camera camera, boolean sortObjects){
        if (!object.visible) return;

        //boolean visible = object.layers.test( camera.layers );

        if ( true ) {
            if ( object instanceof Light) {
                currentRenderState.pushLight( (Light) object );
                if ( object.castShadow ) {
                    currentRenderState.pushShadow( (Light) object );
                }

            } else if ( object instanceof Sprite) {

                if ( ! object.frustumCulled || _frustum.intersectsSprite( object ) ) {

                    if ( sortObjects ) {
                        _vector3.setFromMatrixPosition( object.matrixWorld )
                                .applyMatrix4( _projScreenMatrix );
                    }

                    Sprite sprite = (Sprite) object;
                    BufferGeometry geometry = objects.update( object );
                    Material material = sprite.material;

                    currentRenderList.push( object, geometry, material, (int)_vector3.z, null );

                }

            } else if ( /*object.isImmediateRenderObject*/ false ) {

                // TODO: immediateRenderObject

            } else if ( object instanceof Mesh || object instanceof Line || object instanceof Points) {

                // TODO update skined mesh

                if ( ! object.frustumCulled || _frustum.intersectsObject( object ) ) {
                    if ( sortObjects ) {
                        _vector3.setFromMatrixPosition( object.matrixWorld )
                                .applyMatrix4( _projScreenMatrix );
                    }

                    BufferGeometry geometry = objects.update( object );
                    Material material = object.material;

                    // TODO: material arrays

                    if ( material.visible ) {
                        currentRenderList.push( object, geometry, material, (int)_vector3.z, null );
                    }
                }

            }

        }

        ArrayList<Object3D> children = object.children;

        for ( int i = 0, l = children.size(); i < l; i ++ ) {
            projectObject( children.get(i), camera, sortObjects );
        }
    }

    public void compile(){}

    public int allocTextureUnit() {
        int textureUnit = _usedTextureUnits;

        if ( textureUnit >= capabilities.maxTextures ) {
            System.out.println("exceed max textures.");
        }

        _usedTextureUnits += 1;

        return textureUnit;
    }

    public void setTexture2D(Texture texture, int slot) {
        textures.setTexture2D( texture, slot );
    }

    public void setTexture3D(Texture texture, int slot) {
        textures.setTexture3D( texture, slot );
    }

    public void setTextureCube(CubeTexture texture, int slot) {
        textures.setTextureCube( texture, slot );
    }
    
    public void setSize(int width, int height){
        _width = width;
        _height = height;

        this.setViewport( 0, 0, width, height );
    }

    private void setViewport(int x, int y, int width, int height) {
        _viewport.set( x, _height - y - height, width, height );
        state.viewport( _currentViewport.copy( _viewport ).multiplyScalar( _pixelRatio ) );
    }

    public void clear(boolean color, boolean depth, boolean stencil) {
        int bits = 0;

        if (  color ) bits |= GLES20.GL_COLOR_BUFFER_BIT;
        if (  depth ) bits |= GLES20.GL_DEPTH_BUFFER_BIT;
        if (  stencil ) bits |= GLES20.GL_STENCIL_BUFFER_BIT;

        GLES20.glClear( bits );
    }

    public GLRenderTarget getRenderTarget() {
        return _currentRenderTarget;
    }
}
