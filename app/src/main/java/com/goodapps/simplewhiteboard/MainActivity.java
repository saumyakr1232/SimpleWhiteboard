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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasView = findViewById(R.id.canvasView);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.clearid) {
            canvasView.clear();
        } else if (item.getItemId() == R.id.brushId) {
            Log.d(TAG, "onOptionsItemSelected: Brush clicked");
            showLineWidthDialog();
        } else if (item.getItemId() == R.id.colorId) {
            Log.d(TAG, "onOptionsItemSelected: color clicked");
        } else if (item.getItemId() == R.id.saveId) {
            Log.d(TAG, "onOptionsItemSelected: save Clicked");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
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


        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_LONG).show();
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
        currentAlertDialog.create();
        currentAlertDialog.show();
    }

}