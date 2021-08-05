package com.tavaresrit.postapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.tavaresrit.postapp.databinding.FragmentMainBinding;
import com.tavaresrit.postapp.listfragments.MyPostsFragment;

import org.jetbrains.annotations.NotNull;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);



        // Crie o adaptador que retornará um fragmento para cada seção
        FragmentStateAdapter mPagerAdapter = new FragmentStateAdapter(getParentFragmentManager(),
                getViewLifecycleOwner().getLifecycle()) {
            private final Fragment[] mFragments = new Fragment[]{

                     new MyPostsFragment(),
             };

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return mFragments[position];
            }

            @Override
            public int getItemCount() {
                return mFragments.length;
            }
        };
        // Configure o ViewPager com o adaptador de seções.
        binding.container.setAdapter(mPagerAdapter);
        String[] mFragmentNames = new String[]{

                getString(R.string.heading_my_posts),  //TAB My Posts Fragment


        };
        new TabLayoutMediator(binding.tabs, binding.container,
                (tab, position) -> tab.setText(mFragmentNames[position])
        ).attach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_MainFragment_to_SignInFragment);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
