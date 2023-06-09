package com.example.logintest;

import static android.app.Activity.RESULT_OK;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.databinding.AddproductBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddProductFragment extends Fragment {
    private AddproductBinding binding;
    private final ProductManager productManager = new ProductManager();
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productDescriptionEditText;
    private Spinner productCategorySpinner;

    RecyclerView uploadImageRecyclerView;
    private ActivityResultLauncher<Intent> imageSelectionLauncher;
    private static final int MAX_IMAGE_SELECTION = 10;
    private ArrayList<Bitmap> selectedImages;
    private ArrayList<Bitmap> imagesToBeUploaded;
    private ArrayList<String> extentions;

    final private int productID = utility.generateRandomID();

    public AddProductFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddproductBinding.inflate(inflater, container, false);

        productNameEditText = binding.getRoot().findViewById(R.id.productNameEditText);
        productDescriptionEditText = binding.getRoot().findViewById(R.id.productDescriptionEditText);
        productPriceEditText = binding.getRoot().findViewById(R.id.productPriceEditText);
        productCategorySpinner = binding.getRoot().findViewById(R.id.productCategorySpinner);
        uploadImageRecyclerView = binding.getRoot().findViewById(R.id.uploadImagesRecyclerView);
        Button buttonSelectImage = binding.getRoot().findViewById(R.id.buttonSelectImage);




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategorySpinner.setAdapter(adapter);
        Button addProductButton = binding.getRoot().findViewById(R.id.addProductButton);

        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);

        extentions = new ArrayList<>();
        selectedImages = new ArrayList<>();
        imagesToBeUploaded = new ArrayList<>();

        ImageAdapterGetFromGallery imageAdapterGetFromGallery = new ImageAdapterGetFromGallery(selectedImages);
        uploadImageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        buttonSelectImage.setOnClickListener(v->{
            openImageSelection();
        });


        addProductButton.setOnClickListener(v -> {
            // Get the product details from user input (e.g., name, description, price, category)
            String productName = productNameEditText.getText().toString();
            String productDescription = productDescriptionEditText.getText().toString();
            String productPriceText = productPriceEditText.getText().toString();
            String productCategory = productCategorySpinner.getSelectedItem().toString();

            if (productName.isEmpty() || productDescription.isEmpty() || productPriceText.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return; // Exit the onClickListener without adding the product
            }

            double productPrice;
            try {
                productPrice = Double.parseDouble(productPriceText);
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Invalid price format", Toast.LENGTH_SHORT).show();
                return; // Exit the onClickListener without adding the product
            }

            //uploads images
            upload(imagesToBeUploaded);
            try {
                // Call the addProduct method of the product manager to add the product
                productManager.addProduct(productID,productName, productDescription, productPrice, productCategory);
                Toast.makeText(getActivity(), "Product added successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
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

            ContentResolver contentResolver = requireContext().getContentResolver();
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

            ContentResolver contentResolver = requireContext().getContentResolver();
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
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
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
        builder.addFormDataPart("productID", Integer.toString(productID));

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
