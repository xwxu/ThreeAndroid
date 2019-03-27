package unitTest.math;

import org.junit.Test;
import static org.junit.Assert.*;

import three.math.Box3;

public class Box3Tests {

    @Test
    public void Instancing() {

        Box3 a = new Box3();
        assertTrue(a.min.equals( Constants.posInf3 ));
        assertTrue( a.max.equals( Constants.negInf3 ));

        Box3 b = new Box3( Constants.zero3.Clone(), Constants.zero3.Clone() );
        assertTrue( b.min.equals( Constants.zero3 ));
        assertTrue( b.max.equals( Constants.zero3 ));

        Box3 c = new Box3( Constants.zero3.Clone(), Constants.one3.Clone() );
        assertTrue( c.min.equals( Constants.zero3 ));
        assertTrue( c.max.equals( Constants.one3 ));
    }

}
