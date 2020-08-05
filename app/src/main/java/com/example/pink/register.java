package com.example.pink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    private FirebaseAuth mauth;
    private TextInputLayout memail,mpassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mauth = FirebaseAuth.getInstance();
        memail = findViewById(R.id.emaill);
        mpassword = findViewById(R.id.passwordl);
        Button sign = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("signing up new user......");
        progressDialog.setCanceledOnTouchOutside(false);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = memail.getEditText().getText().toString();
                String password = mpassword.getEditText().getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(register.this, "enter your email address", Toast.LENGTH_SHORT).show();
                }else if (password.isEmpty()){
                    Toast.makeText(register.this, "choose a password", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    createuser(email,password);
                }


            }
        });
    }

    private void createuser(String email, String password) {
        mauth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent i = new Intent(register.this,setup.class);
                startActivity(i);
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, "" + e, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}
