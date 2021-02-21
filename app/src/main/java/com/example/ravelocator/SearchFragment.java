package com.example.ravelocator;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.RaveLocatorModel;
import com.example.ravelocator.util.Venue;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static androidx.core.content.ContextCompat.getSystemService;


public class SearchFragment extends Fragment {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    private Toolbar toolbar;
    View view;
    ViewPager2 viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("California");
        view = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.searchrecyclerview);
        mRaveLocatorViewModel = new ViewModelProvider(requireActivity()).get(RaveLocatorViewModel.class);
        viewPager = view.findViewById(R.id.pager);
        setHasOptionsMenu(true);
        final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                if(newText.length() > 1) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(() -> Search(newText), 400);
                }
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

//            //TODO: make a search location function
//
////            private void SearchLocations(String searchText) {
////                mRaveLocatorViewModel.getDatumOfVenue(searchText);
////                adapter.setRaves(raves, dwv);
////            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        adapter.notifyDataSetChanged();
//        view = getView();
//        //if (view == null) return;
//        int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
//        int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(wMeasureSpec,hMeasureSpec);

//        if(viewPager.getLayoutParams().height != view.getMeasuredHeight()){
//            ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
//            lp.height = view.getMeasuredHeight();
//        }
//    }

//        public void onResume() {
//        super.onResume();
//        int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
//        int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(wMeasureSpec,hMeasureSpec);
//
//        if(viewPager.getLayoutParams().height != view.getMeasuredHeight()){
//            ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
//            lp.height = view.getMeasuredHeight();
//        }
//    }


    private void SetupSearchView(){
//        final SearchView searchview = findViewById(R.id.search);
//        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                mRaveLocatorViewModel.requestRaveLocations().observe(getViewLifecycleOwner(), raveLocatorModel -> {
//                    adapter.setRaves(raveLocatorModel.getData());
//                    // Log.e("ArtistNames", raveLocatorModel.getData().get(0).getArtistList().get(0).getArtistName());
//                });
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

    }



    public void onListItemClick(Datum datum) {
        //Toast.makeText(this, datum., Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
//        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        DatumUpdate isFavorite;
        if(datum.getFavorite() == false) {
            isFavorite = new DatumUpdate(datum.getId(), true);
        } else {
            isFavorite = new DatumUpdate(datum.getId(), false);
        }
        mRaveLocatorViewModel.updateDatumFavorites(isFavorite);
    }



}
