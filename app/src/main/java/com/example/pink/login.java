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

public class login extends AppCompatActivity {

    private FirebaseAuth mauth;
    private TextInputLayout memail,mpassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth = FirebaseAuth.getInstance();
        memail = findViewById(R.id.lemail);
        mpassword = findViewById(R.id.lpass);
        Button sign = findViewById(R.id.log);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("login in......");
        progressDialog.setCanceledOnTouchOutside(false);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = memail.getEditText().getText().toString();
                String password = mpassword.getEditText().getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(login.this, "enter your email address", Toast.LENGTH_SHORT).show();
                }else if (password.isEmpty()){
                    Toast.makeText(login.this, "enter password", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    createuser(email,password);
                }


            }
        });
    }

    private void createuser(String email, String password) {
        mauth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent i = new Intent(login.this,MainActivity.class);
                startActivity(i);
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login.this, "" + e, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}
