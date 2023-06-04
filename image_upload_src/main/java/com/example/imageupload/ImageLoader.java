package com.example.imageupload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader extends AsyncTask<String, Void, Bitmap>
{
    private static final String TAG = ImageLoader.class.getSimpleName();

    private final WeakReference<ImageView> imageViewReference;

    public ImageLoader(ImageView imageView) {
        imageViewReference = new WeakReference<>(imageView);
    }

    public static byte[] readBytes(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try{
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
        }
        catch(IOException e){
            Log.e(TAG, e.getMessage());
        }

        return outputStream.toByteArray();
    }

    @Override
    protected Bitmap doInBackground(@NonNull String... strings) {
        String url = strings[0];
        Bitmap bitmap = null;
        try{
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            byte[] imageBytes = readBytes(inputStream);
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);

            bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(decodedBytes));
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference.get() != null && bitmap != null) {
            ImageView imageView = imageViewReference.get();
            imageView.setImageBitmap(bitmap);
        }
    }
}
