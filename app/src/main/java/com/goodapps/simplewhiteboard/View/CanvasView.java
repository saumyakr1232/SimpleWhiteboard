package com.goodapps.simplewhiteboard.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.goodapps.simplewhiteboard.FingerPath;
import com.goodapps.simplewhiteboard.Utils.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CanvasView extends View {
    public static final float TOUCH_TOLERANCE = 10;
    private static final String TAG = "CanvasView";
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    public static final int DEFAULT_BRUSH_WIDTH = 15;
    public static final int DEFAULT_ERASER_WIDTH = 15;
    private int currentColor;
    private int currentStrokeWidth;
    private int backgroundColor;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint activeBrush;
    private Paint paintBrush;
    private Paint eraser;
    private float mX;
    private float mY;
    private Path mPath;
    private HashMap<Integer, Path> pathMap;
    private HashMap<Integer, Point> prevPointMap;
    private Stack<Integer> saved;
    private ArrayList<FingerPath> fingerPaths;


    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        currentColor = DEFAULT_COLOR;
        currentStrokeWidth = DEFAULT_BRUSH_WIDTH;
        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        paintBrush = new Paint();
        paintBrush.setAntiAlias(true);
        paintBrush.setColor(DEFAULT_COLOR);
        paintBrush.setStrokeWidth(DEFAULT_BRUSH_WIDTH);
        // End of line is round (as in microsoft whiteboard)
        paintBrush.setStrokeCap(Paint.Cap.ROUND);
        paintBrush.setStyle(Paint.Style.STROKE); // default is FILL_AND_STROKE which is bad

        eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setColor(backgroundColor);
        eraser.setStrokeWidth(DEFAULT_ERASER_WIDTH);
        eraser.setStrokeCap(Paint.Cap.ROUND);
        eraser.setStyle(Paint.Style.STROKE);

        pathMap = new HashMap<>();
        prevPointMap = new HashMap<>();

        activeBrush = new Paint();
        activeBrush.setColor(currentColor);
        activeBrush.setStrokeWidth(currentStrokeWidth);
        activeBrush.setStrokeCap(Paint.Cap.ROUND);
        activeBrush.setStyle(Paint.Style.STROKE);

        saved = new Stack<>();

        fingerPaths = new ArrayList<>();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: Called");
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        bitmapCanvas.drawColor(backgroundColor);

        for (FingerPath fp : fingerPaths) {
            activeBrush.setColor(fp.color);
            activeBrush.setStrokeWidth(fp.strokeWidth);

            bitmapCanvas.drawPath(fp.path, activeBrush);
        }
        canvas.drawBitmap(bitmap, 0, 0, activeBrush);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked(); // event type
        int actionIndex = event.getActionIndex(); // finger pointer (Index)

        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_DOWN) { // touch Started

            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) { // touch ended
            touchEnded(event.getPointerId(actionIndex));


        } else {
            touchMoved(event);
        }

        invalidate(); // redraw the screen
        return true;
    }

    private void touchStarted(float x, float y, int pointerId) {
        Log.d(TAG, "touchStarted: called " + pointerId);
        mPath = new Path(); // store the path of touches
        // store the last point in path

        FingerPath fp = new FingerPath(currentColor, currentStrokeWidth, mPath);

        fingerPaths.add(fp);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;


    }


    private void touchMoved(MotionEvent event) {
        float dx = Math.abs(event.getX() - mX);
        float dy = Math.abs(event.getY() - mY);

        if (dx >= TOUCH_TOLERANCE || dy > TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY,
                    (event.getX() + mX) / 2, (event.getY() + mY) / 2);

            mX = event.getX();
            mY = event.getY();
        }


    }

    private void touchEnded(int pointerId) {
        mPath.lineTo(mX, mY);
    }

    public void clear() {
        pathMap.clear();
        fingerPaths.clear();
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

    public int getEraserWidth() {
        Log.d(TAG, "getEraserWidth: HERE " + eraser.getStrokeWidth());
        return (int) eraser.getStrokeWidth();
    }

    public void setEraserWidth(int width) {
        eraser.setStrokeWidth(width);
    }

    public void activateEraser() {
        currentColor = backgroundColor;
        currentStrokeWidth = (int) eraser.getStrokeWidth();


    }

    public void activatePen() {
        currentColor = paintBrush.getColor();
        currentStrokeWidth = (int) paintBrush.getStrokeWidth();

    }


    public String saveImage() {
        Helper helper = new Helper(getContext());
        return helper.saveToInternalStorage(bitmap);
    }

    public void undo() {
        Log.d(TAG, "undo: Stack " + saved.toString());

        if (!saved.isEmpty()) {
            Integer stroke = saved.pop();

        } else {
            Toast.makeText(getContext(), "Nothing to undo", Toast.LENGTH_LONG).show();
        }

    }
}
