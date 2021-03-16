package com.example.ravelocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumFavoriteUpdate;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


public class NearbyFragment extends Fragment {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    ViewPager2 viewPager;
    private FusedLocationProviderClient mFusedLocationClient;
    SharedPreferences sharedPref;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private DatumFavoriteUpdate isFavorite;
    Location mLastLocation;
    RecyclerView search;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        search = view.findViewById(R.id.searchrecyclerview);
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        progressBar = view.findViewById(R.id.progress_bar);
        RecyclerView recyclerView = view.findViewById(R.id.searchrecyclerview);
        final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRaveLocatorViewModel = new ViewModelProvider(requireActivity()).get(RaveLocatorViewModel.class);
        mRaveLocatorViewModel = ViewModelProviders.of(this).get(RaveLocatorViewModel.class);
            try {
                mRaveLocatorViewModel.getLocationId();
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        mRaveLocatorViewModel.getNearbyEvents().observe(getViewLifecycleOwner(), raveLocatorModel -> {
            adapter.setRaves(raveLocatorModel.getData());
            progressBar.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
            });
        final Handler handler = new Handler();
        SearchView searchview = view.findViewById(R.id.search);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mRaveLocatorViewModel.getNearbyEventList().observe(getViewLifecycleOwner(), raveLocatorModel -> {
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
                adapter.setRaves(raves);
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean settingsChanged = sharedPref.getBoolean("settings_changed", false);
        if(settingsChanged){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("settings_changed", false);
        }
    }

    public void onListItemClick(Datum datum) {
        //Toast.makeText(this, datum., Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
//        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        if(datum.getFavorite() == false) {
            isFavorite = new DatumFavoriteUpdate(datum.getId(), true);//if not favorited, set to true
            datum.setFavorite(true);
        } else {
            isFavorite = new DatumFavoriteUpdate(datum.getId(), false);//else set to false
            datum.setFavorite(false);
        }
        mRaveLocatorViewModel.updateDatumFavorites(isFavorite);

    }



}