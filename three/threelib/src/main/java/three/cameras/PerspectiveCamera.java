package three.cameras;

import three.math.Math_;
import three.util.ViewInfo;

public class PerspectiveCamera extends Camera{

    public String type = "PerspectiveCamera";
    public float fov;
    public float near;
    public float far;
    public float focus = 10;
    public float aspect;
    public ViewInfo view = null;
    public int filmGauge = 35;
    public int filmOffset = 0;

    public PerspectiveCamera(){
        super();
        this.type = "PerspectiveCamera";
        fov = 50;
        near = 0.1f;
        far = 2000;
        aspect = 1;
    }

    public PerspectiveCamera(float fov, float aspect, float near, float far){
        super();
        this.type = "PerspectiveCamera";
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.updateProjectionMatrix();
    }

    public void setFocalLength(float focalLength){
        float vExtentSlope = 0.5f * this.getFilmHeight() / focalLength;

        this.fov = Math_.RAD2DEG * 2 * (float) Math.atan( vExtentSlope );
        this.updateProjectionMatrix();
    }

    public float getFocalLength(){
        float vExtentSlope = (float) Math.tan( Math_.DEG2RAD * 0.5 * this.fov );

        return 0.5f * this.getFilmHeight() / vExtentSlope;
    }

    public float getEffectiveFOV(){
        return Math_.RAD2DEG * 2 * (float) Math.atan(
                Math.tan( Math_.DEG2RAD * 0.5 * this.fov ) / this.zoom );
    }


    public float getFilmWidth(){
        return this.filmGauge * Math.min( this.aspect, 1 );
    }

    public float getFilmHeight(){
        return this.filmGauge / Math.max( this.aspect, 1 );
    }

    public void setViewOffset(int fullWidth, int fullHeight,
                              int x, int y, int width, int height){
        if ( this.view == null ) {
            this.view = new ViewInfo();
        }

        this.view.enabled = true;
        this.view.fullWidth = fullWidth;
        this.view.fullHeight = fullHeight;
        this.view.offsetX = x;
        this.view.offsetY = y;
        this.view.width = width;
        this.view.height = height;

        this.updateProjectionMatrix();
    }

    public void clearViewOffset(){
        if ( this.view != null ) {
            this.view.enabled = false;
        }

        this.updateProjectionMatrix();
    }


    @Override
    public void updateProjectionMatrix() {
        float near = this.near;
        float top = near * (float) Math.tan( Math_.DEG2RAD * 0.5f * this.fov ) / this.zoom;
        float height = 2 * top;
        float width = this.aspect * height;
        float left = - 0.5f * width;
         ViewInfo view = this.view;

        if ( this.view != null && this.view.enabled ) {
            int fullWidth = view.fullWidth;
            int fullHeight = view.fullHeight;

            left += view.offsetX * width / fullWidth;
            top -= view.offsetY * height / fullHeight;
            width *= view.width / fullWidth;
            height *= view.height / fullHeight;
        }

        int skew = this.filmOffset;
        if ( skew != 0 ) left += near * skew / this.getFilmWidth();

        this.projectionMatrix.makePerspective( left, left + width, top, top - height, near, this.far );
        this.projectionMatrixInverse.getInverse( this.projectionMatrix );

    }
}
