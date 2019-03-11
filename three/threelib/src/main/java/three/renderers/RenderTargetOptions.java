package three.renderers;

import three.textures.Texture;

import static three.constants.LinearFilter;

public class RenderTargetOptions {
    public boolean generateMipmaps = false;
    public int minFilter = LinearFilter;
    public int magFilter;
    public boolean depthBuffer = true;
    public boolean stencilBuffer = true;
    public Texture depthTexture = null;
    public int wrapS;
    public int wrapT;

    public int format;
    public int type;
    public float anisotropy;
    public int encoding;
}
