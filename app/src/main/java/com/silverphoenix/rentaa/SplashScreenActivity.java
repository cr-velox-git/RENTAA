package com.silverphoenix.rentaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {// Using handler with postDelayed called runnable run method
            @Override
            public void run() {
              onStartActivity();
            }
        }, 5 * 1000);
    }

    private void onStartActivity() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            try {
                FirebaseFirestore.getInstance().collection("USERS").document(user.getUid())
                        .update("last_seen", FieldValue.serverTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SplashScreenActivity.this, "server Error please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(SplashScreenActivity.this, "server Error please try again later", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(this, GoogleAuthActivity.class);
            startActivity(intent);
            finish();
        }
    }
}