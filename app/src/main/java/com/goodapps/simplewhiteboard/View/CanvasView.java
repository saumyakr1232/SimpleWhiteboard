package com.goodapps.simplewhiteboard.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class CanvasView extends View {
    public static final float TOUCH_TOLERANCE = 10;
    private static final String TAG = "CanvasView";
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint paintScreen;
    private Paint paintBrush;
    private HashMap<Integer, Path> pathMap;
    private HashMap<Integer, Point> prevPointMap;


    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        paintScreen = new Paint();
        paintBrush = new Paint();
        paintBrush.setAntiAlias(true);
        paintBrush.setColor(Color.BLUE);
        paintBrush.setStrokeWidth(10);
        // End of line is round (as in microsoft whiteboard)
        paintBrush.setStrokeCap(Paint.Cap.ROUND);
        paintBrush.setStyle(Paint.Style.STROKE); // default is FILL_AND_STROKE which is bad

        pathMap = new HashMap<>();
        prevPointMap = new HashMap<>();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);

        for (Integer key : pathMap.keySet()) {
            canvas.drawPath(pathMap.get(key), paintBrush);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "onTouchEvent: Canvas Touched " + event.getActionMasked() );

        int action = event.getActionMasked(); // event type
        int actionIndex = event.getActionIndex(); // finger pointer (Index)

        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_DOWN) { // touch Started

            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) { // touch ended
//            Log.d(TAG, "onTouchEvent: " + event.getPointerId(actionIndex));
            touchEnded(event.getPointerId(actionIndex));


        } else {
            touchMoved(event);
        }

        invalidate(); // redraw the screen
        return true;
    }

    private void touchMoved(MotionEvent event) {

        for (int i = 0; i < event.getPointerCount(); i++) {
            int pointerId = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerId);

            if (pathMap.containsKey(pointerId) && prevPointMap.containsKey(pointerId)) {
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);

                Path path = pathMap.get(pointerId);
                Point point = prevPointMap.get(pointerId);

                // calculate how far the user moved from the last update
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                // if distance is big enough to consider as movement
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    // path to the new location
                    path.quadTo(point.x, point.y,
                            (newX + point.x) / 2, (newY + point.y) / 2);

                    // store the new coordinates
                    point.x = (int) newX;
                    point.y = (int) newY;


                }

            }
        }

    }

    private void touchEnded(int pointerId) {
        Path path = pathMap.get(pointerId); // get the related path
        bitmapCanvas.drawPath(path, paintBrush); // draw to bimapCanvas
        path.reset();
    }

    public void clear() {
        pathMap.clear();
        prevPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate(); // refresh the screen
    }

    public int getBrushWidth() {
        return (int) paintBrush.getStrokeWidth();
    }

    public void setBrushWidth(int width) {
        paintBrush.setStrokeWidth(width);
    }

    public int getBrushColor() {
        return paintBrush.getColor();
    }

    public void setBrushColor(int color) {
        paintBrush.setColor(color);
    }

    private void touchStarted(float x, float y, int pointerId) {
        Path path; // store the path of touches
        Point point; // store the last point in path

        if (pathMap.containsKey(pointerId) && prevPointMap.containsKey(pointerId)) {
            path = pathMap.get(pointerId);
            point = prevPointMap.get(pointerId);
        } else {
            path = new Path();
            pathMap.put(pointerId, path);
            point = new Point();
            prevPointMap.put(pointerId, point);

        }


        // move to the coordinates of the touch
        path.moveTo(x, y);
        point.x = (int) x;
        point.y = (int) y;
    }
}
