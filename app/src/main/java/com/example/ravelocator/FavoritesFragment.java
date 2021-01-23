package com.example.ravelocator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.RaveLocatorModel;


public class FavoritesFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.content_main, container, false);
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
            final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            RaveLocatorViewModel mRaveLocatorViewModel = new ViewModelProvider(requireActivity()).get(RaveLocatorViewModel.class);
            mRaveLocatorViewModel.getAllFavorites().observe(getViewLifecycleOwner(), adapter::setRaves);
            return view;
        }


    public void onListItemClick(Datum datum) {
        //Toast.makeText(this, datum., Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
//        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        DatumUpdate isFavorite = new DatumUpdate(datum.getId(), true);
        //mRaveLocatorViewModel.updateDatumFavorites(isFavorite);
    }

    @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Bundle args = getArguments();

        }


}