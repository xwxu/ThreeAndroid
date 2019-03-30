package three.math;

public class Spherical {
    public float radius;
    public float phi;
    public float theta;

    public Spherical(){
        radius = 1.0f;
        phi = 0;
        theta = 0;
    }

    public Spherical(float radius, float phi, float theta){
        this.radius = radius;
        this.phi = phi;
        this.theta = theta;
    }

    public Spherical set(int radius, int phi, int theta) {
        this.radius = radius;
        this.phi = phi;
        this.theta = theta;
        return this;
    }

    public Spherical clone_(){
        return new Spherical().copy(this);
    }

    public Spherical copy(Spherical other) {
        this.radius = other.radius;
        this.phi = other.phi;
        this.theta = other.theta;
        return this;
    }

    public Spherical makeSafe() {
        float EPS = 0.000001f;
        this.phi = (float) Math.max( EPS, Math.min( Math.PI - EPS, this.phi ) );
        return this;
    }

    public Spherical setFromVector3(Vector3 v) {
        return this.setFromCartesianCoords( v.x, v.y, v.z );
    }

    public Spherical setFromCartesianCoords(float x, float y, float z) {
        this.radius = (float) Math.sqrt( x * x + y * y + z * z );

        if ( this.radius == 0 ) {
            this.theta = 0;
            this.phi = 0;

        } else {
            this.theta = (float) Math.atan2( x, z );
            this.phi = (float) Math.acos( Math_.clamp( y / this.radius, - 1, 1 ) );
        }

        return this;
    }
}
