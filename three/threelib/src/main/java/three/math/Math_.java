package three.math;

import java.lang.Math;
import java.util.UUID;

public class Math_ {
    public static final float DEG2RAD = (float)Math.PI / 180;
    public static final float RAD2DEG = 180 / (float)Math.PI;
    public static final float LN2 = 0.69314718055994528623f;

    public static String GenerateUUID(){
        return UUID.randomUUID().toString();
    }

    public static float Clamp(float value, float min, float max){
        return Math.max(min, Math.min( max, value ));
    }

    public static float EuclideanModulo(float n, float m)
    {
        return ( ( n % m ) + m ) % m;
    }

    public static float MapLinear(float x, float a1, float a2, float b1, float b2){
        return b1 + ( x - a1 ) * ( b2 - b1 ) / ( a2 - a1 );
    }

    public static float Lerp(float x, float y, float t){
        return ( 1 - t ) * x + t * y;
    }

    public static float SmoothStep(float x, float min ,float max){
        if ( x <= min ) return 0;
        if ( x >= max ) return 1;

        x = ( x - min ) / ( max - min );
        return x * x * ( 3 - 2 * x );
    }

    public static float SmootherStep(float x, float min, float max){
        if ( x <= min ) return 0;
        if ( x >= max ) return 1;

        x = ( x - min ) / ( max - min );
        return x * x * x * ( x * ( x * 6 - 15 ) + 10 );
    }

    public static int RandInt(float low, float high){
        return (int)(low + Math.floor( Math.random() * ( high - low + 1 ) ));
    }

    public static float RandFloat(float range){
        return range * ( 0.5f - (float)Math.random() );
    }

    public static float RandFloatSpread(float range){
        return range * ( 0.5f - (float)Math.random() );
    }

    public static float DegToRad(float degrees){
        return degrees * DEG2RAD;
    }

    public static float RadToDeg(float radians){
        return radians * RAD2DEG;
    }

    public static boolean IsPowerOfTwo(int value){
        return (value & ( value - 1 )) == 0 && value != 0;
    }

    public static float CeilPowerOfTwo(float value){
        return (float)Math.pow( 2, Math.ceil( Math.log( value ) / LN2 ) );
    }

    public static float FloorPowerOfTwo(float value){
        return (float)Math.pow( 2, Math.floor( Math.log( value ) / LN2 ) );
    }
}
