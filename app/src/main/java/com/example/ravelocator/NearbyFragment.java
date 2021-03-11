package com.example.ravelocator;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.RaveLocatorModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class NearbyFragment extends Fragment {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    ViewPager2 viewPager;
    private FusedLocationProviderClient mFusedLocationClient;
    SharedPreferences sharedPref;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Location mLastLocation;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        // method to get the location
        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.searchrecyclerview);
        final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRaveLocatorViewModel = new ViewModelProvider(requireActivity()).get(RaveLocatorViewModel.class);
        mRaveLocatorViewModel = ViewModelProviders.of(this).get(RaveLocatorViewModel.class);
        //mRaveLocatorViewModel.reverseGeocoding();
        //mRaveLocatorViewModel.updateDatabase();
        List<DatumWithVenue> dwv = new ArrayList<>();
        mRaveLocatorViewModel.getNearbyEvents().observe(getViewLifecycleOwner(), raveLocatorModel -> {
            for(int i = 0; i < raveLocatorModel.getData().size(); i++) {
                dwv.add(mRaveLocatorViewModel.getVenueOfDatum(raveLocatorModel.getData().get(i).getId())); //makes list of events by id and its reference to the venue
            }
            adapter.setRaves(raveLocatorModel.getData(), dwv);
            // Log.e("ArtistNames", raveLocatorModel.getData().get(0).getArtistList().get(0).getArtistName());
        });
            // Log.e("ArtistNames", raveLocatorModel.getData().get(0).getArtistList().get(0).getArtistName());


        final Handler handler = new Handler();
        SearchView searchview = view.findViewById(R.id.search);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<VenueWithDatum> vwd = mRaveLocatorViewModel.getDatumOfVenue(query);
                List<Datum> raves = vwd.get(0).datum;
                //adapter.setRaves(raves, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mRaveLocatorViewModel.getNearbyEventList().observe(getViewLifecycleOwner(), raveLocatorModel -> {
                        adapter.setRaves(raveLocatorModel.getData(), dwv);
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
        DatumUpdate isFavorite = new DatumUpdate(datum.getId(), true);
        mRaveLocatorViewModel.updateDatumFavorites(isFavorite);
    }



}