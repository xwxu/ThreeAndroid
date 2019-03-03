package three.math;

import three.materials.Material;

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

    public Line3 Set(Vector3 start, Vector3 end){
        this.start.Copy(start);
        this.end.Copy(end);
        return this;
    }

    public Line3 Clone(){
        Line3 clone = new Line3();
        clone.Copy(this);
        return clone;
    }

    public Line3 Copy(Line3 line){
        this.start.Copy(line.start);
        this.end.Copy(line.end);
        return this;
    }

    public Vector3 GetCenter(Vector3 target){
        if(target == null){
            target = new Vector3();
        }

        return target.AddVectors(this.start, this.end).MultiplyScalar(0.5f);
    }

    public Vector3 Delta(Vector3 target){
        return target.SubVectors( this.end, this.start );
    }

    public float DistanceSq(){
        return this.start.DistanceToSquared( this.end );
    }

    public float Distance(){
        return this.start.DistanceTo( this.end );
    }

    public Vector3 At(float t, Vector3 target){
        return this.Delta( target ).MultiplyScalar( t ).Add( this.start );
    }

    public float ClosestPointToPointParameter(Vector3 point, boolean clampToLine){
        Vector3 startP = new Vector3();
        Vector3 startEnd = new Vector3();

        startP.SubVectors( point, this.start );
        startEnd.SubVectors( this.end, this.start );

        float startEnd2 = startEnd.Dot( startEnd );
        float startEnd_startP = startEnd.Dot( startP );

        float t = startEnd_startP / startEnd2;

        if ( clampToLine ) {
            t = Math_.Clamp( t, 0, 1 );
        }

        return t;
    }

    public Vector3 ClosestPointToPoint(Vector3 point, boolean clampToLine, Vector3 target){
        float t = this.ClosestPointToPointParameter( point, clampToLine );
        return this.Delta( target ).MultiplyScalar( t ).Add( this.start );
    }

    public Line3 ApplyMatrix4(Matrix4 matrix){
        this.start.ApplyMatrix4( matrix );
        this.end.ApplyMatrix4( matrix );
        return this;
    }

    public boolean Equals(Line3 line){
        return line.start.equals( this.start ) && line.end.equals( this.end );
    }

}
