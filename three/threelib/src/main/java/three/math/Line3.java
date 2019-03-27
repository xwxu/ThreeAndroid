package three.math;

public class Line3 {
    public Vector3 start;
    public Vector3 end;

    public Line3(){
        this.start = new Vector3();
        this.end = new Vector3();
    }

    public Line3(Vector3 start, Vector3 end){
        this.start = start;
        this.end = end;
    }

    public Line3 set(Vector3 start, Vector3 end){
        this.start.copy(start);
        this.end.copy(end);
        return this;
    }

    public Line3 clone(){
        Line3 clone = new Line3();
        clone.copy(this);
        return clone;
    }

    public Line3 copy(Line3 line){
        this.start.copy(line.start);
        this.end.copy(line.end);
        return this;
    }

    public Vector3 getCenter(Vector3 target){
        if(target == null){
            target = new Vector3();
        }

        return target.addVectors(this.start, this.end).multiplyScalar(0.5f);
    }

    public Vector3 delta(Vector3 target){
        return target.subVectors( this.end, this.start );
    }

    public float distanceSq(){
        return this.start.distanceToSquared( this.end );
    }

    public float distance(){
        return this.start.distanceTo( this.end );
    }

    public Vector3 at(float t, Vector3 target){
        return this.delta( target ).multiplyScalar( t ).add( this.start );
    }

    public float closestPointToPointParameter(Vector3 point, boolean clampToLine){
        Vector3 startP = new Vector3();
        Vector3 startEnd = new Vector3();

        startP.subVectors( point, this.start );
        startEnd.subVectors( this.end, this.start );

        float startEnd2 = startEnd.dot( startEnd );
        float startEnd_startP = startEnd.dot( startP );

        float t = startEnd_startP / startEnd2;

        if ( clampToLine ) {
            t = Math_.clamp( t, 0, 1 );
        }

        return t;
    }

    public Vector3 closestPointToPoint(Vector3 point, boolean clampToLine, Vector3 target){
        float t = this.closestPointToPointParameter( point, clampToLine );
        return this.delta( target ).multiplyScalar( t ).add( this.start );
    }

    public Line3 applyMatrix4(Matrix4 matrix){
        this.start.applyMatrix4( matrix );
        this.end.applyMatrix4( matrix );
        return this;
    }

    public boolean equals(Line3 line){
        return line.start.equals( this.start ) && line.end.equals( this.end );
    }

}
