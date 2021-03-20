package com.kisaann.thedining.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kisaann.thedining.Models.ChatModel;
import com.kisaann.thedining.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<ChatModel> mChat;
    private String imageUrl;
    private String userPhone;
    /*FirebaseUser firebaseUser;*/
    public MessageAdapter(Context context, List<ChatModel> mChat, String imageUrl, String userPhone){
        this.context = context;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
        this.userPhone = userPhone;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view1 = LayoutInflater.from(context).inflate(R.layout.chat_item_left, viewGroup,false);
            return new MessageAdapter.ViewHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {
        ChatModel chatModel = mChat.get(i);
        viewHolder.show_message.setText(chatModel.getMessage());
        if (imageUrl.equals("default")){
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(imageUrl).into(viewHolder.profile_image);
        }
        if (i == mChat.size()-1){
            if (chatModel.isSeen()){
                viewHolder.txt_seen.setImageResource(R.drawable.ic_done_all_green_24dp);
                /* viewHolder.txt_seen.setText("Seen");*/
            }else {
                viewHolder.txt_seen.setImageResource(R.drawable.ic_done_all_gray_24dp);
                /*viewHolder.txt_seen.setText("Delivered");*/
            }
        }else {
            viewHolder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message ;
        public ImageView txt_seen ;
        public CircleImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        /*firebaseUser = FirebaseAuth.getInstance().getCurrentUser();*/
            if (mChat.get(position).getSender().equals(userPhone)) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
    }
}
