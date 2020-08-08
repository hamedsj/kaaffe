package ir.pitok.cafe.utils.customs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class ZoomableDragableImageView extends ImageView {

    private float scaleFactor = 2f;
    private Matrix savedMatrix;
    private float savedWidth=-1;
    private float savedHeight=-1;
    private float lastPointerDownDistance=-1;
    private float lastDistanceRatio=1;
    private float mode=-1;
    private float dragMode=0;
    private float zoomMode=1;
    private float lastScale=1f;
    private float firstX=-1;
    private float firstY=-1;
    private float lastX=0;
    private float lastY=0;


    public ZoomableDragableImageView(Context context) {
        super(context);
    }

    public ZoomableDragableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomableDragableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//###############################################################################################################

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (savedWidth==-1&&savedHeight==-1){
            float w = getDrawable().getIntrinsicWidth();
            float h = getDrawable().getIntrinsicHeight();
            savedWidth = getWidth();
            savedHeight = savedWidth*(h/w);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                if (savedMatrix == null){
                    savedMatrix = getImageMatrix();
                }
                mode = dragMode;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (savedMatrix == null){
                    savedMatrix = getImageMatrix();
                }
                lastPointerDownDistance = getDistance(event);
                lastDistanceRatio = 1;
                mode = zoomMode;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode==zoomMode) {
                    float distance = getDistance(event);
                    float distanceRatio = distance / lastPointerDownDistance;
                    float scale = distanceRatio/lastDistanceRatio;
                    if (getLastScale()*scale<scaleFactor) {
                        Matrix matrix = savedMatrix;
                        matrix.postScale(scale, scale, getMidPointX(event), getMidPointY(event));
                        setImageMatrix(matrix);
                        invalidate();
                        lastScale = scale;
                        lastDistanceRatio = distanceRatio;
                    }
                }else if (mode == dragMode){
                    if (firstY==-1) firstY = event.getY();
                    if (firstX==-1) firstX = event.getX();
                    float x = event.getX();
                    float dx = x - lastX;
                    if (x == firstX) dx =0;
                    lastX = x;

                    if ((getTranslateX()+dx<0 && dx>0) ||(dx<0 && getEndX()+dx<savedWidth)){ //todo: error
                        Matrix matrix = savedMatrix;
                        matrix.postTranslate(dx,0);
                        setImageMatrix(matrix);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = -1;
                break;
            case MotionEvent.ACTION_UP:
                mode = -1;
                firstY = -1;
                firstX = -1;
                lastX= 0;
                lastY = 0;
                break;
            default:
                break;
        }
        return true;
    }

//###############################################################################################################

    private float getDistance(MotionEvent motionEvent) {
        float dx = motionEvent.getX(0)-motionEvent.getX(1);
        float dy = motionEvent.getY(0)-motionEvent.getY(1);
        return (float) Math.sqrt(dx*dx + dy*dy);
    }

    private float getMidPointX(MotionEvent motionEvent) {
        float dx = motionEvent.getX(0)+motionEvent.getX(1);
        return dx/2;
    }

    private float getMidPointY(MotionEvent motionEvent) {
        float dy = motionEvent.getY(0)+motionEvent.getY(1);
        return dy/2;
    }

    private float getLastScale(){
        float[] f = new float[9];
        getImageMatrix().getValues(f);
        return f[Matrix.MSCALE_X];
    }

    private float getTranslateX(){
        float[] f = new float[9];
        getImageMatrix().getValues(f);
        return f[Matrix.MTRANS_X];
    }

    private float getTranslateY(){
        float[] f = new float[9];
        getImageMatrix().getValues(f);
        return f[Matrix.MTRANS_X];
    }

    private float getEndX(){
        if (getLastScale()==0f)return savedWidth;
        return savedWidth*(1/getLastScale())-getTranslateX();
    }

    private float getEndY(){
        if (getLastScale()==0f)return savedHeight;
        return savedHeight*(1/getLastScale())+getTranslateY();
    }

    public interface ExitDragListener {
        void onExitDragCompleted();
        void onExitDragCanceld();
    }

}
