package three.misc.controls;

import android.view.MotionEvent;

import three.core.Object3D;
import three.math.Quaternion;
import three.math.Vector2;
import three.math.Vector3;

public class TrackballControls {

    enum STATE{ROTATE, ZOOM, PAN, TOUCH_ROTATE, TOUCH_ZOOM_PAN, NONE};
    public Object3D object;
    public boolean enabled = true;
    public int screen_left = 0;
    public int screen_top = 0;
    public int screen_width = 0;
    public int screen_height = 0;
    public float rotateSpeed = 1.0f;
    public float zoomSpeed = 1.2f;
    public float panSpeed = 0.3f;

    public boolean noRotate = false;
    public boolean noZoom = false;
    public boolean noPan = false;

    public boolean staticMoving = false;
    public float dynamicDampingFactor = 0.2f;

    public float minDistance = 0;
    public float maxDistance = Float.POSITIVE_INFINITY;

    public Vector3 target = new Vector3();

    float EPS = 0.000001f;
    Vector3 lastPosition = new Vector3();
    STATE _state = STATE.NONE;
    STATE _prevState = STATE.NONE;

    Vector3 _eye = new Vector3();

    Vector2 _movePrev = new Vector2();
    Vector2 _moveCurr = new Vector2();

    Vector3 _lastAxis = new Vector3();
    float _lastAngle = 0;

    Vector2 _zoomStart = new Vector2();
    Vector2 _zoomEnd = new Vector2();

    float _touchZoomDistanceStart = 0;
    float _touchZoomDistanceEnd = 0;

    Vector2 _panStart = new Vector2();
    Vector2 _panEnd = new Vector2();

    // for reset
    public Vector3 target0 = this.target.Clone();
    public Vector3 position0;
    public Vector3 up0;


    public TrackballControls(Object3D object){
        this.object = object;
        position0 = this.object.position.Clone();
        up0 = this.object.up.Clone();
    }

    public void handleResize(int width, int height){
        this.screen_left = 0;
        this.screen_top = 0;
        this.screen_width = width;
        this.screen_height = height;
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

    private void touchStart(MotionEvent event){
        if (!this.enabled) {
            return;
        }

        _state = STATE.TOUCH_ROTATE;
        _moveCurr.copy( getMouseOnCircle( event.getX(), event.getY() ) );
        _movePrev.copy( _moveCurr );
    }

    private void touchMove(MotionEvent event){
        if (!this.enabled) {
            return;
        }
        _movePrev.copy( _moveCurr );
        _moveCurr.copy( getMouseOnCircle( event.getX(), event.getY() ) );
    }

    private void touchEnd(MotionEvent event){
        if (!this.enabled) {
            return;
        }

        _state = STATE.NONE;
    }

    private Vector2 getMouseOnCircle(float pageX, float pageY){
        Vector2 vector = new Vector2();
        vector.set(
                ( ( pageX - this.screen_width * 0.5f - this.screen_left ) / ( this.screen_width * 0.5f ) ),
                ( ( this.screen_height + 2 * ( this.screen_top - pageY ) ) / this.screen_width ) // screen.width intentional
        );

        return vector;
    }

    public void rotateCamera(){
        Vector3 axis = new Vector3();
        Quaternion quaternion = new Quaternion();
        Vector3 eyeDirection = new Vector3();
        Vector3 objectUpDirection = new Vector3();
        Vector3 objectSidewaysDirection = new Vector3();
        Vector3 moveDirection = new Vector3();
        float angle = 0;

        moveDirection.set( _moveCurr.x - _movePrev.x, _moveCurr.y - _movePrev.y, 0 );
        angle = moveDirection.length();

        if ( angle != 0 ) {

            _eye.copy( this.object.position ).sub( this.target );

            eyeDirection.copy( _eye ).normalize();
            objectUpDirection.copy( this.object.up ).normalize();
            objectSidewaysDirection.crossVectors( objectUpDirection, eyeDirection ).normalize();

            objectUpDirection.setLength( _moveCurr.y - _movePrev.y );
            objectSidewaysDirection.setLength( _moveCurr.x - _movePrev.x );

            moveDirection.copy( objectUpDirection.add( objectSidewaysDirection ) );

            axis.crossVectors( moveDirection, _eye ).normalize();

            angle *= this.rotateSpeed;
            quaternion.setFromAxisAngle( axis, angle );

            _eye.applyQuaternion( quaternion );
            this.object.up.applyQuaternion( quaternion );

            _lastAxis.copy( axis );
            _lastAngle = angle;

        } else if ( ! this.staticMoving && _lastAngle != 0 ) {

            _lastAngle *= Math.sqrt( 1.0 - this.dynamicDampingFactor );
            _eye.copy( this.object.position ).sub( this.target );
            quaternion.setFromAxisAngle( _lastAxis, _lastAngle );
            _eye.applyQuaternion( quaternion );
            this.object.up.applyQuaternion( quaternion );

        }

        _movePrev.copy( _moveCurr );
    }

    public void zoomCamera(){
        float factor;

        if ( _state == STATE.TOUCH_ZOOM_PAN ) {
            factor = _touchZoomDistanceStart / _touchZoomDistanceEnd;
            _touchZoomDistanceStart = _touchZoomDistanceEnd;
            _eye.multiplyScalar( factor );

        } else {
            factor = 1.0f + ( _zoomEnd.y - _zoomStart.y ) * this.zoomSpeed;

            if ( factor != 1.0 && factor > 0.0 ) {
                _eye.multiplyScalar( factor );
            }

            if ( this.staticMoving ) {
                _zoomStart.copy( _zoomEnd );
            } else {
                _zoomStart.y += ( _zoomEnd.y - _zoomStart.y ) * this.dynamicDampingFactor;
            }
        }
    }

    public void panCamera(){
        Vector2 mouseChange = new Vector2();
        Vector3 objectUp = new Vector3();
        Vector3 pan = new Vector3();
        
        mouseChange.copy( _panEnd ).sub( _panStart );

        if ( mouseChange.lengthSq() != 0 ) {

            mouseChange.multiplyScalar( _eye.length() * this.panSpeed );

            pan.copy( _eye ).cross( this.object.up ).setLength( mouseChange.x );
            pan.add( objectUp.copy( this.object.up ).setLength( mouseChange.y ) );

            this.object.position.add( pan );
            this.target.add( pan );

            if ( this.staticMoving ) {
                _panStart.copy( _panEnd );
            } else {
                _panStart.add( mouseChange.subVectors( _panEnd, _panStart ).multiplyScalar( this.dynamicDampingFactor ) );
            }

        }
    }

    public void checkDistances(){
        if ( ! this.noZoom || ! this.noPan ) {
            if ( _eye.lengthSq() > this.maxDistance * this.maxDistance ) {
                this.object.position.addVectors( this.target, _eye.setLength( this.maxDistance ) );
                _zoomStart.copy( _zoomEnd );
            }

            if ( _eye.lengthSq() < this.minDistance * this.minDistance ) {
                this.object.position.addVectors( this.target, _eye.setLength( this.minDistance ) );
                _zoomStart.copy( _zoomEnd );
            }
        }
    }

    public void update(){
        _eye.subVectors( this.object.position, this.target );

        if ( ! this.noRotate ) {
            this.rotateCamera();
        }

        if ( ! this.noZoom ) {
            this.zoomCamera();
        }

        if ( ! this.noPan ) {
            this.panCamera();
        }

        this.object.position.addVectors( this.target, _eye );

        this.checkDistances();

        this.object.lookAt( this.target );

        if ( lastPosition.distanceToSquared( this.object.position ) > EPS ) {
            lastPosition.copy( this.object.position );
        }
    }

    public void reset(){
        _state = STATE.NONE;
        _prevState = STATE.NONE;

        this.target.copy( this.target0 );
        this.object.position.copy( this.position0 );
        this.object.up.copy( this.up0 );

        _eye.subVectors( this.object.position, this.target );

        this.object.lookAt( this.target );

        lastPosition.copy( this.object.position );
    }


}
