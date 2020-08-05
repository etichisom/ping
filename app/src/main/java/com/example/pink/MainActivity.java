package com.example.pink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private  FirebaseAuth firebaseAuth;
    private TextView name;
    private DatabaseReference db;
    private ImageView dp;
    private String imageuri;
    private RecyclerView recyclerView;
    private String id;
    private  DatabaseReference likeref;
    Boolean likechecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recly);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();
        likeref = FirebaseDatabase.getInstance().getReference().child("like");
        displaypost();


        db = FirebaseDatabase.getInstance().getReference().child("userinfo");
        drawerLayout = findViewById(R.id.draw);
        getSupportActionBar().setTitle("Home");

        NavigationView nav = findViewById(R.id.nav);
        View navview = nav.inflateHeaderView(R.layout.navheader);
        dp = navview.findViewById(R.id.navdp);
        name = navview.findViewById(R.id.navusername);
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,post.class);
                startActivity(i);
            }
        });


        db.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sname = dataSnapshot.child("username").getValue().toString();
                 final String imageuri = dataSnapshot.child("imageuri").getValue().toString();
                name.setText(sname);
                Picasso.with(MainActivity.this).load(imageuri).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(dp, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(MainActivity.this).load(imageuri).into(dp);

                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

          actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.friends:
                        Toast.makeText(MainActivity.this, "friends", Toast.LENGTH_SHORT).show();
                        return true;
                    case  R.id.home:
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                    case  R.id.logout:
                        firebaseAuth.signOut();
                        Intent i = new Intent(MainActivity.this, start.class);
                        startActivity(i);
                        return  true;
                    case  R.id.msearch:
                        Intent v = new Intent(MainActivity.this, seach.class);
                        startActivity(v);
                        return  true;

                }
                return false;
            }
        });
    }

    private void displaypost() {
        DatabaseReference b = FirebaseDatabase.getInstance().getReference().child("post");
        b.keepSynced(true);
        final FirebaseRecyclerOptions<postdetails> options = new FirebaseRecyclerOptions.Builder<postdetails>()
                .setQuery(b,postdetails.class).build();
        FirebaseRecyclerAdapter<postdetails,viewholder>adapter = new FirebaseRecyclerAdapter<postdetails, viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewholder viewholder, int i, @NonNull final postdetails postdetails) {
                viewholder.username.setText(postdetails.getUsername());
                viewholder.time.setText("was uloaded " + postdetails.getDate() + " by " + postdetails.getTime());
                viewholder.dec.setText(postdetails.getCaption());
                Picasso.with(MainActivity.this).load(postdetails.getDp()).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewholder.dp, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(MainActivity.this).load(postdetails.getDp()).into(viewholder.dp);

                            }
                        });
                Picasso.with(MainActivity.this).load(postdetails.getPostimage()).fit().into(viewholder.post);
                viewholder.btstatus(postdetails.getPostid());
                viewholder.likeimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        likechecker = true;
                        likeref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (likechecker.equals(true)){
                                    if (dataSnapshot.child(postdetails.getPostid()).hasChild(id)){
                                        likeref.child(postdetails.getPostid()).child(id).removeValue();
                                        likechecker = false;

                                    }else{
                                        likeref.child(postdetails.getPostid()).child(id).setValue(true);
                                        likechecker = false;

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }

            @NonNull
            @Override
            public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.postlayout,parent,false);
                return new viewholder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            Intent i = new Intent(MainActivity.this,start.class);
            startActivity(i);

        }else{
            userstate("online");
        }
    }

    private void userstate(String state) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd,yyyy");
        String date = simpleDateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String time = dateFormat.format(calendar.getTime());
        stateofuser st = new stateofuser();
        st.setDate(date);
        st.setTime(time);
        st.setState(state);
        DatabaseReference sb = FirebaseDatabase.getInstance().getReference().child("state").child(id)
                .child("mystate");
        sb.setValue(st).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            userstate("offline");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            userstate("offline");

        }
    }
}
