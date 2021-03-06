package three.textures;

import android.graphics.Bitmap;

import three.math.Math_;
import three.math.Matrix3;
import three.math.Vector2;

import static three.constants.ClampToEdgeWrapping;
import static three.constants.LinearEncoding;
import static three.constants.LinearFilter;
import static three.constants.LinearMipMapLinearFilter;
import static three.constants.RGBAFormat;
import static three.constants.UVMapping;
import static three.constants.UnsignedByteType;

public class Texture {

    private static int textureId = 0;
    private static final int DEFAULT_MAPPING = UVMapping;

    public int id = textureId ++;
    public String uuid = Math_.generateUUID();
    public String name = "";

    public Bitmap image;
    public int mapping;
    public int wrapS;
    public int wrapT;
    public int magFilter;
    public int minFilter;
    public float anisotropy;
    public int format;
    public int type;
    public Vector2 offset = new Vector2(0, 0);
    public Vector2 repeat = new Vector2(1,1);
    public Vector2 center = new Vector2(0, 0);
    public float rotation = 0;
    public boolean matrixAutoUpdate = true;
    public Matrix3 matrix = new Matrix3();
    public boolean generateMipmaps = true;
    public boolean premultiplyAlpha = false;
    public boolean flipY = true;
    public int unpackAlignment = 4;
    public int encoding;
    public int version = 0;

    public Texture(){
        mapping = DEFAULT_MAPPING;
        wrapS = ClampToEdgeWrapping;
        wrapT = ClampToEdgeWrapping;
        magFilter = LinearFilter;
        minFilter = LinearMipMapLinearFilter;
        anisotropy = 1;
        format = RGBAFormat;
        type = UnsignedByteType;
        encoding = LinearEncoding;
    }

    public Texture(Bitmap image, int mapping, int wrapS, int wrapT, int magFilter, int minFilter,
                   int format, int type, float anisotropy, int encoding){

        this.image = image;
        this.mapping = mapping;
        this.wrapS = wrapS != 0 ? wrapS : ClampToEdgeWrapping;
        this.wrapT = wrapT != 0 ? wrapT : ClampToEdgeWrapping;
        this.magFilter = magFilter != 0 ? magFilter : LinearFilter;
        this.minFilter = minFilter != 0 ? minFilter : LinearMipMapLinearFilter;
        this.format = format != 0 ? format : RGBAFormat;
        this.type = type != 0 ? type : UnsignedByteType;
        this.anisotropy = anisotropy != 0.0 ? anisotropy : 1;
        this.encoding = encoding != 0 ? encoding : LinearEncoding;
    }



    public void updateMatrix(){
        //GLUtils.texImage2D();
        //GLES20.glTexImage2D();
    }

    public Texture Clone(){
        return this;
    }

    public Texture copy(Texture source){
        this.name = source.name;

        this.image = source.image;
        //this.mipmaps = source.mipmaps.slice( 0 );

        this.mapping = source.mapping;

        this.wrapS = source.wrapS;
        this.wrapT = source.wrapT;

        this.magFilter = source.magFilter;
        this.minFilter = source.minFilter;

        this.anisotropy = source.anisotropy;

        this.format = source.format;
        this.type = source.type;

        this.offset.copy( source.offset );
        this.repeat.copy( source.repeat );
        this.center.copy( source.center );
        this.rotation = source.rotation;

        this.matrixAutoUpdate = source.matrixAutoUpdate;
        this.matrix.copy( source.matrix );

        this.generateMipmaps = source.generateMipmaps;
        this.premultiplyAlpha = source.premultiplyAlpha;
        this.flipY = source.flipY;
        this.unpackAlignment = source.unpackAlignment;
        this.encoding = source.encoding;

        return this;
    }
}
