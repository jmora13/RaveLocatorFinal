package com.example.ravelocator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumFavoriteUpdate;


public class FavoritesFragment extends Fragment {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    ToggleButton favorite;
    View view;
    ViewPager2 viewPager;
    ToggleButton favorites;
    @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_favorites, container, false);

            Context context = view.getContext();
            viewPager = view.findViewById(R.id.pager);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
            final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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