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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpScreen extends AppCompatActivity {

    private EditText Fname,Lname,email,password,confirmPassword;
    private Button signUp;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        Fname = findViewById(R.id.Fname);
        Lname = findViewById(R.id.Lname);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        signUp = findViewById(R.id.sign_Up);
        progressBar = findViewById(R.id.pBar);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                saveData();

            }
        });

    }

    public void saveData(){
        if (Fname.getText().toString().trim().isEmpty() || Lname.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Provide all necessary information", Toast.LENGTH_LONG).show();
        }else{
            if (password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){
                auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String currentUser = auth.getCurrentUser().getUid();
                        HashMap<String,String> map = new HashMap<>();
                        map.put("First Name",Fname.getText().toString().trim());
                        map.put("Last Name",Lname.getText().toString().trim());
                        map.put("Email", email.getText().toString().trim());
                        map.put("Password", password.getText().toString().trim());

                        reference.child(currentUser).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUpScreen.this, "Success!! Logging In", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpScreen.this, HomeFragment.class);
                                startActivity(intent);
                                finish();

                                Intent i = new Intent(SignUpScreen.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
