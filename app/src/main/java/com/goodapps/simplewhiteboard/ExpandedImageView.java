package com.goodapps.simplewhiteboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ExpandedImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ImageView expandedImageView = findViewById(R.id.expandedImageView);
        ImageButton btnClose = findViewById(R.id.btnCloseImageView);

        //default image url (loading image)
        String url;

        btnClose.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        url = intent.getStringExtra("imageUrl");

        Glide.with(this)
                .load(url)
                .centerCrop()
                .into(expandedImageView);


    }
}