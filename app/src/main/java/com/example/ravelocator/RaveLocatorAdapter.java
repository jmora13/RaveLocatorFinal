package com.example.ravelocator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;

import java.util.List;

public class RaveLocatorAdapter extends RecyclerView.Adapter<RaveLocatorAdapter.RaveViewHolder>{

    private LayoutInflater mInflater;
    private List<Datum> datum;
    private List<Datum> favorites;
    private DatumUpdate isFavorite;
    private final static int MAX_LINES_COLLAPSED = 1;
    private final boolean INITIAL_IS_COLLAPSED = true;
    private boolean isCollapsed = INITIAL_IS_COLLAPSED;
    private ListItemClickListener mOnClickListener;


    class RaveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView concertName;
        private final TextView concertVenue;
        private final TextView artistPreview;
        private RaveViewHolder(View itemView){
            super(itemView);
            concertName = itemView.findViewById(R.id.concertName);
            concertVenue = itemView.findViewById(R.id.concertVenue);
            artistPreview = itemView.findViewById(R.id.artistPreview);
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.favoriteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    mOnClickListener.onListItemClick(datum.get(position));
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
        if(current.getName() != null) {
            holder.concertName.setText(current.getName());
        } else {
            holder.concertName.setText(getArtists(current));
        }

        holder.concertVenue.setText(current.getVenue().getVenueName());
        TODO:
        //venue not getting saved into databse but is binding to text in ui, most likely bc it is in an array
        holder.artistPreview.setText(getArtists(current));
        holder.concertName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollapsed) {
                    holder.artistPreview.setMaxLines(Integer.MAX_VALUE);
                } else {
                    holder.artistPreview.setMaxLines(MAX_LINES_COLLAPSED);
                }
                isCollapsed = !isCollapsed;
            }
        });

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
