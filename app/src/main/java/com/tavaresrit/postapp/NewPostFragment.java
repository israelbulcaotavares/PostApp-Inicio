package com.tavaresrit.postapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tavaresrit.postapp.databinding.FragmentNewPostBinding;
import com.tavaresrit.postapp.models.Post;
import com.tavaresrit.postapp.models.User;
import java.util.HashMap;
import java.util.Map;


public class NewPostFragment extends BaseFragment {
    private static final String TAG = "NewPostFragment";

    private DatabaseReference mDatabase;
    private FragmentNewPostBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.fabSubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String title = binding.fieldTitle.getText().toString();
        final String body = binding.fieldBody.getText().toString();

        // Desative o botão para que não haja multi-posts
        Toast.makeText(getContext(), "Postando...", Toast.LENGTH_SHORT).show();

        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // Se o usuário for NULO ou inexistente, NÂO escreve
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getContext(),"Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Escreve novo post
                            writeNewPost(userId, user.username, title, body);
                        }

                        NavHostFragment.findNavController(NewPostFragment.this)
                                .navigate(R.id.action_NewPostFragment_to_MainFragment);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());

                    }
                });
    }


    private void writeNewPost(String userId, String username, String title, String body) {
        // Criar uma nova postagem em /user-posts/$userid/$postid e em
        // /posts/$postid simultaneamente
        String key = mDatabase.child("posts").push().getKey(); // TODO: injetando chave key do usuario cadastrado
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>(); //TODO: Obtendo a HASH MAP
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


}
