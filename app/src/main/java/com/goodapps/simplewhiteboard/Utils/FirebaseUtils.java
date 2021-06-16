package com.goodapps.simplewhiteboard.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.goodapps.simplewhiteboard.Models.DrawingImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class FirebaseUtils {
    public static final String USERS = "users";
    public static final String PICTURES = "pictures";
    public static final String USERID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    public static final StorageReference picturesStorageRef = FirebaseStorage.getInstance().getReference()
            .child(USERS).child(USERID).child(PICTURES);
    public static final CollectionReference userPictureCollection = FirebaseFirestore.getInstance()
            .collection("users").document(USERID).collection(PICTURES);
    private static final String TAG = "FirebaseUtils";
    private final CompleteListener completeListener;

    public FirebaseUtils(Context context) {
        this.completeListener = (CompleteListener) context;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }


    public void savePicture(Bitmap bitmap) {

        String filename = Helper.generateFilename();

        StorageReference imageRef = picturesStorageRef.child(filename);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] bytes = outputStream.toByteArray();

        imageRef.putBytes(bytes).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    final String downloadUrl = uri.toString();
                    addPictureToCollection(downloadUrl, filename);
                });
            } else {
                Log.e(TAG, "onComplete: Unable to upload drawingImage to Storage " + Objects.requireNonNull(task.getException()).getMessage());
                completeListener.onFailure("Upload Failure");
            }
        });


    }

    private void addPictureToCollection(String downloadUrl, String filename) {
        DrawingImage drawingImage = new DrawingImage(filename, downloadUrl);

        userPictureCollection.add(drawingImage).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                completeListener.onComplete("Added to Collection");
            } else {
                completeListener.onFailure("Failed to add to Collection");
            }
        });
    }

    public interface CompleteListener {
        void onComplete(String message);

        void onFailure(String message);

    }




}
