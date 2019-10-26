package com.example.famdictionary.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.famdictionary.R;

public class detailed extends AppCompatActivity {
    private TextView main,meaning,example;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        main = findViewById(R.id.main_word);
        meaning = findViewById(R.id.meaning);
        example = findViewById(R.id.examples);

        Intent intent = getIntent();
        String Big_word = intent.getStringExtra("word");
        String Big_meaning = intent.getStringExtra("meaning");
        String Big_example = intent.getStringExtra("example");

        main.setText(Big_word);
        meaning.setText(Big_meaning);
        example.setText(Big_example);

    }
}
