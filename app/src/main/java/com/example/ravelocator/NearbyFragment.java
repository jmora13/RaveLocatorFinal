package com.example.ravelocator;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ravelocator.adapters.RaveLocatorAdapter;
import com.example.ravelocator.databinding.FragmentSearchBinding;
import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumFavoriteUpdate;
import com.example.ravelocator.model.RaveLocatorModel;

import java.util.List;

import dagger.hilt.android.scopes.ViewModelScoped;

import static java.lang.Thread.sleep;

@ViewModelScoped
public class NearbyFragment extends Fragment {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    SharedPreferences sharedPref;
    private DatumFavoriteUpdate isFavorite;
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
        binding.searchrecyclerview.setAdapter(adapter);
        binding.searchrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRaveLocatorViewModel = new ViewModelProvider(this).get(RaveLocatorViewModel.class);

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if(mRaveLocatorViewModel.getAllDatum().getValue() != null){
                adapter.setRaves(mRaveLocatorViewModel.getAllDatum().getValue());
            } else {
                mRaveLocatorViewModel.getAllEvents().observe(getViewLifecycleOwner(), raveLocatorModel -> {
                    adapter.setRaves(raveLocatorModel.getData());
                });
            }
        } else {
            mRaveLocatorViewModel.getLocationId();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mRaveLocatorViewModel.getNearbyEvents().observe(getViewLifecycleOwner(), raveLocatorModel -> {
                adapter.setRaves(raveLocatorModel.getData());
            });
        }

        binding.progressBar.setVisibility(View.GONE);
        binding.searchrecyclerview.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        SearchView searchview = view.findViewById(R.id.search);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText) || newText.length() == 0) {
                    mRaveLocatorViewModel.getNearbyEventList().observe(getViewLifecycleOwner(), raveLocatorModel -> {
                        adapter.setRaves(raveLocatorModel.getData());
                    });
                }
                if(newText.length() > 1) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(() -> Search(newText), 400);
                }
                binding.searchrecyclerview.smoothScrollToPosition(0);

                return false;
            }

            private void Search(String searchText) {
                searchText = "*"+searchText+"*";
                String finalSearchText = searchText.replace(("\""),"\"\""); //sanitize
                List<Datum> raves = mRaveLocatorViewModel.search(finalSearchText);
                adapter.setRaves(raves);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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