package com.example.pink;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewholder extends RecyclerView.ViewHolder {
    public TextView username,dec,time,like;
    public ImageView dp,post,likeimage,comment;
    private DatabaseReference likeref;
    public  int numlike;
    String uid;
    public viewholder(@NonNull View itemView) {
        super(itemView);

        likeref = FirebaseDatabase.getInstance().getReference().child("like");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        username = itemView.findViewById(R.id.rname);
         post = itemView.findViewById(R.id.rpost);
        dec = itemView.findViewById(R.id.rdec);
        dp = itemView.findViewById(R.id.rdp);
        time = itemView.findViewById(R.id.rdate);
        likeimage = itemView.findViewById(R.id.imagelike);
        like = itemView.findViewById(R.id.like);
        comment = itemView.findViewById(R.id.coment);



    }

    public void btstatus(final String postid) {
        likeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).hasChild(uid)){
                    numlike = (int)dataSnapshot.child(postid).getChildrenCount();
                    likeimage.setImageResource(R.drawable.likedd);
                    like.setText(Integer.toString(numlike) + ("likes"));

                }else{
                    numlike = (int)dataSnapshot.child(postid).getChildrenCount();
                    likeimage.setImageResource(R.drawable.liked);
                    like.setText(Integer.toString(numlike) + ("likes"));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
