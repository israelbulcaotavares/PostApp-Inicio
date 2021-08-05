package com.tavaresrit.postapp.listfragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyPostsFragment extends PostListFragment {

    public MyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // Todos Meus Posts
        return databaseReference.child("user-posts").child(getUid());

    }
}
