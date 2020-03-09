package com.example.famdictionary.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.famdictionary.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class new_vocab extends AppCompatActivity {

    private EditText newWord;
    private EditText wordDef;
    private EditText wordEx;
    private Button Submit;
    private FirebaseDatabase database;
    private DatabaseReference ref,ref1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_vocab);
        setTitle("Add Word");
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        newWord = findViewById(R.id.new_word);
        wordDef = findViewById(R.id.word_def);
        wordEx = findViewById(R.id.word_ex);
        Submit = findViewById(R.id.upload_word);
        progressBar = findViewById(R.id.pBar);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("New Vocab");
        ref1 = database.getReference().child("Word Count").child("num");

//        ref1.setValue(0);


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newWord.getText().toString().isEmpty() || wordDef.getText().toString().isEmpty() || wordEx.getText().toString().isEmpty()){
                    Toast.makeText(new_vocab.this, "Input Data", Toast.LENGTH_SHORT).show();
                }else{
                    String NewWord = newWord.getText().toString().substring(0,1).toUpperCase() +  newWord.getText().toString().substring(1).toLowerCase() ;
                    String WordDef = wordDef.getText().toString().substring(0,1).toUpperCase() + wordDef.getText().toString().substring(1).toLowerCase();
                    String WordEx = wordEx.getText().toString().substring(0,1).toUpperCase() + wordEx.getText().toString().substring(1).toLowerCase();

                    progressBar.setVisibility(View.VISIBLE);
                    uploadToDic(NewWord,WordDef,WordEx);
                }
            }
        });

    }

    private void uploadToDic(final String NewWord, final String WordDef, final String WordEx) {

        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int num = Integer.parseInt(dataSnapshot.getValue().toString());
                HashMap<String,String> map = new HashMap<>();

                map.put("New Word", NewWord);
                map.put("Word Meaning", WordDef);
                map.put("Word Example", WordEx);

                ref.child(dataSnapshot.getValue().toString()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(new_vocab.this, "Word Added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(new_vocab.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                num += 1;
                ref1.setValue(num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        finish();
    }

}
