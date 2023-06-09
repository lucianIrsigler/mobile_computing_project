package com.example.logintest;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;
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
import java.util.UUID;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ImageUpload extends AppCompatActivity {
    Button btnSelectImage;
    Button btnUploadImage;
    Button btnShowImage;
    RecyclerView uploadImageRecyclerView;
    RecyclerView showImageRecyclerView;
    private ActivityResultLauncher<Intent> imageSelectionLauncher;
    private static final int MAX_IMAGE_SELECTION = 10;
    private ArrayList<Bitmap> selectedImages;
    private ArrayList<Bitmap> imagesToBeUploaded;
    private ArrayList<String> extentions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);

        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);

        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnShowImage = findViewById(R.id.btnShowImage);
        uploadImageRecyclerView = findViewById(R.id.uploadImagesRecyclerView);
        showImageRecyclerView = findViewById(R.id.showImagesRecyclerView);

        btnSelectImage.setOnClickListener(view -> openImageSelection());

        extentions = new ArrayList<>();
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
                        handleMultipleImageSelection(data.getClipData());
                    } else if (data.getData() != null) {
                        handleSingleImageSelection(data.getData());
                    }
                    imageAdapterGetFromGallery.setImageList(selectedImages, defaultImage);
                }
            }
        });

        btnUploadImage.setOnClickListener(view -> {
            upload(imagesToBeUploaded);
        });

        btnShowImage.setOnClickListener(view -> {
            try {
                HTTPHandler httpHandler = new HTTPHandler();
                JSONObject params = new JSONObject();

                params.put("productID", 3);
                String prefix = "https://lamp.ms.wits.ac.za/home/s2571291/images/";

                List<String> imageUrls = new ArrayList<>();

                JSONArray response = httpHandler.postRequest("https://lamp.ms.wits.ac.za/home/s2571291/images/imageReader.php", params, JSONArray.class);

                if (response.toString().equals("[]")) {
                    Toast.makeText(getApplicationContext(), "No Image found", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < response.length(); i++) {
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
        extentions.clear();

        for (int i = 0; i < imageCount; i++) {
            Uri imageUri = clipData.getItemAt(i).getUri();

            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String fileExtension = mimeTypeMap.getExtensionFromMimeType(
                    contentResolver.getType(imageUri));

            extentions.add(fileExtension);

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
            extentions.clear();

            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String fileExtension = mimeTypeMap.getExtensionFromMimeType(
                    contentResolver.getType(imageUri));

            extentions.add(fileExtension);
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

    public void upload(ArrayList<Bitmap> images_path) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (int i = 0; i < images_path.size(); i++) {
            Bitmap file = images_path.get(i);
            String fileExtention = extentions.get(i);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            String mediaTypeString = "image/jpeg";

            if ("png".equals(fileExtention)) {
                file.compress(Bitmap.CompressFormat.PNG, 100, baos);
                mediaTypeString = "image/png";
            } else {
                file.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            }

            byte[] bitmapData = baos.toByteArray();
            String base64Image = Base64.encodeToString(bitmapData, Base64.DEFAULT);

            String filename = UUID.randomUUID().toString();

            MediaType mediaType = MediaType.parse(mediaTypeString);
            builder.addFormDataPart("image[]", filename + "." + fileExtention,
                    RequestBody.create(mediaType, base64Image));
        }

        //todo put the proper product id
        builder.addFormDataPart("productID", "5");

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2621933/php/insertimages.php")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient.Builder().build();

        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("error","onFailure called");
            }
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
            }
        });
    }
}