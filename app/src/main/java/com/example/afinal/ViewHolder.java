package com.example.afinal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.EventLogTags;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTextView;
    ImageView mImageTv;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

         mTextView=itemView.findViewById(R.id.rTitleTv);
         mImageTv=itemView.findViewById(R.id.rImageView);

    }



}
