package com.tavaresrit.postapp.listfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tavaresrit.postapp.BaseFragment;
import com.tavaresrit.postapp.R;
import com.tavaresrit.postapp.models.Post;
import com.tavaresrit.postapp.viewholder.PostViewHolder;

public abstract class PostListFragment extends BaseFragment {

     private DatabaseReference mDatabase;
     private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
     private RecyclerView mRecycler;
     private LinearLayoutManager mManager;

     public PostListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_post_list, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = rootView.findViewById(R.id.recyclerPostList);
        mRecycler.setHasFixedSize(true);

        setAdapters();

        return rootView;
    }

    private void setAdapters(){
        // Configurar Gerenciador de Layout, layout reverso
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Configura FirebaseRecyclerAdapter com a consulta
        Query postsQuery = getQuery(mDatabase);

        //Adapter do Firebase
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(PostViewHolder viewHolder, int position, final Post model) {

                // Conecta da PostViewHolder
                viewHolder.bindToPost(model);

            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

}
