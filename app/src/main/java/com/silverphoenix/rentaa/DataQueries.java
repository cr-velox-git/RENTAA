package com.silverphoenix.rentaa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataQueries {

    public static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = auth.getCurrentUser();

    public static String USER_NAME, USER_EMAIL, USER_PHONE, USER_ALT_PHONE;

    public static List<propertyViewModel> simpleList = new ArrayList<>();


    /*this method is to get basic database_user information*/
    public static void database_userSetData(final Context context, final String NAME, final String EMAIL,
                                            final String PHONE, Dialog loadingDialog
    ) {


        loadingDialog.show();
        //// creating a map to input data ////
        Map<String, Object> userdata = new HashMap<>();
        String data = NAME + "/-/" + EMAIL + "/-/" + PHONE + "/-/" + null;
        userdata.put("user_data", data);
        userdata.put("date_joined", FieldValue.serverTimestamp());
        userdata.put("last_seen", FieldValue.serverTimestamp());


        ////// writing the data of user to the firestore firebase//// and creating all additional file like wish-list, cart-item, profile, etc ////////////
        firestore.collection("USERS").document(Objects.requireNonNull(auth.getUid()))
                .set(userdata)  // user data added or adding
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        CollectionReference userDataReference = firestore.collection("USERS").
                                document(auth.getUid()).collection("USER_DATA");

                        //////////// Maps

                        //// creating the my wish-list folder inside the user data ////////////////
                        Map<String, Object> buildMap = new HashMap<>();
                        buildMap.put("list_size", (long) 0);

                        Map<String, Object> savedMap = new HashMap<>();
                        savedMap.put("list_size", (long) 0);


                        final List<String> documentName = new ArrayList<>();
                        documentName.add("MY_BUILDING_POST");
                        documentName.add("MY_SAVED");


                        List<Map<String, Object>> documentField = new ArrayList<>();
                        documentField.add(buildMap);
                        documentField.add(savedMap);


                        for (int x = 0; x < documentName.size(); x++) {

                            final int finalX = x;
                            userDataReference.document(documentName.get(x)).set(documentField.get(x)).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    if (finalX == documentName.size() - 1) {
                                        loadingDialog.dismiss();
                                        // login to the data base and adding user id to the updated user list in database
                                        Intent intent = new Intent(context, MainActivity.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    }
                                } else {

                                    loadingDialog.dismiss();
                                }
                            });
                        }
                    } else {

                        loadingDialog.dismiss();
                    }
                });
    }

    public static void database_userGetData(final Context context, Dialog loadingDialog, boolean loadMainActivity) {
        final CollectionReference collectionReference = firestore.collection("USERS");
        loadingDialog.show();

        collectionReference.document(currentUser.getUid()).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if (task.isSuccessful()) {
                        String user_data = documentSnapshot.getString("user_data");

                        String[] data = user_data.split("/-/");


                        USER_NAME = data[0];
                        USER_EMAIL = data[1];
                        USER_PHONE = data[2];
//                        USER_ALT_PHONE = data[3];


                        collectionReference.document(currentUser.getUid()).update("last_seen", FieldValue.serverTimestamp())
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {

                                        loadingDialog.dismiss();
                                        if (loadMainActivity) {

                                            Intent mainIntent = new Intent(context, MainActivity.class);
                                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            context.startActivity(mainIntent);
                                        }
                                    }
                                });

                    } else {
                        loadingDialog.dismiss();

                        Toast.makeText(context, "we are unable to connect the server, all services may not be available please try after sometime.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static void database_simpleBuildView(Dialog loadingDialog, propertyViewAdapter adapter) {
        simpleList.clear();
        firestore.collection("BUILDINGS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {



                    simpleList.add(new propertyViewModel(
                            documentSnapshot.getId(),
                            documentSnapshot.getString("image_url"),
                            documentSnapshot.getString("city"),
                            documentSnapshot.getString("flat_type"),
                            documentSnapshot.getString("price"),
                            documentSnapshot.getString("instalment"),
                            documentSnapshot.getString("address"),
                            documentSnapshot.getString("pincode"),
                            documentSnapshot.getString("des"),
                            documentSnapshot.getString("owner_info"),
                            documentSnapshot.get("upload_date").toString(),
                            documentSnapshot.getBoolean("drink"),
                            documentSnapshot.getBoolean("late_night")
                    ));
                }

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
}
