package com.example.pink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class seach extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText text;
    private ImageView seach;
    private  String name;
    private DatabaseReference db;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);
        toolbar = findViewById(R.id.searchbar);
        text =findViewById(R.id.search);
        seach = findViewById(R.id.seaching);
        db = FirebaseDatabase.getInstance().getReference().child("userinfo");
        recyclerView = findViewById(R.id.searchview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("search for users");
        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = text.getText().toString();
                seachuser(name);
                
            }
        });
    }

    private void seachuser(String name) {
        Query query = db.orderByChild("username").startAt(name).endAt(name + "\uf8ff");
        query.keepSynced(true);

        FirebaseRecyclerOptions<userinfo> options = new FirebaseRecyclerOptions.Builder<userinfo>()
                .setQuery(query,userinfo.class).build();

        FirebaseRecyclerAdapter<userinfo,holder> adapter = new FirebaseRecyclerAdapter<userinfo, holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull holder holder, final int i, @NonNull userinfo userinfo) {
                holder.username.setText(userinfo.getUsername());
                Picasso.with(com.example.pink.seach.this).load(userinfo.getImageuri()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = getRef(i).getKey();
                        Intent intent = new Intent(com.example.pink.seach.this,chat.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seachrow,parent,false);
                return new holder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
