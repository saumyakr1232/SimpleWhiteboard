package com.goodapps.simplewhiteboard.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Helper {
    private Context context;

    public Helper(Context context) {
        this.context = context;
    }

    public static String generateFilename() {

        return "image" + String.valueOf(Timestamp.now().getSeconds()) + ".jpeg";
    }
}
