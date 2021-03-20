package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kisaann.thedining.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {


    public TextView txtUserPhone,txtComment;
    public RatingBar ratingBar;
    public ShowCommentViewHolder(View itemView) {
        super(itemView);
        txtUserPhone = (TextView)itemView.findViewById(R.id.txtUserPhone);
        txtComment = (TextView)itemView.findViewById(R.id.txtComment);
        ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
    }
}
