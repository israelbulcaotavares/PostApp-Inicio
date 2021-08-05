package com.tavaresrit.postapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tavaresrit.postapp.R;
import com.tavaresrit.postapp.models.Post;


public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView bodyView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        bodyView = itemView.findViewById(R.id.postBody);
    }

    public void bindToPost(Post post) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        bodyView.setText(post.body);
    }
}
