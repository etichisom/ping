package com.example.pink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class request extends AppCompatActivity {
    private TextView name;
    private ImageView imageView;
    private Button send,decline;
    private  String recieverid, senderid,currentstae,userid;
    private FirebaseAuth mauth;
    private DatabaseReference db, friendref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        name = findViewById(R.id.requestname);
        decline = findViewById(R.id.requestdecline);
        send = findViewById(R.id.requestsend);
        imageView = findViewById(R.id.requestimage);
        decline.setVisibility(View.INVISIBLE);
        recieverid = getIntent().getExtras().get("id").toString();
        db = FirebaseDatabase.getInstance().getReference().child("userinfo");
        mauth = FirebaseAuth.getInstance();
        friendref =FirebaseDatabase.getInstance().getReference().child("friendrequest");
         userid =mauth.getCurrentUser().getUid();
         cancel();
        currentstae = "not friends";
        if (!userid.equals(recieverid)){
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    send.setEnabled(false);
                    if (currentstae.equals("not friends")){
                        sendrequest();
                        decline.setVisibility(View.INVISIBLE);
                    }
                    if (currentstae.equals("request sent")){
                        cancelrequest();
                        decline.setVisibility(View.INVISIBLE);


                    }
                }
            });

        }


        db.child(recieverid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sname = dataSnapshot.child("username").getValue().toString();
                String imageuri = dataSnapshot.child("imageuri").getValue().toString();
                name.setText(sname);
                Picasso.with(request.this).load(imageuri).into(imageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(request.this, "" +databaseError, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void cancelrequest() {
        friendref.child(userid).child(recieverid).child("request").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    friendref.child(recieverid).child(userid)
                            .child("request").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            send.setEnabled(true);
                            currentstae = "not friends";
                            send.setText("send friend request");


                        }
                    });

                }

            }
        });
    }

    private void cancel() {
        friendref.child(userid).child(recieverid).child("request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String rstate = dataSnapshot.getValue().toString();
                    if (rstate.equals("sent")){
                        send.setEnabled(true);
                        send.setText("cancel request");
                        currentstae = "request sent";
                    }else if (rstate.equals("recieved")){
                        send.setText("accept request");
                        decline.setVisibility(View.VISIBLE);
                        decline.setEnabled(true);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendrequest() {
        friendref.child(userid).child(recieverid).child("request").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    friendref.child(recieverid).child(userid)
                            .child("request").setValue("recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            send.setEnabled(true);
                            currentstae = "request_sent";
                            send.setText("cancel request");


                        }
                    });

                }

            }
        });
    }
}
