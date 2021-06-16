package com.goodapps.simplewhiteboard.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;


import com.goodapps.simplewhiteboard.Picture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private final PictureSaveCompleteListener saveCompleteListener;

    public FirebaseUtils(Context context) {
        this.saveCompleteListener = (PictureSaveCompleteListener) context;
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
                Log.e(TAG, "onComplete: Unable to upload picture to Storage " + Objects.requireNonNull(task.getException()).getMessage());
                saveCompleteListener.onFailure("Upload Failure");
            }
        });


    }

    private void addPictureToCollection(String downloadUrl, String filename) {
        Picture picture = new Picture(filename, downloadUrl);

        userPictureCollection.add(picture).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveCompleteListener.onComplete("Added to Collection");
            } else {
                saveCompleteListener.onFailure("Failed to add to Collection");
            }
        });
    }

    public interface PictureSaveCompleteListener {
        void onComplete(String message);

        void onFailure(String message);
    }


}
