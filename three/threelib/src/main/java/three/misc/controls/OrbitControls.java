package three.misc.controls;

import android.view.MotionEvent;

import three.cameras.Camera;
import three.cameras.OrthographicCamera;
import three.cameras.PerspectiveCamera;
import three.core.Object3D;
import three.math.Matrix4;
import three.math.Quaternion;
import three.math.Spherical;
import three.math.Vector2;
import three.math.Vector3;

public class OrbitControls {

    public Camera object;
    public boolean enabled = true;
    public Vector3 target = new Vector3();

    public float minDistance = 0;
    public float maxDistance = Float.POSITIVE_INFINITY;

    public float minZoom = 0;
    public float maxZoom = Float.POSITIVE_INFINITY;

    public float minPolarAngle = 0;
    public float maxPolarAngle = (float) Math.PI;

    public float minAzimuthAngle = Float.NEGATIVE_INFINITY;
    public float maxAzimuthAngle = Float.POSITIVE_INFINITY;

    public boolean enableDamping = false;
    public float dampingFactor = 0.25f;

    public boolean enableZoom = true;
    public float zoomSpeed = 1.0f;

    public boolean enableRotate = true;
    public float rotateSpeed = 1.0f;

    public boolean enablePan = true;
    public float panSpeed = 0.3f;
    public boolean screenSpacePanning = false;
    public float keyPanSpeed = 7;

    public boolean autoRotate = false;
    public float autoRotateSpeed = 2.0f;

    // for reset
    public Vector3 target0 = this.target.Clone();
    public Vector3 position0;
    public float zoom0;

    enum STATE { NONE , ROTATE, DOLLY, PAN, TOUCH_ROTATE, TOUCH_DOLLY_PAN };

    STATE state = STATE.NONE;

    float EPS = 0.000001f;

    // current position in spherical coordinates
    Spherical spherical = new Spherical();
    Spherical sphericalDelta = new Spherical();

    float scale = 1;
    Vector3 panOffset = new Vector3();
    boolean zoomChanged = false;

    Vector2 rotateStart = new Vector2();
    Vector2 rotateEnd = new Vector2();
    Vector2 rotateDelta = new Vector2();

    Vector2 panStart = new Vector2();
    Vector2 panEnd = new Vector2();
    Vector2 panDelta = new Vector2();

    Vector2 dollyStart = new Vector2();
    Vector2 dollyEnd = new Vector2();
    Vector2 dollyDelta = new Vector2();

    float clientHeight;
    float clientWidth;

    public OrbitControls(Camera object){
        this.object = object;
        position0 = this.object.position.Clone();
        zoom0 = this.object.zoom;
    }

    public void handleResize(int width, int height){
        clientWidth = width;
        clientHeight = height;
    }

    public void handleTouchEvent(MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(e);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(e);
                break;
            case MotionEvent.ACTION_UP:
                touchEnd(e);
                break;
        }
    }

    public float getPolorAngle(){
        return spherical.phi;
    }

    public float getAzimuthalAngle(){
        return spherical.theta;
    }

    public void saveState(){
        this.target0.copy( this.target );
        this.position0.copy( this.object.position );
        this.zoom0 = this.object.zoom;
    }

    public void reset(){
        this.target.copy( this.target0 );
        this.object.position.copy( this.position0 );
        this.object.zoom = this.zoom0;

        this.object.updateProjectionMatrix();
        //this.dispatchEvent( changeEvent );

        this.update();

        state = STATE.NONE;
    }

    public boolean update(){
        Vector3 offset = new Vector3();
        // so camera.up is the orbit axis
        Quaternion quat = new Quaternion().setFromUnitVectors( object.up, new Vector3( 0, 1, 0 ) );
        Quaternion quatInverse = quat.clone_().inverse();

        Vector3 lastPosition = new Vector3();
        Quaternion lastQuaternion = new Quaternion();

        Vector3 position = this.object.position;
        offset.copy( position ).sub( this.target );

        // rotate offset to "y-axis-is-up" space
        offset.applyQuaternion( quat );
        // angle from z-axis around y-axis
        spherical.setFromVector3( offset );

        if ( this.autoRotate && state == STATE.NONE ) {
            rotateLeft( getAutoRotationAngle() );
        }

        spherical.theta += sphericalDelta.theta;
        spherical.phi += sphericalDelta.phi;

        // restrict theta to be between desired limits
        spherical.theta = Math.max( this.minAzimuthAngle, Math.min( this.maxAzimuthAngle, spherical.theta ) );
        // restrict phi to be between desired limits
        spherical.phi = Math.max( this.minPolarAngle, Math.min( this.maxPolarAngle, spherical.phi ) );

        spherical.makeSafe();
        spherical.radius *= scale;
        // restrict radius to be between desired limits
        spherical.radius = Math.max( this.minDistance, Math.min( this.maxDistance, spherical.radius ) );

        // move target to panned location
        this.target.add( panOffset );
        offset.setFromSpherical( spherical );

        // rotate offset back to "camera-up-vector-is-up" space
        offset.applyQuaternion( quatInverse );
        position.copy( this.target ).add( offset );

        this.object.lookAt( this.target );

        if (this.enableDamping) {
            sphericalDelta.theta *= ( 1 - this.dampingFactor );
            sphericalDelta.phi *= ( 1 - this.dampingFactor );
            panOffset.multiplyScalar( 1 - this.dampingFactor );

        } else {
            sphericalDelta.set( 0, 0, 0 );
            panOffset.set( 0, 0, 0 );
        }

        scale = 1;

        // update condition is:
        // min(camera displacement, camera rotation in radians)^2 > EPS
        // using small-angle approximation cos(x/2) = 1 - x^2 / 8
        if ( zoomChanged ||
                lastPosition.distanceToSquared( this.object.position ) > EPS ||
                8 * ( 1 - lastQuaternion.dot( this.object.quaternion ) ) > EPS ) {

            //this.dispatchEvent( changeEvent );
            lastPosition.copy( this.object.position );
            lastQuaternion.copy( this.object.quaternion );
            zoomChanged = false;
            return true;
        }

        return false;
    }

    private float getAutoRotationAngle(){
        return 2 * (float)Math.PI / 60 / 60 * this.autoRotateSpeed;
    }

    private float getZoomScale(){
        return (float) Math.pow( 0.95, this.zoomSpeed );
    }

    private void rotateLeft(float angle){
        sphericalDelta.theta -= angle;
    }

    private void rotateUp(float angle){
        sphericalDelta.phi -= angle;
    }

    private void panLeft(float distance, Matrix4 objectMatrix){
        Vector3 v = new Vector3();
        v.setFromMatrixColumn( objectMatrix, 0 ); // get X column of objectMatrix
        v.multiplyScalar( - distance );
        panOffset.add( v );
    }

    private void panUp(float distance, Matrix4 objectMatrix){
        Vector3 v = new Vector3();
        if (this.screenSpacePanning) {
            v.setFromMatrixColumn( objectMatrix, 1 );

        } else {
            v.setFromMatrixColumn( objectMatrix, 0 );
            v.crossVectors( this.object.up, v );
        }

        v.multiplyScalar( distance );
        panOffset.add( v );
    }

    private void pan(int deltaX, int deltaY){
        Vector3 offset = new Vector3();
        if ( this.object instanceof PerspectiveCamera ) {
            // perspective
            Vector3 position = this.object.position;
            offset.copy( position ).sub( this.target );
            float targetDistance = offset.length();

            // half of the fov is center to top of screen
            targetDistance *= Math.tan( ( ((PerspectiveCamera)this.object).fov / 2 ) * Math.PI / 180.0 );

            // we use only clientHeight here so aspect ratio does not distort speed
            panLeft( 2 * deltaX * targetDistance / clientHeight, this.object.matrix );
            panUp( 2 * deltaY * targetDistance / clientHeight, this.object.matrix );

        } else if ( this.object instanceof OrthographicCamera) {
            OrthographicCamera orthoCam = (OrthographicCamera)this.object;
            // orthographic
            panLeft( deltaX * ( orthoCam.right - orthoCam.left ) / this.object.zoom / clientWidth, this.object.matrix );
            panUp( deltaY * ( orthoCam.top - orthoCam.bottom ) / this.object.zoom / clientHeight, this.object.matrix );

        }
    }

    private void dollyIn(float dollyScale){
        if ( this.object instanceof PerspectiveCamera ) {
            scale /= dollyScale;

        } else if ( this.object instanceof OrthographicCamera ) {

            this.object.zoom = Math.max( this.minZoom, Math.min( this.maxZoom, this.object.zoom * dollyScale ) );
            this.object.updateProjectionMatrix();
            zoomChanged = true;

        }
    }

    private void dollyOut(float dollyScale){
        if ( this.object instanceof PerspectiveCamera ) {
            scale *= dollyScale;

        } else if ( this.object instanceof OrthographicCamera ) {

            this.object.zoom = Math.max( this.minZoom, Math.min( this.maxZoom, this.object.zoom / dollyScale ) );
            this.object.updateProjectionMatrix();
            zoomChanged = true;

        }
    }

    private void handleTouchStartRotate(MotionEvent event){
        rotateStart.set( event.getX(), event.getY() );
    }

    private void handleTouchStartDollyPan(MotionEvent event){
//        if ( this.enableZoom ) {
//            float dx = event.touches[ 0 ].pageX - event.touches[ 1 ].pageX;
//            float dy = event.touches[ 0 ].pageY - event.touches[ 1 ].pageY;
//
//            float distance = (float) Math.sqrt( dx * dx + dy * dy );
//            dollyStart.set( 0, distance );
//        }
//
//        if ( this.enablePan ) {
//            float x = 0.5 * ( event.touches[ 0 ].pageX + event.touches[ 1 ].pageX );
//            float y = 0.5 * ( event.touches[ 0 ].pageY + event.touches[ 1 ].pageY );
//
//            panStart.set( x, y );
//        }
    }

    private void handleTouchMoveDollyPan(MotionEvent event) {
    }

    private void handleTouchMoveRotate(MotionEvent event){
        rotateEnd.set( event.getX(), event.getY());
        rotateDelta.subVectors( rotateEnd, rotateStart ).multiplyScalar( this.rotateSpeed );
        rotateLeft( 2 * (float)Math.PI * rotateDelta.x / clientHeight ); // yes, height
        rotateUp( 2 * (float)Math.PI * rotateDelta.y / clientHeight );
        rotateStart.copy( rotateEnd );
        this.update();
    }

    private void handleTouchEnd(MotionEvent event) {
    }

    private void touchStart(MotionEvent event) {
        if ( this.enabled == false ) return;

        switch ( event.getPointerCount()) {

            case 1:	// one-fingered touch: rotate
                if ( this.enableRotate == false ) return;
                handleTouchStartRotate( event );
                state = STATE.TOUCH_ROTATE;
                break;

            case 2:	// two-fingered touch: dolly-pan
                if ( this.enableZoom == false && this.enablePan == false ) return;
                handleTouchStartDollyPan( event );
                state = STATE.TOUCH_DOLLY_PAN;
                break;

            default:
                state = STATE.NONE;

        }

        if ( state != STATE.NONE ) {
            //this.dispatchEvent( startEvent );
        }
    }

    private void touchMove(MotionEvent event) {
        if ( this.enabled == false ) return;

        switch ( event.getPointerCount() ) {

            case 1: // one-fingered touch: rotate
                if ( this.enableRotate == false ) return;
                if ( state != STATE.TOUCH_ROTATE ) return; // is this needed?
                handleTouchMoveRotate( event );
                break;

            case 2: // two-fingered touch: dolly-pan

                if ( this.enableZoom == false && this.enablePan == false ) return;
                if ( state != STATE.TOUCH_DOLLY_PAN ) return; // is this needed?
                handleTouchMoveDollyPan( event );
                break;

            default:
                state = STATE.NONE;
        }
    }



    private void touchEnd(MotionEvent event) {
        if ( this.enabled == false ) return;
        handleTouchEnd( event );
        state = STATE.NONE;
    }

}
