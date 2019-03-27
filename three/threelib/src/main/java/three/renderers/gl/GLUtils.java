package three.renderers.gl;

import android.opengl.GLES20;

import static  three.constants.*;

public class GLUtils {
    public static int convert(int p){

        if ( p == RepeatWrapping ) return GLES20.GL_REPEAT;
        if ( p == ClampToEdgeWrapping ) return  GLES20.GL_CLAMP_TO_EDGE;
        if ( p == MirroredRepeatWrapping ) return  GLES20.GL_MIRRORED_REPEAT;

        if ( p == NearestFilter ) return  GLES20.GL_NEAREST;
        if ( p == NearestMipMapNearestFilter ) return  GLES20.GL_NEAREST_MIPMAP_NEAREST;
        if ( p == NearestMipMapLinearFilter ) return  GLES20.GL_NEAREST_MIPMAP_LINEAR;

        if ( p == LinearFilter ) return  GLES20.GL_LINEAR;
        if ( p == LinearMipMapNearestFilter ) return  GLES20.GL_LINEAR_MIPMAP_NEAREST;
        if ( p == LinearMipMapLinearFilter ) return  GLES20.GL_LINEAR_MIPMAP_LINEAR;

        if ( p == UnsignedByteType ) return  GLES20.GL_UNSIGNED_BYTE;
        if ( p == UnsignedShort4444Type ) return  GLES20.GL_UNSIGNED_SHORT_4_4_4_4;
        if ( p == UnsignedShort5551Type ) return  GLES20.GL_UNSIGNED_SHORT_5_5_5_1;
        if ( p == UnsignedShort565Type ) return  GLES20.GL_UNSIGNED_SHORT_5_6_5;

        if ( p == ByteType ) return  GLES20.GL_BYTE;
        if ( p == ShortType ) return  GLES20.GL_SHORT;
        if ( p == UnsignedShortType ) return  GLES20.GL_UNSIGNED_SHORT;
        if ( p == IntType ) return  GLES20.GL_INT;
        if ( p == UnsignedIntType ) return  GLES20.GL_UNSIGNED_INT;
        if ( p == FloatType ) return  GLES20.GL_FLOAT;
        if ( p == AlphaFormat ) return  GLES20.GL_ALPHA;
        if ( p == RGBFormat ) return  GLES20.GL_RGB;
        if ( p == RGBAFormat ) return  GLES20.GL_RGBA;
        if ( p == LuminanceFormat ) return  GLES20.GL_LUMINANCE;
        if ( p == LuminanceAlphaFormat ) return  GLES20.GL_LUMINANCE_ALPHA;
        if ( p == DepthFormat ) return  GLES20.GL_DEPTH_COMPONENT;
        if ( p == DepthStencilFormat ) return  GLES20.GL_STENCIL_BITS; // NOT SURE CORRECT
        if ( p == RedFormat ) return  GLES20.GL_RED_BITS; // NOT SURE CORRECT

        if ( p == AddEquation ) return  GLES20.GL_FUNC_ADD;
        if ( p == SubtractEquation ) return  GLES20.GL_FUNC_SUBTRACT;
        if ( p == ReverseSubtractEquation ) return  GLES20.GL_FUNC_REVERSE_SUBTRACT;

        if ( p == ZeroFactor ) return  GLES20.GL_ZERO;
        if ( p == OneFactor ) return  GLES20.GL_ONE;
        if ( p == SrcColorFactor ) return  GLES20.GL_SRC_COLOR;
        if ( p == OneMinusSrcColorFactor ) return  GLES20.GL_ONE_MINUS_SRC_COLOR;
        if ( p == SrcAlphaFactor ) return  GLES20.GL_SRC_ALPHA;
        if ( p == OneMinusSrcAlphaFactor ) return  GLES20.GL_ONE_MINUS_SRC_ALPHA;
        if ( p == DstAlphaFactor ) return  GLES20.GL_DST_ALPHA;
        if ( p == OneMinusDstAlphaFactor ) return  GLES20.GL_ONE_MINUS_DST_ALPHA;

        if ( p == DstColorFactor ) return  GLES20.GL_DST_COLOR;
        if ( p == OneMinusDstColorFactor ) return  GLES20.GL_ONE_MINUS_DST_COLOR;
        if ( p == SrcAlphaSaturateFactor ) return  GLES20.GL_SRC_ALPHA_SATURATE;

        return 0;
    }

    public static byte[] trimZero(byte[] bytes){
        int pos = 0;
        for(int i =0; i < bytes.length; ++i){
            if(bytes[i] == 0){
                pos = i;
                break;
            }
            pos ++;
        }
        byte[] newBytes = new byte[pos];
        for(int i = 0; i < pos; ++i){
            newBytes[i] = bytes[i];
        }
        return newBytes;
    }
}
