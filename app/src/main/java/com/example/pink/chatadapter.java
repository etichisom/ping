package com.example.pink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class chatadapter extends RecyclerView.Adapter<chatadapter.chatholder> {
    private List<Message> chats;
    private FirebaseAuth mauth;

    public chatadapter(List<Message> chats) {
        this.chats = chats;
    }


    @NonNull
    @Override
    public chatholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatrow,parent,false);
        mauth = FirebaseAuth.getInstance();
        return new chatholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final chatholder holder, int position) {
        Message message = chats.get(position);
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        String userid = mauth.getCurrentUser().getUid();
        String senderid = message.getFrom();

        if (userid.equals(senderid)){
            holder.semessage.setText(message.getMessage());
            holder.rmesssage.setVisibility(View.INVISIBLE);
            holder.image.setVisibility(View.INVISIBLE);

        }else{
            holder.rmesssage.setText(message.getMessage());
            holder.semessage.setVisibility(View.INVISIBLE);

        }




        }








    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class chatholder extends RecyclerView.ViewHolder {
        private TextView rmesssage,semessage;
        private ImageView image;
        public chatholder(@NonNull View itemView) {
            super(itemView);
            rmesssage = itemView.findViewById(R.id.me);
            semessage = itemView.findViewById(R.id.him);
            image = itemView.findViewById(R.id.meimage);

        }
    }
}
