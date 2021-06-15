package com.goodapps.simplewhiteboard;

import androidx.annotation.NonNull;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasView = findViewById(R.id.canvasView);


    }

    private SeekBar.OnSeekBarChangeListener colorSeekbarChangeListener = new SeekBar.OnSeekBarChangeListener() {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.clearid) {
            canvasView.clear();
        } else if (item.getItemId() == R.id.brushId) {
            showLineWidthDialog();
        } else if (item.getItemId() == R.id.colorId) {
            showColorDialog();
        } else if (item.getItemId() == R.id.saveId) {
            Log.d(TAG, "onOptionsItemSelected: save Clicked");
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLineWidthDialog() {
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
                currentDialog.dismiss();
            }
        });

        currentAlertDialog.setView(view);
        currentDialog = currentAlertDialog.create();
        currentDialog.setTitle("Set Brush Color");
        currentDialog.show();


    }

}