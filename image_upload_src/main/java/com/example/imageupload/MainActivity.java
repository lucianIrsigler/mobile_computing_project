package com.example.imageupload;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    Button btnSelectImage;
    Button btnUploadImage;
    Button btnShowImage;
    RecyclerView uploadImageRecyclerView;
    RecyclerView showImageRecyclerView;
    private ActivityResultLauncher<Intent> imageSelectionLauncher;
    private static final int MAX_IMAGE_SELECTION = 10;
    private List<Bitmap> selectedImages;
    private List<Bitmap> imagesToBeUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);

        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnShowImage = findViewById(R.id.btnShowImage);
        uploadImageRecyclerView = findViewById(R.id.uploadImagesRecyclerView);
        showImageRecyclerView = findViewById(R.id.showImagesRecyclerView);

        btnSelectImage.setOnClickListener(view -> openImageSelection());

        selectedImages = new ArrayList<>();
        imagesToBeUploaded = new ArrayList<>();
        ImageAdapterGetFromGallery imageAdapterGetFromGallery = new ImageAdapterGetFromGallery(selectedImages);
        uploadImageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        uploadImageRecyclerView.setAdapter(imageAdapterGetFromGallery);

        imageSelectionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    if (data.getClipData() != null) {
                        // Multiple images selected
                        handleMultipleImageSelection(data.getClipData());
                    } else if (data.getData() != null) {
                        // Single image selected
                        handleSingleImageSelection(data.getData());
                    }
                    imageAdapterGetFromGallery.setImageList(selectedImages, defaultImage);
                }
            }
        });

        btnUploadImage.setOnClickListener(view -> {
            if(!imagesToBeUploaded.isEmpty())
            {
                try{
                    JSONObject params = new JSONObject();
                    HTTPHandler httpHandler = new HTTPHandler();

                    for(int i = 0; i < imagesToBeUploaded.size(); i++) {
                        params.put("image" + i, imagesToBeUploaded.get(i));
                    }

                    String response = httpHandler.postRequest("https://lamp.ms.wits.ac.za/home/s2571291/images/imageUpload.php", params, String.class);

                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "No images selected", Toast.LENGTH_SHORT).show();
            }
        });

        btnShowImage.setOnClickListener(view -> {
            try {
                HTTPHandler httpHandler = new HTTPHandler();
                JSONObject params = new JSONObject();

                params.put("productID", 3);
                String prefix = "https://lamp.ms.wits.ac.za/home/s2571291/images/";

                List<String> imageUrls = new ArrayList<>();

                JSONArray response = httpHandler.postRequest("https://lamp.ms.wits.ac.za/home/s2571291/images/imageReader.php", params, JSONArray.class);

                if(response.toString().equals("[]")) {
                    Toast.makeText(getApplicationContext(), "No Image found", Toast.LENGTH_SHORT).show();
                }
                else {
                    for(int i = 0; i < response.length(); i++) {
                        String imagePath = prefix + response.getJSONObject(i).getString("imagePath");
                        imageUrls.add(imagePath);
                    }

                    ImageAdapterGetFromDatabase imageAdapterGetFromDatabase = new ImageAdapterGetFromDatabase(imageUrls);
                    showImageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    showImageRecyclerView.setAdapter(imageAdapterGetFromDatabase);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void openImageSelection() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageSelectionLauncher.launch(Intent.createChooser(intent, "Select Images"));
    }

    private void handleMultipleImageSelection(@NonNull ClipData clipData) {
        int itemCount = clipData.getItemCount();
        int imageCount = Math.min(itemCount, MAX_IMAGE_SELECTION);

        imagesToBeUploaded.clear();
        selectedImages.clear();

        for (int i = 0; i < imageCount; i++) {
            Uri imageUri = clipData.getItemAt(i).getUri();
            Bitmap bitmap = getBitmapFromUri(imageUri);
            if (bitmap != null) {
                selectedImages.add(bitmap);

                // Compress the bitmap before adding to the list for upload
                Bitmap compressedBitmap = compressBitmap(bitmap);
                imagesToBeUploaded.add(compressedBitmap);
            }
        }
    }

    private void handleSingleImageSelection(Uri imageUri) {
        Bitmap bitmap = getBitmapFromUri(imageUri);
        if (bitmap != null) {
            selectedImages.clear();
            imagesToBeUploaded.clear();
            selectedImages.add(bitmap);

            // Compress the bitmap before adding to the list for upload
            Bitmap compressedBitmap = compressBitmap(bitmap);
            imagesToBeUploaded.add(compressedBitmap);
        }
    }

    private Bitmap compressBitmap(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteArray = stream.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @Nullable
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}