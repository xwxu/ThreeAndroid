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
        this.SetHex(hex);
    }

    public Color Set(int value){
        return this;
    }

    public Color SetScalar(float scalar){
        this.r = scalar;
        this.g = scalar;
        this.b = scalar;
        return this;
    }

    public Color SetHex(int hex){

        this.r = (float) ( hex >> 16 & 255 ) / 255;
        this.g = (float) ( hex >> 8 & 255 ) / 255;
        this.b = (float) ( hex & 255 ) / 255;

        return this;
    }

    public Color SetRGB(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
        return this;
    }

    public Color SetHSL(int value){
        return this;
    }

    public Color Clone(){
        return new Color(this.r, this.g, this.b);
    }

    public Color Copy(Color color){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        return this;
    }

    public Color CopyGammaToLinear(Color color, float gammaFactor){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        return this;
    }

    public Color CopyLinearToGamma(Color color, float gammaFactor){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        return this;
    }

    public Color FromArray(float[] array, int offset){
        this.r = array[ offset ];
        this.g = array[ offset + 1 ];
        this.b = array[ offset + 2 ];

        return this;
    }

    public float[] ToArray(float[] array, int offset){
        array[ offset ] = this.r;
        array[ offset + 1 ] = this.g;
        array[ offset + 2 ] = this.b;

        return array;
    }

    public Color MultiplyScalar(float s) {
        this.r *= s;
        this.g *= s;
        this.b *= s;
        return this;
    }

}

