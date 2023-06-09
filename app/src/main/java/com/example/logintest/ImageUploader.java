package com.example.logintest;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ImageUploader {
    private static final String UPLOAD_URL = "https://lamp.ms.wits.ac.za/home/s2571291/images/imageUploader.php";

    public static void uploadImages(List<Bitmap> imagesToUpload, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (int i = 0; i < imagesToUpload.size(); i++) {
            Bitmap bitmap = imagesToUpload.get(i);
            String encodedImage = encodeImage(bitmap);

            builder.addFormDataPart("image" + (i + 1), "image.png",
                    RequestBody.create(MediaType.parse("image/png"), encodedImage));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(UPLOAD_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    private static String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}

