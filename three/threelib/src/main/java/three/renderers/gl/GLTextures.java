package three.renderers.gl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLES30;

import three.renderers.GLRenderTarget;
import three.textures.CubeTexture;
import three.textures.Texture;
import three.util.TextureProperties;

public class GLTextures {

    private GLState state;
    private GLProperties properties;
    private GLInfo info;

    public GLTextures(GLState state, GLProperties properties, GLInfo info){
        this.state = state;
        this.properties = properties;
        this.info = info;
    }

    public void UpdateRenderTargetMipmap(GLRenderTarget renderTarget) {
    }

    public void SetTexture2D(Texture texture, int slot) {
        TextureProperties textureProperties = properties.GetTexture( texture );

        // TODO: update video texture

        if ( texture.version > 0 && textureProperties.__version != texture.version ) {
            Bitmap image = texture.image;

            if ( image == null ) {
                System.out.println( "Texture marked for update but image is undefined" );
            } else {
                UploadTexture( textureProperties, texture, slot );
                return;
            }
        }

        state.ActiveTexture( GLES20.GL_TEXTURE0 + slot );
        state.BindTexture( GLES20.GL_TEXTURE_2D, textureProperties.__webglTexture );

    }

    private void UploadTexture(TextureProperties textureProperties, Texture texture, int slot) {
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
            textureProperties.__webglTexture = buf[0];

            info.memory.textures ++;

        }
        state.ActiveTexture( GLES20.GL_TEXTURE0 + slot );


        state.BindTexture( textureType, textureProperties.__webglTexture );

        // GLES20 doesnt have UNPACK_FLIP_Y && UNPACK_PREMULTIPLY_ALPHA
        GLES20.glPixelStorei( GLES20.GL_UNPACK_ALIGNMENT, texture.unpackAlignment );


        boolean isPowerOfTwoImage = true;//IsPowerOfTwo( image );

        int glFormat = GLUtils.Convert( texture.format );
        int glType = GLUtils.Convert( texture.type );
        int glInternalFormat = GetInternalFormat( glFormat, glType );

        SetTextureParameters( textureType, texture, isPowerOfTwoImage );

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

                state.TexImage2D( GLES20.GL_TEXTURE_2D, 0, glInternalFormat, glType, texture.image );
                textureProperties.__maxMipLevel = 0;

            }

        }

        // TODO: generate mip maps

        textureProperties.__version = texture.version;

    }

    private void SetTextureParameters(int textureType, Texture texture, boolean isPowerOfTwoImage) {
    }

    public void SetTexture3D(Texture texture, int slot) {
    }


    public void SetTextureCube(CubeTexture texture, int slot) {
    }

    private int GetInternalFormat(int glFormat, int glType){
        return glFormat;
    }
}
