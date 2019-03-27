package three.renderers.gl;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import three.renderers.GLRenderTarget;
import three.renderers.GLRenderTargetCube;
import three.textures.CubeTexture;
import three.textures.DepthTexture;
import three.textures.Texture;
import three.util.RenderTargetProperties;
import three.util.TextureProperties;

import static three.constants.DepthFormat;
import static three.constants.DepthStencilFormat;
import static three.constants.NearestFilter;
import static three.constants.NearestMipMapLinearFilter;
import static three.constants.NearestMipMapNearestFilter;

public class GLTextures {

    private GLState state;
    private GLProperties properties;
    private GLInfo info;

    public GLTextures(GLState state, GLProperties properties, GLInfo info){
        this.state = state;
        this.properties = properties;
        this.info = info;
    }

    public void updateRenderTargetMipmap(GLRenderTarget renderTarget) {
    }

    public void setTexture2D(Texture texture, int slot) {
        TextureProperties textureProperties = properties.getTexture( texture );

        // TODO: update video texture

        if ( texture.version > 0 && textureProperties.__version != texture.version ) {
            Bitmap image = texture.image;

            if ( image == null ) {
                System.out.println( "Texture marked for update but image is undefined" );
            } else {
                uploadTexture( textureProperties, texture, slot );
                return;
            }
        }

        state.activeTexture( GLES20.GL_TEXTURE0 + slot );
        state.bindTexture( GLES20.GL_TEXTURE_2D, textureProperties.__glTexture);

    }

    private void uploadTexture(TextureProperties textureProperties, Texture texture, int slot) {
        int textureType;

        if ( false ) {
            //textureType = _gl.TEXTURE_3D;
        } else {
            textureType = GLES20.GL_TEXTURE_2D;
        }


        if ( textureProperties.__webglInit ==  false) {

            textureProperties.__webglInit = true;
            //texture.addEventListener( 'dispose', onTextureDispose );

            int[] buf = new int[1];
            GLES20.glGenTextures(1, buf, 0);
            textureProperties.__glTexture = buf[0];

            info.memory.textures ++;

        }
        state.activeTexture( GLES20.GL_TEXTURE0 + slot );


        state.bindTexture( textureType, textureProperties.__glTexture);

        // GLES20 doesnt have UNPACK_FLIP_Y && UNPACK_PREMULTIPLY_ALPHA
        GLES20.glPixelStorei( GLES20.GL_UNPACK_ALIGNMENT, texture.unpackAlignment );


        boolean isPowerOfTwoImage = true;//isPowerOfTwo( image );

        int glFormat = GLUtils.convert( texture.format );
        int glType = GLUtils.convert( texture.type );
        int glInternalFormat = getInternalFormat( glFormat, glType );

        setTextureParameters( textureType, texture, isPowerOfTwoImage );

        //var mipmap, mipmaps = texture.mipmaps;

        if ( false ) {
            // TODO: depth texture
        } else if ( false ) {
            // TODO: data texture
        } else if ( false ) {
            // TODO: compressed texture

        } else if ( false ) {
            // TODO: data texture 3d

        } else {

            // regular Texture (image, video, canvas)

            // use manually created mipmaps if available
            // if there are no manual mipmaps
            // set 0 level mipmap and then use GL to generate other mipmap levels

            if ( false ) {

                // TODO: mip maps

            } else {

                state.texImage2D( GLES20.GL_TEXTURE_2D, 0, glInternalFormat, glType, texture.image );
                textureProperties.__maxMipLevel = 0;

            }

        }

        // TODO: generate mip maps

        textureProperties.__version = texture.version;

    }

    private void setTextureParameters(int textureType, Texture texture, boolean isPowerOfTwoImage) {
        if ( isPowerOfTwoImage ) {

            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_WRAP_S, GLUtils.convert( texture.wrapS ) );
            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_WRAP_T, GLUtils.convert( texture.wrapT ) );

            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_MAG_FILTER, GLUtils.convert( texture.magFilter ) );
            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_MIN_FILTER, GLUtils.convert( texture.minFilter ) );

        } else {

            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );

            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_MAG_FILTER, filterFallback( texture.magFilter ) );
            GLES20.glTexParameteri( textureType, GLES20.GL_TEXTURE_MIN_FILTER, filterFallback( texture.minFilter ) );



        }
    }

    private int filterFallback(int f) {
        if ( f == NearestFilter || f == NearestMipMapNearestFilter || f == NearestMipMapLinearFilter ) {
            return GLES20.GL_NEAREST;
        }

        return GLES20.GL_LINEAR;
    }

    public void setTexture3D(Texture texture, int slot) {
    }


    public void setTextureCube(CubeTexture texture, int slot) {
    }

    private int getInternalFormat(int glFormat, int glType){
        return glFormat;
    }

    public void setupRenderTarget(GLRenderTarget renderTarget) {
        RenderTargetProperties renderTargetProperties = properties.getRenderTarget( renderTarget );
        TextureProperties textureProperties = properties.getTexture( renderTarget.texture );

        //todo: add dispose listener

        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        textureProperties.__glTexture = tex[0];

        info.memory.textures ++;

        boolean isCube = ( renderTarget instanceof GLRenderTargetCube );
        boolean isTargetPowerOfTwo = false; //isPowerOfTwo( renderTarget );

        // setup framebuffer
        if ( isCube ) {
            renderTargetProperties.__glFramebuffers = new int[6];

            int[] buf = new int[6];
            GLES20.glGenFramebuffers(6, buf, 0);
            for ( int i = 0; i < 6; i ++ ) {
                renderTargetProperties.__glFramebuffers[ i ] = buf[i];
            }

        } else {
            int[] buf = new int[1];
            GLES20.glGenFramebuffers(1, buf, 0);
            renderTargetProperties.__glFramebuffer = buf[0];
        }

        // setup color buffer

        if ( isCube ) {
            // TODO
        } else {

            state.bindTexture( GLES20.GL_TEXTURE_2D, textureProperties.__glTexture );
            setTextureParameters( GLES20.GL_TEXTURE_2D, renderTarget.texture, isTargetPowerOfTwo );
            setupFrameBufferTexture( renderTargetProperties.__glFramebuffer, renderTarget, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D );

            // todo: generate mipmaps

            state.bindTexture( GLES20.GL_TEXTURE_2D, 0 );

        }

        // setup depth and stencil buffers

        if ( renderTarget.depthBuffer ) {
            setupDepthRenderbuffer( renderTarget );
        }

    }

    private void setupDepthRenderbuffer(GLRenderTarget renderTarget) {
        RenderTargetProperties renderTargetProperties = properties.getRenderTarget( renderTarget );

        boolean isCube = renderTarget instanceof GLRenderTargetCube;

        if ( renderTarget.depthTexture != null ) {
            setupDepthTexture( renderTargetProperties.__glFramebuffer, renderTarget );
        } else {

            if ( isCube ) {
                // TODO
            } else {
                GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, renderTargetProperties.__glFramebuffer );
                int[] buf = new int[1];
                GLES20.glGenRenderbuffers(1, buf, 0);
                renderTargetProperties.__glDepthbuffer = buf[0];
                setupRenderBufferStorage( renderTargetProperties.__glDepthbuffer, renderTarget );
            }
        }

        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, 0 );
    }

    private void setupRenderBufferStorage(int renderbuffer, GLRenderTarget renderTarget) {
        GLES20.glBindRenderbuffer( GLES20.GL_RENDERBUFFER, renderbuffer );

        if ( renderTarget.depthBuffer && ! renderTarget.stencilBuffer ) {

            GLES20.glRenderbufferStorage( GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, renderTarget.width, renderTarget.height );
            GLES20.glFramebufferRenderbuffer( GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderbuffer );

        } else if ( renderTarget.depthBuffer && renderTarget.stencilBuffer ) {

            //GLES20.glRenderbufferStorage( GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_STENCIL, renderTarget.width, renderTarget.height );
            //GLES20.glFramebufferRenderbuffer( GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_STENCIL_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderbuffer );

        } else {

            // FIXME: We don't support !depth !stencil
            GLES20.glRenderbufferStorage( GLES20.GL_RENDERBUFFER, GLES20.GL_RGBA4, renderTarget.width, renderTarget.height );

        }

        GLES20.glBindRenderbuffer( GLES20.GL_RENDERBUFFER, 0 );
    }

    private void setupDepthTexture(int framebuffer, GLRenderTarget renderTarget) {
        boolean isCube = renderTarget instanceof GLRenderTargetCube;

        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, framebuffer );

        if ( ! ( renderTarget.depthTexture != null && renderTarget.depthTexture instanceof DepthTexture) ) {

        }

        // upload an empty depth texture with framebuffer size
//        if ( ! properties.getTexture( renderTarget.depthTexture ).__glTexture ||
//                renderTarget.depthTexture.image.width != renderTarget.width ||
//                renderTarget.depthTexture.image.height != renderTarget.height ) {
//
//            renderTarget.depthTexture.image.width = renderTarget.width;
//            renderTarget.depthTexture.image.height = renderTarget.height;
//            renderTarget.depthTexture.needsUpdate = true;
//        }

        setTexture2D( renderTarget.depthTexture, 0 );

        int glDepthTexture = properties.getTexture( renderTarget.depthTexture ).__glTexture;

        if ( renderTarget.depthTexture.format == DepthFormat ) {

            GLES20.glFramebufferTexture2D( GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                    GLES20.GL_TEXTURE_2D, glDepthTexture, 0 );

        } else if ( renderTarget.depthTexture.format == DepthStencilFormat ) {

            //GLES20.glFramebufferTexture2D( GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_STENCIL_ATTACHMENT, _gl.TEXTURE_2D, glDepthTexture, 0 );

        } else {
        }
    }

    private void setupFrameBufferTexture(int framebuffer, GLRenderTarget renderTarget, int attachment, int textureTarget) {
        int glFormat = GLUtils.convert( renderTarget.texture.format );
        int glType = GLUtils.convert( renderTarget.texture.type );
        int glInternalFormat = getInternalFormat( glFormat, glType );
        state.texImage2D( textureTarget, 0, glInternalFormat, renderTarget.width, renderTarget.height, 0, glFormat, glType, null );
        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, framebuffer );
        GLES20.glFramebufferTexture2D( GLES20.GL_FRAMEBUFFER, attachment, textureTarget, properties.getTexture( renderTarget.texture ).__glTexture, 0 );
        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, 0 );
    }
}
