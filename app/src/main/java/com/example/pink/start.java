package com.example.pink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void login(View view) {
        Intent i = new Intent(start.this,login.class);
        startActivity(i);

    }

    public void signup(View view) {
        Intent i = new Intent(start.this,register.class);
        startActivity(i);


    }
}
