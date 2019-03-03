package unitTest.math;

import three.math.Vector2;
import three.math.Vector3;

public class Constants {

    public static final double x = 2;
    public static final double y = 3;
    public static final double z = 4;
    public static final double w = 5;

    public static final Vector2 negInf2 = new Vector2( Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY );
    public static final Vector2 posInf2 = new Vector2( Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY );

    public static final Vector2 zero2 = new Vector2();
    public static final Vector2 one2 = new Vector2( 1, 1 );
    public static final Vector2 two2 = new Vector2( 2, 2 );

    public static final Vector3 negInf3 = new Vector3( Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY );
    public static final Vector3 posInf3 = new Vector3( Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY );

    public static final Vector3 zero3 = new Vector3();
    public static final Vector3 one3 = new Vector3( 1, 1, 1 );
    public static final Vector3 two3 = new Vector3( 2, 2, 2 );

    public static final double eps = 0.0001;
}
