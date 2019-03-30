package three.cameras;

import three.util.ViewInfo;

public class OrthographicCamera extends Camera{
    public ViewInfo view;
    public float left;
    public float right;
    public float top;
    public float bottom;
    public float near;
    public float far;

    public OrthographicCamera(){
        super();
        this.type = "OrthographicCamera";
        this.view = null;
        this.left = -1;
        this.right = 1;
        this.top = 1;
        this.bottom = -1;
        this.near = 0.1f;
        this.far = 2000;
        this.updateProjectionMatrix();
    }

    public OrthographicCamera(float left, float right, float top, float bottom, float near, float far){
        super();
        this.type = "OrthographicCamera";
        this.zoom = 1;
        this.view = null;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.near = near;
        this.far = far;
        this.updateProjectionMatrix();
    }

    @Override
    public void updateProjectionMatrix(){
        float dx = ( this.right - this.left ) / ( 2 * this.zoom );
        float dy = ( this.top - this.bottom ) / ( 2 * this.zoom );
        float cx = ( this.right + this.left ) / 2;
        float cy = ( this.top + this.bottom ) / 2;

        float left = cx - dx;
        float right = cx + dx;
        float top = cy + dy;
        float bottom = cy - dy;

        if ( this.view != null && this.view.enabled ) {
            float zoomW = this.zoom / ( this.view.width / this.view.fullWidth );
            float zoomH = this.zoom / ( this.view.height / this.view.fullHeight );
            float scaleW = ( this.right - this.left ) / this.view.width;
            float scaleH = ( this.top - this.bottom ) / this.view.height;

            left += scaleW * ( this.view.offsetX / zoomW );
            right = left + scaleW * ( this.view.width / zoomW );
            top -= scaleH * ( this.view.offsetY / zoomH );
            bottom = top - scaleH * ( this.view.height / zoomH );
        }

        this.projectionMatrix.makeOrthographic( left, right, top, bottom, this.near, this.far );
        this.projectionMatrixInverse.getInverse( this.projectionMatrix );
    }

    public OrthographicCamera copy(OrthographicCamera source){
        this.left = source.left;
        this.right = source.right;
        this.top = source.top;
        this.bottom = source.bottom;
        this.near = source.near;
        this.far = source.far;

        this.zoom = source.zoom;
        this.view = source.view;
        return this;
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

}
