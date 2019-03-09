package three.renderers.gl;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import three.materials.Material;
import three.math.Vector4;
import three.util.BoundTexture;
import three.util.ColorBuffer;
import three.util.DepthBuffer;
import three.util.StencilBuffer;

import static three.constants.AddEquation;
import static three.constants.AdditiveBlending;
import static three.constants.BackSide;
import static three.constants.CullFaceNone;
import static three.constants.CullFaceFront;
import static three.constants.CullFaceBack;
import static three.constants.CustomBlending;
import static three.constants.DoubleSide;
import static three.constants.LessEqualDepth;
import static three.constants.MultiplyBlending;
import static three.constants.NoBlending;
import static three.constants.NormalBlending;
import static three.constants.SubtractiveBlending;

public class GLState {
    public ColorBuffer colorBuffer;
    public DepthBuffer depthBuffer;
    public StencilBuffer stencilBuffer;
    public int maxVertexAttributes;
    public IntBuffer newAttributes;
    public IntBuffer enabledAttributes;
    public IntBuffer attributeDivisors;

    public int currentProgram;
    public boolean currentBlendingEnabled;
    public int currentBlending;
    public int currentBlendEquation;
    public int currentBlendSrc;
    public int currentBlendDst;
    public int currentBlendEquationAlpha;
    public int currentBlendSrcAlpha;
    public int currentBlendDstAlpha;
    public boolean currentPremultipledAlpha;
    public boolean currentFlipSided;
    public int currentCullFace;
    public float currentLineWidth;
    public float currentPolygonOffsetFactor;
    public float currentPolygonOffsetUnits;
    public int maxTextures;
    public int currentTextureSlot = -1;
    public HashMap<Integer, BoundTexture> currentBoundTextures = new HashMap<>();
    public Vector4 currentScissor;
    public Vector4 currentViewport;
    public HashMap<Integer, Integer> emptyTextures = new HashMap<>();

    public GLState(){
        this.colorBuffer = new ColorBuffer();
        this.depthBuffer = new DepthBuffer();
        this.stencilBuffer = new StencilBuffer();

        final int[] maxVertexAttributes = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS, maxVertexAttributes, 0);
        this.maxVertexAttributes = maxVertexAttributes[0];

        this.newAttributes = IntBuffer.allocate(this.maxVertexAttributes);
        this.enabledAttributes = IntBuffer.allocate(this.maxVertexAttributes);
        this.attributeDivisors = IntBuffer.allocate(this.maxVertexAttributes);

        this.currentPremultipledAlpha = false;
        final int[] maxTextures = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, maxTextures, 0);
        this.maxTextures = maxTextures[0];

        this.currentScissor = new Vector4();
        this.currentViewport = new Vector4();

        emptyTextures.put(GLES20.GL_TEXTURE_2D, CreateTexture(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_2D, 1));
        emptyTextures.put(GLES20.GL_TEXTURE_CUBE_MAP, CreateTexture(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 6));

        this.colorBuffer.SetClear(0 , 0, 0, 1, false);
        this.depthBuffer.SetClear(1);
        this.stencilBuffer.SetClear(0);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        this.depthBuffer.SetFunc(LessEqualDepth);

        SetFlipSided(false);
        SetCullFace(CullFaceBack);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        SetBlending( NoBlending, 0, 0, 0, 0, 0, 0, false );

    }

    private int CreateTexture(int type, int target, int count){
        ByteBuffer data = ByteBuffer.allocateDirect(4);

        final int textureBuf[] = new int[1];
        GLES20.glGenTextures(1, textureBuf, 0);
        int texture = textureBuf[0];

        GLES20.glBindTexture(type, texture);
        GLES20.glTexParameteri(type, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(type, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        for (int i = 0; i < count; i++){
            GLES20.glTexImage2D(target+i, 0, GLES20.GL_RGBA, 1, 1, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
        }

        return texture;
    }

    public void InitAttributes(){
        for ( int i = 0, l = newAttributes.capacity(); i < l; i ++ ) {
            newAttributes.put(i, 0);
        }
    }

    public void EnableAttribute(int attribute){
        EnableAttributeAndDivisor(attribute, 0);
    }

    public void EnableAttributeAndDivisor(int attribute, int meshPerAttribute){
        newAttributes.put(attribute, 1);

        if ( enabledAttributes.get(attribute) == 0 ) {
            GLES20.glEnableVertexAttribArray( attribute );
            enabledAttributes.put(attribute, 1);
        }

        if ( attributeDivisors.get(attribute) != meshPerAttribute ) {
            attributeDivisors.put(attribute, meshPerAttribute);
        }
    }

    public void DisableUnusedAttributes(){
        for ( int i = 0, l = enabledAttributes.capacity(); i != l; ++ i ) {
            if ( enabledAttributes.get(i) != newAttributes.get(i) ) {
                GLES20.glDisableVertexAttribArray( i );
                enabledAttributes.put(i, 0);
            }
        }
    }

    public boolean UseProgram(int program){
        if ( currentProgram != program ) {
            GLES20.glUseProgram( program );
            currentProgram = program;
            return true;
        }

        return false;
    }

    public void SetBlending(int blending, int blendEquation, int blendSrc, int blendDst, int blendEquationAlpha,
                            int blendSrcAlpha, int blendDstAlpha, boolean premultipliedAlpha){
        if ( blending == NoBlending ) {
            if ( currentBlendingEnabled ) {
                GLES20.glDisable(GLES20.GL_BLEND);
                currentBlendingEnabled = false;
            }
            return;
        }

        if ( ! currentBlendingEnabled ) {
            GLES20.glEnable(GLES20.GL_BLEND);
            currentBlendingEnabled = true;
        }

        if ( blending != CustomBlending ) {
            if (blending != currentBlending || premultipliedAlpha != currentPremultipledAlpha) {
                if (currentBlendEquation != AddEquation || currentBlendEquationAlpha != AddEquation) {
                    GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
                    currentBlendEquation = AddEquation;
                    currentBlendEquationAlpha = AddEquation;
                }

                if (premultipliedAlpha) {
                    switch (blending) {
                        case NormalBlending:
                            GLES20.glBlendFuncSeparate(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                            break;
                        case AdditiveBlending:
                            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
                            break;
                        case SubtractiveBlending:
                            GLES20.glBlendFuncSeparate(GLES20.GL_ZERO, GLES20.GL_ZERO, GLES20.GL_ONE_MINUS_SRC_COLOR, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                            break;
                        case MultiplyBlending:
                            GLES20.glBlendFuncSeparate(GLES20.GL_ZERO, GLES20.GL_SRC_COLOR, GLES20.GL_ZERO, GLES20.GL_SRC_ALPHA);
                            break;
                        default:
                            break;
                    }

                } else {
                    switch (blending) {
                        case NormalBlending:
                            GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                            break;
                        case AdditiveBlending:
                            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
                            break;
                        case SubtractiveBlending:
                            GLES20.glBlendFunc(GLES20.GL_ZERO, GLES20.GL_ONE_MINUS_SRC_COLOR);
                            break;
                        case MultiplyBlending:
                            GLES20.glBlendFunc(GLES20.GL_ZERO, GLES20.GL_SRC_COLOR);
                            break;
                        default:
                            break;
                    }
                }

                currentBlendSrc = 0;
                currentBlendDst = 0;
                currentBlendSrcAlpha = 0;
                currentBlendDstAlpha = 0;
                currentBlending = blending;
                currentPremultipledAlpha = premultipliedAlpha;
            }
            return;
        }

        // custom blending
        blendEquationAlpha = blendEquationAlpha;
        blendSrcAlpha = blendSrcAlpha ;
        blendDstAlpha = blendDstAlpha;

        if ( blendEquation != currentBlendEquation || blendEquationAlpha != currentBlendEquationAlpha ) {
            GLES20.glBlendEquationSeparate( GLUtils.Convert( blendEquation ), GLUtils.Convert( blendEquationAlpha ) );
            currentBlendEquation = blendEquation;
            currentBlendEquationAlpha = blendEquationAlpha;
        }

        if ( blendSrc != currentBlendSrc || blendDst != currentBlendDst || blendSrcAlpha != currentBlendSrcAlpha || blendDstAlpha != currentBlendDstAlpha ) {
            GLES20.glBlendFuncSeparate( GLUtils.Convert( blendSrc ), GLUtils.Convert( blendDst ), GLUtils.Convert( blendSrcAlpha ), GLUtils.Convert( blendDstAlpha ) );

            currentBlendSrc = blendSrc;
            currentBlendDst = blendDst;
            currentBlendSrcAlpha = blendSrcAlpha;
            currentBlendDstAlpha = blendDstAlpha;
        }

        currentBlending = blending;
        currentPremultipledAlpha = false;
    }

    public void SetMaterial(Material material, boolean frontFaceCW){
        if(material.side == DoubleSide){
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }else{
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }

        boolean flipSided = ( material.side == BackSide );
        if ( frontFaceCW ) flipSided = ! flipSided;

        SetFlipSided( flipSided );

        if(material.blending == NormalBlending && !material.transparent){
            SetBlending( NoBlending, 0, 0, 0, 0, 0, 0, false );
        }else{
            SetBlending( material.blending, material.blendEquation, material.blendSrc, material.blendDst,
                    material.blendEquationAlpha, material.blendSrcAlpha, material.blendDstAlpha, material.premultipliedAlpha );
        }

        depthBuffer.SetFunc( material.depthFunc );
        depthBuffer.SetTest( material.depthTest );
        depthBuffer.SetMask( material.depthWrite );
        colorBuffer.SetMask( material.colorWrite );

        SetPolygonOffset( material.polygonOffset, material.polygonOffsetFactor, material.polygonOffsetUnits );
    }


    public void SetFlipSided( boolean flipSided ){
        if ( currentFlipSided != flipSided ) {

            if ( flipSided ) {
                GLES20.glFrontFace( GLES20.GL_CW );
            } else {
                GLES20.glFrontFace( GLES20.GL_CCW );
            }

            currentFlipSided = flipSided;
        }
    }

    public void SetCullFace(int cullFace){

        if ( cullFace != CullFaceNone ) {
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            if ( cullFace != currentCullFace ) {
                if ( cullFace == CullFaceBack ) {
                    GLES20.glCullFace(GLES20.GL_BACK);
                } else if ( cullFace == CullFaceFront ) {
                    GLES20.glCullFace(GLES20.GL_FRONT);
                } else {
                    GLES20.glCullFace(GLES20.GL_FRONT_AND_BACK);
                }
            }
        } else {
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }

        currentCullFace = cullFace;
    }

    public void SetLineWidth(float width){
        if ( width != currentLineWidth ) {
            GLES20.glLineWidth(width);
            currentLineWidth = width;
        }
    }

    public void SetPolygonOffset(boolean polygonOffset, float factor, float units){
        if ( polygonOffset ) {
            GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
            if ( currentPolygonOffsetFactor != factor || currentPolygonOffsetUnits != units ) {
                GLES20.glPolygonOffset(factor, units);
                currentPolygonOffsetFactor = factor;
                currentPolygonOffsetUnits = units;
            }
        } else {
            GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        }
    }

    public void SetScissorTest(boolean scissorTest){
        if ( scissorTest ) {
            GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        } else {
            GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
        }
    }

    public void ActiveTexture(int slot){
        if ( slot == -1 ) slot = GLES20.GL_TEXTURE0 + maxTextures - 1;

        if ( currentTextureSlot != slot ) {
            GLES20.glActiveTexture( slot );
            currentTextureSlot = slot;
        }
    }

    public void BindTexture(int glType, int glTexture){
        if ( currentTextureSlot == -1 ) {
            ActiveTexture(-1);
        }

        BoundTexture boundTexture = currentBoundTextures.get(currentTextureSlot);

        if ( boundTexture == null ) {
            boundTexture = new BoundTexture();
            currentBoundTextures.put(currentTextureSlot, boundTexture);
        }

        if ( boundTexture.type != glType || boundTexture.texture != glTexture ) {

            int textureSlot = glTexture > 0 ? glTexture : emptyTextures.get(glType);
            GLES20.glBindTexture( glType, textureSlot );
            boundTexture.type = glType;
            boundTexture.texture = glTexture;

        }
    }

    public void TexImage2D(int target, int level, int internalFormat, int type, Bitmap image) {
        //GLES20.glTexImage2D(target, level, internalFormat, glFormat, );
        android.opengl.GLUtils.texImage2D(target, level, internalFormat, image, type, 0);
    }

    public void Scissor( Vector4 scissor){
        if (!currentScissor.Equals( scissor )) {
            GLES20.glScissor((int)scissor.x, (int)scissor.y, (int)scissor.z, (int)scissor.w);
            currentScissor.Copy( scissor );
        }
    }

    public void Viewport( Vector4 viewport){
        if (!currentViewport.Equals(viewport)) {
            GLES20.glViewport( (int)viewport.x, (int)viewport.y, (int)viewport.z, (int)viewport.w );
            currentViewport.Copy( viewport );
        }
    }

    public void Reset(){
        for ( int i = 0; i < enabledAttributes.capacity(); i ++ ) {
            if ( enabledAttributes.get(i) == 1 ) {
                GLES20.glDisableVertexAttribArray( i );
                enabledAttributes.put(i, 0);
            }
        }

        //currentTextureSlot = null;
        //currentBoundTextures = {};

        currentProgram = 0;
        currentBlending = 0;
        currentFlipSided = false;
        currentCullFace = 0;
        colorBuffer.Reset();
        depthBuffer.Reset();
        stencilBuffer.Reset();
    }


}
