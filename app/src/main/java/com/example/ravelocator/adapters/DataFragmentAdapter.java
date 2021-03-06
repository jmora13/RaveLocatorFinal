package com.example.ravelocator.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ravelocator.FavoritesFragment;
import com.example.ravelocator.NearbyFragment;

public class DataFragmentAdapter extends FragmentStateAdapter {


    public DataFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new FavoritesFragment();
            default:
                return new NearbyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
