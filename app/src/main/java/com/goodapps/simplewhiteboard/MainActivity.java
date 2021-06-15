package com.goodapps.simplewhiteboard;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.goodapps.simplewhiteboard.View.CanvasView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CanvasView canvasView;
    private AlertDialog.Builder currentAlertDialog;
    AlertDialog currentDialog;
    private SeekBar alphaSeekbar;
    private SeekBar redSeekbar;
    private SeekBar greenSeekbar;
    private SeekBar blueSeekbar;
    private View colorPrevView;

    private final SeekBar.OnSeekBarChangeListener colorSeekbarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            colorPrevView.setBackgroundColor(Color.argb(
                    alphaSeekbar.getProgress(), redSeekbar.getProgress(), greenSeekbar.getProgress(), blueSeekbar.getProgress()
            ));

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private ImageButton btnSave, btnOpenSaved, btnOpenControls, btnClearCanvas,
            btnBrushColor, btnEraserWidth, btnBrushWidth, btnUndo, btnRedo;
    private RadioButton eraser, pen;
    private RadioGroup radioGroup;
    private LinearLayout controlsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasView = findViewById(R.id.canvasView);
        initViews();

        btnClearCanvas.setOnClickListener(v -> canvasView.clear());

        btnOpenControls.setOnClickListener(v -> toggleControls());

        btnBrushColor.setOnClickListener(v -> showColorDialog());

        btnBrushWidth.setOnClickListener(v -> showBrushWidthDialog());

        btnEraserWidth.setOnClickListener(v -> showEraserWidthDialog());


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "onCheckedChanged: pen" + checkedId);
                if (checkedId == R.id.eraser) {
                    eraser.getBackground().setTint(getColor(R.color.red_200));
                    pen.getBackground().setTint(Color.GRAY);
                    canvasView.activateEraser();
                    Toast.makeText(getApplicationContext(), "Eraser selected", Toast.LENGTH_SHORT).show();
                } else {
                    canvasView.activatePen();
                    eraser.getBackground().setTint(Color.GRAY);
                    pen.getBackground().setTint(canvasView.getBrushColor());
                    Toast.makeText(getApplicationContext(), "Pen Selected", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void toggleControls() {
        if (controlsLayout.getVisibility() == View.GONE) {
            btnClearCanvas.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            btnOpenSaved.setVisibility(View.GONE);
            btnOpenControls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_baseline_close_fullscreen_24));
            controlsLayout.setVisibility(View.VISIBLE);
        } else {
            btnClearCanvas.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            btnOpenSaved.setVisibility(View.VISIBLE);
            btnOpenControls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_baseline_open_in_full_36));
            controlsLayout.setVisibility(View.GONE);

        }
    }

    private void initViews() {
        eraser = findViewById(R.id.eraser);
        pen = findViewById(R.id.pen1);
        pen.getBackground().setTint(canvasView.getBrushColor());
        btnBrushColor = findViewById(R.id.btnBrushColor);
        btnBrushWidth = findViewById(R.id.btnBrushWidth);
        btnEraserWidth = findViewById(R.id.btnEraserWidth);
        btnUndo = findViewById(R.id.btnUndo);
        btnRedo = findViewById(R.id.btnRedo);

        controlsLayout = findViewById(R.id.controlsLayout);

        btnSave = findViewById(R.id.btnSave);
        btnOpenSaved = findViewById(R.id.btnOpenSaved);

        btnOpenControls = findViewById(R.id.btnOpenControls);
        btnClearCanvas = findViewById(R.id.btnClearCanvas);

        controlsLayout.setVisibility(View.GONE);

        radioGroup = findViewById(R.id.pensRadioGroup);


    }

    private void showBrushWidthDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.brush_width_dialog, null);
        SeekBar widthSeekbar = view.findViewById(R.id.widthDialogSeekbar);
        Button buttonDone = view.findViewById(R.id.widthDialogBtnDone);
        ImageView widthImageView = view.findViewById(R.id.dialogWidthImageView);
        Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(canvasView.getBrushColor());
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(canvasView.getBrushWidth());
        paint.setAntiAlias(true);

        bitmap.eraseColor(Color.WHITE);
        canvas.drawLine(20, 50, 350, 50, paint);
        widthImageView.setImageBitmap(bitmap);
        widthSeekbar.setProgress(canvasView.getBrushWidth());

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setBrushWidth(widthSeekbar.getProgress());
                Log.d(TAG, "onClick: currentDialog dismissed");
                currentDialog.cancel();
                currentAlertDialog = null;
            }
        });

        widthSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                paint.setStrokeWidth(progress);

                bitmap.eraseColor(Color.WHITE);
                canvas.drawLine(20, 50, 350, 50, paint);
                widthImageView.setImageBitmap(bitmap);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        currentAlertDialog.setView(view);
        currentDialog = currentAlertDialog.create();
        currentDialog.setTitle("Change Brush Width");
        currentDialog.show();
    }

    private void showEraserWidthDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.brush_width_dialog, null);
        SeekBar widthSeekbar = view.findViewById(R.id.widthDialogSeekbar);
        Button buttonDone = view.findViewById(R.id.widthDialogBtnDone);
        ImageView widthImageView = view.findViewById(R.id.dialogWidthImageView);
        Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(canvasView.getBrushWidth());
        paint.setAntiAlias(true);

        bitmap.eraseColor(canvasView.getBrushColor());
        canvas.drawLine(50, 50, 350, 50, paint);
        widthImageView.setImageBitmap(bitmap);

        widthSeekbar.setMax(80);
        widthSeekbar.setProgress(canvasView.getEraserWidth());


        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setEraserWidth(widthSeekbar.getProgress());
                Log.d(TAG, "onClick: currentDialog dismissed");
                currentDialog.cancel();
                currentAlertDialog = null;
            }
        });

        widthSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                paint.setStrokeWidth(progress);

                bitmap.eraseColor(canvasView.getBrushColor());
                canvas.drawLine(50, 50, 350, 50, paint);
                widthImageView.setImageBitmap(bitmap);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        currentAlertDialog.setView(view);
        currentDialog = currentAlertDialog.create();
        currentDialog.setTitle("Change Eraser Width");
        currentDialog.show();
    }

    private void showColorDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.change_color_dialog, null);
        alphaSeekbar = view.findViewById(R.id.alphaSeekbar);
        redSeekbar = view.findViewById(R.id.redSeekbar);
        greenSeekbar = view.findViewById(R.id.greenSeekbar);
        blueSeekbar = view.findViewById(R.id.blueSeekbar);
        colorPrevView = view.findViewById(R.id.colorPrevView);

        alphaSeekbar.setOnSeekBarChangeListener(colorSeekbarChangeListener);
        redSeekbar.setOnSeekBarChangeListener(colorSeekbarChangeListener);
        greenSeekbar.setOnSeekBarChangeListener(colorSeekbarChangeListener);
        blueSeekbar.setOnSeekBarChangeListener(colorSeekbarChangeListener);

        // initial state of seek bars
        int color = canvasView.getBrushColor();
        alphaSeekbar.setProgress(Color.alpha(color));
        redSeekbar.setProgress(Color.red(color));
        greenSeekbar.setProgress(Color.green(color));
        blueSeekbar.setProgress(Color.blue(color));

        colorPrevView.setBackgroundColor(color);

        Button btnDone = view.findViewById(R.id.colorChangeDialogBtnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setBrushColor(Color.argb(alphaSeekbar.getProgress(), redSeekbar.getProgress(), greenSeekbar.getProgress(), blueSeekbar.getProgress()));
                pen.getBackground().setTint(canvasView.getBrushColor());
                currentDialog.dismiss();
            }
        });

        currentAlertDialog.setView(view);
        currentDialog = currentAlertDialog.create();
        currentDialog.setTitle("Set Brush Color");
        currentDialog.show();


    }

}