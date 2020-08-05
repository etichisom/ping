package com.example.pink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat extends AppCompatActivity {
    private EditText chatmessage;
    private ImageView send;
    private RecyclerView recyclerView;
    private String userid,recieverrid, message,mid,time,date;
    private DatabaseReference db;
    private FirebaseAuth mauth;
    private List<Message>messageList;
    private chatadapter c;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatmessage = findViewById(R.id.chatmessage);
        toolbar = findViewById(R.id.chatoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send = findViewById(R.id.sendmessage);
        showuseinfo();
        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.displaychat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mauth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        recieverrid = getIntent().getExtras().get("id").toString();
        userid = mauth.getCurrentUser().getUid();
        DatabaseReference bb= FirebaseDatabase.getInstance().getReference().child("userinfo").child(recieverrid);
        bb.keepSynced(true);
        bb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                getSupportActionBar().setTitle(username);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        c = new chatadapter(messageList);
        getime();
        fecthmessage();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = chatmessage.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(chat.this, "nothing", Toast.LENGTH_SHORT).show();
                }else{
                    sendmessage();
                }
            }
        });

    }

    private void showuseinfo() {

    }

    private void fecthmessage() {
        DatabaseReference cb = db.child("message").child(userid).child(recieverrid);
        cb.keepSynced(true);
                cb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message m = dataSnapshot.getValue(Message.class);
                messageList.add(m);

                recyclerView.setAdapter(c);
                c.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendmessage() {
        DatabaseReference usermkey = db.child("message").child(userid).child(recieverrid).push();
        String messid = usermkey.getKey();
       final Message m = new Message();
        String sendref = "message/" + userid +"/" +recieverrid;
        String reref = "message/" + recieverrid +"/" +userid;
        Map map = new HashMap();
        map.put("date",date);
        map.put("time",time);
        map.put("message",message);
        map.put("from",userid);
         Map messagebody = new HashMap();
         messagebody.put(sendref +"/" + messid +"/", map);
        messagebody.put(reref +"/" + messid +"/", map);


        db.updateChildren(messagebody).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                chatmessage.setText(null);

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(chat.this, "" + e, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getime() {
        Calendar calendar = Calendar.getInstance();
        time = SimpleDateFormat.getTimeInstance().format(calendar.getTime());
        Calendar cal = Calendar.getInstance();
        date = SimpleDateFormat.getDateTimeInstance().format(cal.getTime());
    }
}
