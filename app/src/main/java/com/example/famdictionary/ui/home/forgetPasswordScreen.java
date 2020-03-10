package com.example.famdictionary.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.famdictionary.MainActivity;
import com.example.famdictionary.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPasswordScreen extends AppCompatActivity {

    private EditText emailAddress;
    private Button requestPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        emailAddress = findViewById(R.id.userEmal);
        requestPassword = findViewById(R.id.requestPassword);
        progressBar = findViewById(R.id.pGressBar);

        requestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailAddress.getText().toString().trim().isEmpty()){
                    Toast.makeText(forgetPasswordScreen.this, "Provide email", Toast.LENGTH_LONG).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    requestNewPassword();
                }

            }
        });

    }

    private void requestNewPassword() {
        firebaseAuth.sendPasswordResetEmail(emailAddress.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(forgetPasswordScreen.this, "A confirmation email has been sent. Please follow the link", Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(forgetPasswordScreen.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(forgetPasswordScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
