package com.example.imageupload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    Button btnUploadImage;
    Button btnShowImage;
    ImageView imageView;
    ImageView showImage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnShowImage = findViewById(R.id.btnShowImage);
        showImage = findViewById(R.id.showImage);
        imageView = findViewById(R.id.UploadImage);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data != null ? data.getData() : null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }
        );

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        btnUploadImage.setOnClickListener(view -> {
            HTTPHandler httpHandler = new HTTPHandler();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if(bitmap != null){
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                final String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                JSONObject params = new JSONObject();
                try {
                    params.put("Image", encodedImage);
                } catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }

                String response = httpHandler.postRequest("https://lamp.ms.wits.ac.za/home/s2571291/images/imageUploader.php", params, String.class);

                if(response.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            }
            else Toast.makeText(getApplicationContext(), "Select an Image first", Toast.LENGTH_SHORT).show();
        });

        btnShowImage.setOnClickListener(view -> {
            HTTPHandler httpHandler = new HTTPHandler();
            JSONObject params = new JSONObject();
            try {
                params.put("productID", 1);
                String prefix = "https://lamp.ms.wits.ac.za/home/s2571291/images/";

                JSONArray response = httpHandler.getRequest("https://lamp.ms.wits.ac.za/home/s2571291/images/imageReader.php", params, JSONArray.class);

                if(response.toString().equals("[]")) {
                    Toast.makeText(getApplicationContext(), "No Image found", Toast.LENGTH_SHORT).show();
                } else {
                    for(int i = 0; i < response.length(); i++)
                    {
                        String imagePath = prefix + response.getJSONObject(i).getString("imagePath");
                        bitmap = BitmapFactory.decodeStream(new java.net.URL(imagePath).openStream());
                        showImage.setImageBitmap(bitmap);

                        /*
                        ImageLoader imageLoader = new ImageLoader(showImage);
                        imageLoader.execute(imagePath);
                        */
                    }

                }
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}