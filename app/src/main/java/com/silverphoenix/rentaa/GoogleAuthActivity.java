package com.silverphoenix.rentaa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class GoogleAuthActivity extends AppCompatActivity {


    private LinearLayout googleAuthBtn;
    private boolean ACTIVITY_RUNNING = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private GoogleSignInClient mGoogleSignInClint;
    private int SIGN_IN_TYPE = -1;
    private int GOOGLE_SIGN_IN = 0;
    private int GC_SIGN_IN = 1;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_auth);

        googleAuthBtn = findViewById(R.id.google_btn);

        //........................... no internet connection layout start ............................//
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.solid_30);
        ImageView Image = loadingDialog.findViewById(R.id.loading_image);
        Glide.with(getApplicationContext()).load(R.drawable.gg).into(Image);
        //........................... no internet connection layout end ............................//



        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        /*.................................. Google Sign Up starts ...................................*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClint = GoogleSignIn.getClient(this, gso);
        googleAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ACTIVITY_RUNNING) {
                    GoogleSignUp();
                } else {
                    Toast.makeText(GoogleAuthActivity.this, "Please wait, Other Activity is running.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*................................. Google sign up ends.......................................*/
    }

    /*.................... Google Sign In setting start ........................*/
    private void GoogleSignUp() {
        Intent googleSignInIntent = mGoogleSignInClint.getSignInIntent();
        SIGN_IN_TYPE = GOOGLE_SIGN_IN;
        startActivityForResult(googleSignInIntent, GC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ACTIVITY_RUNNING = true;
        if (SIGN_IN_TYPE == GOOGLE_SIGN_IN) {
            if (requestCode == GC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Toast.makeText(this, "We got access from Google, We will attempt Sign In now Please wait.", Toast.LENGTH_SHORT).show();
                    FirebaseGoogleAuth(account);
                } catch (ApiException e) {
                    Toast.makeText(this, "Unable to get access from Google, please try again after sometime.  " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }
        ACTIVITY_RUNNING = false;
    }

    private void FirebaseGoogleAuth(final GoogleSignInAccount account) {
        ACTIVITY_RUNNING = true;
        loadingDialog.show();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                FirebaseUser user = mAuth.getCurrentUser();
                boolean newUser = Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser();

                if (newUser) {
                    DataQueries.database_userSetData(this,user.getDisplayName(), user.getEmail(), user.getPhoneNumber(),loadingDialog);
                } else {
                    DataQueries.database_userGetData(this,loadingDialog,true);
                }
            } else {
                Toast.makeText(GoogleAuthActivity.this, "Login Failed, Please try after sometime.", Toast.LENGTH_SHORT).show();
            }
        });
        ACTIVITY_RUNNING = false;
    }

    /*.................... Google Sign In setting end   ........................*/
}

