package com.example.ravelocator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ravelocator.adapters.RaveLocatorAdapter;
import com.example.ravelocator.databinding.FragmentFavoritesBinding;
import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumFavoriteUpdate;


public class FavoritesFragment extends Fragment {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    private FragmentFavoritesBinding binding;
    @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            binding = FragmentFavoritesBinding.inflate(inflater, container, false);
            View view = binding.getRoot();
            final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
            binding.recyclerview.setAdapter(adapter);
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRaveLocatorViewModel = new ViewModelProvider(requireActivity()).get(RaveLocatorViewModel.class);
            mRaveLocatorViewModel.getAllFavorites().observe(getViewLifecycleOwner(), adapter::setRaves);
            return view;
        }


    public void onListItemClick(Datum datum) {
        DatumFavoriteUpdate isFavorite;
        isFavorite = new DatumFavoriteUpdate(datum.getId(), false);
        mRaveLocatorViewModel.updateDatumFavorites(isFavorite);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}