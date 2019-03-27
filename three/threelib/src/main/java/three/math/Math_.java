package three.math;

import java.lang.Math;
import java.util.UUID;

public class Math_ {
    public static final float DEG2RAD = (float)Math.PI / 180;
    public static final float RAD2DEG = 180 / (float)Math.PI;
    public static final float LN2 = 0.69314718055994528623f;

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static float clamp(float value, float min, float max){
        return Math.max(min, Math.min( max, value ));
    }

    public static float euclideanModulo(float n, float m)
    {
        return ( ( n % m ) + m ) % m;
    }

    public static float mapLinear(float x, float a1, float a2, float b1, float b2){
        return b1 + ( x - a1 ) * ( b2 - b1 ) / ( a2 - a1 );
    }

    public static float lerp(float x, float y, float t){
        return ( 1 - t ) * x + t * y;
    }

    public static float smoothStep(float x, float min , float max){
        if ( x <= min ) return 0;
        if ( x >= max ) return 1;

        x = ( x - min ) / ( max - min );
        return x * x * ( 3 - 2 * x );
    }

    public static float smootherStep(float x, float min, float max){
        if ( x <= min ) return 0;
        if ( x >= max ) return 1;

        x = ( x - min ) / ( max - min );
        return x * x * x * ( x * ( x * 6 - 15 ) + 10 );
    }

    public static int randInt(float low, float high){
        return (int)(low + Math.floor( Math.random() * ( high - low + 1 ) ));
    }

    public static float randFloat(float range){
        return range * ( 0.5f - (float)Math.random() );
    }

    public static float randFloatSpread(float range){
        return range * ( 0.5f - (float)Math.random() );
    }

    public static float degToRad(float degrees){
        return degrees * DEG2RAD;
    }

    public static float radToDeg(float radians){
        return radians * RAD2DEG;
    }

    public static boolean isPowerOfTwo(int value){
        return (value & ( value - 1 )) == 0 && value != 0;
    }

    public static float ceilPowerOfTwo(float value){
        return (float)Math.pow( 2, Math.ceil( Math.log( value ) / LN2 ) );
    }

    public static float floorPowerOfTwo(float value){
        return (float)Math.pow( 2, Math.floor( Math.log( value ) / LN2 ) );
    }
}
