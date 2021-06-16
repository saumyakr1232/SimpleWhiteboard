package com.goodapps.simplewhiteboard.Utils;



import com.google.firebase.Timestamp;


public class Helper {

    public static String generateFilename() {

        return "image" + Timestamp.now().getSeconds() + ".jpeg";
    }
}
