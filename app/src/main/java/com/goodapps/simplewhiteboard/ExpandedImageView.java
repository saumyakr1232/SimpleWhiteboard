package com.goodapps.simplewhiteboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ExpandedImageView extends AppCompatActivity {

    private ImageView expandedImageView;
    private ImageButton btnClose;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        expandedImageView = findViewById(R.id.expandedImageView);
        btnClose = findViewById(R.id.btnCloseImageView);

        //default image url (loading image)
        url = "https://wallpaperaccess.com/full/271965.jpg";

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        Intent intent = getIntent();

        url = intent.getStringExtra("imageUrl");

        Glide.with(this)
                .load(url)
                .centerCrop()
                .into(expandedImageView);


    }
}