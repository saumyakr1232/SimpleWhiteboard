package com.goodapps.simplewhiteboard;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goodapps.simplewhiteboard.Adapters.GalleryRecViewAdapter;
import com.goodapps.simplewhiteboard.Models.DrawingImage;
import com.goodapps.simplewhiteboard.Utils.FirebaseUtils;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = "GalleryActivity";
    private final ArrayList<DrawingImage> drawingImages = new ArrayList<>();
    private RecyclerView galleryRecView;
    private ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initViews();


        btnClose.setOnClickListener(v -> onBackPressed());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        GalleryRecViewAdapter adapter = new GalleryRecViewAdapter(this, displayMetrics);
        galleryRecView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        galleryRecView.addItemDecoration(new GalleryItemDecoration(spacingInPixels));


        galleryRecView.setAdapter(adapter);

        FirebaseUtils.userPictureCollection.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                drawingImages.clear();
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);

                    return;
                }

                for (QueryDocumentSnapshot image : value) {
                    drawingImages.add(new DrawingImage(image.getString("name"), image.getString("url")));
                }


                Log.d(TAG, "onEvent: " + value.toString());
                adapter.setImages(drawingImages);

            }
        });


    }

    private void initViews() {
        galleryRecView = (RecyclerView) findViewById(R.id.galleryRecView);
        btnClose = findViewById(R.id.btnCloseGallery);


    }
}