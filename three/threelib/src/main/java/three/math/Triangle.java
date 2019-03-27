package three.math;

public class Triangle {
    public Vector3 a;
    public Vector3 b;
    public Vector3 c;

    public Triangle(){
        this.a = new Vector3();
        this.b = new Vector3();
        this.c = new Vector3();
    }

    public Triangle(Vector3 a, Vector3 b, Vector3 c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle set(Vector3 a, Vector3 b, Vector3 c){
        this.a.copy( a );
        this.b.copy( b );
        this.c.copy( c );
        return this;
    }

    public Triangle setFromPointsAndIndices(){
        return this;
    }

    public Triangle Clone(){
        return this;
    }

    public Triangle copy(Triangle triangle){
        this.a.copy( triangle.a );
        this.b.copy( triangle.b );
        this.c.copy( triangle.c );
        return  this;
    }

    public float getArea(){
        Vector3 v0 = new Vector3();
        Vector3 v1 = new Vector3();

        v0.subVectors( this.c, this.b );
        v1.subVectors( this.a, this.b );

        return v0.cross( v1 ).length() * 0.5f;
    }

    public Triangle getMidPoint(){
        return this;
    }

    public static Vector3 getNormal(Vector3 a, Vector3 b, Vector3 c, Vector3 target){
        Vector3 v0 = new Vector3();
        target.subVectors( c, b );
        v0.subVectors( a, b );
        target.cross( v0 );

        float targetLengthSq = target.lengthSq();
        if ( targetLengthSq > 0 ) {
            return target.multiplyScalar( 1 / (float)Math.sqrt( targetLengthSq ) );
        }

        return target.set( 0, 0, 0 );
    }

    public Vector3 getNormal(Vector3 target){
        return Triangle.getNormal( this.a, this.b, this.c, target );
    }

    public static Vector3 getBarycoord(Vector3 point, Vector3 a, Vector3 b, Vector3 c, Vector3 target){
        Vector3 v0 = new Vector3();
        Vector3 v1 = new Vector3();
        Vector3 v2 = new Vector3();

        v0.subVectors( c, a );
        v1.subVectors( b, a );
        v2.subVectors( point, a );

        float dot00 = v0.dot( v0 );
        float dot01 = v0.dot( v1 );
        float dot02 = v0.dot( v2 );
        float dot11 = v1.dot( v1 );
        float dot12 = v1.dot( v2 );

        float denom = ( dot00 * dot11 - dot01 * dot01 );

        // collinear or singular triangle
        if ( denom == 0 ) {
            // arbitrary location outside of triangle?
            // not sure if this is the best idea, maybe should be returning undefined
            return target.set( - 2, - 1, - 1 );
        }

        float invDenom = 1 / denom;
        float u = ( dot11 * dot02 - dot01 * dot12 ) * invDenom;
        float v = ( dot00 * dot12 - dot01 * dot02 ) * invDenom;

        // barycentric coordinates must always sum to 1
        return target.set( 1 - u - v, v, u );
    }

    public Vector3 getBarycoord(Vector3 point, Vector3 target){
        return Triangle.getBarycoord( point, this.a, this.b, this.c, target );
    }

    public static boolean containsPoint(Vector3 point, Vector3 a, Vector3 b, Vector3 c){
        Vector3 v1 = new Vector3();
        Triangle.getBarycoord( point, a, b, c, v1 );
        return ( v1.x >= 0 ) && ( v1.y >= 0 ) && ( ( v1.x + v1.y ) <= 1 );
    }

    public boolean containsPoint(Vector3 point){
        return Triangle.containsPoint( point, this.a, this.b, this.c );
    }

    public static Vector2 getUV(Vector3 point, Vector3 p1, Vector3 p2, Vector3 p3,
                                Vector2 uv1, Vector2 uv2, Vector2 uv3, Vector2 target){
        Vector3 barycoord = new Vector3();
        Triangle.getBarycoord( point, p1, p2, p3, barycoord );

        target.set( 0, 0 );
        target.addScaledVector( uv1, barycoord.x );
        target.addScaledVector( uv2, barycoord.y );
        target.addScaledVector( uv3, barycoord.z );

        return target;
    }

    public Vector2 getUV(Vector3 point, Vector2 uv1, Vector2 uv2, Vector2 uv3, Vector2 result){
        return Triangle.getUV( point, this.a, this.b, this.c, uv1, uv2, uv3, result );
    }

    public Plane getPlane(Vector3 target){
        return null;
    }

    public boolean intersectsBox(Box3 box){
        return box.intersectsTriangle(this);
    }

    public Vector3 closestPointToPoint(Vector3 p, Vector3 target){
        Vector3 vab = new Vector3();
        Vector3 vac = new Vector3();
        Vector3 vbc = new Vector3();
        Vector3 vap = new Vector3();
        Vector3 vbp = new Vector3();
        Vector3 vcp = new Vector3();

        Vector3 a = this.a, b = this.b, c = this.c;
        float v, w;

        // algorithm thanks to Real-Time Collision Detection by Christer Ericson,
        // published by Morgan Kaufmann Publishers, (c) 2005 Elsevier Inc.,
        // under the accompanying license; see chapter 5.1.5 for detailed explanation.
        // basically, we're distinguishing which of the voronoi regions of the triangle
        // the point lies in with the minimum amount of redundant computation.

        vab.subVectors( b, a );
        vac.subVectors( c, a );
        vap.subVectors( p, a );
        float d1 = vab.dot( vap );
        float d2 = vac.dot( vap );
        if ( d1 <= 0 && d2 <= 0 ) {
            // vertex region of A; barycentric coords (1, 0, 0)
            return target.copy( a );
        }

        vbp.subVectors( p, b );
        float d3 = vab.dot( vbp );
        float d4 = vac.dot( vbp );
        if ( d3 >= 0 && d4 <= d3 ) {

            // vertex region of B; barycentric coords (0, 1, 0)
            return target.copy( b );

        }

        float vc = d1 * d4 - d3 * d2;
        if ( vc <= 0 && d1 >= 0 && d3 <= 0 ) {
            v = d1 / ( d1 - d3 );
            // edge region of AB; barycentric coords (1-v, v, 0)
            return target.copy( a ).addScaledVector( vab, v );

        }

        vcp.subVectors( p, c );
        float d5 = vab.dot( vcp );
        float d6 = vac.dot( vcp );
        if ( d6 >= 0 && d5 <= d6 ) {
            // vertex region of C; barycentric coords (0, 0, 1)
            return target.copy( c );
        }

        float vb = d5 * d2 - d1 * d6;
        if ( vb <= 0 && d2 >= 0 && d6 <= 0 ) {
            w = d2 / ( d2 - d6 );
            // edge region of AC; barycentric coords (1-w, 0, w)
            return target.copy( a ).addScaledVector( vac, w );

        }

        float va = d3 * d6 - d5 * d4;
        if ( va <= 0 && ( d4 - d3 ) >= 0 && ( d5 - d6 ) >= 0 ) {

            vbc.subVectors( c, b );
            w = ( d4 - d3 ) / ( ( d4 - d3 ) + ( d5 - d6 ) );
            // edge region of BC; barycentric coords (0, 1-w, w)
            return target.copy( b ).addScaledVector( vbc, w ); // edge region of BC

        }

        // face region
        float denom = 1 / ( va + vb + vc );
        // u = va * denom
        v = vb * denom;
        w = vc * denom;
        return target.copy( a ).addScaledVector( vab, v ).addScaledVector( vac, w );

    }

    public boolean equals(Triangle triangle){
        return triangle.a.equals( this.a ) && triangle.b.equals( this.b ) && triangle.c.equals( this.c );
    }
}
