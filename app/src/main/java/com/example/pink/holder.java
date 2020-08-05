package com.example.pink;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class holder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView username;
    public holder(@NonNull View itemView) {

        super(itemView);
        imageView = itemView.findViewById(R.id.simage);
        username = itemView.findViewById(R.id.sname);

    }
}
