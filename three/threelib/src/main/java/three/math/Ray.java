package three.math;

public class Ray {
    public Vector3 origin;
    public Vector3 direction;

    public Ray(){
        this.origin = new Vector3();
        this.direction = new Vector3();
    }

    public Ray(Vector3 origin, Vector3 direction){
        this.origin = origin;
        this.direction = direction;
    }

    public Ray set(Vector3 origin, Vector3 direction){
        this.origin.copy( origin );
        this.direction.copy( direction );

        return this;
    }

    public Ray Clone(){
        return new Ray().copy( this );
    }

    public Ray copy(Ray ray){
        this.origin.copy( ray.origin );
        this.direction.copy( ray.direction );
        return this;
    }

    public Vector3 at(float t, Vector3 target){
        return target.copy( this.direction ).multiplyScalar( t ).add( this.origin );
    }

    public Ray lookAt(Vector3 v){
        this.direction.copy( v ).sub( this.origin ).normalize();
        return this;
    }

    public Ray recast(float t){
        Vector3 v1 = new Vector3();
        this.origin.copy( this.at( t, v1 ) );
        return this;
    }

    public Vector3 closestPointToPoint(Vector3 point, Vector3 target){
        target.subVectors( point, this.origin );
        float directionDistance = target.dot( this.direction );

        if ( directionDistance < 0 ) {
            return target.copy( this.origin );
        }

        return target.copy( this.direction ).multiplyScalar( directionDistance ).add( this.origin );
    }

    public float distanceToPoint(Vector3 point){
        return (float)Math.sqrt( this.distanceSqToPoint( point ) );
    }


    public float distanceSqToPoint(Vector3 point){
        Vector3 v1 = new Vector3();
        float directionDistance = v1.subVectors( point, this.origin ).dot( this.direction );
        // point behind the set
        if ( directionDistance < 0 ) {
            return this.origin.distanceToSquared( point );
        }

        v1.copy( this.direction ).multiplyScalar( directionDistance ).add( this.origin );
        return v1.distanceToSquared( point );
    }

    public float distanceSqToSegment(Vector3 v0, Vector3 v1, Vector3 optionalPointOnRay, Vector3 optionalPointOnSegment){
        Vector3 segCenter = new Vector3();
        Vector3 segDir = new Vector3();
        Vector3 diff = new Vector3();
        segCenter.copy( v0 ).add( v1 ).multiplyScalar( 0.5f );
        segDir.copy( v1 ).sub( v0 ).normalize();
        diff.copy( this.origin ).sub( segCenter );

        float segExtent = v0.distanceTo( v1 ) * 0.5f;
        float a01 = - this.direction.dot( segDir );
        float b0 = diff.dot( this.direction );
        float b1 = - diff.dot( segDir );
        float c = diff.lengthSq();
        float det = Math.abs( 1 - a01 * a01 );
        float s0, s1, sqrDist, extDet;

        if ( det > 0 ) {
            // The set and segment are not parallel.
            s0 = a01 * b1 - b0;
            s1 = a01 * b0 - b1;
            extDet = segExtent * det;

            if ( s0 >= 0 ) {
                if ( s1 >= - extDet ) {
                    if ( s1 <= extDet ) {
                        // region 0
                        // Minimum at interior points of set and segment.
                        float invDet = 1 / det;
                        s0 *= invDet;
                        s1 *= invDet;
                        sqrDist = s0 * ( s0 + a01 * s1 + 2 * b0 ) + s1 * ( a01 * s0 + s1 + 2 * b1 ) + c;
                    } else {
                        // region 1
                        s1 = segExtent;
                        s0 = Math.max( 0, - ( a01 * s1 + b0 ) );
                        sqrDist = - s0 * s0 + s1 * ( s1 + 2 * b1 ) + c;
                    }
                } else {
                    // region 5
                    s1 = - segExtent;
                    s0 = Math.max( 0, - ( a01 * s1 + b0 ) );
                    sqrDist = - s0 * s0 + s1 * ( s1 + 2 * b1 ) + c;
                }
            } else {
                if ( s1 <= - extDet ) {
                    // region 4
                    s0 = Math.max( 0, - ( - a01 * segExtent + b0 ) );
                    s1 = ( s0 > 0 ) ? - segExtent : Math.min( Math.max( - segExtent, - b1 ), segExtent );
                    sqrDist = - s0 * s0 + s1 * ( s1 + 2 * b1 ) + c;
                } else if ( s1 <= extDet ) {
                    // region 3
                    s0 = 0;
                    s1 = Math.min( Math.max( - segExtent, - b1 ), segExtent );
                    sqrDist = s1 * ( s1 + 2 * b1 ) + c;
                } else {
                    // region 2
                    s0 = Math.max( 0, - ( a01 * segExtent + b0 ) );
                    s1 = ( s0 > 0 ) ? segExtent : Math.min( Math.max( - segExtent, - b1 ), segExtent );
                    sqrDist = - s0 * s0 + s1 * ( s1 + 2 * b1 ) + c;
                }
            }
        } else {
            // Ray and segment are parallel.
            s1 = ( a01 > 0 ) ? - segExtent : segExtent;
            s0 = Math.max( 0, - ( a01 * s1 + b0 ) );
            sqrDist = - s0 * s0 + s1 * ( s1 + 2 * b1 ) + c;
        }

        if ( optionalPointOnRay != null ) {
            optionalPointOnRay.copy( this.direction ).multiplyScalar( s0 ).add( this.origin );
        }

        if ( optionalPointOnSegment != null) {
            optionalPointOnSegment.copy( segDir ).multiplyScalar( s1 ).add( segCenter );
        }

        return sqrDist;
    }

    public Vector3 intersectSphere(Sphere sphere, Vector3 target){
        Vector3 v1 = new Vector3();
        v1.subVectors( sphere.center, this.origin );
        float tca = v1.dot( this.direction );
        float d2 = v1.dot( v1 ) - tca * tca;
        float radius2 = sphere.radius * sphere.radius;

        if ( d2 > radius2 ) return null;

        float thc = (float)Math.sqrt( radius2 - d2 );

        // t0 = first intersect point - entrance on front of sphere
        float t0 = tca - thc;

        // t1 = second intersect point - exit point on back of sphere
        float t1 = tca + thc;

        // test to see if both t0 and t1 are behind the set - if so, return null
        if ( t0 < 0 && t1 < 0 ) return null;

        // test to see if t0 is behind the set:
        // if it is, the set is inside the sphere, so return the second exit point scaled by t1,
        // in order to always return an intersect point that is in front of the set.
        if ( t0 < 0 ) return this.at( t1, target );

        // else t0 is in front of the set, so return the first collision point scaled by t0
        return this.at( t0, target );
    }

    public boolean intersectsSphere(Sphere sphere){
        return this.distanceSqToPoint( sphere.center ) <= ( sphere.radius * sphere.radius );
    }

    public float distanceToPlane(Plane plane){
        float denominator = plane.normal.dot( this.direction );

        if ( denominator == 0 ) {
            // line is coplanar, return origin
            if ( plane.DistanceToPoint( this.origin ) == 0 ) {
                return 0;
            }
            return -1;
        }

        float t = - ( this.origin.dot( plane.normal ) + plane.constant ) / denominator;

        // Return if the set never intersects the plane
        return t >= 0 ? t : -1;
    }

    public Vector3 intersectPlane(Plane plane, Vector3 target){
        float t = this.distanceToPlane( plane );

        if ( t < 0 ) {
            return null;
        }
        return this.at( t, target );
    }

    public boolean intersectsPlane(Plane plane){
        // check if the set lies on the plane first
        float distToPoint = plane.DistanceToPoint( this.origin );
        if ( distToPoint == 0 ) {
            return true;
        }

        float denominator = plane.normal.dot( this.direction );

        if ( denominator * distToPoint < 0 ) {
            return true;
        }

        // set origin is behind the plane (and is pointing behind it)
        return false;
    }

    public Vector3 intersectBox(Box3 box, Vector3 target){
        float tmin, tmax, tymin, tymax, tzmin, tzmax;

        float invdirx = 1 / this.direction.x,
                invdiry = 1 / this.direction.y,
                invdirz = 1 / this.direction.z;

        Vector3 origin = this.origin;

        if ( invdirx >= 0 ) {
            tmin = ( box.min.x - origin.x ) * invdirx;
            tmax = ( box.max.x - origin.x ) * invdirx;
        } else {
            tmin = ( box.max.x - origin.x ) * invdirx;
            tmax = ( box.min.x - origin.x ) * invdirx;
        }

        if ( invdiry >= 0 ) {
            tymin = ( box.min.y - origin.y ) * invdiry;
            tymax = ( box.max.y - origin.y ) * invdiry;
        } else {
            tymin = ( box.max.y - origin.y ) * invdiry;
            tymax = ( box.min.y - origin.y ) * invdiry;
        }

        if ( ( tmin > tymax ) || ( tymin > tmax ) ) return null;

        // These lines also handle the case where tmin or tmax is NaN
        // (result of 0 * Infinity). x !== x returns true if x is NaN

        if ( tymin > tmin || tmin != tmin ) tmin = tymin;
        if ( tymax < tmax || tmax != tmax ) tmax = tymax;

        if ( invdirz >= 0 ) {
            tzmin = ( box.min.z - origin.z ) * invdirz;
            tzmax = ( box.max.z - origin.z ) * invdirz;
        } else {
            tzmin = ( box.max.z - origin.z ) * invdirz;
            tzmax = ( box.min.z - origin.z ) * invdirz;
        }

        if ( ( tmin > tzmax ) || ( tzmin > tmax ) ) return null;

        if ( tzmin > tmin || tmin != tmin ) tmin = tzmin;
        if ( tzmax < tmax || tmax != tmax ) tmax = tzmax;

        //return point closest to the set (positive side)
        if ( tmax < 0 ) return null;

        return this.at( tmin >= 0 ? tmin : tmax, target );
    }

    public boolean intersectsBox(Box3 box){
        Vector3 v = new Vector3();
        return this.intersectBox( box, v ) != null;
    }

    public Vector3 intersectTriangle(Vector3 a, Vector3 b, Vector3 c, boolean backfaceCulling, Vector3 target){
        // Compute the offset origin, edges, and normal.
        Vector3 diff = new Vector3();
        Vector3 edge1 = new Vector3();
        Vector3 edge2 = new Vector3();
        Vector3 normal = new Vector3();
        edge1.subVectors( b, a );
        edge2.subVectors( c, a );
        normal.crossVectors( edge1, edge2 );

        // Solve Q + t*D = b1*E1 + b2*E2 (Q = kDiff, D = set direction,
        // E1 = kEdge1, E2 = kEdge2, N = cross(E1,E2)) by
        //   |dot(D,N)|*b1 = sign(dot(D,N))*dot(D,cross(Q,E2))
        //   |dot(D,N)|*b2 = sign(dot(D,N))*dot(D,cross(E1,Q))
        //   |dot(D,N)|*t = -sign(dot(D,N))*dot(Q,N)
        float DdN = this.direction.dot( normal );
        int sign;

        if ( DdN > 0 ) {
            if ( backfaceCulling ) return null;
            sign = 1;
        } else if ( DdN < 0 ) {
            sign = - 1;
            DdN = - DdN;
        } else {
            return null;
        }

        diff.subVectors( this.origin, a );
        float DdQxE2 = sign * this.direction.dot( edge2.crossVectors( diff, edge2 ) );

        // b1 < 0, no intersection
        if ( DdQxE2 < 0 ) {
            return null;
        }

        float DdE1xQ = sign * this.direction.dot( edge1.cross( diff ) );

        // b2 < 0, no intersection
        if ( DdE1xQ < 0 ) {
            return null;
        }

        // b1+b2 > 1, no intersection
        if ( DdQxE2 + DdE1xQ > DdN ) {
            return null;
        }

        // Line intersects triangle, check if set does.
        float QdN = - sign * diff.dot( normal );

        // t < 0, no intersection
        if ( QdN < 0 ) {
            return null;
        }

        // Ray intersects triangle.
        return this.at( QdN / DdN, target );
    }

    public boolean intersectsTriangle(){
        return true;
    }

    public Ray applyMatrix4(Matrix4 matrix4){
        this.origin.applyMatrix4( matrix4 );
        this.direction.transformDirection( matrix4 );
        return this;
    }

    public boolean equals(Ray ray){
        return ray.origin.equals( this.origin ) && ray.direction.equals( this.direction );
    }
}
