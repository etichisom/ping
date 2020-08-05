package com.example.pink;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class post extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView post;
    private  FirebaseAuth mauth;
    private int pick = 1;
    private EditText caption;
    private Button postb;
    private Uri imageuri;
    private DatabaseReference db;
    private StorageReference sr;
    private ProgressDialog progressDialog;
    private String currentdate, currentime,uid,profileuri,username,mcaption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        toolbar = findViewById(R.id.postbar);
        setSupportActionBar(toolbar);
        mauth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("updating post.....");
        progressDialog.setCanceledOnTouchOutside(false);
        uid = mauth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference().child("post");
        sr = FirebaseStorage.getInstance().getReference("post");
        geturi();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add new post");
        post = findViewById(R.id.postimage);
        caption = findViewById(R.id.caption);
        postb = findViewById(R.id.postb);
        postb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcaption = caption.getText().toString();
                if (mcaption.isEmpty()){
                    Toast.makeText(post.this, "add a capion", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    uploadpost();
                }


            }
        });
        gettime();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,pick);
            }
        });
    }

    private void uploadpost() {
        StorageReference srr = sr.child(System.currentTimeMillis()+".jpg");
        srr.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String pid = db.push().getKey();
                postdetails p = new postdetails();
                p.setCaption(mcaption);
                p.setDate(currentdate);
                p.setDp(profileuri);
                p.setTime(currentime);
                p.setUid(uid);
                p.setPostid(pid);
                p.setUsername(username);
                p.setPostimage(taskSnapshot.getDownloadUrl().toString());
                db.child(pid).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(com.example.pink.post.this,MainActivity.class);
                        startActivity(i);
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(post.this, "" + e, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void geturi() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("userinfo").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sname = dataSnapshot.child("username").getValue().toString();
                String imageur = dataSnapshot.child("imageuri").getValue().toString();

                    username = sname;
                    profileuri = imageur;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(post.this, "" + databaseError, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void gettime() {
        Calendar calendar = Calendar.getInstance();
        currentdate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat time =new SimpleDateFormat("HH:mm");
        currentime = time.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageuri = data.getData();
            Picasso.with(this).load(imageuri).into(post);
        }
    }
}
