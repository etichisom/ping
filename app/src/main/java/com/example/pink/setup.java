package com.example.pink;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class setup extends AppCompatActivity {
    private EditText mname,musername,mcountry;
    private ImageView dp;
    private Uri imageuri;
    private  int pick = 1;
    private DatabaseReference db;
    private StorageReference sr ;
    private FirebaseAuth mauth;
    private ProgressDialog progressDialog;

    private  String name,username,country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mname =findViewById(R.id.fullname);
        mcountry = findViewById(R.id.country);
        musername = findViewById(R.id.username);
        mauth = FirebaseAuth.getInstance();
        dp = findViewById(R.id.dp);
        sr = FirebaseStorage.getInstance().getReference("dp");
        db = FirebaseDatabase.getInstance().getReference().child("userinfo");
        Button save = findViewById(R.id.save);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("saving user info......");
        progressDialog.setCanceledOnTouchOutside(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = mname.getText().toString();
                username = musername.getText().toString();
                country = mcountry.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(setup.this, "enter your full name", Toast.LENGTH_SHORT).show();
                }else if (username.isEmpty()){
                    Toast.makeText(setup.this, "username cannot be blank", Toast.LENGTH_SHORT).show();
                }else if (country.isEmpty()){
                    Toast.makeText(setup.this, "select a country", Toast.LENGTH_SHORT).show();

                }else  if (imageuri== null){
                    Toast.makeText(setup.this, "put a display pics ", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    upload();
                }


            }
        });
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,pick);
            }
        });
    }

    private void upload() {
        StorageReference srr = sr.child(System.currentTimeMillis() + "jpg");
        srr.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String uid = mauth.getCurrentUser().getUid();
                userinfo u = new userinfo();
                u.setCountry(country);
                u.setName(name);
                u.setUsername(username);
                u.setImageuri(taskSnapshot.getDownloadUrl().toString());
                db.child(uid).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(setup.this, MainActivity.class);
                        startActivity(i);
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(setup.this, "" + e, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(setup.this, "" + e, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageuri = data.getData();
            Picasso.with(this).load(imageuri).into(dp);
        }
    }
}
