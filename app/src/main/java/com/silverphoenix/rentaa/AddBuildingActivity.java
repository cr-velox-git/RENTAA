package com.silverphoenix.rentaa;

import static com.silverphoenix.rentaa.DataQueries.USER_ALT_PHONE;
import static com.silverphoenix.rentaa.DataQueries.USER_EMAIL;
import static com.silverphoenix.rentaa.DataQueries.USER_NAME;
import static com.silverphoenix.rentaa.DataQueries.USER_PHONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddBuildingActivity extends AppCompatActivity {

    private ImageView bd_image;
    private EditText ow_name, ow_email, ow_phone, ow_alt_phone,
            bd_pincode, bd_des, bd_address, bd_price, bd_instalment;
    private Spinner bd_citySpinner, bd_flatTypeSpinner;
    private CheckBox bd_all_drinkSmoke, bd_lateNightEntry;

    private Button uploadBtn;

    private Uri imageUri = null;
    private String imageURL;

    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        bd_image = findViewById(R.id.image);
        ow_name = findViewById(R.id.name);
        ow_email = findViewById(R.id.email);
        ow_phone = findViewById(R.id.phone);
        ow_alt_phone = findViewById(R.id.alt_phone);
        bd_pincode = findViewById(R.id.pin_code);
        bd_des = findViewById(R.id.des);
        bd_address = findViewById(R.id.address);
        bd_price = findViewById(R.id.price);
        bd_instalment = findViewById(R.id.installment);
        bd_citySpinner = findViewById(R.id.city_dpinner);
        bd_flatTypeSpinner = findViewById(R.id.flat_spinner);
        bd_all_drinkSmoke = findViewById(R.id.drink);
        bd_lateNightEntry = findViewById(R.id.night);
        uploadBtn = findViewById(R.id.button);

        //........................... no internet connection layout start ............................//
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.solid_30);
        ImageView Image = loadingDialog.findViewById(R.id.loading_image);
        Glide.with(getApplicationContext()).load(R.drawable.gg).into(Image);
        //........................... no internet connection layout end ............................//

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.cityList));
        cityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bd_citySpinner.setAdapter(cityAdapter);

        ArrayAdapter<String> flatAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.flatList));
        flatAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bd_flatTypeSpinner.setAdapter(flatAdapter);

        bd_image.setOnClickListener(v -> {
            // " M " stands for marshmallow
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK);  // creating a intent to go in gallery of device
                galleryIntent.setType("image/*"); // we are choosing what type of pick we will use "image" is type and " * " means all item
                startActivityForResult(galleryIntent, 1);
            } else { // if we didn't get permission we have to request for it
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        });
        uploadBtn.setOnClickListener(v -> {
            checkData();
        });
    }

    private void checkData() {
        if (imageUri != null) {
            if (ow_name.getText().length() > 4) {
                if (ow_email.getText().toString().contains(".com") && ow_email.getText().toString().contains("@")) {
                    if (ow_phone.getText().length() == 10) {
                        if (ow_alt_phone.getText().length() == 10) {
                            if (!bd_citySpinner.getSelectedItem().toString().equals("select")) {
                                if (bd_address.getText().length() > 4) {
                                    if (bd_pincode.getText().length() == 6) {
                                        if (!bd_flatTypeSpinner.getSelectedItem().toString().equals("select")) {
                                            if (bd_price.getText().length() != 0) {
                                                uploadImage();
                                            } else {
                                                Toast.makeText(this, "Enter Proper Price", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "Select FLat Type", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "select the city", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Please enter proper alternate number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please enter proper number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter proper email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter proper name", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("USERS/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/BUILDING_IMAGE/" + FieldValue.serverTimestamp().toString() + ".jpg");


        Glide.with(this).asBitmap().optionalFitCenter().load(imageUri).into(new ImageViewTarget<Bitmap>(bd_image) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                //uploading image to firebase storage
                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        storageReference.getDownloadUrl().addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {

                                imageURL = task12.getResult().toString();
                                Toast.makeText(AddBuildingActivity.this, imageURL, Toast.LENGTH_SHORT).show();
/*
                                private ImageView bd_image, ow_name, ow_email, ow_phone, ow_alt_phone,
                                        bd_pincode, bd_address, bd_price, bd_instalment;
                                private Spinner bd_citySpinner, bd_flatTypeSpinner;
                                private CheckBox bd_all_drinkSmoke, bd_lateNightEntry;

                                private Button uploadBtn;

                                private Uri imageUri = null;
                                private String imageURL;
*/
                                Map<String, Object> postData = new HashMap<>();
                                String ownerInfo = ow_name.getText().toString() + "/-/" + ow_email.getText().toString() + "/-/" + ow_phone.getText().toString() + "/-/" + ow_alt_phone.getText().toString();
                                postData.put("owner_info", ownerInfo);
                                postData.put("city", bd_citySpinner.getSelectedItem().toString());
                                postData.put("address", bd_address.getText().toString());
                                postData.put("pincode", bd_pincode.getText().toString());
                                postData.put("des", bd_des.getText().toString());
                                postData.put("flat_type", bd_flatTypeSpinner.getSelectedItem().toString());
                                postData.put("price", bd_price.getText().toString());
                                postData.put("instalment", bd_instalment.getText().toString());
                                postData.put("drink", bd_all_drinkSmoke.isChecked());
                                postData.put("late_night", bd_lateNightEntry.isChecked());
                                postData.put("image_url", imageURL);
                                postData.put("upload_date", FieldValue.serverTimestamp());


                                //updating the current user data
                                FirebaseFirestore.getInstance().collection("BUILDINGS").add(postData).addOnCompleteListener(task1 -> {
                                    String postId = task1.getResult().getId();

                                    Map<String, Object> postUserData = new HashMap<>();
                                    postUserData.put("build_id", postId);
                                    postUserData.put("upload_date", FieldValue.serverTimestamp());

                                    if (task1.isSuccessful()) {
//todo here is an error in the path fix it
                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .collection("MY_BUILDING_POST").document(postId).set(postUserData)
                                                .addOnCompleteListener(task2 -> {
                                                    Intent intent = new Intent(AddBuildingActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                });


                                    } else {
                                        //  Log.d(TAG, "creating like and comment collection in post file");
                                    }
                                });


                            } else {
                                // loadingDialog.dismiss();
                                String error = task12.getException().getMessage();
                                Toast.makeText(AddBuildingActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // loadingDialog.dismiss();
                        // Log.d(TAG, " uploading image failed");
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(AddBuildingActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            @Override
            protected void setResource(@Nullable Bitmap resource) {

            }
        });


    }

    private void uploadData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();  // here we are getting the image uri
                    Glide.with(this).asBitmap().optionalFitCenter().load(imageUri).optionalCenterCrop().into(bd_image);
                    // Glide.with(getContext()).load(imageUri).into(imageView);
                } else {
                    Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);  // creating a intent to go in gallery of device
                galleryIntent.setType("image/*"); // we are choosing what type of pick we will use "image" is type and " * " means all item
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}