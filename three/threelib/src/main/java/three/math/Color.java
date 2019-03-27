package three.math;

public class Color {
    public float r;
    public float g;
    public float b;

    public Color(){
        this.r = 1;
        this.g = 1;
        this.b = 1;
    }

    public Color(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color(Color color) {
    }

    public Color(int hex) {
        this.setHex(hex);
    }

    public Color set(int value){
        return this;
    }

    public Color setScalar(float scalar){
        this.r = scalar;
        this.g = scalar;
        this.b = scalar;
        return this;
    }

    public Color setHex(int hex){

        this.r = (float) ( hex >> 16 & 255 ) / 255;
        this.g = (float) ( hex >> 8 & 255 ) / 255;
        this.b = (float) ( hex & 255 ) / 255;

        return this;
    }

    public Color setRGB(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
        return this;
    }

    public Color setHSL(int value){
        return this;
    }

    public Color clone(){
        return new Color(this.r, this.g, this.b);
    }

    public Color copy(Color color){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        return this;
    }

    public Color copyGammaToLinear(Color color, float gammaFactor){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        return this;
    }

    public Color copyLinearToGamma(Color color, float gammaFactor){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        return this;
    }

    public Color fromArray(float[] array, int offset){
        this.r = array[ offset ];
        this.g = array[ offset + 1 ];
        this.b = array[ offset + 2 ];

        return this;
    }

    public float[] toArray(float[] array, int offset){
        array[ offset ] = this.r;
        array[ offset + 1 ] = this.g;
        array[ offset + 2 ] = this.b;

        return array;
    }

    public Color multiplyScalar(float s) {
        this.r *= s;
        this.g *= s;
        this.b *= s;
        return this;
    }

}

