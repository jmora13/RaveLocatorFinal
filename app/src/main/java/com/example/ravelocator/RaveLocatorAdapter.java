package com.example.ravelocator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.Venue;

import java.util.List;

public class RaveLocatorAdapter extends RecyclerView.Adapter<RaveLocatorAdapter.RaveViewHolder>{

    private LayoutInflater mInflater;
    private List<Datum> datum;
    private Venue venue;
    private List<Datum> favorites;
    private DatumUpdate isFavorite;
    private final static int MAX_LINES_COLLAPSED = 1;
    private final boolean INITIAL_IS_COLLAPSED = true;
    private boolean isCollapsed = INITIAL_IS_COLLAPSED;
    private ListItemClickListener mOnClickListener;
    private Toolbar toolbar;
    private RaveLocatorViewModel mRaveLocatorViewModel;
    private List<DatumWithVenue> dwv;

    class RaveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView concertName;
        private final TextView concertVenue;
        private final TextView artistPreview;
        private final TextView separatorView;
        private final TextView initialSeparatorView;
        private RaveViewHolder(View itemView){
            super(itemView);
            concertName = itemView.findViewById(R.id.concertName);
            concertVenue = itemView.findViewById(R.id.concertVenue);
            artistPreview = itemView.findViewById(R.id.artistPreview);
            separatorView = itemView.findViewById(R.id.separatorView);
            initialSeparatorView = itemView.findViewById(R.id.initalSeparator);
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.favoriteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    mOnClickListener.onListItemClick(datum.get(position));
                    Toast.makeText(v.getContext(), datum.get(position).getVenue().getVenueName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {
//            int position = getBindingAdapterPosition();
//            mOnClickListener.onListItemClick(position);
        }
    }
    interface ListItemClickListener{
        void onListItemClick(Datum datum);
    }

    public RaveLocatorAdapter(Context context, ListItemClickListener onClickListener) {
        mInflater = LayoutInflater.from(context);
        this.mOnClickListener = onClickListener;
    }
    @NonNull
    @Override
    public RaveLocatorAdapter.RaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View concertName = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RaveViewHolder(concertName);
    }

    @Override
    public void onBindViewHolder(@NonNull RaveLocatorAdapter.RaveViewHolder holder, int position) {
        Datum current = datum.get(position);

        if(position == 0){
            holder.initialSeparatorView.setText(datum.get(position).getDate());
            holder.initialSeparatorView.setVisibility(View.VISIBLE);
        } else if(position + 1 != datum.size() && !current.getDate().equals(datum.get(position + 1).getDate()) ){ //set separator visible if new date is encountered
            holder.separatorView.setText(datum.get(position + 1).getDate());
            holder.separatorView.setVisibility(View.VISIBLE);
        } else {
            holder.initialSeparatorView.setVisibility(View.GONE);
            holder.separatorView.setVisibility(View.GONE);
        }
            if (current.getName() != null) {
                holder.concertName.setText(current.getName());
            } else {
                holder.concertName.setText(getArtists(current));
            }
            if(current.getLivestreamInd() == true) {
                holder.concertVenue.setText("Livestream");
            } if(dwv == null){
                holder.concertVenue.setText(current.getVenue().getVenueName());
                } else {
                holder.concertVenue.setText(dwv.get(position).venue.getVenueName());
            }
            holder.concertVenue.setOnClickListener(v -> collapseArtistList(holder));
            holder.artistPreview.setText(getArtists(current));
            holder.artistPreview.setOnClickListener(v -> collapseArtistList(holder));
            holder.concertName.setOnClickListener(v -> collapseArtistList(holder));

    }

    private void collapseArtistList(RaveViewHolder holder){
        if (isCollapsed) {
            holder.artistPreview.setMaxLines(Integer.MAX_VALUE);
        } else {
            holder.artistPreview.setMaxLines(MAX_LINES_COLLAPSED);
        }
        isCollapsed = !isCollapsed;
    }

    public String getArtists(Datum current){
        String artists = "";
        for(int i = 0; i < current.getArtistList().size(); i++){
            artists +=  current.getArtistList().get(i).getArtistName() + ", ";
        }
        if(artists != "") {
            artists = artists.substring(0, artists.length() - 2);
        }
        return artists;
    }


    void setRaves(List<Datum> raves, List<DatumWithVenue> dwv){
        datum = raves;
        this.dwv = dwv;
        notifyDataSetChanged();
    }

    void setRaves(List<Datum> raves){
        datum = raves;
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        if(datum != null){
            return datum.size();
        } else return 0;
    }



}
