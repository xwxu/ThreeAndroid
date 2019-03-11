package three.renderers;

import three.math.Vector4;
import three.textures.Texture;

public class GLRenderTarget {
    public int width;
    public int height;
    public Vector4 scissor; ;
    public boolean scissorTest = false;
    public Vector4 viewport;
    public Texture texture;
    public boolean depthBuffer;
    public boolean stencilBuffer;
    public Texture depthTexture;

    public GLRenderTarget(int width, int height, RenderTargetOptions options){
        this.width = width;
        this.height = height;
        this.scissor = new Vector4(0, 0, width, height);
        this.viewport = new Vector4(0, 0, width, height);
        this.texture = new Texture(null, -1, options.wrapS, options.wrapT,
                options.magFilter, options.minFilter, options.format, options.type, options.anisotropy, options.encoding);
        this.texture.generateMipmaps = options.generateMipmaps;
        this.texture.minFilter = options.minFilter;
        this.depthBuffer = options.depthBuffer;
        this.stencilBuffer = options.stencilBuffer;
        this.depthTexture = options.depthTexture;
    }

    public void SetSize(int width, int height){
        if ( this.width != width || this.height != height ) {

            this.width = width;
            this.height = height;

            this.Dispose();
        }

        this.viewport.Set( 0, 0, width, height );
        this.scissor.Set( 0, 0, width, height );
    }

    public GLRenderTarget Clone(){
        return new GLRenderTarget(0,0, null).Copy(this);
    }

    public GLRenderTarget Copy(GLRenderTarget source){
        this.width = source.width;
        this.height = source.height;

        this.viewport.Copy( source.viewport );

        this.texture = source.texture.Clone();

        this.depthBuffer = source.depthBuffer;
        this.stencilBuffer = source.stencilBuffer;
        this.depthTexture = source.depthTexture;

        return this;
    }

    private void Dispose() {
    }
}
