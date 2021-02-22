package com.example.ravelocator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.RaveLocatorModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class NearbyFragment extends Fragment {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    ViewPager2 viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.searchrecyclerview);
        final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRaveLocatorViewModel = new ViewModelProvider(requireActivity()).get(RaveLocatorViewModel.class);
        mRaveLocatorViewModel = ViewModelProviders.of(this).get(RaveLocatorViewModel.class);
        mRaveLocatorViewModel.requestRaveLocations().observe(getViewLifecycleOwner(), raveLocatorModel -> {
            adapter.setRaves(raveLocatorModel.getData());
            // Log.e("ArtistNames", raveLocatorModel.getData().get(0).getArtistList().get(0).getArtistName());
        });

        final Handler handler = new Handler();
        SearchView searchview = view.findViewById(R.id.search);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<VenueWithDatum> vwd = mRaveLocatorViewModel.getDatumOfVenue(query);
                List<Datum> raves = vwd.get(0).datum;
                adapter.setRaves(raves, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mRaveLocatorViewModel.requestRaveLocations().observe(getViewLifecycleOwner(), raveLocatorModel -> {
                        adapter.setRaves(raveLocatorModel.getData());
                    });
                }
                if(newText.length() > 1) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(() -> Search(newText), 400);
                }
                recyclerView.smoothScrollToPosition(0);

                return false;
            }

            private void Search(String searchText) {
                searchText = "*"+searchText+"*";
                String finalSearchText = searchText;
                List<Datum> raves = mRaveLocatorViewModel.search(finalSearchText);
                List<DatumWithVenue> dwv = new ArrayList<>();
                for(int i = 0; i < raves.size(); i++) {
                    dwv.add(mRaveLocatorViewModel.getVenueOfDatum(raves.get(i).getId())); //makes list of events by id and its reference to the venue
                    //vwd.add(mRaveLocatorViewModel.g)
                }
                adapter.setRaves(raves, dwv);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
    }

    public void onListItemClick(Datum datum) {
        //Toast.makeText(this, datum., Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
//        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        DatumUpdate isFavorite = new DatumUpdate(datum.getId(), true);
        mRaveLocatorViewModel.updateDatumFavorites(isFavorite);
    }



}