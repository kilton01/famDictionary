package com.example.famdictionary.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.famdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {
    private EditText username,password;
    private Button login;
    private TextView forgotPassword,signUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        setTitle("Log In Screen");

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = username.getText().toString().trim();
                String passcode = password.getText().toString();

                if (email.isEmpty() || passcode.isEmpty()){
                    Toast.makeText(LoginScreen.this, "Input email", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email,passcode).addOnSuccessListener(LoginScreen.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginScreen.this, "Log In Successful", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnFailureListener(LoginScreen.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });



    }


}
